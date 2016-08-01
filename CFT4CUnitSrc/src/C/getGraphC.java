package C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;

public class getGraphC {
	private Node beginFun, endFunc;
	private String ListNodeDescription;
	private int STT = 0;
	private ArrayList<Node> nodelist = new ArrayList<>();
	private IASTTranslationUnit u;

	public static void main(String[] args) throws Exception {
		getGraphC p = new getGraphC();
		p.runFromSource("C:\\Users\\anhanh\\Documents\\5_Sum.c");
	}

	private static IASTTranslationUnit getIASTTranslationUnit(char[] code) throws Exception 
	{
		FileContent fc = FileContent.create("/Path/ToResolveIncludePaths.c", code);
		Map<String, String> macroDefinitions = new HashMap<String, String>();
		String[] includeSearchPaths = new String[0];
		IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
		IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
		IIndex idx = null;
		int options = ILanguage.OPTION_IS_SOURCE_UNIT;
		IParserLogService log = new DefaultLogService();
		return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp, idx, options, log);
	}

	private void run(String statement) throws Exception {
		u = getIASTTranslationUnit(statement.toCharArray());

		ASTVisitor visitor = new ASTVisitor() {

			@Override
			public int visit(IASTStatement s) {
				parseBlock(s, beginFun, endFunc, /* breakPoint */endFunc, /* ContinuePoint */
						beginFun);
				setNodeListID(beginFun);
				displayNodeList(beginFun);
				// System.out.println("num Nodes=" + STT);
				// System.out.println(ListNodeDescription);
				return PROCESS_ABORT;
			}
		};
		visitor.shouldVisitStatements = true;

		u.accept(visitor);
	}

	private boolean isForWhileStatement(IASTNode n) {
		if (getType(getRawContent(n)) == FOR_BLOCK)
			return true;
		if (getType(getRawContent(n)) == DO_BLOCK)
			return true;
		if (getType(getRawContent(n)) == WHILE_BLOCK)
			return true;
		return false;

	}

	private void parseBlock(IASTNode n, Node begin, Node end, /* breakPoint */
			Node breakPoint, /* ContinuePoint */Node continuePoint) {
		IASTNode[] childSet = n.getChildren();
		Node[] subBegin = new Node[childSet.length];
		Node[] subEnd = new Node[childSet.length];
		for (int i = 0; i < childSet.length; i++) {
			subBegin[i] = new Node();
			subEnd[i] = new Node();
		}
		if (childSet.length == 1)
			visitBlock(begin, end, childSet[0], breakPoint, continuePoint);
		else
		{
			visitBlock(subBegin[childSet.length - 1], end, childSet[childSet.length - 1],
					/* breakPoint */breakPoint, /* ContinuePoint */
					continuePoint);
			for (int i = childSet.length - 2; i >= 1; i--)
				if (!isForWhileStatement(childSet[i]))
					visitBlock(subBegin[i], subBegin[i + 1].trueBranch, childSet[i], /* breakPoint */
							breakPoint, /* ContinuePoint */continuePoint);
				else
					visitBlock(subBegin[i], subBegin[i + 1].trueBranch, childSet[i], /* breakPoint */
							subBegin[i + 1].trueBranch, /* ContinuePoint */
							subBegin[i]);
			if (isForWhileStatement(childSet[0]))
				visitBlock(begin, subBegin[1].trueBranch, childSet[0], /* breakPoint */
						breakPoint, /* ContinuePoint */continuePoint);
			else
				visitBlock(begin, subBegin[1].trueBranch, childSet[0], /* breakPoint */
						subBegin[1].trueBranch, /* ContinuePoint */begin);
		}
	}

	private void visitBlock(Node begin, Node end, IASTNode n, /* breakPoint */
			Node breakPoint, /* ContinuePoint */Node continuePoint) {
		IASTNode[] child = n.getChildren();
		switch (getType(n.getRawSignature().toLowerCase())) {
		case IF_BLOCK:
			switch (child.length) {
			case 2:
				String condition = getRawContent(child[0]);
				Node nCondition = new Node(condition, null, end);
				nCondition.setType(Node.CONDITION_NODE);
				begin.setAll(nCondition);
				Node subBegin = new Node();
				visitBlock(subBegin, end, child[1], /* breakPoint */breakPoint, /* ContinuePoint */
						continuePoint);
				nCondition.setTrue(subBegin.trueBranch);
				break;
			default: // >2
				String con = getRawContent(child[0]);
				Node nCon = new Node(con, null, null);
				nCon.setType(Node.CONDITION_NODE);
				begin.setAll(nCon);
				Node subBegin2 = new Node();
				visitBlock(subBegin2, end, child[1], /* breakPoint */breakPoint, /* ContinuePoint */
						continuePoint);
				nCon.setTrue(subBegin2.trueBranch);
				//
				Node subBegin3 = new Node();
				visitBlock(subBegin3, end, child[2], /* breakPoint */breakPoint, continuePoint);
				nCon.setFalse(subBegin3.trueBranch);

				break;
			}
			break;
		case FOR_BLOCK:
			switch (child.length) {
			case 4:
				String ini = getRawContent(child[0]);
				String condition = getRawContent(child[1]);
				String incre = getRawContent(child[2]);
				//
				Node iniNode = new Node(ini);
				Node conNode = new Node(condition);
				conNode.setIDLoop(Node.iCountLoop);
				Node increNode = new Node(incre);
				Node subBegin = new Node("");
				//
				begin.setAll(iniNode);
				iniNode.setAll(conNode);
				conNode.setFalse(end);
				conNode.setType(Node.CONDITION_NODE);
				increNode.setAll(conNode);
				increNode.setIDLoop(Node.iCountLoop++);
				//
				visitBlock(subBegin, increNode, child[3], /* breakPoint */end, /* ContinuePoint */
						increNode);
				conNode.setTrue(subBegin.trueBranch);
				break;
			default:// ko xet
				break;
			}
			break;
		case DO_BLOCK:
			switch (child.length) {
			case 2:
				String condition = getRawContent(child[1]);
				Node nCondition = new Node(condition, null, end);
				nCondition.setType(Node.CONDITION_NODE);
				nCondition.setIDLoop(Node.iCountLoop++);
				Node subBegin = new Node();
				visitBlock(subBegin, nCondition, child[0], /* breakPoint */end, /* ContinuePoint */
						nCondition);
				nCondition.setTrue(subBegin.trueBranch);
				begin.setAll(subBegin.trueBranch);
				break;
			default:// ko xet
				break;
			}
			break;
		case WHILE_BLOCK:
			switch (child.length) {
			case 2:
				String condition = getRawContent(child[0]);
				Node nCondition = new Node(condition, null, end);
				nCondition.setType(Node.CONDITION_NODE);
				nCondition.setIDLoop(Node.iCountLoop);
				begin.setAll(nCondition);
				Node subBegin = new Node();
				visitBlock(subBegin, nCondition, child[1], /* breakPoint */end, /* ContinuePoint */nCondition);
				nCondition.setTrue(subBegin.trueBranch);
				break;
			default:// ko xet
				break;
			}
			break;
		case RETURN_STATEMENT:
			begin.setAll(new Node(new String(getRawContent(n)), endFunc, endFunc));
			break;
		default:// normal block
			/*
			 * The content of IASTNode n might be a BLOCK(start with { and end
			 * with }), or a SIMPLE STATEMENT (initialize statement, assigned
			 * statement). In case of BLOCK, the number of its children is equal
			 * to the analysis of the body of function. For example 1, a block
			 * has two children { if (a==b)a = 1; b = 2; } For example 2,a block
			 * has one children { if (a==b)a = 1; //b = 2; }
			 */
			if (n.getChildren().length > 1) {
				/* a block has at least two children */
				parseBlock(n, begin, end, breakPoint, continuePoint);
			} else {
				/* might a block or a simple statement */
				if (getRawContent(n).charAt(0) == '{') {
					/* is a block */
					parseBlock(n, begin, end, breakPoint, continuePoint);
				} else {
					/* is a simple statement */
					if (getRawContent(n).indexOf("break") == 0)
						begin.setAll(new Node(new String(getRawContent(n)), breakPoint, breakPoint));
					else if (getRawContent(n).indexOf("continue") == 0)
						begin.setAll(new Node(new String(getRawContent(n)), continuePoint, continuePoint));
					else
						begin.setAll(new Node(new String(getRawContent(n)), end, end));
				}
			}
			break;
		}
	}

	private void displayNodeList(Node begin) {
		if (begin == null) {
			return;
		}
		if (begin.visited == false) {
			return;
		}
		if (begin.trueBranch == null && begin.falseBranch == null) {

			ListNodeDescription += begin.ID + "#Ket thuc ham#true=-1#false=-1\n";
			nodelist.add(begin);
			begin.visited = false;
			return;
		}
		nodelist.add(begin);
		ListNodeDescription += begin.ID + "#" + begin.data + "#true=" + begin.trueBranch.ID + "#false="
				+ begin.falseBranch.ID + "\n";
		begin.visited = false;
		displayNodeList(begin.trueBranch);
		displayNodeList(begin.falseBranch);

	}

	/**
	 * Set each node an unique ID
	 * 
	 * @param begin
	 *            Node BatDauHam
	 */
	private void setNodeListID(Node begin) {
		if (begin == null) {
			return;
		} else if (begin.visited == true) {
			return;
		}
		begin.setID(STT);
		STT++;
		begin.visited = true;
		if (begin.trueBranch == null || begin.falseBranch == null) {
		} else {
			setNodeListID(begin.trueBranch);
			setNodeListID(begin.falseBranch);
		}
	}

	private int getType(String data) {
		if (data.indexOf("if") == 0) {
			return IF_BLOCK;
		}
		if (data.indexOf("for") == 0) {
			return FOR_BLOCK;
		}
		if (data.indexOf("do{") == 0 || data.indexOf("do ") == 0) {
			return DO_BLOCK;
		}
		if (data.indexOf("while") == 0) {
			return WHILE_BLOCK;
		}
		if (data.indexOf("return") == 0) {
			return RETURN_STATEMENT;
		}
		return NORMAL_BLOCK;
	}

	/**
	 * 
	 * @param n
	 * @return the raw statement n holds.
	 */
	private String getRawContent(IASTNode n) {
		return n.getRawSignature().replace("\n", "");
	}

	public getGraphC() {
		beginFun = new Node("Bat dau ham");
		endFunc = new Node("Ket thuc ham");
		ListNodeDescription = new String();
	}

	public int getSTT() {
		return STT;
	}

	public void runFromFile(String fileInput) throws Exception {
		String code = Utils.getContentFile(fileInput);
		normalizeFunctionSource ns = new normalizeFunctionSource(code);
		ns.run();
		run(ns.getNormalizeCode());
	}

	public String getListNodeDescription() {
		return ListNodeDescription;
	}

	public void runFromSource(String source) throws Exception {
		run(source);
	}

	private static final int RETURN_STATEMENT = -4;
	private static final int IF_BLOCK = 0;
	private static final int FOR_BLOCK = 1;
	private static final int NORMAL_BLOCK = 2;
	private static final int WHILE_BLOCK = 3;
	private static final int DO_BLOCK = 4;
}

class Node {

	protected int IDLoop = -1;
	protected Node trueBranch;
	protected Node falseBranch;
	protected String data = new String();
	protected int type = 0;// normal-default
	protected static int CONDITION_NODE = 1;
	protected boolean visited = false;
	protected static int STT = 0;
	protected int ID = 0;
	protected static int iCountLoop = 0;

	Node(String data, Node trueBranch, Node falseBranch) {
		this.data = data;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	void setID() {
		ID = STT;
		STT++;
	}

	void setID(int n) {
		ID = n;

	}

	Node() {

	}

	Node(String data) {
		this.data = data;
	}

	void setData(String d) {
		this.data = d;
	}

	void setType(int n) {
		this.type = n;
	}

	void setTrue(Node t) {
		trueBranch = t;
	}

	void setFalse(Node f) {
		falseBranch = f;
	}

	void setAll(Node n) {
		setTrue(n);
		setFalse(n);

	}

	void setIDLoop(int id) {
		IDLoop = id;
	}
}

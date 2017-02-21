package C;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
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

/**
 * Analyse complex condition to a graph. Ex: (a!=b)&&(c>=0) <br/>
 * (a-b==0 && c-d!=0)<br/>
 * Complex condition always contains at least two sub-conditions.
 * 
 * @author anhanh
 * 
 */
public class analysisComplexCondition {

	/**
	 * is used to create the index of all element in output graph
	 * ListNodeDescription. See ListNodeDescription String to know more about
	 * the graph. <br/>
	 * The default value is set to 0
	 */
	int rootIndex = 0;
	/**
	 * the default ID of end node. But finally, end Node's ID will be replace by
	 * the another number.
	 */
	final int TEMPORARY_ID_OF_END_NODE = 1000;
	/**
	 * The new graph (which is represented by ListNodeDescription String) has
	 * the root (beginTree) and destionation (endTree).
	 */
	private final Node beginTree = new Node("root"), endTree = new Node("end");
	/**
	 * graph we need to get. Ex: With the rootIndex is set to 0, the output is:
	 * <br/>
	 * 0#root#true=1#false=1<br/>
	 * 1#(a <= 0)#true=3#false=2<br/> 3#(b <= 0)#true=4#false=2<br/>
	 * 4#(1>0)#true=5#false=2<br/> 2#(d>0)#true=5#false=5<br/>
	 * 5#end#true=-1#false=-1<br/> (end)
	 * 
	 */
	private String ListNodeDescription = "0#root#true=1#false=1\n";

	/**
	 * create IASTTranslationUnit instance. The translation unit represents a
	 * compilable unit of source
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private IASTTranslationUnit parseBlock(char[] code) throws Exception {
		FileContent fc = FileContent.create("/Path/ToResolveIncludePaths.c", code);
		Map<String, String> macroDefinitions = new HashMap<>();
		String[] includeSearchPaths = new String[0];
		IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
		IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
		IIndex idx = null;
		int options = ILanguage.OPTION_IS_SOURCE_UNIT;
		IParserLogService log = new DefaultLogService();
		return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp, idx, options, log);
	}

	/**
	 * 
	 * @param function
	 *            This function only contains a if-statement without return
	 *            statement. <br/>
	 *            Ex1:int t1(){ if ((a>=0||c>0)&& d>=0){ } } <br/>
	 *            Ex2:int t2(){ if (a>=0||c>0){ } }
	 * @throws Exception
	 */
	private void runBlock(String function) throws Exception {
		IASTTranslationUnit u = parseBlock(function.toCharArray());
		ASTVisitor visitor = new ASTVisitor() {

			@Override
			public int visit(IASTExpression s) {
				String s_toString = s.getRawSignature();
				if (s_toString.contains("&&") || s_toString.contains("||")) {
					parse(s, beginTree, endTree, endTree);
				}
				return PROCESS_ABORT;
			}
		};
		visitor.shouldVisitExpressions = true;
		u.accept(visitor);
	}

	private String getContent(IASTNode n) {
		return n.getRawSignature();
	}

	private boolean isComplexCondition(IASTNode n) {
		return getContent(n).contains("&&") || getContent(n).contains("||");
	}

	/**
	 * 
	 * @param begin
	 *            the root of tree. Every node contains ID, content, trueBranch,
	 *            falseBranch. See nested class Node to know more information.
	 */
	private void getNodeListInString(Node begin) {
		if (begin == null || begin.visited == false) {
			return;
		}
		ListNodeDescription += begin.ID + "#" + begin.content + "#true=" + begin.trueBranch.ID + "#false="
				+ begin.falseBranch.ID + "\n";
		begin.visited = false;
		getNodeListInString(begin.trueBranch);
		getNodeListInString(begin.falseBranch);
		if (!ListNodeDescription.contains("\n" + TEMPORARY_ID_OF_END_NODE)) {
			ListNodeDescription += TEMPORARY_ID_OF_END_NODE + "#end#true=-1#false=-1\n";
		}
	}

	/**
	 * Set each node in tree an unique ID . Notice that the ID of end Node is
	 * defined in TEMPORARY_ID_OF_END_NODE
	 * 
	 * @param begin
	 *            the root of tree. Every node contains ID, content, trueBranch,
	 *            falseBranch. See nested class Node to know more information.
	 */
	private void setNodeListID(Node begin) {
		if (begin == null) {
			return;
		} else if (begin.visited == true) {
			return;
		} else if (begin.content.equals(endTree.content)) {
			begin.setID(TEMPORARY_ID_OF_END_NODE);
			return;
		}
		begin.setID(rootIndex);
		rootIndex++;
		begin.visited = true;
		if (begin.trueBranch == null || begin.falseBranch == null) {
		} else {
			setNodeListID(begin.falseBranch);
			setNodeListID(begin.trueBranch);
		}
	}

	/**
	 * 
	 * @param node
	 * @param begin
	 * @param endIfTrue
	 *            in case node is true
	 * @param endIfFalse
	 *            in case node is false
	 */
	private void parse(IASTNode node, Node begin, Node endIfTrue, Node endIfFalse) {
		/*
		 * notice that : ((a+b <= c) && (a+c <= b))|| (b+c <= a) || (a>0) has 2
		 * children: ((a+b <= c) && (a+c <= b))|| (b+c <= a) AND (a>0)
		 */
		if (isComplexCondition(node) && node.getChildren().length == 1) {
			parse(node.getChildren()[0], begin, endIfTrue, endIfFalse);
		} else if (isComplexCondition(node)) {
			IASTNode[] child_set = node.getChildren();
			if (Utils.isAndDelimiter(getContent(child_set[0]), getContent(child_set[1]), getContent(node))) {
				Node beginSub = new Node("");
				parse(child_set[1], beginSub, endIfTrue, endIfFalse);
				parse(child_set[0], begin, beginSub.trueBranch, endIfFalse);
			} else {
				Node beginSub = new Node("");
				parse(child_set[1], beginSub, endIfTrue, endIfFalse);
				parse(child_set[0], begin, endIfTrue, beginSub.falseBranch);
			}
		} else {
			Node n = new Node(getContent(node));
			begin.setAll(n);
			n.trueBranch = endIfTrue;
			n.falseBranch = endIfFalse;
		}
	}

	/**
	 * 
	 * @return graph we need to get. Ex: <br/>
	 *         0#root#true=1#false=1<br/>
	 *         1#(a <= 0)#true=3#false=2<br/> 3#(b <= 0)#true=4#false=2<br/>
	 *         4#(1>0)#true=5#false=2<br/> 2#(d>0)#true=5#false=5<br/>
	 *         5#end#true=-1#false=-1<br/> Notice that the output start with
	 *         "0#root#true=1#false=1" and end with
	 *         "[MAX_INT_ID]#end#true=-1#false=-1" by default
	 */
	public String getResult() {
		return ListNodeDescription;
	}

	/**
	 * 
	 * @param complexCondition
	 *            is always put in pair of NGOAC. complex condition is not
	 *            single condition (condition (a>0) is not available) <br/>
	 *            <br/>
	 *            Ex1: (a>0 || b>0) <br/>
	 *            Ex2: (a>0 && b>0)
	 * @throws Exception
	 */
	public void run(String complexCondition) throws Exception {
		String code = "int t()\n" + "{\n" + "	if " + complexCondition + "{}\n" + "}";
		runBlock(code);
		setNodeListID(beginTree);
		getNodeListInString(beginTree.trueBranch);
		ListNodeDescription = arrangeNodeList(ListNodeDescription);
		ListNodeDescription = ListNodeDescription.replace(TEMPORARY_ID_OF_END_NODE + "", rootIndex + "");
	}

	/**
	 * arrange all items (each line is one item) by specified order. For more
	 * details about order, see getResult
	 * 
	 * @param nodeList
	 *            Ex:<br/>
	 *            0#root#true=1#false=1<br/>
	 *            1#a>=0#true=3#false=2<br/>
	 *            3#d>=0#true=4#false=4<br/>
	 *            4#end#true=-1#false=-1<br/>
	 *            2#c>0#true=3#false=4<br/>
	 * 
	 * @return Ex:<br/>
	 *         0#root#true=1#false=1<br/>
	 *         1#a>=0#true=3#false=2<br/>
	 *         3#d>=0#true=4#false=4<br/>
	 *         2#c>0#true=3#false=4<br/>
	 *         4#end#true=-1#false=-1<br/>
	 */
	private String arrangeNodeList(String nodeList) {
		String newNodeList = "";
		String maxNodeId = "";
		for (String item : nodeList.split("\n"))
			if (item.indexOf(TEMPORARY_ID_OF_END_NODE + "") == 0) {
				maxNodeId = item;
			} else
				newNodeList += item + "\n";
		newNodeList += maxNodeId;
		return newNodeList;
	}

	public void setNewRootIndex(int newRootIndex) {
		rootIndex = newRootIndex;
		ListNodeDescription = newRootIndex + "#root#true=" + (newRootIndex + 1) + "#false=" + (newRootIndex + 1) + "\n";
	}

	public static void main(String[] args) throws Exception {
		analysisComplexCondition p = new analysisComplexCondition();
		p.setNewRootIndex(10);
		p.run("((a>=0||c>0)&&d>=0)");
		System.out.println(p.getResult());
	}

	class Node 
	{
		String content;
		Node trueBranch;
		Node falseBranch;
		int ID = 0;
		boolean visited = false;

		Node(String c) 
		{
			this.content = c;
		}

		void setAll(Node n) 
		{
			trueBranch = falseBranch = n;
		}

		void setID(int n) 
		{
			ID = n;
		}
	}
}

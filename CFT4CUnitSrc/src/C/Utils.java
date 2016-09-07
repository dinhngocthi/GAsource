package C;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static boolean stopCriteria = false;
	public static int maxpathcount = 1000;
	public static int[] path = new int[maxpathcount];

	/**
	 * [0]: minute [1]: second
	 * 
	 * @return
	 */
	public static int[] getTime() {
		int startP = Calendar.getInstance().get(Calendar.MINUTE);
		int startS = Calendar.getInstance().get(Calendar.SECOND);
		int startMili = Calendar.getInstance().get(Calendar.MILLISECOND);
		return new int[] { startP, startS };
	}

	public static boolean isCanBeSolvedBySmtSolver(String expression) {
		String[] notSupport = { "!=", "sin", "cos", "sqrt", "pow" };
		for (String item : notSupport) {
			if (expression.contains(item)) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Reduce node list by deleting unneccessary edges. In addition, one new
	 * node is created by merging above nodes.
	 * 
	 * @param nodeList
	 *            Ex:
	 * @return simplified node list
	 */
	public static String simplifyNodeList(String[] nodeList) {
		String output = "";
		/*
		 * Step 1: tao do thi lien ket G
		 */
		ArrayList<Node> nodeArr = new ArrayList<Node>();
		for (int i = 0; i < nodeList.length; i++) {
			nodeArr.add(new Node(getDataOfNodeStr(nodeList[i])));
			nodeArr.get(i).setID(toInt(getIdOfNodeStr(nodeList[i])));
		}
		for (int i = 0; i < nodeArr.size(); i++) {
			if (!isEndNode(nodeArr.get(i))) {
				nodeArr.get(i).setTrue(getNodeById(nodeArr, toInt(getIdTrueBranchOfNodeStr(nodeList[i]))));
				nodeArr.get(i).setFalse(getNodeById(nodeArr, toInt(getIdFalseBranchOfNodeStr(nodeList[i]))));
			}
		}
		/*
		 * Step 2: reduce do thi lien ket G
		 */
		int i = 0;
		while (i < nodeArr.size()) {
			Node n = nodeArr.get(i);
			Node nNext = n.trueBranch;
			// System.out.println(i+":"+n.data);
			if (isEndNode(n) || isBeginNode(n) || isEndNode(n.trueBranch) || isEndNode(n.falseBranch)) {
				i++;
			} else if (!isConditionNode(n) && !isConditionNode(nNext) && laySoConTroTroDenNode(nodeArr, nNext) == 2) {
				n.data += ";" + nNext.data;
				/*
				 * Chuyen con tro tu Node n.next sang Node n
				 */
				n.trueBranch = nNext.trueBranch;
				n.falseBranch = nNext.falseBranch;
				/*
				 * Loai bo Node n.next
				 */
				nodeArr.remove(nNext);
			} else {
				i++;
			}
		}
		/*
		 * Xuat output
		 */
		Node nEnd = null;
		for (Node n : nodeArr) {
			if (!isEndNode(n)) {
				output += n.ID + "#" + n.data + "#true=" + n.trueBranch.ID + "#false=" + n.falseBranch.ID + "\n";
			} else {
				nEnd = n;
			}
		}
		output += nEnd.ID + "#Ket thuc ham#true=-1#false=-1";
		return output;
	}

	private static String getIdOfNodeStr(String node) {
		return node.split("#")[0];
	}

	private static boolean isEndNode(Node node) {
		return node.data.equals("Ket thuc ham");
	}

	private static boolean isBeginNode(Node node) {
		return node.data.equals("Bat dau ham");
	}

	private static String getDataOfNodeStr(String node) {
		return node.split("#")[1];
	}

	private static String getIdTrueBranchOfNodeStr(String node) {
		return node.split("#")[2].replace("true=", "");
	}

	private static String getIdFalseBranchOfNodeStr(String node) {
		return node.split("#")[3].replace("false=", "");
	}

	private static boolean isConditionNode(Node n) {
		try {
			return n.trueBranch.ID != n.falseBranch.ID;
		} catch (Exception e) {
			return false;
		}
	}

	private static int laySoConTroTroDenNode(ArrayList<Node> nodeArr, Node n) {
		int soLienKet = 0;
		for (int j = 0; j < nodeArr.size(); j++) {
			if (!isEndNode(nodeArr.get(j)) && nodeArr.get(j).trueBranch.ID == n.ID) {
				soLienKet++;
			}
		}
		for (int j = 0; j < nodeArr.size(); j++) {
			if (!isEndNode(nodeArr.get(j)) && nodeArr.get(j).falseBranch.ID == n.ID) {
				soLienKet++;
			}
		}
		return soLienKet;
	}

	private static Node getNodeById(ArrayList<Node> nodeArr, int id) {
		if (id == -1) {
			return null;
		}
		for (Node n : nodeArr) {
			if (n.ID == id) {
				return n;
			}
		}
		return null;
	}

	// ----------------------------------------------------------------
	public static ArrayList<String> convertToArrayList(String[] input) {
		ArrayList<String> output = new ArrayList<String>();
		for (String item : input) {
			output.add(item);
		}
		return output;
	}

	public static String[] convertToStringList(ArrayList<String> input) {
		String[] output = new String[input.size()];
		for (int i = 0; i < input.size(); i++) {
			output[i] = input.get(i);
		}
		return output;
	}

	/** 
	 * @param indexList
	 * @param luanlyList
	 * @return 
	 */
	public static String[] getContentOfOuterLoop(String[] indexList, String[] luanlyList) {
		int[] loopIndex = getLoopIndexForSimpleLoopAndNestedLoop(indexList);
		int endOuterLoop = loopIndex[1];
		int startOuterLoop = loopIndex[0];
		//
		String innerLoopLuanLy = "", innerLoopIndex = "";
		for (int tmp = startOuterLoop + 1; tmp <= endOuterLoop - 2; tmp++) {
			innerLoopLuanLy += luanlyList[tmp] + "#";
			innerLoopIndex += indexList[tmp] + " ";
		}
		innerLoopLuanLy += luanlyList[endOuterLoop - 1];
		innerLoopIndex += indexList[endOuterLoop - 1];
		return new String[] { innerLoopLuanLy, innerLoopIndex, luanlyList[endOuterLoop] };
	}

	/**
	 * 
	 * @param indexList
	 * @return
	 */
	public static int[] getLoopIndexForSimpleLoopAndNestedLoop(String[] indexList) {
		for (int i = 0; i <= indexList.length - 1; i++) {
			for (int j = indexList.length - 1; j > i; j--) {
				if (indexList[i].equals(indexList[j])) {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}

	public static boolean isAvailable(String s) {
		return s != null && s.length() > 0;
	}

	public static String[] split(String s, String delimiter) {
		if (s == null || s.length() == 0) {
			return null;
		}
		s = s.replaceAll("^" + Utils.toRegex(delimiter), "");
		s = s.replaceAll(Utils.toRegex(delimiter) + "$", "");
		s = s.replace(delimiter + delimiter, delimiter);
		return s.split(delimiter);
	}

	public static String getRowOfOneDimensionArrayItem(String OneDimensionArrayItem) {
		if (OneDimensionArrayItem.indexOf("[") + 1 == OneDimensionArrayItem.indexOf("]")) {
			return null;
		}
		return OneDimensionArrayItem.substring(OneDimensionArrayItem.indexOf("[") + 1,
				OneDimensionArrayItem.indexOf("]"));
	}

	public static String getRowOfTwoDimensionArrayItem(String TwoDimensionArrayItem) {
		if (TwoDimensionArrayItem.indexOf("[") + 1 == TwoDimensionArrayItem.indexOf("]")) {
			return null;
		}
		return TwoDimensionArrayItem.substring(TwoDimensionArrayItem.indexOf("[") + 1,
				TwoDimensionArrayItem.indexOf("]"));
	}

	public static String getColumnOfTwoDimensionArrayItem(String TwoDimensionArrayItem) {
		if (TwoDimensionArrayItem.lastIndexOf("[") + 1 == TwoDimensionArrayItem.lastIndexOf("]")) {
			return null;
		}
		return TwoDimensionArrayItem.substring(TwoDimensionArrayItem.lastIndexOf("[") + 1,
				TwoDimensionArrayItem.lastIndexOf("]"));
	}

	public static String getNameOfArrayItem(String nameArrayVariale) {
		return nameArrayVariale.substring(0, nameArrayVariale.indexOf("["));
	}

	public static String getIndexOfArrayItem(String nameArrayVariale) {
		return nameArrayVariale.substring(nameArrayVariale.indexOf("["));
	}

	public static String getNameOfVariable(String fullNameVariable) {
		if (Utils.isArrayItem(fullNameVariable)) {
			return fullNameVariable.substring(0, fullNameVariable.indexOf("["));
		} else {
			return fullNameVariable;
		}
	}

	public static int getDimensionOfArrayItem(String nameArrayVariale) {
		int dimension = 0;
		for (int i = 0; i < nameArrayVariale.length(); i++) {
			if (nameArrayVariale.charAt(i) == '[') {
				dimension++;
			}
		}
		return dimension;
	}

	/**
	 * Copy a String to N times.
	 * 
	 * @param data
	 * @param heSo
	 * @return
	 */
	public static String copy(String data, int heSo, String delimiter) {
		if (heSo <= 0) {
			return "";
		} else {
			String output = "";
			for (int i = 1; i <= heSo - 1; i++) {
				output += data + delimiter;
			}
			output += data;
			return output;
		}
	}

	/**
	 * Convert text to html format. Notice that with beginning text, the &gt;
	 * and &lt; replace # characters with red arrows.
	 * 
	 * @param text
	 *            Any text
	 * @return text in html format
	 */
	public static String convertToHtml(String text) {
		return text.replace(">", "&gt;").replace("<", "&lt;").replace("#", "<b style=\"color:red;\">&#8594;</b>");
	}

	/**
	 * Convert normal text to regex.
	 * 
	 * @param text
	 * @return the regex format of text.
	 */
	public static String toRegex(String text) {
		return text.replace("[", "\\[").replace("]", "\\]").replace("(", "\\(").replace(")", "\\)").replace("{", "\\{")
				.replace("}", "\\}").replace(".", "\\.").replace("*", "\\*").replace("+", "\\+");
	}

	/**
	 * 
	 * @param equation
	 * @return true if the equation is put in a pair of parentheses. Ex:<br/>
	 *         equation="(a[2]-n)" return true.<br/>
	 *         equation="(a[2]-n)+1" return false.<br/>
	 */
	public static boolean isSurroundingEquation(String equation) {
		if (equation.indexOf("(") != 0 || equation.lastIndexOf(")") != equation.length() - 1) {
			return false;
		}
		int dem = 0;
		boolean isInner = false;
		for (int i = 0; i < equation.length(); i++) {
			char c = equation.charAt(i);
			if (c == '(') {
				dem++;
				isInner = true;
			}
			if (c == ')') {
				dem--;
			}
			if (dem == 0 && i == equation.length() - 1) {
				return true;
			}
			if (dem == 0 && i != equation.length() - 1 && isInner) {
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param equation
	 * @return true if the equation is put in an Floor function. Ex:<br/>
	 *         equation="floor(a[2]-n)" return true.<br/>
	 *         equation="floor(a[2]-n)+1" return false.<br/>
	 */
	public static boolean isFloorEquation(String equation) {
		if (equation.indexOf("floor(") != 0 || equation.lastIndexOf(")") != equation.length() - 1) {
			return false;
		}
		int dem = 0;
		boolean isInner = false;
		for (int i = 0; i < equation.length(); i++) {
			char c = equation.charAt(i);
			if (c == '(') {
				dem++;
				isInner = true;
			}
			if (c == ')') {
				dem--;
			}
			if (dem == 0 && i == equation.length() - 1) {
				return true;
			}
			if (dem == 0 && i != equation.length() - 1 && isInner) {
				return false;
			}
		}
		return false;
	}

	public static boolean containArrayItem(String expression) {
		return expression.contains("[");
	}

	/**
	 * 
	 * @param variable
	 *            content of variable.
	 * @return
	 */
	public static boolean isArrayItem(String variable) {
		return variable.contains("[");
	}

	/**
	 * This method is identical to replaceAll
	 * 
	 * @param str
	 * @param removeStr
	 * @param rplStr
	 * @return
	 */
	public static String remove(String str, String removeStr, String replaceStr) {
		Pattern p = Pattern.compile(removeStr);
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, replaceStr);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Get characters of a specified file.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String getContentFile(String filePath) throws IOException {
		StringBuilder content = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
			String line = br.readLine();
			while (line != null) {
				content.append(line);
				content.append("\n");
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return content.toString();
	}

	/**
	 * no description.
	 * 
	 * @param file_input
	 * @return
	 */
	public static String getSourceCode(String file_input) {
		String sourceInString = "";
		try {
			FileInputStream fileInp = new FileInputStream(file_input);
			int r;
			while ((r = fileInp.read()) != -1) {
				switch (r) {
				case 13: // enter ASCII
					break;
				case 9:// tab ASCII
					sourceInString += "          ";
					break;
				case 32:// space ASCII
					sourceInString += "  ";
					break;
				default:
					char c = (char) r;
					sourceInString += c;
					break;
				}
			}
			sourceInString += "";
			fileInp.close();
			return sourceInString;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * return true if s is an integer
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * return true if s is an integer. s may put in pair of "(" and ")". Ex:s=5
	 * <br/>
	 * s=(5)<br/>
	 * s=(-5)
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {
		if (s.indexOf("(") == 0) {
			s = s.substring(1, s.length() - 1);
		}
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Get new String without any spaces.
	 * 
	 * @param s
	 * @return
	 */
	public static String getNonSpaceString(String s) {
		return s.replace(" ", "");
	}

	/**
	 * Return an respective array of integer by parsing an array of string.
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static int[] parseStringArray(String[] s) throws Exception {
		int[] i = new int[s.length];
		for (int k = 0; k < i.length; k++) {
			i[k] = Integer.parseInt(s[k] + "");
		}
		return i;
	}

	/**
	 * Generate an integer between [canDuoi, canTren]
	 * 
	 * @param canDuoi
	 *            might be smaller than 0
	 * @param canTren
	 *            might be smaller than 0, but always greater than canDuoi
	 * @return
	 */
	public static int SinhRandomSoNguyen(int canDuoi, int canTren) {
		if (canDuoi == canTren) {
			return canDuoi;
		}
		Random randomGenerator = new Random();
		if (canTren <= 0) {
			return -1 * (randomGenerator.nextInt(-1 * canDuoi - (-1) * canTren) + (-1) * canTren);
		}
		if (canDuoi >= 0) {
			return randomGenerator.nextInt(canTren - canDuoi) + canDuoi;
		}
		// canDuoi <0, canTren>0
		return randomGenerator.nextInt(canTren + (-1) * canDuoi) + canDuoi;
	}

	/**
	 * Return the respective integer if s is an integer. in other cases, return
	 * 999999999 .
	 * 
	 * @param s
	 * @return
	 */
	public static int toInt(String s) {
		if (isInt(s)) {
			return Integer.parseInt(s);
		}
		return 999999999;
	}

	/**
	 * Convert an array of integer numbers to a String. Each integer in array
	 * seperates by space
	 * 
	 * @param arr
	 * @return a String <br/>
	 *         Ex: arr = {1,2,3} <br/>
	 *         Output="1 2 3 "
	 */
	public static String toString(int[] arr) {
		String output = "";
		for (int i : arr) {
			output += i + " ";
		}
		return output;
	}

	/**
	 * 
	 * @param expression
	 * @return
	 */
	public static String convertCharacterToInt(String expression) {
		for (int i = 32; i <= 126; i++) {
			expression = expression.replace("'" + (char) i + "'", i + "");
		}
		return expression;
	}

	/**
	 * 
	 * @param a
	 *            first children of parent
	 * @param b
	 *            second children of parent
	 * @param parent
	 *            contains at most and at least two children by parsing AST.
	 * @return true if the connection operator between two children is AND
	 *         operator, otherwise return false
	 */
	public static boolean isAndDelimiter(String a, String b, String parent) {
		return parent.replace(a, "").replace(b, "").contains("&&");
	}

	public static void main(String[] args) {
		System.out.println(Utils.isFloorEquation("floor(floor(a)-floor(a))"));
		System.out.println(Utils.SinhRandomSoNguyen(3, 7));
	}

}

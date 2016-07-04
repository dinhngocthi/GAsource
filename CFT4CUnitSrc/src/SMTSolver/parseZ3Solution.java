package SMTSolver;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Phân tích nghiệm biểu diễn trong Z3 về dạng cơ bản
 * 
 * @author anhanh
 * 
 */
public class parseZ3Solution {
	private String Z3Solution;
	private String solution;

	public parseZ3Solution(String Z3Solution) {
		this.Z3Solution = Z3Solution;
		solution = parse(Z3Solution);

	}

	public static void main(String[] args) {
		String Z3Solution = "unknown\n(error \"line 27 column 10: model is not available\")";
		parseZ3Solution parser = new parseZ3Solution(Z3Solution);
		System.out.println(parser.getSolution());
	}

	private String parse(String Z3Solution) {
		String solution = "";
		String[] lineList = Z3Solution.split("\n");
		String name = "";
		String value = "";
		boolean ignoreEndLine = false;
		for (String line : lineList) {
			switch (getTypeOfLine(line)) {
			case KHAI_BAO_ID:
				ignoreEndLine = false;
				name = getName(line);
				break;
			case IF_THEN_ELSE_ID:
				ArrayList<String> indexList = getIndex(line);
				value = getValueOfIte(line);
				solution += name;
				for (String i : indexList)
					solution += "[" + i + "]";
				solution += "=" + value + ",";
				ignoreEndLine = true;
				break;
			case VALUE_ID:
				if (!ignoreEndLine) {
					value = getValueOfVariable(line);
					solution += name + "=" + value + ",";
				}
				break;
			}
		}
		return solution;
	}

	/**
	 * Lấy tên hàm
	 * 
	 * @param defineFun
	 * @return
	 */
	private String getName(String defineFun) {
		String nameFunction = "";
		Matcher m = Pattern.compile("\\(define-fun\\s(\\w+)").matcher(defineFun);
		while (m.find()) {
			nameFunction = m.group(1);
			break;
		}
		return nameFunction;
	}

	/**
	 * Lấy các chỉ số của hàm
	 * 
	 * @param ifThenElse
	 * @return
	 */
	private ArrayList<String> getIndex(String ifThenElse) {
		ArrayList<String> indexList = new ArrayList<>();
		Matcher m = Pattern.compile("=\\s(\\w+)!(\\d+)\\s([^\\)]+)").matcher(ifThenElse);
		while (m.find()) {
			indexList.add(m.group(3));
		}
		return indexList;
	}

	/**
	 * Lấy giá trị của biến mảng, với giá trị chỉ số đã xác định ở hàm khác
	 * 
	 * @param ifThenElse
	 * @return
	 */
	private String getValueOfIte(String ifThenElse) {
		String value = "";
		Matcher m = Pattern.compile("(\\(=\\s\\w+!\\d+\\s\\d+\\)\\s*)+(.*)").matcher(ifThenElse);
		while (m.find()) {
			value = m.group(2).replace(") ", "").replace(" ", "");
			;
			break;
		}
		return value;
	}

	/**
	 * Lấy giá trị biến số nguyên, số thực
	 * 
	 * @param src
	 * @return
	 */
	private String getValueOfVariable(String line) {
		String value = "";
		Matcher m = Pattern.compile("(.*)\\)").matcher(line);
		while (m.find()) {
			value = m.group(1).replace(" ", "");
			break;
		}
		return value;
	}

	/**
	 * Xác định loại dòng lệnh
	 * 
	 * @param line
	 * @return
	 */
	private int getTypeOfLine(String line) {
		if (line.contains(KHAI_BAO))
			return KHAI_BAO_ID;
		else if (line.contains(IF_THEN_ELSE))
			return IF_THEN_ELSE_ID;
		else if (line.contains(MODEL))
			return MODEL_ID;
		else if (line.equals(END))
			return END_ID;
		else if (line.equals(SAT))
			return SAT_ID;
		else if (line.equals(UNSAT))
			return UNSAT_ID;
		else if (line.equals(UNKNOWN))
			return UNKNOWN_ID;
		return VALUE_ID;
	}

	public String getSolution() {
		return solution;
	}

	private static final String KHAI_BAO = "define-fun";
	private static final String IF_THEN_ELSE = "ite";
	private static final String END = ")";
	private static final String MODEL = "model";
	private static final String SAT = "sat";
	private static final String UNSAT = "unsat";
	private static final String UNKNOWN = "unknown";
	private static final int KHAI_BAO_ID = 0;
	private static final int MODEL_ID = -1;
	private static final int IF_THEN_ELSE_ID = 1;
	private static final int VALUE_ID = 2;
	private static final int END_ID = 3;
	private static final int SAT_ID = 4;
	private static final int UNSAT_ID = 5;
	private static final int UNKNOWN_ID = 6;
}

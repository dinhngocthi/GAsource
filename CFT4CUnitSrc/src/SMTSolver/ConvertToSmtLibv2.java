package SMTSolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import C.ChuongTrinhChinh;


public class ConvertToSmtLibv2 {
	private String expression;
	private Map<String, String> arrayItemMap;

	public ConvertToSmtLibv2(String expression) {
		this.expression = expression;
		arrayItemMap = new HashMap<>();
	}

	public void run() {
		expression = new ConvertNotEqual(expression).getOutput();
		expression = replaceArrayItem(expression, arrayItemMap);
		expression = new ConvertToSmtLib(expression).getSmt_Lib_Expression();
		for (String arrayItem : arrayItemMap.keySet()) {
			String arrayItemSmtLib = new String("(" + arrayItem.split("\\[")[0] + " ");
			for (String index : getArrayIndexList(arrayItem)) {
				try {
					arrayItemSmtLib += Integer.parseInt(index) + " ";
				} catch (Exception e) {
					arrayItemSmtLib += new ConvertToSmtLib(index).getSmt_Lib_Expression() + " ";
				}
			}
			arrayItemSmtLib += ")";
			String replacement = arrayItemMap.get(arrayItem).toString();
			expression = expression.replace(replacement, arrayItemSmtLib);
			standlizeOutput();
		}
	}

	public String getOutput() {
		return expression;
	}

	public static void main(String[] args) throws IOException, InterruptedException 
	{
	    //String expression = "(a+b>c)&&(b+c>a)&&(c+a>b)";
	    String expression = "(a!=b)&&(b!=c)&&(c!=a)";
	    //String expression = "((a==b)&&(b!=c))||((b==c)&&(c!=a))||((c==a)&&(a!=b))";
	    //String expression = "b * b == 4 * a * c";
		//String expression = "b == c";
		//ConvertToSmtLibv2 c = new ConvertToSmtLibv2("(C==D/((B-2)-1))");
		ConvertToSmtLibv2 c = new ConvertToSmtLibv2(expression);
		c.run();
		System.out.println("(assert " +  c.getOutput() + ")");

        String smtFileName = "test.smt2";
        PrintWriter fpSmt = new PrintWriter(smtFileName, "UTF-8");
        // z3
        //fpSmt.printf("(declare-const a Real)\n");
        //fpSmt.printf("(declare-const b Real)\n");
        //fpSmt.printf("(declare-const c Real)\n");

        // yices                
        fpSmt.printf("(set-logic QF_NRA)\n");
        fpSmt.printf("(declare-fun a () Real)\n");
        fpSmt.printf("(declare-fun b () Real)\n");
        fpSmt.printf("(declare-fun c () Real)\n");
        fpSmt.printf("(assert (> a 0))\n");
        fpSmt.printf("(assert (> b 0))\n");
        fpSmt.printf("(assert (> c 0))\n");
        
        fpSmt.printf(c.getOutput() + "\n");
        
        fpSmt.printf("(check-sat)\n");
        fpSmt.printf("(get-model)");
        fpSmt.close();

        String classPath     = ConvertToSmtLibv2.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String smtSolverPath = classPath.replace("CFT4CUnitSrc/bin/", "z3/bin/Z3");  // Using smt solver Z3
//        String smtSolverPath = classPath.replace("CFT4CUnitSrc/bin/", "yices/bin/yices-smt2");  // Using smt solver yices
        RunZ3OnCMD r = new RunZ3OnCMD(smtSolverPath, smtFileName);
        System.out.println(r.getOutput());
	}

	private void standlizeOutput() {
		expression = expression.replaceAll("\\s*\\(\\s*", "(").replaceAll("\\s*\\)\\s*", ")").replaceAll("\\s+", " ");
	}

	private String[] getArrayIndexList(String arrayItem) {
		// Ex: arrayItem=a[3][3+x]
		return arrayItem./* step 1 */substring(arrayItem.indexOf("[") + 1)
				./* step 2 */replace("[", " ").replace("]", " ")./* step 3 */replaceAll("\\s+", " ")
				./* step 4 */split(" ");
	}

	private ArrayList<String> getArrayItemList(String expression) {
		ArrayList<String> arrayItemList = new ArrayList<>();
		// do something here
		Matcher m = Pattern.compile("\\w+(\\[([^\\]])+\\])+").matcher(expression);
		while (m.find()) {
			if (!arrayItemList.contains(m.group(0)))
				arrayItemList.add(m.group(0));
		}
		return arrayItemList;
	}

	private String replaceArrayItem(String expression, Map<String, String> arrayItemMap) {
		final String PREFIX = "tvw";
		int startPREFIX = 65;
		for (String arrayItem : getArrayItemList(expression)) {
			String replacement = PREFIX + (char) (startPREFIX++);
			arrayItemMap.put(arrayItem, replacement);
			expression = expression.replace(arrayItem, replacement);
		}
		return expression;
	}
}

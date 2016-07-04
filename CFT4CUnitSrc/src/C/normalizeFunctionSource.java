package C;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Normalize source code of C function by removing unnecessary spaces and
 * converting SWITCH blocks to IF blocks.
 */
public class normalizeFunctionSource {

	private String source;

	public normalizeFunctionSource(String source) {
		this.source = source;
		run();
	}

	public static void main(String[] args) {
		new normalizeFunctionSource(
				"int switchExample(char grade){int max;switch(grade){case 'A':case 'B':switch(max){case 0:max=1;break;default:break;}break;case 'D':case 'F':max=2;break;default:break;}return -1;}")
						.run();
	}

	/**
	 * 
	 * @param str
	 *            an assignment expression looks like: <br/>
	 *            Ex: max = (a>b)?(a:b) <br/>
	 *            (int max = (a>b)?(a:b) isnt available)
	 * @return the respective if block. <br/>
	 *         Ex: if (a>b) max = a; else max = b;
	 */
	private String convertSimplifiedIf(String str) {
		Pattern p = Pattern.compile("([\\[\\]\\w]+)=([^\\?]+)\\?([^:]+):([^;]+);");
		Matcher m = p.matcher(str);
		while (m.find()) {
			str = str.replace(m.group(0), "if (" + m.group(2) + ") " + m.group(1) + "=" + m.group(3) + "; else "
					+ m.group(1) + "= " + m.group(4) + ";");
		}
		return str;
	}

	private String removeComment(String code) {
		code = code.replaceAll("//[^\n]*", "");
		code = code.replaceAll("/\\*.*\\*/", "");
		return code;
	}

	/**
	 * @param code
	 *            doesnot contains any comments.
	 * @return the new String without any unnecessary spaces.
	 */
	private String removeUnnecessarySpaces(String code) {
		code = code.replaceAll("/{2}[^//^]+", "");
		code = code.replaceAll("\t{1,}", " ");
		code = code.replaceAll("\n", "");
		code = code.replaceAll("  ", " ");
		code = code.replaceAll("\\s*,\\s*", ",");
		code = code.replaceAll("\\s*\\(\\s*", "(");
		code = code.replaceAll("\\s*\\)\\s*", ")");
		code = code.replaceAll("\\s*\\]\\s*", "]");
		code = code.replaceAll("\\s*\\[\\s*", "[");
		code = code.replaceAll("\\s*;\\s*", ";");
		code = code.replaceAll("\\s*=\\s*", "=");
		code = code.replaceAll("\\s*\\+\\s*", "+");
		code = code.replaceAll("\\s*-\\s*", "-");
		code = code.replaceAll("\\s*\\*\\s*", "*");
		code = code.replaceAll("\\s*/\\s*", "/");
		code = code.replaceAll("\\s*>\\s*", ">");
		code = code.replaceAll("\\s*<\\s*", "<");
		code = code.replaceAll("\\s*\\{\\s*", "{");
		code = code.replaceAll("\\s*\\}\\s*", "}");
		code = code.replaceAll("\\s*&&\\s*", "&&");
		code = code.replaceAll("\\s*\\|\\|\\s*", "||");
		code = code.replaceAll("\\s*:\\s*", ":");
		code = code.replaceAll("\\s*!\\s*", "!");
		// fix error
		code = code.replaceAll("\\s*return-\\s*", "return -");
		// add later
		code = code.replaceAll("\\s*%\\s*", "%");
		return code;
	}

	/**
	 * Get the max position of SWITCH block in source. it might be the position
	 * of innest SWITCH block or the single final SWITCH block (in case
	 * continous SWITCH blocks).
	 * 
	 * @param source
	 * @return if source does not contains any SWITCH blocks, return -1
	 */
	private int getMaxSwitchBlockPosition(String source) {
		String[] matchStr = { ";switch(", "}switch(", "{switch(", ":switch(" };
		int[] posSwitchBlockPosition = new int[matchStr.length];
		for (int i = 0; i < matchStr.length; i++)
			posSwitchBlockPosition[i] = source.lastIndexOf(matchStr[i]) + 1;
		int maxSwitchBlockPosition = -1;
		for (int i : posSwitchBlockPosition)
			maxSwitchBlockPosition = maxSwitchBlockPosition < i ? i : maxSwitchBlockPosition;
		return maxSwitchBlockPosition == 0 ? maxSwitchBlockPosition - 1 : maxSwitchBlockPosition;
	}

	/**
	 * Get the innest SWITCH block of source.
	 * 
	 * @param source
	 * @return return null if source does not contains any SWITCH blocks.
	 */
	private String getInnestSwitchBlock(String source) {
		int beginSwitchBlockPos = getMaxSwitchBlockPosition(source);
		if (beginSwitchBlockPos == -1)
			return null;
		int endSwitchBlockPos = beginSwitchBlockPos + 1;
		int dem = 0;
		while (!(dem == 1 && source.charAt(endSwitchBlockPos) == '}')) {
			char T = source.charAt(endSwitchBlockPos);
			if (T == '{')
				dem++;
			else if (T == '}')
				dem--;
			endSwitchBlockPos++;
			if (endSwitchBlockPos == source.length() - 1)
				break;
		}
		String innestSwitchBlock = source.substring(beginSwitchBlockPos, endSwitchBlockPos + 1);
		return innestSwitchBlock;
	}

	public void run() {
		source = removeComment(source);
		source = removeUnnecessarySpaces(source);
		source = convertSimplifiedIf(source);
		while (getInnestSwitchBlock(source) != null) {
			String innestSwitchBlock = getInnestSwitchBlock(source);
			ConvertSwitchToIf c = new ConvertSwitchToIf(innestSwitchBlock);
			c.run();
			source = source.replace(innestSwitchBlock, c.getIfBlock());
		}
		source = removeUnnecessarySpaces(source);
		// System.out.println(source);
	}

	/**
	 * 
	 * @return normalize source code which doesnot contain any unnecessary
	 *         spaces as well as SWITCH-CASE block. All characters of the String
	 *         is put in a single line.
	 */
	public String getNormalizeCode() {
		return source;
	}

	/**
	 * This class used to parse a SWITCH block to the respective IF block.
	 * 
	 * @author anhanh
	 * 
	 */
	private class ConvertSwitchToIf {
		/**
		 * 
		 * Ex:<br/>
		 * switch (grade){<br/>
		 * &nbsp;&nbsp;&nbsp;case 'A': a=1;break;<br/>
		 * &nbsp;&nbsp;&nbsp;default: break;<br/>
		 */
		private String switchBlock;
		/**
		 * The condition var of Switch Block.<br/>
		 * Ex: The folowing example has condition var named "grade".<br/>
		 * switch (grade){<br/>
		 * &nbsp;&nbsp;&nbsp;case 'A': a=1;break;<br/>
		 * &nbsp;&nbsp;&nbsp;default: break;<br/>
		 * }
		 */
		private String conditionVar;

		ConvertSwitchToIf(String switchBlock) {
			conditionVar = "";
			this.switchBlock = switchBlock;
		}

		/**
		 * 
		 * @return the respective IF block of SWITCH block. Notice that the
		 *         respective IF block doesnot contain any unnecessary spaces
		 *         <br/>
		 *         Ex:<br/>
		 *         switch (grade){<br/>
		 *         &nbsp;&nbsp;&nbsp;case 'A': a=1;break;<br/>
		 *         &nbsp;&nbsp;&nbsp;default: break;<br/>
		 *         <br/>
		 *         The output is:<br/>
		 *         if(grade=='A') a=1;
		 */
		private String getIfBlock() {
			return switchBlock;
		}

		/**
		 * Replace single CASE by respective IF statement
		 * 
		 * @param switchBlock
		 * @return
		 */
		private String convertSingleCASEToIF(String switchBlock) {
			String regex = "(case\\s([^:]+):)";
			Matcher m = Pattern.compile(regex).matcher(switchBlock);
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				String value = "(" + conditionVar + "==" + m.group(2) + ")";
				m.appendReplacement(sb, "}else if" + value.toString() + "{");
			}
			m.appendTail(sb);
			return sb.toString();
		}

		/**
		 * Replace continous CASEs by respective IF statement
		 * 
		 * @param switchBlock
		 * @return
		 */
		private String convertContinousCASEsToIF(String switchBlock) {
			String regex = "(case\\s([^:]+):){2,}";
			Matcher m = Pattern.compile(regex).matcher(switchBlock);
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				String value = m.group(0);
				value = m.group(0).replace("case ", "(" + conditionVar + "==");
				value = value.replace(":", ")||");
				value = value.substring(0, value.lastIndexOf("|") - 1);
				value = "}else if(" + value + "){";
				m.appendReplacement(sb, value);
			}
			m.appendTail(sb);
			return sb.toString();
		}

		/**
		 * Get condition of SWITCH block.
		 * 
		 * @param switchBlock
		 * @return the condition of specfied switch block.
		 */
		private String getCondition(String switchBlock) {
			Matcher m = Pattern.compile("switch\\(([^\\)]+)\\)").matcher(switchBlock);
			if (m.find()) {
				return m.group(1);
			}
			return "";
		}

		private void run() {
			switchBlock = switchBlock.replace("default:break;", "");
			conditionVar = getCondition(switchBlock);
			switchBlock = Utils.remove(switchBlock, "\\bbreak;", "");
			switchBlock = convertContinousCASEsToIF(switchBlock);
			switchBlock = convertSingleCASEToIF(switchBlock);
			switchBlock = switchBlock.substring(switchBlock.indexOf("if"));
			switchBlock = switchBlock.replace("default:", "}else{");
		}
	}
}

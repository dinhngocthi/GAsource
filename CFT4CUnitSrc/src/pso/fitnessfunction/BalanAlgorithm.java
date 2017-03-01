package pso.fitnessfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class BalanAlgorithm {

    private String expression = null;
    private static final String[] BRACKET_MARKS = new String[]{"BRACKET_MARK", "()", "[]"};
    private static final String DOUBLE_REGEX = "[-+]{0,1}[0-9]+[.]{0,1}[0-9]+";

    private String bracketMark = "";
    private boolean isNegative = false;
    private List<String> parameterNames = new ArrayList<String>();
    private List<Double> parameterValues = new ArrayList<Double>();

    public BalanAlgorithm(String expression) {
        this.expression = expression;
        removeNoneExpressionChars();
        selectMarks();
        parseAllParameterNames();
    }

    public void setExpression(String expression) {
        this.expression = expression;
        removeNoneExpressionChars();
        selectMarks();
        parseAllParameterNames();
    }

    public String getExpression() {
        return expression;
    }

    public boolean isNegative() {
        return isNegative;
    }

    private int getParameterIndex(String parameterName) {
        for (int i = 0; i < parameterNames.size(); i++) {
            if (parameterNames.get(i).equals(parameterName)) {
                return i;
            }
        }
        return -1;
    }

    public void setParameter(String parameterName, double value) throws IllegalArgumentException {
        int index = getParameterIndex(parameterName);
        if (index == -1) {
            throw new IllegalArgumentException("Parameter not found");
        } else {
            parameterNames.set(index, parameterName);
            parameterValues.set(index, value);
        }
    }

    public void setParameters(HashMap<String, Double> parameters) throws IllegalArgumentException {
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            setParameter(key, parameters.get(key));
        }
    }

    public void setParameters(double[] parameters) throws IllegalArgumentException {
        setParameters(parameters, false);
    }

    public void setParameters(double[] parameters, boolean allowSizeLarger) throws IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters null");
        }
        if (allowSizeLarger && parameters.length >= parameterNames.size()) {

        } else if (parameterNames.size() != parameters.length) {
            throw new IllegalArgumentException("Size of parameters passed to this method is not match size of parameters in the expression");
        }
        for (int i = 0; i < parameterNames.size(); i++) {
            parameterValues.set(i, parameters[i]);
        }
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    private void parseAllParameterNames() {
        parameterNames.clear();
        parameterValues.clear();
        List<String> parameterList = new ArrayList<String>();
        Stack<String> postfixStack = convertExpressionToPostfix();
        for (String postfixItem : postfixStack) {
            if (postfixItem.equals(bracketMark)) {

            } else if (isOperator(postfixItem) == 0) {
                try {
                    int lastCharIndex = postfixItem.length() - 1;
                    char lastChar = postfixItem.charAt(lastCharIndex);
                    String itemWithoutLastChar = postfixItem.substring(0, lastCharIndex);
                    if ((lastChar == 'L' || lastChar == 'l') && itemWithoutLastChar.matches(DOUBLE_REGEX)) {
                        postfixItem = itemWithoutLastChar;
                    }
                    Double.valueOf(postfixItem);
                } catch (Exception e) {
                    parameterList.add(postfixItem);
                }
            }
        }
        for (String parameter : parameterList) {
            int index = getParameterIndex(parameter);
            if (index == -1) {
                parameterNames.add(parameter);
                parameterValues.add(Double.NaN);
            }
        }
    }

    public void dumpPostfixStack() {
        Stack<String> postfixStack = convertExpressionToPostfix();
        System.out.println("========== DUMP POSTFIX ========== START");
        String dumpString = "";
        for (String postfixItem : postfixStack) {
            dumpString += postfixItem + ",";
        }
        System.out.println(dumpString.isEmpty() ? "" : dumpString.substring(0, dumpString.length() - 1));
        System.out.println("========== DUMP POSTFIX ========== END");
    }

    public void convertToDeMorganExpression() {
        Stack<String> postfixStack = convertExpressionToPostfix();
        Stack<String> resultStack = new Stack<String>();
        for (String postfixItem : postfixStack) {
            if (postfixItem.equals(bracketMark)) {
                resultStack.push("(" + resultStack.pop() + ")");
            } else if (isOperator(postfixItem) == 0) {
                resultStack.push(postfixItem);
            } else {
                String lastItem = resultStack.pop();
                String firstItem = resultStack.pop();
                String negativeOperator = "";
                switch (postfixItem) {
                    case "*":
                    case "/":
                    case "%":
                    case "+":
                    case "-":
                        negativeOperator = postfixItem;
                        break;
                    case ">=":
                        negativeOperator = "<";
                        break;
                    case ">":
                        negativeOperator = "<=";
                        break;
                    case "<=":
                        negativeOperator = ">";
                        break;
                    case "<":
                        negativeOperator = ">=";
                        break;
                    case "!=":
                        negativeOperator = "==";
                        break;
                    case "==":
                        negativeOperator = "!=";
                        break;
                    case "&&":
                        negativeOperator = "||";
                        break;
                    case "||":
                        negativeOperator = "&&";
                        break;
                }
                resultStack.push(firstItem + negativeOperator + lastItem);
            }
        }
        expression = resultStack.pop();
        isNegative = !isNegative;
    }

    public static List<String> parseAllExpressions(String allExpression) {
        List<String> expressions = new ArrayList<String>();
        int pos = 0;
        while (true) {
            int start = allExpression.indexOf("[", pos);
            int[] ends = new int[4];
            ends[0] = allExpression.indexOf("]F", pos);
            ends[1] = allExpression.indexOf("]T", pos);
            ends[2] = allExpression.indexOf("F]", pos);
            ends[3] = allExpression.indexOf("T]", pos);
            int end = 0;
            List<Integer> endFound = new ArrayList<Integer>();
            for (int i = 0; i < ends.length; i++) {
                if (ends[i] != -1) {
                    endFound.add(ends[i]);
                }
            }
            if (start == -1 || endFound.isEmpty()) {
                break;
            }
            if (endFound.size() == 1) {
                end = endFound.get(0) + 2;
            } else {
                end = endFound.get(0);
                for (int i = 1; i < endFound.size(); i++) {
                    end = Math.min(end, endFound.get(i));
                }
                end += 2;
            }
            String exp = allExpression.substring(start, end);
            pos = end - 1;
            expressions.add(exp);
        }
        return expressions;
    }

    public void dumpAllParameters() {
        String fullExpression = "[" + expression + (isNegative ? "]F" : "]T");
        System.out.println("========== DUMP PARAMETERS: " + fullExpression + " ========== START");
        for (int i = 0; i < parameterNames.size(); i++) {
            System.out.println(parameterNames.get(i) + " = " + parameterValues.get(i));
        }
        System.out.println("========== DUMP PARAMETERS: " + fullExpression + " ========== END");
    }

    public static double GetBranchDistance(double x, double y, String strOpType) 
    {
        double ret = 0;
        final double k = 0.1;
        int opType = 0;
        if (strOpType.equals("==")) {
            opType = 3;
        } else if (strOpType.equals("!=")) {
            opType = 4;
        } else if (strOpType.equals("<")) {
            opType = 5;
        } else if (strOpType.equals("<=")) {
            opType = 6;
        } else if (strOpType.equals(">")) {
            opType = 7;
        } else if (strOpType.equals(">=")) {
            opType = 8;
        }
        switch (opType) {
            case 3: // == condition
                if (Math.abs(x - y) == 0) {
                    ret = 0;
                } else {
                    ret = Math.abs(x - y) + k;
                }
                break;
            case 4: // != condition
                if (Math.abs(x - y) != 0) {
                    ret = 0;
                } else {
                    ret = k;
                }
                break;
            case 5: // < condition
                if (x - y < 0) {
                    ret = 0;
                } else {
                    ret = Math.abs(x - y) + k;
                }
                break;
            case 6: // <= condition
                if (x - y <= 0) {
                    ret = 0;
                } else {
                    ret = Math.abs(x - y) + k;
                }
                break;
            case 7: // > condition
                if (y - x < 0) {
                    ret = 0;
                } else {
                    ret = Math.abs(y - x) + k;
                }
                break;
            case 8: // >= condition
                if (y - x <= 0) {
                    ret = 0;
                } else {
                    ret = Math.abs(y - x) + k;
                }
                break;
            default:
                break;
        }
        return ret;
    }

    private void removeNoneExpressionChars() {
        expression = expression.trim();
        if (expression.charAt(0) == '[') {
            expression = expression.substring(1);
        }
        String subString = expression.substring(expression.length() - 2);
        if (subString.equals("T]") || subString.equals("F]") || subString.equals("]T") || subString.equals("]F")) {
            isNegative = (subString.equals("F]") || subString.equals("]F"));
            expression = expression.substring(0, expression.length() - 2);
            expression = expression.trim();
            if (expression.charAt(expression.length() - 1) == ',') {
                expression = expression.substring(0, expression.length() - 1);
            }
        }
    }

    private void selectMarks() {
        for (String bracketMarkItem : BRACKET_MARKS) {
            if (!expression.contains(bracketMarkItem)) {
                bracketMark = bracketMarkItem;
                break;
            }
        }
    }

    private int getPriority(String operator) {
        switch (operator) {
            case "*":
            case "/":
            case "%":
                return 4;
            case "+":
            case "-":
                return 3;
            case ">=":
            case ">":
            case "<=":
            case "<":
            case "!=":
            case "==":
                return 2;
            case "&&":
            case "||":
                return 1;
            default:
                return 0;
        }
    }

    private int isOperator(String operator) {
        if (getPriority(operator) == 0) {
            switch (operator) {
                case "(":
                case ")":
                    return 1;
                default:
                    return 0;
            }
        }
        return 2;
    }

    private boolean isLongOperator(int index) {
        if (expression.length() - 1 == index) {
            return false;
        }
        char s = expression.charAt(index);
        char next = expression.charAt(index + 1);
        if (s == '&' && next == '&') {
            return true;
        } else if (s == '|' && next == '|') {
            return true;
        } else if (s == '>' && next == '=') {
            return true;
        } else if (s == '<' && next == '=') {
            return true;
        } else if (s == '!' && next == '=') {
            return true;
        } else if (s == '=' && next == '=') {
            return true;
        }
        return false;
    }

    private void dump(Stack<String> stack) {
        System.out.print("\n[");
        for (String s : stack) {
            System.out.print(s + ",");
        }
        System.out.println("]");
    }

    public Stack<String> convertExpressionToPostfix() {
        Stack<String> stack = new Stack<String>();
        Stack<String> output = new Stack<String>();
        String compareExpression = "";
        for (int i = 0; i < expression.length(); i++) {
            int endIndex = isLongOperator(i) ? i + 2 : i + 1;
            String s = expression.substring(i, endIndex);
            if (isOperator(s) == 0) {
                compareExpression += s;
            } else {
                compareExpression = compareExpression.trim();
                if (!compareExpression.isEmpty()) {
                    output.push(compareExpression);
                    compareExpression = "";
                }
                if (isOperator(s) == 1) {
                    if (s.equals("(")) {
                        stack.push(s);
                    } else if (s.equals(")")) {
                        String pop = stack.pop();
                        while (!pop.equals("(")) {
                            output.push(pop);
                            pop = stack.pop();
                        }
                        output.push(bracketMark);
                    }
                } else {
                    while (!stack.empty() && getPriority(stack.peek()) >= getPriority(s)) {
                        output.push(stack.pop());
                    }
                    stack.push(s);
                }
            }
            i += (endIndex - i - 1);
        }
        compareExpression = compareExpression.trim();
        if (!compareExpression.isEmpty()) {
            output.push(compareExpression);
        }
        while (!stack.empty()) {
            output.push(stack.pop());
        }
        return output;
    }

    private double getValue(String item) throws BalanException {
        item = item.trim();
        int index = getParameterIndex(item);
        if (index != -1) {
            return parameterValues.get(index);
        }
        try {
            int lastCharIndex = item.length() - 1;
            char lastChar = item.charAt(lastCharIndex);
            String itemWithoutLastChar = item.substring(0, lastCharIndex);
            if ((lastChar == 'L' || lastChar == 'l') && itemWithoutLastChar.matches(DOUBLE_REGEX)) {
                item = itemWithoutLastChar;
            }
            return Double.valueOf(item);
        } catch (Exception e) {
            throw new BalanException(e.getMessage());
        }
    }

    private double min(double a, double b) {
        return Math.min(a, b);
    }

    public double calculateBranchExpression() throws BalanException {
        for (Double parameterValue : parameterValues) {
            if (parameterValue.equals(Double.NaN)) {
                throw new BalanException("Some parameters are not initialized");
            }
        }
        Stack<String> postfixStack = convertExpressionToPostfix();
        Stack<String> resultStack = new Stack<String>();
        for (String postfixItem : postfixStack) {
            if (postfixItem.equals(bracketMark)) {

            } else if (isOperator(postfixItem) == 0) {
                resultStack.push(postfixItem);
            } else {
                double lastItem = getValue(resultStack.pop());
                double firstItem = getValue(resultStack.pop());
                switch (postfixItem) {
                    case "*":
                        resultStack.push((firstItem * lastItem) + "");
                        break;
                    case "/":
                        resultStack.push((firstItem / lastItem) + "");
                        break;
                    case "%":
                        resultStack.push((firstItem % lastItem) + "");
                        break;
                    case "+":
                        resultStack.push((firstItem + lastItem) + "");
                        break;
                    case "-":
                        resultStack.push((firstItem - lastItem) + "");
                        break;
                    case ">=":
                    case ">":
                    case "<=":
                    case "<":
                    case "!=":
                    case "==":
                        resultStack.push(GetBranchDistance(firstItem, lastItem, postfixItem) + "");
                        break;
                    case "&&":
                        resultStack.push((firstItem + lastItem) + "");
                        break;
                    case "||":
                        resultStack.push((min(firstItem, lastItem)) + "");
                        break;
                }
            }
        }
        double returnVal = Double.valueOf(resultStack.pop());
        return returnVal;
    }

}

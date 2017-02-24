package pso.fitnessfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class BalanAlgorithm {

    private String expression = null;

    private static final String[] APPEND_MARKS = new String[]{"EXP_CONVERTED:", "MARK_AS_CONVERTED:"};
    private static final String[] BRACKET_MARKS = new String[]{"BRACKET_MARK", "()", "[]"};
    private static final boolean REMOVE_ROOT_BRACKET = true;

    private String appendMark = "";
    private String bracketMark = "";
    private boolean isNegative = false;
//    private HashMap<String, Double> parameters = new HashMap<String, Double>();
    private List<String> parameterNames = new ArrayList<String>();
    private List<Double> parameterValues = new ArrayList<Double>();

    class BalanException extends Exception {

        public BalanException(String message) {
            super(message);
        }
    }

    public BalanAlgorithm(String expression) {
        this.expression = expression;
        removeNoneExpressionChars();
        selectMarks();
    }

    public void setExpression(String expression) {
        this.expression = expression;
        removeNoneExpressionChars();
        selectMarks();
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

    public void setParameter(String parameterName, double value) {
        int index = getParameterIndex(parameterName);
        if (index == -1) {
            parameterNames.add(parameterName);
            parameterValues.add(value);
        } else {
            parameterNames.set(index, parameterName);
            parameterValues.set(index, value);
        }
    }

    public void setParameters(HashMap<String, Double> parameters) {
        parameterNames.clear();
        parameterValues.clear();
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            parameterNames.add(key);
            parameterValues.add(parameters.get(key));
        }
    }

    public void setParameters(double[] parameters) throws IllegalArgumentException {
        setParameters(parameters, false);
    }

    public void setParameters(double[] parameters, boolean allowSizeLarger) throws IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters null");
        }
        parameterNames.clear();
        parameterValues.clear();
        List<String> parameterList = new ArrayList<String>();
        Stack<String> prefixStack = convertExpressionToPrefix();
        for (String prefixItem : prefixStack) {
            if (prefixItem.equals(bracketMark)) {

            } else if (isOperator(prefixItem) == 0) {
                if (!prefixItem.matches("[0-9]+")) {
                    parameterList.add(prefixItem);
                }
            }
        }
        for (int i = 0; i < parameterList.size(); i++) {
            int index = getParameterIndex(parameterList.get(i));
            if (index == -1) {
                parameterNames.add(parameterList.get(i));
                parameterValues.add(0.0);
            } else {
                parameterNames.set(index, parameterList.get(i));
                parameterValues.set(index, 0.0);
            }
        }
        if (allowSizeLarger && parameters.length >= parameterNames.size()) {

        } else if (parameterNames.size() != parameters.length) {
            throw new IllegalArgumentException("Size of parameters passed to this method is not match size of parameters in the expression");
        }
        for (int i = 0; i < parameterNames.size(); i++) {
            parameterValues.set(i, parameters[i]);
        }
    }

    public void dumpPrefixStack() {
        Stack<String> prefixStack = convertExpressionToPrefix();
        System.out.println("========== DUMP PREFIX ========== START");
        for (String prefixItem : prefixStack) {
            System.out.print(prefixItem + ",");
        }
        System.out.println("\n========== DUMP PREFIX ========== END");
    }

    public void convertToDeMorganExpression() throws BalanException {
        if (!isNegative) {
            throw new BalanException("The expression is not negative");
        }
        Stack<String> prefixStack = convertExpressionToPrefix();
        Stack<String> resultStack = new Stack<String>();
        for (String prefixItem : prefixStack) {
            if (prefixItem.equals(bracketMark)) {
                resultStack.push(appendMark + "(" + resultStack.pop() + ")");
            } else if (isOperator(prefixItem) == 0) {
                resultStack.push(prefixItem);
            } else {
                String lastItem = resultStack.pop();
                String firstItem = resultStack.pop();
                String negativeOperator = "";
                switch (prefixItem) {
                    case "*":
                    case "/":
                    case "%":
                    case "+":
                    case "-":
                        negativeOperator = prefixItem;
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
        System.out.println("========== DUMP PARAMETERS ========== START");
        for (int i = 0; i < parameterNames.size(); i++) {
            System.out.println(parameterNames.get(i) + " = " + parameterValues.get(i));
        }
        System.out.println("========== DUMP PARAMETERS ========== END");
    }

    public static double GetBranchDistance(double x, double y, String strOpType) {
        double ret = 0;
        double k = 0.01;
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
        for (String appendMarkItem : APPEND_MARKS) {
            if (!expression.contains(appendMarkItem)) {
                appendMark = appendMarkItem;
                break;
            }
        }
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

    public Stack<String> convertExpressionToPrefix() {
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

    private String convertCompareExpression(String compareExpression) throws BalanException {
        if (compareExpression.startsWith(appendMark)) {
            return compareExpression.substring(appendMark.length());
        }
        String[] operator = new String[]{">=", "<=", "!=", ">", "<", "=="};
        for (int i = 0; i < operator.length; i++) {
            if (compareExpression.contains(operator[i])) {
                String[] parts = compareExpression.split(operator[i]);
                if (parts.length != 2) {
                    throw new BalanException("Wrong expression!");
                }
                return "fBchDist(" + parts[0].trim() + ", " + operator[i] + ", " + parts[1].trim() + ")";
            }
        }
        return compareExpression;
    }

    private double getValue(String item) throws BalanException {
        item = item.trim();
        int index = getParameterIndex(item);
        if (index != -1) {
            return parameterValues.get(index);
        }
        try {
            return Double.valueOf(item);
        } catch (Exception e) {
            throw new BalanException(e.getMessage());
        }
    }

    private double min(double a, double b) {
        return Math.min(a, b);
    }

    public double calculateBranchExpression() throws BalanException {
        Stack<String> prefixStack = convertExpressionToPrefix();
        Stack<String> resultStack = new Stack<String>();
        for (String prefixItem : prefixStack) {
            if (prefixItem.equals(bracketMark)) {

            } else if (isOperator(prefixItem) == 0) {
                resultStack.push(prefixItem);
            } else {
                double lastItem = getValue(resultStack.pop());
                double firstItem = getValue(resultStack.pop());
                switch (prefixItem) {
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
                        resultStack.push(GetBranchDistance(firstItem, lastItem, prefixItem) + "");
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

    public String convertToBranchExpression() throws BalanException {
        Stack<String> prefixStack = convertExpressionToPrefix();
        Stack<String> resultStack = new Stack<String>();
        for (String prefixItem : prefixStack) {
            if (prefixItem.equals(bracketMark)) {
                String lastItem = convertCompareExpression(resultStack.pop());
                resultStack.push(appendMark + "(" + lastItem + ")");
            } else if (isOperator(prefixItem) == 0) {
                resultStack.push(prefixItem);
            } else {
                String lastItem = convertCompareExpression(resultStack.pop());
                String firstItem = convertCompareExpression(resultStack.pop());
                switch (prefixItem) {
                    case "&&":
                        resultStack.push(appendMark + firstItem + " + " + lastItem);
                        break;
                    default: // ||
                        resultStack.push(appendMark + "min(" + firstItem + ", " + lastItem + ")");
                }
            }
        }
        String resultExpression = resultStack.pop();
        if (resultExpression.startsWith(appendMark)) {
            resultExpression = resultExpression.substring(appendMark.length());
        }
        if (REMOVE_ROOT_BRACKET) {
            if (resultExpression.charAt(0) == '(' && resultExpression.charAt(resultExpression.length() - 1) == ')') {
                resultExpression = resultExpression.substring(1, resultExpression.length() - 1);
            }
        }
        return resultExpression;
    }

}

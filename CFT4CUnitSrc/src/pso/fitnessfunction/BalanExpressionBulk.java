package pso.fitnessfunction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BalanExpressionBulk 
{
    private List<BalanAlgorithm[]> balanParsers = new ArrayList<BalanAlgorithm[]>();
    private List<String> parameterNames = new ArrayList<String>();

    public BalanExpressionBulk(String expressionLine) throws BalanException {
        if (expressionLine.contains("\n")) {
            throw new BalanException("The new line character is not allowed");
        }
        parseExpressionLine(expressionLine);
    }

    public BalanExpressionBulk(File expressionFile) throws BalanException {
        if (!expressionFile.exists() || !expressionFile.isFile()) {
            throw new BalanException("The expression file is not existed");
        }
        String content = FileUtil.readData(expressionFile);
        String[] lines = content.split("\n");
        for (String line : lines) {
            parseExpressionLine(line);
        }
    }

    public int getExpressionLineCount() {
        return balanParsers.size();
    }

    public int getExpressionCount(int lineIndex) {
        if (balanParsers.size() <= lineIndex) {
            return 0;
        }
        return balanParsers.get(lineIndex).length;
    }

    private void parseExpressionLine(String line) {
        List<String> expressions = BalanAlgorithm.parseAllExpressions(line);
        BalanAlgorithm[] balanAlgorithms = new BalanAlgorithm[expressions.size()];
        int i = 0;
        for (String expression : expressions) 
        {        	
            BalanAlgorithm balanAlgorithm = new BalanAlgorithm(expression);
            List<String> parameterNames = balanAlgorithm.getParameterNames();
            for (String parameterName : parameterNames) {
                int index = getParameterIndex(parameterName);
                if (index == -1) {
                    this.parameterNames.add(parameterName);
                }
            }
            balanAlgorithms[i++] = balanAlgorithm;
        }
        balanParsers.add(balanAlgorithms);
    }

    public int getTotalParameterCount() {
        return this.parameterNames.size();
    }
    
    public void setParameters(double[] parameters) throws IllegalArgumentException {
        setParameters(parameters, false);
    }

    public void setParameters(double[] parameters, boolean allowSizeLarger) throws IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters null");
        }
        if (allowSizeLarger && parameters.length >= this.parameterNames.size()) {

        } else if (this.parameterNames.size() != parameters.length) {
            throw new IllegalArgumentException("Size of parameters passed to this method is not match size of parameters in the expression");
        }

        for (BalanAlgorithm[] balanAlgorithms : balanParsers) {
            for (BalanAlgorithm balanAlgorithm : balanAlgorithms) {
                List<String> parameterNames = balanAlgorithm.getParameterNames();
                for (String parameterName : parameterNames) {
                    balanAlgorithm.setParameter(parameterName, parameters[getParameterIndex(parameterName)]);
                }
            }
        }
    }

    public void dumpAllParametersOfExpression(int lineIndex, int expressionIndex) throws BalanException {
        if (balanParsers.size() <= lineIndex) {
            throw new BalanException("The lineIndex is out of range");
        }
        BalanAlgorithm[] balanAlgorithms = balanParsers.get(lineIndex);
        if (balanAlgorithms.length <= expressionIndex) {
            throw new BalanException("The expressionIndex is out of range");
        }
        BalanAlgorithm balanAlgorithm = balanAlgorithms[expressionIndex];
        balanAlgorithm.dumpAllParameters();
    }

    private int getParameterIndex(String parameterName) {
        for (int i = 0; i < parameterNames.size(); i++) {
            if (parameterNames.get(i).equals(parameterName)) {
                return i;
            }
        }
        return -1;
    }

    public double calculateAllExpressions() throws BalanException {
        double resultValue = 0.0;
        for (BalanAlgorithm[] balanAlgorithms : balanParsers) {
            for (BalanAlgorithm balanAlgorithm : balanAlgorithms) {
                if (balanAlgorithm.isNegative()) {
                    balanAlgorithm.convertToDeMorganExpression();
                }
                resultValue += balanAlgorithm.calculateBranchExpression();
            }
        }
        return resultValue;
    }
    
    public double calculateAllExpressions(int lineIndex) throws BalanException {
        double resultValue = 0.0;
        if (balanParsers.size() <= lineIndex) {
            throw new BalanException("The lineIndex is out of range");
        }
        BalanAlgorithm[] balanAlgorithms = balanParsers.get(lineIndex);
        for (BalanAlgorithm balanAlgorithm : balanAlgorithms) {
            if (balanAlgorithm.isNegative()) {
                balanAlgorithm.convertToDeMorganExpression();
            }
            resultValue += balanAlgorithm.calculateBranchExpression();
        }
        return resultValue;
    }
}

package pso.fitnessfunction;

import java.util.ArrayList;
import java.util.List;

public class NewMain {

    public static void main(String[] args) throws BalanException {
//        BalanAlgorithm ba = new BalanAlgorithm("[a>b&&a-c<=1||b==0]F");
//        ba.setParameters(new double[]{2.1, 6.6, 0.2});
//        ba.dumpAllParameters();
//        ba.convertToDeMorganExpression();
//        System.out.println(ba.getExpression());
//        Double d = Double.parseDouble("12L");
//        long l = Long.parseLong("13L");

        String content = FileUtil.readData("exprs2.txt");
        List<String> exps = BalanAlgorithm.parseAllExpressions(content);
        for (String exp : exps) {
            BalanAlgorithm ba = new BalanAlgorithm(exp);
            System.out.println("======================================================================================");
            System.out.println(ba.getExpression());
            if (ba.isNegative()) {
                ba.convertToDeMorganExpression();
                System.out.println("DeMorgan: " + ba.getExpression());
            }
            ba.setParameters(new double[]{1.1, 2.3, 4.5, 6.8, 6.9, 7.2, 4.2, 8.8, 9.1, 4.4}, true);
            ba.dumpAllParameters();
            System.out.println("RESULT: " + ba.calculateBranchExpression());
        }
    }
    
    public static double fx(double[] x)
	{
		double x0 = x[0]; 
    	double x1 = x[1];
    	double x2 = x[2];
		double ret = Math.sqrt(x0 - 1) + Math.sqrt(x1 - 2) + Math.sqrt(x2 - 10);
		return ret;
	}

}

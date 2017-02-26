package pso.fitnessfunction;

import java.util.ArrayList;
import java.util.List;

import pso.PSOProcess;

public class NewMain 
{
    public static void main(String[] args) throws BalanAlgorithm.BalanException 
    {
        //String content = FileUtil.readData("exprs2.txt");
        //String content = FileUtil.readData("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt");
        String content = FileUtil.readData("D:/PhD/GA/GAsource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt");
        List<String> exps = BalanAlgorithm.parseAllExpressions(content);
        for (String exp : exps) 
        {
            BalanAlgorithm ba = new BalanAlgorithm(exp);
            System.out.println("======================================================================================");
            System.out.println(ba.getExpression());
            if (ba.isNegative()) 
            {
                ba.convertToDeMorganExpression();
                System.out.println("DeMorgan: " + ba.getExpression());
            }
            ba.setParameters(new double[]{1.1, 2.3, 4.5, 6.8, 6.9, 7.2, 4.2, 8.8, 9.1, 4.4}, true);
            ba.dumpAllParameters();
//            ba.dumpPrefixStack();
            System.out.println("RESULT: " + ba.calculateBranchExpression());
        }
     // Assign fitness function for PSO
        
        List<Fitness> functions = new ArrayList<Fitness>();
//        Fitness fit = new Fitness();
//	       functions.add(fit);
        PSOProcess pso = new PSOProcess("", 0);
        pso.start();
    }

    public static double fx(double[] x)
    {
    	double ret = 0;
    	double x0 = x[0]; 
    	double x1 = x[1];
    	double x2 = x[2];
    	ret = Math.sqrt(x0 - 1) + Math.sqrt(x1 - 2) + Math.sqrt(x2 - 10);
    	// How to assign this function as the fitness function of PSO 
    	return ret;
    }
}

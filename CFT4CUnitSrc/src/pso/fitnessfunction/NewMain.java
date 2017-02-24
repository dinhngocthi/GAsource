package pso.fitnessfunction;

import java.util.List;

public class NewMain {

    public static void main(String[] args) throws BalanAlgorithm.BalanException 
    {
        //String content = FileUtil.readData("exprs2.txt");
        String content = FileUtil.readData("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt");
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
    }

}

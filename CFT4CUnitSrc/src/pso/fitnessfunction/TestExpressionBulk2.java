package pso.fitnessfunction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pso.PSOProcess;

public class TestExpressionBulk2 {

    public static void main(String[] args) throws BalanException {
//        String content = FileUtil.readData("exprs2.txt");
//        content = content.split("\n")[0];
//        BalanExpressionBulk beb = new BalanExpressionBulk(content);
//        beb.setParameters(new double[]{1.1, 2.6, 3.1, 0.8, 9.9, 6.3, 8.2}, true);
//        System.out.println("Result of BalanExpressionBulk: " + beb.calculateAllExpressions());
//        List<String> exps = BalanAlgorithm.parseAllExpressions(content);
//        for (int i = 0; i < exps.size(); i++) {
//            beb.dumpAllParametersOfExpression(i);
//        }
        //BalanExpressionBulk beb = new BalanExpressionBulk(new File("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt"));
        BalanExpressionBulk beb = new BalanExpressionBulk(new File("D:/PhD/GA/GAsource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt"));

        beb.setParameters(new double[]{1.1, 2.6, 3.1, 0.8, 9.9, 6.3, 8.2, 1.6, 8.8, 9.1, 3.3, 2.2, 0.9, 2.3, 6.8, 9.9, 1.3, 2.1}, true);
        //System.out.println("Result of BalanExpressionBulk: " + beb.calculateAllExpressions());
        int i = 3;
        System.out.println("Result of BalanExpressionBulk of line " + i + ":" + beb.calculateAllExpressions(i));

        new PSOProcess("", 0).start();
        /*
         int lineCount = beb.getExpressionLineCount();
         for (int i = 0; i < lineCount; i++) {
         int expCount = beb.getExpressionCount(i);
         for (int j = 0; j < expCount; j++) {
         beb.dumpAllParametersOfExpression(i, j);
         }
         }
         */
    }


    /*    
     public TestExpressionBulk(String testpathfile)
     {
     try
     {
     this.beb = new BalanExpressionBulk(new File(testpathfile));
     }
     catch (Exception e)
     {
     e.printStackTrace();
     }
     }
     */
    public static double fx(double[] x, int lineidx) //public double fx(double[] x, int lineidx)
    {
        double ret = 0;
        try {    		
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt");
            BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/calDay.txt");
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/computeTax.txt");
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/getDayNum.txt");
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/line.txt");
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/remainder.txt");
            
    		//System.out.println("c = " + BalanExpressionFactory.count());
            //this.beb.setParameters(x, true);
            synchronized (beb) {
                beb.setParameters(x, true);
//                System.out.println(lineidx + ":" + beb.toString());
//                beb.dumpAllParameters(lineidx);

                ret = beb.calculateAllExpressions(lineidx);
            }
//                Thread.currentThread().sleep(2);
            //ret = this.beb.calculateAllExpressions(lineidx);
        } catch (Exception e) {
        }
        return ret;
    }
}

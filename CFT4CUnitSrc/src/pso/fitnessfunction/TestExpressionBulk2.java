package pso.fitnessfunction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pso.PSOProcess;

public class TestExpressionBulk2 
{
    public static double fx(double[] x, int lineidx) //public double fx(double[] x, int lineidx)
    {
        double ret = 0;
        try 
        {    		
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/triangleType.txt");
            //BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/calDay.txt");
            BalanExpressionBulk beb = BalanExpressionFactory.getBalanExpressionBulk("D:/Thi.DN/PhD/GA/GASource/CFT4CUnitSrc/src/sample/PPSObenchmark/computeTax.txt");
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

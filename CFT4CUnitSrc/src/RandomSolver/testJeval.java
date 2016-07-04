package RandomSolver;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

public class testJeval
{
    testJeval(String bieuThucLogic) throws EvaluationException
    {
        String giaTri = new Evaluator().evaluate(bieuThucLogic);
        System.out.println(giaTri);
    }

    public static void main(String[] args) throws EvaluationException
    {
//        new testJeval(" !(6<0) ");
        
        Evaluator mEvaluator = new Evaluator();
        //String r1 = mEvaluator.evaluate(" (6<0) and (-1<0)");
        String r1 = mEvaluator.evaluate("(2+4>3)&&(4+3>2)&&(3+2>4)");
        //String r1 = mEvaluator.evaluate("1/10000"); 
        System.out.println(r1);
    }
}

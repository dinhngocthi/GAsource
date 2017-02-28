package pso.fitnessfunction;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;

public class RunAllParams {

    public static void main(String[] args) throws BalanException {
        String content = FileUtil.readData("exprs2.txt");
        String[] lines = content.split("\n");

        HashMap<String, Double> params = new HashMap<String, Double>();
        params.put("xl2", 1.2);
        params.put("xl1", 3.5);
        params.put("xr2", 1.7);
        params.put("xr1", 1.9);
        params.put("y", 0.2);
        params.put("x", 1.6);
        params.put("year", 2.1);
        params.put("yl2", 4.2);
        params.put("yl1", 5.2);
        params.put("yr2", 1.1);
        params.put("status", 3.8);
        params.put("yr1", 7.2);
        params.put("income", 9.1);
        params.put("c", 6.6);
        params.put("b", 0.1);
        params.put("month", 1.4);
        params.put("a", 6.9);
        params.put("aaa", 6.9);

        System.out.println("================== All params ==================");
        Set<String> keys = params.keySet();
        for (String key : keys) {
            System.out.println(key + " = " + params.get(key));
        }
        System.out.println("================== Calculate ===================");

        for (int i = 0; i < lines.length; i++) {
            int pos = 0;
            while (true) {
                int start = lines[i].indexOf("[", pos);
                int endF = lines[i].indexOf("]F", pos);
                int endT = lines[i].indexOf("]T", pos);
                if (start == -1 || (endF == -1 && endT == -1)) {
                    break;
                }
                int end = 0;
                if (endF == -1) {
                    end = endT + 2;
                } else if (endT == -1) {
                    end = endF + 2;
                } else {
                    end = Math.min(endF, endT) + 2;
                }
                String exp = lines[i].substring(start, end);
                pos = end - 1;

                BalanAlgorithm ba = new BalanAlgorithm(exp);
                for (String key : keys) {
                    try {
                        ba.setParameter(key, params.get(key));
                    } catch (Exception e) {
                    }
                }
                Stack<String> stack = ba.convertExpressionToPostfix();
                System.out.println("========================================");
                for (String stack1 : stack) {
                    System.out.println(stack1);
                }
                System.out.println("Line " + (i + 1) + ": " + exp + " = " + ba.calculateBranchExpression());
            }
        }
    }

}

package pso.fitnessfunction;

import java.util.Hashtable;

public class RandomCheck {

    public static void main(String[] args) {
        Hashtable<String, Double> params = new Hashtable<String, Double>();
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
//        params.put("month", 1.4);
        params.put("a", 6.9);
        
        params.put("month", 1.1);

        // Line 1: [month>=1&&month<=12]F = 0.0
        double x = BalanAlgorithm.GetBranchDistance(params.get("month"), 1, ">=")
                + BalanAlgorithm.GetBranchDistance(params.get("month"), 12, "<=");
        System.out.println(x);
        
        // month = 1.1 => 0.9099999999999999
        x = BalanAlgorithm.GetBranchDistance(params.get("month"), 2, "==");
        System.out.println(x);

        // Line 2: [month==4||month==6||month==9||month==11]F = 2.61
        x = Math.min(BalanAlgorithm.GetBranchDistance(params.get("month"), 4, "=="),
                BalanAlgorithm.GetBranchDistance(params.get("month"), 6, "=="));
        x = Math.min(x, BalanAlgorithm.GetBranchDistance(params.get("month"), 9, "=="));
        x = Math.min(x, BalanAlgorithm.GetBranchDistance(params.get("month"), 11, "=="));
        System.out.println(x);

        /*
         Line 7: [xl1>=xr1&&
         xl1<=xr2&&
         xl2>=xr1&&
         xl2<=xr2&&
         yl1>=yr1&&
         yl1<=yr2&&
         yl2>=yr1&&
         yl2<=yr2]F = 14.759999999999998
         */
        x = BalanAlgorithm.GetBranchDistance(params.get("xl1"), params.get("xr1"), ">=")
                + BalanAlgorithm.GetBranchDistance(params.get("xl1"), params.get("xr2"), "<=")
                + BalanAlgorithm.GetBranchDistance(params.get("xl2"), params.get("xr1"), ">=")
                + BalanAlgorithm.GetBranchDistance(params.get("xl2"), params.get("xr2"), "<=")
                + BalanAlgorithm.GetBranchDistance(params.get("yl1"), params.get("yr1"), ">=")
                + BalanAlgorithm.GetBranchDistance(params.get("yl1"), params.get("yr2"), "<=")
                + BalanAlgorithm.GetBranchDistance(params.get("yl2"), params.get("yr1"), ">=")
                + BalanAlgorithm.GetBranchDistance(params.get("yl2"), params.get("yr2"), "<=");
        System.out.println(x);
    }

}

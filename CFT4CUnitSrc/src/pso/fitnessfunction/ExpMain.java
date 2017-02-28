package pso.fitnessfunction;

public class ExpMain {

    public static void main(String[] args) throws BalanException {
        String exp = "[(month == 1 && year % 400 == 0 || (month - 5 >= 1 && year * 5 < 7)), T]";
        int month = 5;
        int year = 500;
        double x = Math.min(BalanAlgorithm.GetBranchDistance(month, 1, "==") + BalanAlgorithm.GetBranchDistance(year % 400, 0, "=="),
                BalanAlgorithm.GetBranchDistance(month - 5, 1, ">=") + BalanAlgorithm.GetBranchDistance(year * 5, 7, "<"));
        System.out.println("x = " + x);

        BalanAlgorithm bl = new BalanAlgorithm(exp);
        bl.setParameter("month", month);
        bl.setParameter("year", year);
        System.out.println("" + bl.calculateBranchExpression());

        exp = "[(month == 1 && year % 400 == 0 && month - 5 >= 1), T]";
        x = BalanAlgorithm.GetBranchDistance(month, 1, "==")
                + BalanAlgorithm.GetBranchDistance(year % 400, 0, "==") + BalanAlgorithm.GetBranchDistance(month - 5, 1, ">=");
        System.out.println("x = " + x);

        bl = new BalanAlgorithm(exp);
        bl.setParameter("month", month);
        bl.setParameter("year", year);
        System.out.println("" + bl.calculateBranchExpression());

        exp = "[(month == 1 && year % 400 == 0 && month - 5 >= 1 || year / 100 == 1), T]";
        x = Math.min(BalanAlgorithm.GetBranchDistance(month, 1, "==")
                + BalanAlgorithm.GetBranchDistance(year % 400, 0, "==") + BalanAlgorithm.GetBranchDistance(month - 5, 1, ">="),
                BalanAlgorithm.GetBranchDistance(year / 100, 1, "=="));
        System.out.println("x = " + x);

        bl = new BalanAlgorithm(exp);
        bl.setParameter("month", month);
        bl.setParameter("year", year);
        System.out.println("" + bl.calculateBranchExpression());

        exp = "[(month == 1 && year % 400 == 0 && (month - 5 == 1 || year / 2 == 1) && ((month - 5) * 3 >= 1 || year / 100 == 1)), T]";
        x = BalanAlgorithm.GetBranchDistance(month, 1, "==")
                + BalanAlgorithm.GetBranchDistance(year % 400, 0, "==")
                + Math.min(BalanAlgorithm.GetBranchDistance(month - 5, 1, "=="), BalanAlgorithm.GetBranchDistance(year / 2, 1, "=="))
                + Math.min(BalanAlgorithm.GetBranchDistance((month - 5) * 3, 1, ">="), BalanAlgorithm.GetBranchDistance(year / 100, 1, "=="));
        System.out.println("x = " + x);

        bl = new BalanAlgorithm(exp);
        bl.setParameter("month", month);
        bl.setParameter("year", year);
        System.out.println("" + bl.calculateBranchExpression());
    }

}

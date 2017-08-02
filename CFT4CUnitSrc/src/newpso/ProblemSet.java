package newpso;

//this is the problem to be solved
//to find an x and a y that minimize the function below:
//f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
//where 1 <= x <= 4, and -1 <= y <= 1

//you can modify the function depends on your needs
//if your problem space is greater than 2-dimensional space
//you need to introduce a new variable (other than x and y)

public class ProblemSet 
{
	public static final double LOC_LOW  = 1;
	public static final double LOC_HIGH = 10000;
	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;
	
	//public static final double ERR_TOLERANCE = 0.1;//1E-5; // the smaller the tolerance, the more accurate the result, 
	public static final double ERR_TOLERANCE = 1E-15; // the smaller the tolerance, the more accurate the result,
	                                                  // but the number of iteration is increased
	private static final double k = 0.1;

	public static double evaluate(Location location) 
	{
		double result = 0;
		double x1 = location.getLoc()[0];
		double x2 = location.getLoc()[1];
/*
		double x3 = location.getLoc()[2];
		double x4 = location.getLoc()[3];
*/		
		//result = Math.pow((x1-1), 2) + Math.pow((x2-2), 2) + Math.pow((x3-3), 2);
		//result = Math.abs(x1+x4-1) + Math.abs(x2-2) + Math.abs(x3-3);
		result = PUT.foo1((int)x1, (int)x2);
		return result;
	}	
}
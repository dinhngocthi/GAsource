package pso;

import pso.fitnessfunction.TestExpressionBulk;
import pso.fitnessfunction.TestExpressionBulk2;

import java.lang.reflect.Method;

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
	/*
	public static final double LOC_X_LOW = 1;
	public static final double LOC_X_HIGH = 1000;
	public static final double LOC_Y_LOW = 1;
	public static final double LOC_Y_HIGH = 1000;
	public static final double LOC_Z_LOW = 1;
	public static final double LOC_Z_HIGH = 1000;
	*/
	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;
	
	public static final double ERR_TOLERANCE = 0.1;//1E-5; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	private static final double k = 0.1;

	public static double evaluate(Location location, String PUTName, int testpathID) 
	{
		double result = 0;
		result = TestExpressionBulk2.fx(location.getLoc(), PUTName, testpathID); 
		return result;
	}
	
	private static double GetBranchDistance(double x, double y, String strOpType)
	{
		double ret = 0;
		int opType = 0;
		if (strOpType.equals("=="))
			opType = 3;
		else if (strOpType.equals("!="))
			opType = 4;
		else if (strOpType.equals("<"))
			opType = 5;
		else if (strOpType.equals("<="))
			opType = 6;
		else if (strOpType.equals(">"))
			opType = 7;
		else if (strOpType.equals(">="))
			opType = 8;
		switch (opType)
		{
			case 3: // == condition
				if (Math.abs(x - y) == 0)
					ret = 0;
				else
					ret = Math.abs(x - y) + k;
				break;
			case 4: // != condition
				if (Math.abs(x - y) != 0)
					ret = 0;
				else
					ret = k;
				break;
			case 5: // < condition
				if (x - y < 0)
					ret = 0;
				else
					ret = Math.abs(x - y) + k;
				break;
			case 6: // <= condition
				if (x - y <= 0)
					ret = 0;
				else
					ret = Math.abs(x - y) + k;
				break;
			case 7: // > condition
				if (y - x < 0)
					ret = 0;
				else
					ret = Math.abs(y - x) + k;
				break;
			case 8: // >= condition
				if (y - x <= 0)
					ret = 0;
				else
					ret = Math.abs(y - x) + k;
				break;
			default:
				break;
		}
		return ret;
	}	
}
package pso;

//this is the problem to be solved
//to find an x and a y that minimize the function below:
//f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
//where 1 <= x <= 4, and -1 <= y <= 1

//you can modify the function depends on your needs
//if your problem space is greater than 2-dimensional space
//you need to introduce a new variable (other than x and y)

public class ProblemSet {
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
	
	public static final double ERR_TOLERANCE = 1E-5; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	private static final double k = 0.1;

	public static double evaluate(Location location, int branchID) 
	{
		double result = 0;
		double x = location.getLoc()[0]; // the "x" part of the location
		double y = location.getLoc()[1]; // the "y" part of the location
		double z = location.getLoc()[2]; // the "z" part of the location
/*		
		int month = (int)x;
		int year = (int)y;

		double ret1 = GetBranchDistance(month, 1, 8) + GetBranchDistance(month, 12, 6);
		double ret2 = GetBranchDistance(month, 2, 3);
		
		double ret31 = GetBranchDistance(year % 4, 0, 3) + GetBranchDistance(year % 100, 0, 3);
		double ret32 = GetBranchDistance(year % 400, 0, 3);
		double ret3 = Math.min(ret31, ret32);

		double ret4 = GetBranchDistance(year % 4, 0, 4);

		switch (branchID)
		{
			case 0:	
				result = ret1;  // branch 1 
				break;
			case 1:
				result = ret1 + ret2; // branch 2
				break;
			case 2:
				result = ret1 + ret2 + ret3; // branch 3
				break;
			case 3:
				result = ret1 + ret2 + ret4; // branch 4
				break;
			default:
				break;
		}
		//result = TargetFunctions.Tritype(x, y, z);
*/
		result = GetBranchDistance(x + y, z, 7) + GetBranchDistance(x + z, y, 7) + GetBranchDistance(y + z, x, 7);
		result = Integer.MAX_VALUE - Math.abs(result);
		return result;
	}
	
	private static double GetBranchDistance(double x, double y, int opType)
	{
		double ret = 0;
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

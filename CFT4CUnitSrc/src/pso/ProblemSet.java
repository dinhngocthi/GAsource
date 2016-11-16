package pso;

import C.TargetFunctions;
import C.Utils;

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
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
	public static double evaluate(Location location, int functionID) 
	{
		double result = 0;
		double x = location.getLoc()[0]; // the "x" part of the location
		double y = location.getLoc()[1]; // the "y" part of the location
		double z = location.getLoc()[2]; // the "z" part of the location
		/*
		result = Math.pow(2.8125 - x + x * Math.pow(y, 4), 2) + 
				Math.pow(2.25 - x + x * Math.pow(y, 2), 2) + 
				Math.pow(1.5 - x + x * y, 2);
		*/
		//result = Math.pow(x - 1, 2) + Math.pow(y - 2, 2) + Math.pow(z - 3, 2);
		//result = Math.abs(x - y) + Math.abs(y - z);
		switch (functionID)
		{
			case 0:	
				result = Math.abs(Math.pow(y, 2) - (4 * x * z));
				break;
			case 1:
				result = Math.abs(x) + Math.abs(y);
				break;
			default:
				break;
		}
		Utils.iterationcount++;
		//result = TargetFunctions.Tritype(x, y, z);
		return result;
	}
}

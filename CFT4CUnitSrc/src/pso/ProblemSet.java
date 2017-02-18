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

	public static double evaluate(Location location, int testpathID) 
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
		//result = Math.min(GetBranchDistance(x + y, z, 6), Math.min(GetBranchDistance(x + z, y, 6), GetBranchDistance(y + z, x, 6)));
		result = FtriangleType(x, y, z, testpathID);
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
		else if (strOpType.equals("!="))
			opType = 5;
		else if (strOpType.equals("<"))
			opType = 6;
		else if (strOpType.equals("<="))
			opType = 7;
		else if (strOpType.equals(">"))
			opType = 8;
		else if (strOpType.equals(">="))
			opType = 9;
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
	
	private static double computeTax(int status, double income, int testpathID)
	{
		double ret = 0;
		//[status==0]F
		double ret1F = GetBranchDistance(status, 0, "!=");
		//[status==0]T
		double ret1T = GetBranchDistance(status, 0, "==");
		
		//[status==1]F
		double ret2F = GetBranchDistance(status, 1, "!=");
		//[status==1]T
		double ret2T = GetBranchDistance(status, 1, "==");
		
		//[status==2]F
		double ret3F = GetBranchDistance(status, 2, "!=");
		//[status==2]F
		double ret3T = GetBranchDistance(status, 2, "==");
		
		//[status==3]F
		double ret4F = GetBranchDistance(status, 3, "!=");
		//[status==3]F
		double ret4T = GetBranchDistance(status, 3, "==");
		
		//[income<=8350]F
		double ret5F = GetBranchDistance(income, 8350, ">");
		//[income<=8350]T
		double ret5T = GetBranchDistance(income, 8350, "<=");
		
		//[income<=33950]F 
		double ret6F = GetBranchDistance(income, 33950, ">");
		//[income<=33950]T 
		double ret6T = GetBranchDistance(income, 33950, "<=");

		//[income<=82250]F 
		double ret7F = GetBranchDistance(income, 82250, ">");
		//[income<=82250]T
		double ret7T = GetBranchDistance(income, 82250, "<=");
		
		//[income<=171550]F 
		double ret8F = GetBranchDistance(income, 171550, ">");
		//[income<=171550]T
		double ret8T = GetBranchDistance(income, 171550, "<=");
		
		//[income<=372950]F 
		double ret9F = GetBranchDistance(income, 372950, ">");
		//[income<=372950]T 
		double ret9T = GetBranchDistance(income, 372950, "<=");
		
		switch (testpathID)
		{
			case 1:
				//Path 1: [status==0]F [status==1]F [status==2]F [status==3]F 
				ret = ret1F + ret2F + ret3F + ret4F;
				break;
			case 2:
				//Path 2: [status==0]F [status==1]F [status==2]F [status==3]T 
				ret = ret1F + ret2F + ret3F + ret4T;
			case 3:
				//Path 3: [status==0]F [status==1]F [status==2]T
				ret = ret1F + ret2F + ret3T;
			case 4:
				//Path 4: [status==0]F [status==1]T 
				ret = ret1F + ret2T;
			default:
				break;
		}
		return ret;
	}

	private static double FtriangleType(double a, double b, double c, int testpathID)
	{
		double ret = 0;
		// Decision node1 [a+b>c&&a+c>b&&b+c>a]F -> a+b<=c||a+c<=b||b+c<=a
		double ret1F = Math.min(GetBranchDistance(a+b, c, "<="), Math.min(GetBranchDistance(a+c, b, "<="), GetBranchDistance(c+b, a, "<=")));
		// Decision node1 [a+b>c&&a+c>b&&b+c>a]T
		double ret1T = GetBranchDistance(a+b, c, ">") + GetBranchDistance(a+c, b, ">") + GetBranchDistance(c+b, a, ">");
		// Decision node2 [a==b&&b==c]F -> a!=b||b!=c
		double ret2F = Math.min(GetBranchDistance(a, b, "!="), GetBranchDistance(b, c, "!="));
		// Decision node2 [a==b&&b==c]T
		double ret2T = GetBranchDistance(a, b, "==") + GetBranchDistance(b, c, "==");
		// Decision node3 [a==b||b==c||c==a]F -> a!=b&&b!=c&&c!=a  
		double ret3F = GetBranchDistance(a, b, "!=") + GetBranchDistance(a, c, "!=") + GetBranchDistance(c, b, "!=");
		// Decision node3 [a==b||b==c||c==a]T
		double ret3T = Math.min(GetBranchDistance(a, b, "=="), Math.min(GetBranchDistance(a, c, "=="), GetBranchDistance(c, b, "==")));
		
		switch (testpathID)
		{
			case 1: 
				// Path 1: [a+b>c&&a+c>b&&b+c>a]F
				ret = ret1F;
				break;
			case 2:
				// Path 2: [a+b>c&&a+c>b&&b+c>a]T [a==b&&b==c]F [a==b||b==c||c==a]F
				ret = ret1T + ret2F + ret3F;
				break;
			case 3:
				// Path 3: [a+b>c&&a+c>b&&b+c>a]T [a==b&&b==c]F [a==b||b==c||c==a]T
				ret = ret1T + ret2F + ret3T;
				break;
			case 4:
				// Path 4: [a+b>c&&a+c>b&&b+c>a]T [a==b&&b==c]T
				ret = ret1T + ret2T;
				break;
			default:
				break;
		}
		return ret;
	}
}

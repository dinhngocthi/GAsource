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
	
	public static final double ERR_TOLERANCE = 1E-5; // the smaller the tolerance, the more accurate the result, 
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
	
	private static double FgetdayNum(int year, int month, int testpathID)
	{
		double ret = 0;
		//[month>=1&&month<=12]F -> [month<1||month>12]T
		double ret1F = Math.min(GetBranchDistance(month, 1, "<"), GetBranchDistance(month, 12, ">")); 
		//[month>=1&&month<=12]T
		double ret1T = GetBranchDistance(month, 1, ">=") + GetBranchDistance(month, 12, "<=");

		//[month==2]F -> [month!=2]T
		double ret2F = GetBranchDistance(month, 2, "!=");
		double ret2T = GetBranchDistance(month, 2, "==");
		
		//[month==4||month==6||month==9||month==11]F -> [month!=4&&month!=6&&month!=9&&month!=11]T
		double ret3F = GetBranchDistance(month, 4, "!=") + GetBranchDistance(month, 6, "!=") + GetBranchDistance(month, 9, "!=") + GetBranchDistance(month, 11, "!=");
		double ret3T = Math.min(Math.min(GetBranchDistance(month, 4, "=="), GetBranchDistance(month, 6, "==")),
				                Math.min(GetBranchDistance(month, 9, "=="), GetBranchDistance(month, 11, "==")));
		
		//[year%400==0||(year%4==0&&year%100==0)]F -> [year%400!=0&&(year%4!=0||year%100!=0)]T
		double ret4F = GetBranchDistance(year%400, 0, "!=") + Math.min(GetBranchDistance(year%4, 0, "!="), GetBranchDistance(year%100, 0, "!=")); 
		double ret4T = Math.min(GetBranchDistance(year%400, 0, "=="), (GetBranchDistance(year%4, 0, "==") + GetBranchDistance(year%100, 0, "==")));
		
		switch (testpathID)
		{
			case 1:
				//Path 1: [month>=1&&month<=12]F
				ret = ret1F;
				break;
			case 2:
				//Path 2: [month>=1&&month<=12]T [month==2]F [month==4||month==6||month==9||month==11]F
				ret = ret1T + ret2F + ret3F;
				break;
			case 3:
				//Path 3: [month>=1&&month<=12]T [month==2]F [month==4||month==6||month==9||month==11]T
				ret = ret1T + ret2F + ret3T;
				break;
			case 4:
				//Path 4: [month>=1&&month<=12]T [month==2]T [year%400==0||(year%4==0&&year%100==0)]F
				ret = ret1T + ret2T + ret4F;
				break;
			case 5:
				//Path 5: [month>=1&&month<=12]T [month==2]T [year%400==0||(year%4==0&&year%100==0)]T
				ret = ret1T + ret2T + ret4T;
				break;
			default:
				break;
		}
		return ret;
	}

	private static double Fline(int xr1, int xr2, int yr1, int yr2, int xl1, int xl2, int yl1, int yl2, int testpathID)
	{
		double ret = 0;
		//[xr1<xr2&&yr1<yr2&&xl1<=xl2]F -> [xr1>=xr2!!yr1>=yr2!!xl1>xl2]T
		double ret1F = Math.min(GetBranchDistance(xr1, xr2, ">="), Math.min(GetBranchDistance(yr1, yr2, ">="), GetBranchDistance(xl1, xl2, ">")));
		//[xr1<xr2&&yr1<yr2&&xl1<=xl2]T
		double ret1T = GetBranchDistance(xr1, xr2, "<") + GetBranchDistance(yr1, yr2, "<") + GetBranchDistance(xl1, xl2, "<=");
		
		//  [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F
		//->[xl1<xr1 ||xl1>xr2 ||xl2<xr1 ||xl2>xr2 ||yl1<yr1 ||yl1>yr2 ||yl2<yr1 ||yl2>yr2]T
		double min1 = Math.min(GetBranchDistance(xl1, xr1, "<"), GetBranchDistance(xl1, xr2, ">"));
		double min2 = Math.min(GetBranchDistance(xl2, xr1, "<"), GetBranchDistance(xl2, xr2, ">"));
		double min3 = Math.min(GetBranchDistance(yl1, yr1, "<"), GetBranchDistance(yl1, yr2, ">"));
		double min4 = Math.min(GetBranchDistance(yl2, yr1, "<"), GetBranchDistance(yl2, yr2, ">"));	
		double ret2F = Math.min(Math.min(min1, min2), Math.min(min3, min4));  
        //[xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]T
		double ret2T = GetBranchDistance(xl1, xr1, ">=") + GetBranchDistance(xl1, xr2, "<=") + GetBranchDistance(xl2, xr1, ">=") + GetBranchDistance(xl2, xr2, "<=") +
                       GetBranchDistance(yl1, yr1, ">=") + GetBranchDistance(yl1, yr2, "<=") + GetBranchDistance(yl2, yr1, ">=") + GetBranchDistance(yl2, yr2, "<=");
		
		//[yl1==yl2]F -> [yl1!=yl2]T  
		double ret3F = GetBranchDistance(yl1, yl2, "!="); 
		//[yl1==yl2]T  
		double ret3T = GetBranchDistance(yl1, yl2, "==");
				
		//[xl1==xl2]F -> [xl1!=xl2]T
		double ret4F = GetBranchDistance(xl1, xl2, "!=");
		//[xl1==xl2]T
		double ret4T = GetBranchDistance(xl1, xl2, "==");
		
		//[yl1<yr1&&yl2<yr1]F -> [yl1>=yr1||yl2>=yr1]T
		double ret5F = Math.min(GetBranchDistance(yl1, yr1, ">="), GetBranchDistance(yl2, yr1, ">=")); 
		//[yl1<yr1&&yl2<yr1]T
		double ret5T = GetBranchDistance(yl1, yr1, "<") + GetBranchDistance(yl2, yr1, "<");
				
		//[yl1>yr2&&yl2>yr2]F -> [yl1<=yr2||yl2<=yr2]T
		double ret6F = Math.min(GetBranchDistance(yl1, yr2, "<="), GetBranchDistance(yl2, yr2, "<="));
		//[yl1>yr2&&yl2>yr2]T
		double ret6T = GetBranchDistance(yl1, yr2, ">") + GetBranchDistance(yl2, yr2, ">");
		
		//[xl1<xr1&&xl2<xr1]F -> [xl1>=xr1||xl2>=xr1]T  
		double ret7F = Math.min(GetBranchDistance(xl1, xr1, ">="), GetBranchDistance(xl2, xr1, ">="));
		//[xl1<xr1&&xl2<xr1]T  
		double ret7T = GetBranchDistance(xl1, xr1, "<") + GetBranchDistance(xl2, xr1, "<");
				
		//[xl1>xr2&&xl2>xr2]F -> [xl1<=xr2||xl2<=xr2]T 
		double ret8F = Math.min(GetBranchDistance(xl1, xr2, "<="), GetBranchDistance(xl2, xr2, "<="));
		//[xl1>xr2&&xl2>xr2]T 
		double ret8T = GetBranchDistance(xl1, xr2, ">") + GetBranchDistance(xl2, xr2, ">");
		
		double x1 = (yl2 - yl1) * (yr1 - yl1) + xl1;
//		if (x1 != 0)
			x1 = (xl2 - xl1)/x1;
//		else
//			x1 = Integer.MAX_VALUE;
//		double x1 = (xl2 - xl1) / (yl2 - yl1) * (yr1 - yl1) + xl1;
		//[x1>=xr1&&x<=xr2]F -> [x1<xr1||x1>xr2]T
		double ret9F = Math.min(GetBranchDistance(x1, xr1, "<"), GetBranchDistance(x1, xr2, ">"));
		//[x1>=xr1&&x1<=xr2]T
		double ret9T = GetBranchDistance(x1, xr1, ">=") + GetBranchDistance(x1, xr2, "<=");

		double x2 = (yl2 - yl1) * (yr2 - yl1) + xl1;
//		if (x2 != 0)
			x2 = (xl2 - xl1)/x2;
//		else
//			x2 = Integer.MAX_VALUE;;
		//double x2 = (xl2 - xl1) / (yl2 - yl1) * (yr2 - yl1) + xl1;
		//[x2>=xr1&&x2<=xr2]F -> [x2<xr1||x2>xr2]T 
		double ret10F = Math.min(GetBranchDistance(x2, xr1, "<"), GetBranchDistance(x2, xr2, ">"));
		//[x2>=xr1&&x2<=xr2]T
		double ret10T = GetBranchDistance(x2, xr1, ">=") + GetBranchDistance(x2, xr2, "<=");
		
		double y1 = (xl2 - xl1) * (xr1 - xl1) + yl1;
//		if (y1 != 0)
			y1 = (yl2 - yl1) / y1;
//		else 
//			y1 = Integer.MAX_VALUE;
		//double y1 = (yl2 - yl1) / (xl2 - xl1) * (xr1 - xl1) + yl1;
		//[y1>=yr1&&y1<=yr2]F -> [y1<yr1||y1>yr2]T 
		double ret11F = Math.min(GetBranchDistance(y1, yr1, "<"), GetBranchDistance(y1, yr2, ">"));
		//[y1>=yr1&&y1<=yr2]T
		double ret11T = GetBranchDistance(y1, yr1, ">=") + GetBranchDistance(y1, yr2, "<=");

		double y2 = (xl2 - xl1) * (xr2 - xl1) + yl1;
//		if (y2 != 0)
			y2 = (yl2 - yl1) / y2;
//		else
//			y2 = Integer.MAX_VALUE;
		//double y2 = (yl2 - yl1) / (xl2 - xl1) * (xr2 - xl1) + yl1;
		//[y2>=yr1&&y2<=yr2]F
		double ret12F = Math.min(GetBranchDistance(y2, yr1, "<"), GetBranchDistance(y2, yr2, ">"));
		//[y2>=yr1&&y2<=yr2]T
		double ret12T = GetBranchDistance(y2, yr1, ">=") + GetBranchDistance(y2, yr2, "<=");

		//[xl1<xr1]F
		double ret13F = GetBranchDistance(xl1, xr1, ">=");
		//[xl1<xr1]T
		double ret13T = GetBranchDistance(xl1, xr1, "<");
		
		//[xl1>xr2]F
		double ret14F = GetBranchDistance(xl1, xr2, "<=");
		//[xl1>xr2]T
		double ret14T = GetBranchDistance(xl1, xr2, ">");
		
		//[yl1>yr2||yl2<yr1]F -> [yl1<=yr2&&yl2>=yr1]T
		double ret15F = GetBranchDistance(yl1, yr2, "<=") + GetBranchDistance(yl2, yr1, ">=");
		//[yl1>yr2||yl2<yr1]T
		double ret15T = Math.min(GetBranchDistance(yl1, yr2, ">"), GetBranchDistance(yl2, yr1, "<"));

		//[yl1<yr1]F 
		double ret16F = GetBranchDistance(yl1, yr1, ">=");
		//[yl1<yr1]T 
		double ret16T = GetBranchDistance(yl1, yr1, "<");

		//[yl1>yr2]F
		double ret17F = GetBranchDistance(yl1, yr2, "<=");
		//[yl1>yr2]T
		double ret17T = GetBranchDistance(yl1, yr2, ">");
		
		//[xl1>xr2||xl2<xr1]F -> [xl1<=xr2&&xl2>=xr1]T
		double ret18F = GetBranchDistance(xl1, xr2, "<=") + GetBranchDistance(xl2, xr1, ">=");
		//[xl1>xr2||xl2<xr1]T
		double ret18T = Math.min(GetBranchDistance(xl1, xr2, ">"), GetBranchDistance(xl2, xr1, "<"));

		switch (testpathID)
		{
			case 1:
				//Path 1: [xr1<xr2&&yr1<yr2&&xl1<=xl2]F
				ret = ret1F;
				break;
			case 2:
				//Path 2: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]F [xl1>xr2&&xl2>xr2]F [x>=xr1&&x<=xr2]F [x>=xr1&&x<=xr2]F [y>=yr1&&y<=yr2]F [y>=yr1&&y<=yr2]F
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7F + ret8F + ret9F + ret10F + ret11F + ret12F;
				break;
			case 3:
				//Path 3: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]F [xl1>xr2&&xl2>xr2]F [x>=xr1&&x<=xr2]F [x>=xr1&&x<=xr2]F [y>=yr1&&y<=yr2]F [y>=yr1&&y<=yr2]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7F + ret8F + ret9F + ret10F + ret11F + ret12T;
				break;
			case 4:
				//Path 4: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]F [xl1>xr2&&xl2>xr2]F [x>=xr1&&x<=xr2]F [x>=xr1&&x<=xr2]F [y>=yr1&&y<=yr2]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7F + ret8F + ret9F + ret10F + ret11T;
				break;
			case 5:
				//Path 5: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]F [xl1>xr2&&xl2>xr2]F [x>=xr1&&x<=xr2]F [x>=xr1&&x<=xr2]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7F + ret8F + ret9F + ret10T;
				break;
			case 6:
				//Path 6: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]F [xl1>xr2&&xl2>xr2]F [x>=xr1&&x<=xr2]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7F + ret8F + ret9T;
				break;
			case 7:
				//Path 7: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]F [xl1>xr2&&xl2>xr2]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7F + ret8T;
				break;
			case 8:
				//Path 8: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]F [xl1<xr1&&xl2<xr1]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6F + ret7T;
				break;
			case 9:
				//Path 9: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]F [yl1>yr2&&yl2>yr2]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5F + ret6T;
				break;
			case 10:
				//Path 10: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]F [yl1<yr1&&yl2<yr1]T
				ret = ret1T + ret2F + ret3F + ret4F + ret5T;
				break;
			case 11:
				//Path 11: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]T [xl1<xr1]F [xl1>xr2]F [yl1>yr2||yl2<yr1]F
				ret = ret1T + ret2F + ret3F + ret4T + ret13F + ret14F + ret15F;
				break;
			case 12:
				//Path 12: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]T [xl1<xr1]F [xl1>xr2]F [yl1>yr2||yl2<yr1]T
				ret = ret1T + ret2F + ret3F + ret4T + ret13F + ret14F + ret15T;
				break;
			case 13:
				//Path 13: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]T [xl1<xr1]F [xl1>xr2]T
				ret = ret1T + ret2F + ret3F + ret4T + ret13F + ret14T;
				break;
			case 14:
				//Path 14: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]F [xl1==xl2]T [xl1<xr1]T
				ret = ret1T + ret2F + ret3F + ret4T + ret13T;
				break;
			case 15:
				//Path 15: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]T [yl1<yr1]F [yl1>yr2]F [xl1>xr2||xl2<xr1]F
				ret = ret1T + ret2F + ret3T + ret16F + ret17F + ret18F;
				break;
			case 16:
				//Path 16: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]T [yl1<yr1]F [yl1>yr2]F [xl1>xr2||xl2<xr1]T
				ret = ret1T + ret2F + ret3T + ret16F + ret17F + ret18T;
				break;
			case 17:
				//Path 17: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]T [yl1<yr1]F [yl1>yr2]T
				ret = ret1T + ret2F + ret3T + ret16F + ret17T;
				break;
			case 18:
				//Path 18: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]F [yl1==yl2]T [yl1<yr1]T 
				ret = ret1T + ret2F + ret3T + ret16T;
				break;
			case 19:
				//Path 19: [xr1<xr2&&yr1<yr2&&xl1<=xl2]T [xl1>=xr1&&xl1<=xr2&&xl2>=xr1&&xl2<=xr2&&yl1>=yr1&&yl1<=yr2&&yl2>=yr1&&yl2<=yr2]T
				ret = ret1T + ret2T;
				break;
			default:
				break;	
		}
		return ret;
	}
	
	private static double FcomputeTax(int status, double income, int testpathID)
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
				break;
			case 3:
				//Path 3: [status==0]F [status==1]F [status==2]T
				ret = ret1F + ret2F + ret3T;
				break;
			case 4:
				//Path 4: [status==0]F [status==1]T 
				ret = ret1F + ret2T;
				break;
			case 5:
				//Path 5: [status==0]T [income<=8350]F [income<=33950]F [income<=82250]F [income<=171550]F [income<=372950]F
				ret = ret1T + ret5F + ret6F + ret7F + ret8F + ret9F;
				break;
			case 6:
				//Path 6: [status==0]T [income<=8350]F [income<=33950]F [income<=82250]F [income<=171550]F [income<=372950]T
				ret = ret1T + ret5F + ret6F + ret7F + ret8F + ret9T;
				break;
			case 7:
				//Path 7: [status==0]T [income<=8350]F [income<=33950]F [income<=82250]F [income<=171550]T
				ret = ret1T + ret5F + ret6F + ret7F + ret8T;
				break;
			case 8:
				//Path 8: [status==0]T [income<=8350]F [income<=33950]F [income<=82250]T
				ret = ret1T + ret5F + ret6F + ret7T;
				break;
			case 9:
				//Path 9: [status==0]T [income<=8350]F [income<=33950]T
				ret = ret1T + ret5F + ret6T;
				break;
			case 10:
				//Path 10: [status==0]T [income<=8350]T
				ret = ret1T + ret5T;
				break;
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
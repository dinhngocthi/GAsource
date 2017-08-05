package newpso;

public class PUT 
{	
	public static void main(String[] args)
	{
		foo(1, 3);
		System.out.println("OK");
	}

	public static double sin(double X)
	{
		double ret = 0;
		int type = 0;

		//instrument code start
		if (Math.sin(X) == 1) ret = 0;
		else ret = Math.abs(Math.sin(X) - 1) + 0.1;
		//instrument code end

		if (Math.sin(X) == 1)
			type = 1;
		return ret;
	}	
	
	public static double test (double a, double b, double c)
	{
		double ret = 0;
		int type = 1;
		double d = a + b + c;

		// instrument code start
		if (d == 2) ret = 0;
		else ret = Math.abs(d - 2) + 0.1;
		// instrument code end				
		if (d == 2)
		{
			System.out.println("OK");
			type = -1;			
		}
		return ret;
	}
	
	// motivation sample
	public static double foo(int X, int Y)
	{
		if (X <= 0 || Y <= 0)
			return 0;
		double Z;
		if ((X < Y/2) || (Y == 0))
			Z = 1; //Target 1
		else if (Y > 3 * X)
			Z = 2; //Target 2
		else
		{
			Z = fun(X,Y);
			if ((Z > 8) && (Y == 10))
				if (Z == Y)
					Z = 3; //Target 3
		}
		return Z;
	}
	
	public static double fun(int x, int y) {
		double ret = 0;
		if (y != 0)
			ret = x * x / y;
		return ret;
	}
	
	public static double fun1(int x, int y) {
		double ret = 0;
		if (y != 0)
			ret = x + y + Math.sin(x);
		return ret;
	}
		
	public static double foo1(int x, int y) {
		// instrument code start
		double ret = Math.abs(fun1(x, y) - 1001);
		// instrument code end

		if (fun1(x, y) == 1001)
			System.out.println("OK");

		return ret;
	}
}

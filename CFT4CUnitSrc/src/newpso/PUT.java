package newpso;

public class PUT 
{	
	public static void main(String[] args)
	{
		foo(1, 3);
		double d[] = new double[10];
		Velocity v = new Velocity(d);
		test1(v);
		System.out.println("Completed");
	}
	
	public static void test1 (Velocity v)
	{
		v.aa = 10;
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
		if(X<=0 || Y<=0)
			return 0;
		double Z;
		if ((X < Y/2)|| (Y==0))
			Z= 1; //Target 1
		else if (Y > 3 * X)
			Z=2; //Target 2
		else
		{
			Z = fun(X,Y);
			if ((Z >8) && (Y==10))
				if (Z==Y)
					Z=3;//Target 3
		}
		return Z;
	}
	
	public static double fun(int x, int y)
	{
		double ret = 0;
		if (y != 0)
			ret = x * x / y;
		return ret;
	}
}

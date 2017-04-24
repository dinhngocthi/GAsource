package newpso;

public class PUT {
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
}

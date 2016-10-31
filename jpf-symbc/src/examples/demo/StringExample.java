package demo;

import gov.nasa.jpf.symbc.Debug;

public class StringExample {
	
	public static void main (String[] args) {
		System.out.println("start");
		test("<<<<<a href=\">    @");
		test1(1, 2);
		System.out.println ("end");
	}
	
	public static void test(String body) {
		if (body == null)
			return;
		int len = body.length();
		for(int i=0; i< len; i++) 
			if (body.charAt(i) != '<') 
				return;
		System.out.println("false "+Debug.getSymbolicStringValue(body));
		assert false;
	}
	
	public static void test1(int a, int b)
	{
		int c, n;
		if (a < 5)
		{
			c = a;
		}
		else
		{
			c = b;
		}

		n = c;
		while (n <= 8)
		{
			if (b > c)
			{
				c = 3;
			}
			else
			{
				n = n + c;
			}
			n = n + 1;
		}
		
		System.out.println ("completed");
	}
}
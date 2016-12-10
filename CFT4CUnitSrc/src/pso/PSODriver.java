package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args)
	{
		long t1 = System.currentTimeMillis(); // start time

		new PSOProcess().execute(0);
		new PSOProcess().execute(1);
		new PSOProcess().execute(2);
		
		long t2 = System.currentTimeMillis(); // end time
		System.out.println("Estimated time = " + (t2-t1));
	}
}
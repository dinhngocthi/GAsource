package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args)   throws InterruptedException
	{
		long t1 = System.currentTimeMillis(); // start time
/*
		PSOConstants.functionID = 0;
		new PSOProcess().start();
		Thread.sleep(10);
*/
		PSOConstants.functionID = 2;
		new PSOProcess().start();
		Thread.sleep(3);
		PSOConstants.functionID = 3;
		new PSOProcess().start();

		long t2 = System.currentTimeMillis(); // end time
		System.out.println("Estimated time = " + (t2-t1));
	}
}
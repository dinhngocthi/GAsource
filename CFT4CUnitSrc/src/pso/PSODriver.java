package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args)   throws InterruptedException
	{	
		long t1 = System.currentTimeMillis(); // start time
		new PSOProcess(1).start();
		new PSOProcess(2).start();
		new PSOProcess(3).start();
		new PSOProcess(4).start();
		new PSOProcess(5).start();
		new PSOProcess(6).start();
		new PSOProcess(7).start();
		new PSOProcess(8).start();
		new PSOProcess(9).start();
		new PSOProcess(10).start();
		long t2 = System.currentTimeMillis(); // end time
		System.out.println("Estimated time = " + (t2-t1));
	}
}
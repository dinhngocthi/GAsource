package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args)   throws InterruptedException
	{
/*
		String PUTName = "computeTax";
		int testpathsize = 10;
*/

		String PUTName = "triangleType";
		int testpathsize = 4;

		for (int i = 1; i <=  testpathsize; i++)
			new PSOProcess(PUTName, i).start();
	}
}
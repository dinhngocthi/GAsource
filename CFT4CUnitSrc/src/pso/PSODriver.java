package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args) throws InterruptedException
	{
/*
		String PUTName = "computeTax";
		int testpathsize = 10;
*/
/*
		String PUTName = "triangleType";
		int testpathsize = 4;
*/
		String PUTName = "line";		
		int testpathsize = 19;
		for (int i = 1; i <=  testpathsize; i++)
			new PSOProcess(PUTName, i).start();
	}
	public static void generateTC(String inPUTname)
	{
		String PUTName = "";		
		int testpathsize = 0;
		
		if (inPUTname.contains("computeTax"))
		{
			PUTName = "computeTax";
			testpathsize = 10;
		}
		else if (inPUTname.contains("triangleType"))
		{
			PUTName = "triangleType";
			testpathsize = 4;
		}
		else if (inPUTname.contains("line"))
		{
			PUTName = "line";
			testpathsize = 19;
		}

		for (int i = 1; i <=  testpathsize; i++)
			new PSOProcess(PUTName, i).start();
	}
}
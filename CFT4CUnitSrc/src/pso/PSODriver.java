package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args) throws InterruptedException
	{
//		String PUTName = "computeTax";
//		int testpathsize = 10;

//		String PUTName = "triangleType";
//		int testpathsize = 4;

		String PUTName = "line";		
//		int testpathsize = 19;

//		String PUTName = "getDayNum";		
		int testpathsize = 5;
	
		for (int i = 0; i <  testpathsize; i++)
			new PSOProcess(PUTName, i).start();
//		new PSOProcess(PUTName, 3).start();
	}
	public static void generateTC(String inPUTpath)
	{		
		int testpathsize = 0;
		
		if (inPUTpath.contains("computeTax"))
		{
			testpathsize = 10;
		}
		else if (inPUTpath.contains("triangleType"))
		{
			testpathsize = 4;
		}
		else if (inPUTpath.contains("line"))
		{
			testpathsize = 19;
		}
		else if (inPUTpath.contains("getDayNum"))
		{
			testpathsize = 5;
		}

//		for (int i = 1; i <= testpathsize; i++)
//			new PSOProcess(inPUTpath, i).start();
	}
}
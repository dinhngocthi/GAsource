package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args) throws InterruptedException
	{
//		String PUTName = "computeTax";
		int testpathsize = 10;

//		String PUTName = "triangleType";
//		int testpathsize = 4;

		String PUTName = "line";		
//		int testpathsize = 19;

//		String PUTName = "getDayNum";		
//		int testpathsize = 5;
	
		for (int i = 0; i <  testpathsize; i++)
			new PSOProcess(PUTName, i).start();
//		new PSOProcess(PUTName, 3).start();
	}

	public static void generateTC(String inPUTpath)
	{		
		String testpathfile = inPUTpath.replace(".c", ".txt");
		int testpathsize = gettestpathsize(testpathfile);
				
		for (int i = 0; i < testpathsize; i++)
			new PSOProcess(testpathfile, i).start();
	}
	private static int gettestpathsize(String testpathfile)
	{
		int ret = 0;
		try
		{
			FileReader fileReader = new FileReader(new File(testpathfile));
			BufferedReader br = new BufferedReader(fileReader);
			String line = null;
			// if no more lines the readLine() returns null
			while ((line = br.readLine()) != null) 
			{
		      // reading lines until the end of the file
				ret++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
}
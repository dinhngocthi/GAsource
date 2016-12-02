package pso;

import  java.io.*;

public class PSODriver 
{
	public static void main(String[] args)
	{
		long t1 = System.currentTimeMillis(); // start time

		new PSOProcess().execute(0);
		new PSOProcess().execute(1);		   
		
		long t2 = System.currentTimeMillis(); // end time
		System.out.println("Estimated time = " + (t2-t1));

		//getListFiles();

	}

	public static void getListFiles()
	{
		File folder = new File("Z:/10_Projects/40_JAëSî_ÇΩÇ‹Ç≤/02_ê›åvèë/í†ï[_20161130");
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) 
	    {
	      if (listOfFiles[i].isFile()) 
	      {
	        System.out.println("File " + listOfFiles[i].getName());
	      } 
	      else if (listOfFiles[i].isDirectory()) 
	      {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	}
}
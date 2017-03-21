package pso;

//this is the heart of the PSO program
//the code is for 2-dimensional space problem
//but you can easily modify it to solve higher dimensional space problem

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import pso.fitnessfunction.BalanExpressionBulk;

public class PSOProcess extends Thread  
{
	// Constants
	final int SWARM_SIZE = 30;
	final int MAX_ITERATION = 1000;
	final double C1 = 2.0;  // acceleration coefficient
	final double C2 = 2.0;  // acceleration coefficient
	final double W_UPPERBOUND = 1.0;
	final double W_LOWERBOUND = 0.0;
	
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];

	private String PUTName;
	private int testpathID;
	private int PROBLEM_DIMENSION = 10;
	
	Random generator = new Random();
	
	public PSOProcess(String PUTName, int testpathID)
	{
		this.PUTName = PUTName;
		this.testpathID = testpathID;		
		try
		{
			File file = new File(PUTName);
			BalanExpressionBulk balanExpressionBulk = new BalanExpressionBulk(file);
			PROBLEM_DIMENSION = balanExpressionBulk.getTotalParameterCount();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
 
	public void run()
	{
		initializeSwarm();
		updateFitnessList(PUTName, testpathID);

		for(int i=0; i<SWARM_SIZE; i++) 
		{
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int t = 0;
		double w;
		double err = 9999;

		while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) 
		{
			// step 1 - update pBest
			for (int i=0; i<SWARM_SIZE; i++) 
			{
				if (fitnessValueList[i] < pBest[i]) 
				{
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}

			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) 
			{
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			
			for(int i=0; i<SWARM_SIZE; i++) 
			{
				double r1 = generator.nextDouble(); // random number between 0 and 1
				double r2 = generator.nextDouble(); // random number between 0 and 1

				Particle p = swarm.get(i);

				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				for (int j = 0; j < PROBLEM_DIMENSION; j++)
				{
					newVel[j] = (w * p.getVelocity().getPos()[j]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
							(r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]);
				}
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);

				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];
				for (int j = 0; j < PROBLEM_DIMENSION; j++)
				{
					newLoc[j] = p.getLocation().getLoc()[j] + newVel[j];
				}
				Location loc = new Location(newLoc);
				p.setLocation(loc);
			}
			
			err = ProblemSet.evaluate(gBestLocation, PUTName, testpathID); // minimizing the functions means it's getting closer to 0

			String testpathfile = this.PUTName.replace(".txt", (testpathID + 1) + ".txt");			
			try
			{
				FileWriter fw = new FileWriter(testpathfile, true); //the true will append the new data
				fw.write("ITERATION " + t + ": \n");
				//System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
				//System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
				for (int i = 0; i < PROBLEM_DIMENSION; i++)
					fw.write("     Best X" + (i + 1) + ": " + (int)gBestLocation.getLoc()[i] + "\n");
				fw.write("     Value: " + err + "\n");
				fw.close();
			}
			catch (Exception e) {}

			t++;
			updateFitnessList(PUTName, testpathID);
		}

		System.out.println("===============================================");
		System.out.println("Test path ID:  " + (testpathID + 1));
		System.out.println("Solution found at iteration " + (t - 1) + ", the solutions is:");

		for (int i = 0; i < PROBLEM_DIMENSION; i++)
			System.out.println("     Best X" + (i + 1) + ": " + (int)gBestLocation.getLoc()[i]);
		System.out.println("===============================================");

        try
        {
        	FileWriter fw = new FileWriter(this.PUTName, true); //the true will append the new data
        	fw.write("===============================================\n");//appends the string to the file        	
        	fw.write("Test path ID:  " + (testpathID + 1) + "\n");
        	fw.write("Solution found at iteration " + (t - 1) + ", the solutions is:\n");
     		for (int i = 0; i < PROBLEM_DIMENSION; i++)
     			fw.write("     Best X" + (i + 1) + ": " + (int)gBestLocation.getLoc()[i] + "\n");
     		fw.write("===============================================\n");//appends the string to the file
     		fw.close();
        }
        catch (Exception e) {}
	}
	
	public void initializeSwarm() 
	{
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) 
		{
			p = new Particle();
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];			
			for (int j = 0; j < PROBLEM_DIMENSION; j++)
				loc[j] = ProblemSet.LOC_LOW + generator.nextDouble() * (ProblemSet.LOC_HIGH - ProblemSet.LOC_LOW);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];			
			for (int j = 0; j < PROBLEM_DIMENSION; j++)
				vel[j] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
	
	public void updateFitnessList(String PUTName, int testpathID) 
	{
		for(int i = 0; i < SWARM_SIZE; i++) 
		{
			fitnessValueList[i] = swarm.get(i).getFitnessValue(PUTName, testpathID);
		}
	}
}

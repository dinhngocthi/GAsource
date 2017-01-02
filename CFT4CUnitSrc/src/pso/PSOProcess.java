package pso;

//this is the heart of the PSO program
//the code is for 2-dimensional space problem
//but you can easily modify it to solve higher dimensional space problem

import java.util.Random;
import java.util.Vector;

//public class PSOProcess implements PSOConstants
public class PSOProcess extends Thread  
{
	// Constants
	final int SWARM_SIZE = 30;
	final int MAX_ITERATION = 100;
	final int PROBLEM_DIMENSION = 3;
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
	private int branchID;
	
	Random generator = new Random();
	
	PSOProcess(int branchID)
	{
		this.branchID = branchID;	
	}

	//public void execute(int functionID) 
	public void run()
	{
		//int functionID = PSOConstants.functionID;
		System.out.println("branch ID = " + branchID);

		initializeSwarm();
		updateFitnessList(branchID);

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
			
			err = ProblemSet.evaluate(gBestLocation, branchID) - 0; // minimizing the functions means it's getting closer to 0
/*
			System.out.println("ITERATION " + t + ": ");
			System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
			System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
			System.out.println("     Best Z: " + gBestLocation.getLoc()[2]);
			System.out.println("     Value: " + err);
*/
			t++;
			updateFitnessList(branchID);
		}
		
		System.out.println("Solution found at iteration " + (t - 1) + ", the solutions is:");
		System.out.println("     Best X: " + (int)gBestLocation.getLoc()[0]);
		System.out.println("     Best Y: " + (int)gBestLocation.getLoc()[1]);
		//System.out.println("     Best Z: " + gBestLocation.getLoc()[2]);
	}
	
	public void initializeSwarm() 
	{
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) 
		{
			p = new Particle();
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
			loc[0] = ProblemSet.LOC_LOW + generator.nextDouble() * (ProblemSet.LOC_HIGH - ProblemSet.LOC_LOW);
			loc[1] = ProblemSet.LOC_LOW + generator.nextDouble() * (ProblemSet.LOC_HIGH - ProblemSet.LOC_LOW);
			loc[2] = ProblemSet.LOC_LOW + generator.nextDouble() * (ProblemSet.LOC_HIGH - ProblemSet.LOC_LOW);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[2] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
	
	public void updateFitnessList(int branchID) 
	{
		for(int i = 0; i < SWARM_SIZE; i++) 
		{
			fitnessValueList[i] = swarm.get(i).getFitnessValue(branchID);
		}
	}
}

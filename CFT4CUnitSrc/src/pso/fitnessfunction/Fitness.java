package pso.fitnessfunction;

public class Fitness {
	public double fx(double[] x)
	{
		double x0 = x[0]; 
    	double x1 = x[1];
    	double x2 = x[2];
		double ret = Math.sqrt(x0 - 1) + Math.sqrt(x1 - 2) + Math.sqrt(x2 - 10);
		return ret;
	}
}
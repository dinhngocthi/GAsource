package newpso;

public class PSODriver 
{	
	public static void main(String[] args) throws InterruptedException
	{
		new PSOProcess(1).start();
		System.out.println("Completed");
	}
}
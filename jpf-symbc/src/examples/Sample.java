
public class Sample {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		example(1, 1, 1);
	}

    public static int example(int x, int y, double z) 
    {
    	int ret = 0;
        boolean flag = y > 1000;
        if (x + y == 1024)
        {
            if (flag)
            { 
                if ((Math.cos(z) - 0.95) < Math.exp(z))
                {
                	ret = 1;
                    System.out.println("path 1");
                }
                else
                {
                	ret = 2;
                	System.out.println("path 2");                    
                }
            }
            else
            {
            	ret = 3;
            	System.out.println("path 3");
            }
        }
        else
        {
        	ret = 4;
        	System.out.println("path 4");
        }

        return ret;
    }    
}

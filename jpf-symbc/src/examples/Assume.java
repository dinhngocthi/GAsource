import gov.nasa.jpf.symbc.Debug;

public class Assume {
	public int test(int x, int y) {
		Debug.assume(x>y);
		return x-y;
	}
	public int test1(int a, int b, int c) {
//		Debug.assume(x>y);
//		return x-y;
		
        double root1, root2;
        int path1, path2, path3, path4, path5;
        path1 = path2 = path3 = path4 = path5 = 0;
/*
        if (a == 0)
        {
            if (b != 0)
            {
//                root1 = (-c)/b;
                path1++;
            }
            else
            {
            	path2++;
            }         
        }
*/        
        return 0;
	}

    public double QuadraticEquation2(double a, double b, double c)
    {
    	System.out.println("AAAA");
        double root1, root2;
        int path1, path2, path3, path4, path5;
        path1 = path2 = path3 = path4 = path5 = 0;
        double ret = 0;

        if (a == 0)
        {
            if (b != 0)
            {
                root1 = (-c)/b;
                path1++;
                ret = 1;
            }
            else
            {
            	path2++;
            	ret = 2;
            }         
        }
/*
        //if (((b*b) - (4*a*c)) < 0)
        if (true)
        {            
        	path3++;
        	ret = 3;
        }        
        else 
        {
            //if (((b*b) - (4*a*c)) == 0)
        	if (true)
            {
                root1 = (-b)/(a*2);
                path4++;
                ret = 4;
            }
            else
            {
                root1 = ((-b + Math.sqrt(((b*b) - (4*a*c))))/(2*a));
                root2 = ((-b - Math.sqrt(((b*b) - (4*a*c))))/(2*a));
                path5++;
                ret = 5;
            }
        }
*/
        return ret;
    }

	// The test driver
	public static void main(String[] args)
	{
		Assume testinst = new Assume();
//		int x = testinst.test(1, 2);
//		x = testinst.test1(1, 2, 3);
		testinst.QuadraticEquation2(1, 2, 3);
//		System.out.println("symbolic value of x: " + Debug.getSymbolicIntegerValue(x));
//		Debug.printPC("\n Path Condition: ");
	}
}
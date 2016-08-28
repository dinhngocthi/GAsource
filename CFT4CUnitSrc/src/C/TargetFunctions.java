package C;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.refactoring.descriptors.ExtractSuperclassDescriptor;

import SMTSolver.RunZ3OnCMD;

public class TargetFunctions
{
    public static int path1, path2, path3, path4, path5, path6;
    public static boolean stopCriteria; 
    
    TargetFunctions()
    {
        path1 = path2 = path3 = path4 = path5 = path6 = 0;
        stopCriteria = false;
    }
        
    public static void main(String[] args)
    {    	
    	example3(2, 5, 5);
    }

    public static double example4(double a, double b, double c)
    {
        double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;
        
        if (a == b)
        {
        	ret1 = 100 - Math.abs(c); 
        	if (Math.abs(c) > 100)
        		path1++;
        	else
        		path2++;
        }
        else if (a > b)
        {
        	ret1 = 100 - Math.abs(c);
        	if (Math.abs(c) > 100)
        		path3++;
        	else
        		path4++;
        }
        else
        {
        	ret1 = 100 - Math.abs(c);
        	if (Math.abs(c) > 100)
        		path5++;
        	else
        		path6++;
        }
    	return (ret1 + ret2 + ret3);
    }
    
    public static double example3(double corner, double edge1, double edge2) 
    {
        double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;

        ret1 = Math.min(-corner, corner - Math.PI);
    	if (corner > 0 && corner < Math.PI)
    	{
    		ret2 = Math.abs(edge1 - edge2);
    		if (edge1 == edge2) {
    			ret3 = (0.01 - Math.abs(Math.toDegrees(corner) - 60));
    			if (Math.abs(Math.toDegrees(corner) - 60) < 0.01) {
    				path1 ++;  // Equilateral
//    				System.out.println("Equilateral");
    			}
    			else {
    				path2 ++;  // Isosceles
//    				System.out.println("Isosceles");
    			}
    		}
    		else {
    			path3 ++; // Scalene
//    			System.out.println("Scalene");
    		}
    	}
    	else {
    		path4++; // Not a triangle
//    		System.out.println("Not a triangle");
    	}

        stopCriteria = (path1 > 0) && (path2 > 0);
    	return (ret1 + ret2 + ret3);
    }

    public static double example2(double x, double y, double z) 
    {
    	double ret = Math.min(Math.abs(x - Math.hypot(y, z)), Math.abs(y - z));    	

    	if ((Math.round(x) == Math.round(Math.hypot(y, z))) && (y == z))
    		path1++;
    	else
    		path2++;

        stopCriteria = (path1 > 0) && (path2 > 0);
    	return ret;
    }
    
    public double example1(double x, double y, double z) 
    {
        double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;
        
        ret1 = (Math.cos(z) - 0.95) - Math.exp(z);
        if (Math.cos(z) - 0.95 < Math.exp(z))
        {
        	if ((x + y == 1024) && (y > 1000))
        		path1++;
        }
        else
        {
        	path2++;
        }

        stopCriteria = (path1 > 0) && (path2 > 0);
        return (ret1 + ret2 + ret3);
    }

    public static double Tritype(double a, double b, double c)
    {
    	int type = -1;
        
    	double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;

        ret1 = -(a + b + c); // if (a + b > c && a + c > b && b + c > a) 
    	if (a + b > c && a + c > b && b + c > a)
    	{
    		ret2 = Math.abs(a - b) + Math.abs(b - c); 
    		if (a == b && b == c)
	    	{
	    		type = 3;  // Equilateral
	    		//System.out.println("Equilateral");
	    		path2++;
	    	}
	    	else
	    	{
	    		ret3 = Math.min(Math.min(Math.abs(a - b), Math.abs(b - a)), Math.abs(c - a));
	    		if (a == b || b == c || c == a)
		    	{
		    		type = 2;  // Isosceles
		    		//System.out.println("Isosceles");
		    		path3++;
		    	}
		    	else
		    	{
		    		type = 1;  // Scalene
		    		//System.out.println("Scalene");
		    		path4++;
		    	}
	    	}
    	}
    	else
    	{
    		type = -1; // Not a triangle
    		//System.out.println("Not a triangle");
    		path1++;
    	}

    	stopCriteria = (path1 > 0) && (path2 > 0) && (path3 > 0) && (path4 > 0);
    	return (ret1 + ret2 + ret3);
    }
    
    public double insertion(double[] anyArray, int length)
    {
        double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;
	
        for (int i = 1; i < length; i++)
        {
        	double x = anyArray[i];
        	int j = i - 1;
        	while ((j > 0) && (anyArray[j] > x))
        	{
        		anyArray[j+1] = anyArray[j];
        		j = j - 1;
        		anyArray[j+1] = x;
        	}
        }
        stopCriteria = (path1 > 0) && (path2 > 0) && (path3 > 0) && (path4 > 0);
        return (ret1 + ret2 + ret3);
    }

    public double example(int x, int y, double z) 
    {
        double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;
        boolean flag = y > 1000; 
//        ret1 = Math.abs((x + y) - 1024);
//        if (x + y == 1024)
        {
            ret2 = (1000 - y);
//            if (y > -0)
        	if (flag)
            {
                ret3 = (Math.cos(z) - 0.95) - Math.exp(z);
                if (Math.cos(z) - 0.95 < Math.exp(z))
                {
                    path1++;                    
                }
                else
                {
                    path2++;
/*
                    if (path2 == 1)
                    {
                    	System.out.println("example");
                    	System.out.println("Call times = " + calltime);
                    }
*/
                }
            }
            else
            {
                path3++;
            }
        }
//        else
        {
//            path4++;
        }

        //return Math.min(ret1, Math.min(ret2, ret3));
        stopCriteria = (path1 > 0) && (path2 > 0) && (path3 > 0) && (path4 > 0);
        //stopCriteria = (path1 > 0) && (path2 > 0) && (path4 > 0);
        return (ret1 + ret2 + ret3);
    }
/*    
    public void example(int x, int y, double z, ArrayList<VertexTF> executedPath) 
    {
        if (x + y == 1024)
        {
            // instrumented code
            executedPath.add(new VertexTF(1, "T"));

            if (y > 1000)
            {
                // instrumented code                
                executedPath.add(new VertexTF(2, "T"));
 
                if (Math.cos(z) - 0.95 < Math.exp(z))
                {
                    // instrumented code
                    executedPath.add(new VertexTF(3, "T"));

                    path1++;
                }
                else
                {
                    // instrumented code                    
                    executedPath.add(new VertexTF(3, "F"));

                    path2++;
                }
            }
            else
            {
                // instrumented code                
                executedPath.add(new VertexTF(2, "F"));

                path3++;
            }
        }
        else
        {
            // instrumented code            
            executedPath.add(new VertexTF(1, "F"));
            
            path4++;
        }
    }
*/

    public double QuadraticEquation2(double a, double b, double c)
    {
        double root1, root2;

        double ret1, ret2, ret3, ret4;
        ret1 = ret2 = ret3 = ret4 = 0;

        ret1 = Math.abs(a);
        if (a == 0)
        {
        	ret2 = Math.abs(a);
            if (b != 0)
            {
                root1 = (-c)/b;
                path1++;
            }
            else
            {
            	path2++;
            }
            //return ret1;
            return (ret1 + ret2);
        }        

        ret2 = (b*b) - (4*a*c);
        if (((b*b) - (4*a*c)) < 0)
        {            
        	path3++;
        	return ret2;
        }        
        else 
        {
        	ret3 = Math.abs((b*b) - (4*a*c));
            if (((b*b) - (4*a*c)) == 0)
            {
                root1 = (-b)/(a*2);
                path4++;
            }
            else
            {
                root1 = ((-b + Math.sqrt(((b*b) - (4*a*c))))/(2*a));
                root2 = ((-b - Math.sqrt(((b*b) - (4*a*c))))/(2*a));
                path5++;
            }
        }
        
        stopCriteria = (path1 > 0) && (path2 > 0) && (path3 > 0) && (path4 > 0) && (path5 > 0);
        return (ret1 + ret2 + ret3);
    }
    
    
/*
    public int tritypeBueno2002(double a, double b, double c, ArrayList<VertexTF> executedPath)
    {
        int    type = -1; // Scalene
        double area = 0;
        
        if ((a < b) || (b < c))
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            return type;
        }        
        // instrumented code
        VertexTF vertex1  = new VertexTF();
        vertex1.id = 1;
        vertex1.decision  = "F"; 
        executedPath.add(vertex1);

        if (a >= (b + c))
        {
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "T"; 
            executedPath.add(vertex2);

            return type;
        }
        // instrumented code
        VertexTF vertex2  = new VertexTF();
        vertex2.id = 2;
        vertex2.decision  = "F"; 
        executedPath.add(vertex2);

        if ((a != b) && (b != c) ) // escaleno
        {
            // instrumented code
            VertexTF vertex3  = new VertexTF();
            vertex3.id = 3;
            vertex3.decision  = "T"; 
            executedPath.add(vertex3);

            double as = a*a;
            double bs = b*b;
            double cs = c*c;
            
            if (as == (bs + cs))  // retangulo
            {
                // instrumented code
                VertexTF vertex6  = new VertexTF();
                vertex6.id = 6;
                vertex6.decision  = "T"; 
                executedPath.add(vertex6);

                type = 2;// 'Rectangle';
                area = (b*c)/2.0;
            }
            else
            {
                // instrumented code
                VertexTF vertex6  = new VertexTF();
                vertex6.id = 6;
                vertex6.decision  = "F"; 
                executedPath.add(vertex6);

                double s = (a+b+c) / 2.0;
                area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
                
                if ( as < bs + cs )
                {
                    type = 3; // agudo
                    
                    // instrumented code
                    VertexTF vertex7  = new VertexTF();
                    vertex7.id = 7;
                    vertex7.decision  = "T"; 
                    executedPath.add(vertex7);
                }
                else
                {
                    type = 4; // obtuso
                    
                    // instrumented code
                    VertexTF vertex7  = new VertexTF();
                    vertex7.id = 7;
                    vertex7.decision  = "F"; 
                    executedPath.add(vertex7);
                }
            }
        }
        else
        {
            // instrumented code
            VertexTF vertex3  = new VertexTF();
            vertex3.id = 3;
            vertex3.decision  = "F"; 
            executedPath.add(vertex3);

            if ((a == b) && (b == c))
            {
                type = 5;  // equilatero 
                area = a*a*Math.sqrt(3.0)/4.0;
                
                // instrumented code
                VertexTF vertex4  = new VertexTF();
                vertex4.id = 4;
                vertex4.decision  = "T"; 
                executedPath.add(vertex4);
            }
            else
            {
                // instrumented code
                VertexTF vertex4  = new VertexTF();
                vertex4.id = 4;
                vertex4.decision  = "F"; 
                executedPath.add(vertex4);

                type = 6; // isoceles 

                if (a == b)
                {
                    area = c*Math.sqrt(4*a*b-c*c)/4.0;
                    
                    // instrumented code
                    VertexTF vertex5  = new VertexTF();
                    vertex5.id = 5;
                    vertex5.decision  = "T"; 
                    executedPath.add(vertex5);
                }
                else
                {
                    area = a*Math.sqrt(4*b*c-a*c)/4.0;
                    
                    // instrumented code
                    VertexTF vertex5  = new VertexTF();
                    vertex5.id = 5;
                    vertex5.decision  = "F"; 
                    executedPath.add(vertex5);
                }
            }
        }
        
        return type;
    }
    
    public int triangleMansour2004(double a, double b, double c, ArrayList<VertexTF> executedPath)
    {
        int type = 1; // Scalene
        
        if (a == b)
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            if (b == c)
            {
                type = 2; // Equilateral

                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);
            }
            else 
            {
                type = 3; // Isosceles
                
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "F"; 
                executedPath.add(vertex2);
            }
        }
        else
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "F"; 
            executedPath.add(vertex1);

            if (b == c)
            {
                type = 3; // Isosceles
                
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);
            }
            else
            {
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "F"; 
                executedPath.add(vertex2);
            }
        }
        if (a*a == (b*b + c*c))
        {
            type = 4; // Right
            
            // instrumented code
            VertexTF vertex3  = new VertexTF();
            vertex3.id = 3;
            vertex3.decision  = "T"; 
            executedPath.add(vertex3);
        }
        else
        {
            // instrumented code
            VertexTF vertex3  = new VertexTF();
            vertex3.id = 3;
            vertex3.decision  = "F"; 
            executedPath.add(vertex3);            
        }
        
        return type;
    }

    public void QuadraticEquation2(double a, double b, double c, ArrayList<VertexTF> executedPath)
    {
        double root1, root2;

        if (a == 0)
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            if (b != 0)
            {
                root1 = (-c)/b;
                
                VertexTF vertex4  = new VertexTF();
                vertex4.id = 4;
                vertex4.decision  = "T"; 
                executedPath.add(vertex4);
            }
            else
            {
                VertexTF vertex4  = new VertexTF();
                vertex4.id = 4;
                vertex4.decision  = "F"; 
                executedPath.add(vertex4);    
            }
            return;
        }
        // instrumented code
        VertexTF vertex1  = new VertexTF();
        vertex1.id = 1;
        vertex1.decision  = "F"; 
        executedPath.add(vertex1);

        //double delta = ((b*b) - (4*a*c));
        
        if (((b*b) - (4*a*c)) < 0)
        {
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "T"; 
            executedPath.add(vertex2);
            
            return;
        }
        else 
        {
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "F"; 
            executedPath.add(vertex2);

            if (((b*b) - (4*a*c)) == 0)
            {
                root1 = (-b)/(a*2);
                
                // instrumented code
                VertexTF vertex3  = new VertexTF();
                vertex3.id = 3;
                vertex3.decision  = "T"; 
                executedPath.add(vertex3);
            }
            else
            {
                root1 = ((-b + Math.sqrt(((b*b) - (4*a*c))))/(2*a));
                root2 = ((-b - Math.sqrt(((b*b) - (4*a*c))))/(2*a));
                
                // instrumented code
                VertexTF vertex3  = new VertexTF();
                vertex3.id = 3;
                vertex3.decision  = "F"; 
                executedPath.add(vertex3);
            }
        }
    }
    
    public int gcd(int number[], ArrayList<VertexTF> executedPath)
    {
        int a = number[0];
        int b = number[1];

        if (a == 0)
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);
            
            return b;
        }
        else
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "F"; 
            executedPath.add(vertex1);

            while (b != 0)
            {
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);

                if (a > b)
                {
                    a = a - b;
                    
                    // instrumented code
                    VertexTF vertex3  = new VertexTF();
                    vertex3.id = 3;
                    vertex3.decision  = "T"; 
                    executedPath.add(vertex3);
                }
                else
                {
                    b = b - a;
                    
                    // instrumented code
                    VertexTF vertex3  = new VertexTF();
                    vertex3.id = 3;
                    vertex3.decision  = "F"; 
                    executedPath.add(vertex3);
                }
            }
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "F"; 
            executedPath.add(vertex2);

            return a;
        }
    }
    
    public int mmTriangle(double  arr[], int size, ArrayList<VertexTF> executedPath)
    {
        double min = arr[0];
        double max = arr[0];
        int i = 1;
        while (i < size)
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            if (min > arr[i])
            {
                min = arr[i];
                
                // instrumented code
                VertexTF vertex5  = new VertexTF();
                vertex5.id = 5;
                vertex5.decision  = "T"; 
                executedPath.add(vertex5);
            }
            else
            {
                // instrumented code
                VertexTF vertex5  = new VertexTF();
                vertex5.id = 5;
                vertex5.decision  = "F"; 
                executedPath.add(vertex5);   
            }
            
            if (max < arr[i]) 
            {
                max = arr[i];
                
                // instrumented code
                VertexTF vertex6  = new VertexTF();
                vertex6.id = 6;
                vertex6.decision  = "T"; 
                executedPath.add(vertex6);
            }
            else
            {
                // instrumented code
                VertexTF vertex6  = new VertexTF();
                vertex6.id = 6;
                vertex6.decision  = "F"; 
                executedPath.add(vertex6);
            }
            i++;
        }
        // instrumented code
        VertexTF vertex1  = new VertexTF();
        vertex1.id = 1;
        vertex1.decision  = "F"; 
        executedPath.add(vertex1);
        
        double a = arr[0];
        double b = arr[1];
        double c = arr[2];

        int trityp = 0;
        if ((a + b > c) && (b + c > a) && (c + a > b))
        {
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "T"; 
            executedPath.add(vertex2);

            if ((a != b) && (b != c) && (c != a))
            {
                trityp = 1;      // Scalene
                
                // instrumented code
                VertexTF vertex3  = new VertexTF();
                vertex3.id = 3;
                vertex3.decision  = "T"; 
                executedPath.add(vertex3);
            }
            else
            {
                // instrumented code
                VertexTF vertex3  = new VertexTF();
                vertex3.id = 3;
                vertex3.decision  = "F"; 
                executedPath.add(vertex3);

                if (((a == b) && (b != c)) || ((b == c) && (c != a)) || ((c == a) && (a != b)))
                {
                    trityp = 2;  // Isosceles
                    
                    // instrumented code
                    VertexTF vertex4  = new VertexTF();
                    vertex4.id = 4;
                    vertex4.decision  = "T"; 
                    executedPath.add(vertex4);
                }
                else
                {
                    trityp = 3;  // Equilateral
                    
                    // instrumented code
                    VertexTF vertex4  = new VertexTF();
                    vertex4.id = 4;
                    vertex4.decision  = "F"; 
                    executedPath.add(vertex4);
                }
            }
        }
        else
        {
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "F"; 
            executedPath.add(vertex2);
            
            trityp = -1;        // Not a triangle
        }
        
        return trityp;
    }

    public void getMinMax(double a[], int size, ArrayList<VertexTF> executedPath)
    {
        double min = a[0];
        double max = a[0];
        int i = 1;
        
        while (i < size)
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            if (min > a[i]) 
            {
                min = a[i];
                
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);
            }
            else
            {
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "F"; 
                executedPath.add(vertex2);
            }
            
            if (max < a[i]) 
            {
                max = a[i];
                
                // instrumented code
                VertexTF vertex3  = new VertexTF();
                vertex3.id = 3;
                vertex3.decision  = "T"; 
                executedPath.add(vertex3);
            }
            else
            {
                // instrumented code
                VertexTF vertex3  = new VertexTF();
                vertex3.id = 3;
                vertex3.decision  = "F"; 
                executedPath.add(vertex3);
            }
            i++;
        }
        // instrumented code
        VertexTF vertex1  = new VertexTF();
        vertex1.id = 1;
        vertex1.decision  = "F"; 
        executedPath.add(vertex1);
    }
    
    public void InsertionSort (double a[], int size, ArrayList<VertexTF> executedPath)
    { 
        for (int i=1; i<size; i++) 
        { 
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            int j=i;
            while (j>0 && a[j]<a[j-1]) 
            { 
                //swap 
                double temp=a[j-1]; 
                a[j-1]=a[j]; 
                a[j]=temp; 
                j--; 
                
                // instrumented code
                VertexTF vertex2  = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);
            }
            // instrumented code
            VertexTF vertex2  = new VertexTF();
            vertex2.id = 2;
            vertex2.decision  = "F"; 
            executedPath.add(vertex2);
        } 
        // instrumented code
        VertexTF vertex1  = new VertexTF();
        vertex1.id = 1;
        vertex1.decision  = "F"; 
        executedPath.add(vertex1);
    }
    
    public int tA2008_Triangle(double a, double b, double c, ArrayList<VertexTF> executedPath)
    {        
        int trityp = 0;

        if ((a + b > c) && (b + c > a) && (c + a > b))
        {
            // instrumented code
            VertexTF vertex1  = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            if ((a != b) && (b != c) && (c != a))
            {
                trityp = 1;      // Scalene

                // instrumented code
                VertexTF vertex2 = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);
            }
            else
            {
                // instrumented code
                VertexTF vertex2 = new VertexTF();
                vertex2.id = 2;
                vertex2.decision  = "F"; 
                executedPath.add(vertex2);

                if (((a == b) && (b != c)) || ((b == c) && (c != a)) || ((c == a) && (a != b)))
                {
                    trityp = 2;  // Isosceles
                    
                    // instrumented code
                    VertexTF vertex3 = new VertexTF();
                    vertex3.id = 3;
                    vertex3.decision  = "T"; 
                    executedPath.add(vertex3);
                }
                else
                {
                    trityp = 3;  // Equilateral
                    
                    // instrumented code
                    VertexTF vertex3 = new VertexTF();
                    vertex3.id = 3;
                    vertex3.decision  = "F"; 
                    executedPath.add(vertex3);
                }
            }
        }
        else
        {
            trityp = -1;        // Not a triangle
            
            // instrumented code
            VertexTF vertex1 = new VertexTF();
            vertex1.id = 1;
            vertex1.decision  = "F"; 
            executedPath.add(vertex1);
        }
        
        return trityp;
    }
*/    
}

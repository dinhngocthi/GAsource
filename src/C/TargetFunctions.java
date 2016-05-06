package C;

import java.io.IOException;
import java.util.ArrayList;

import SMTSolver.RunZ3OnCMD;

public class TargetFunctions
{
    public static void main(String[] args)
    {
        double a = 9.00000000701390;
        double b = 9.00000000701389;
        double epsilon = 0.000001;
        if (Math.abs(a-b) < epsilon)
        {
            System.out.format("%8.10f", Math.abs(a-b));
            System.out.println();
        }
        if (a == b)
        {
            System.out.format("%8.10f", Math.abs(a-b));
        }
    }

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

        if ((a != b) && (b != c) ) /* escaleno */
        {
            // instrumented code
            VertexTF vertex3  = new VertexTF();
            vertex3.id = 3;
            vertex3.decision  = "T"; 
            executedPath.add(vertex3);

            double as = a*a;
            double bs = b*b;
            double cs = c*c;
            
            if (as == (bs + cs))  /* retangulo */
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
                    type = 3; /* agudo */
                    
                    // instrumented code
                    VertexTF vertex7  = new VertexTF();
                    vertex7.id = 7;
                    vertex7.decision  = "T"; 
                    executedPath.add(vertex7);
                }
                else
                {
                    type = 4; /* obtuso */
                    
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
                type = 5;  /* equilatero */
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

                type = 6; /* isoceles */

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
            }
            return;
        }
        // instrumented code
        VertexTF vertex1  = new VertexTF();
        vertex1.id = 1;
        vertex1.decision  = "F"; 
        executedPath.add(vertex1);

        double delta = ((b*b) - (4*a*c));
        
        if (delta < 0)
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

            if (delta == 0)
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
                root1 = ((-b + Math.sqrt(delta))/(2*a));
                root2 = ((-b - Math.sqrt(delta))/(2*a));
                
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
    
    public int Tritype(double a, double b, double c, ArrayList<VertexTF> executedPath)
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
}

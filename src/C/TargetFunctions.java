package C;

import java.util.ArrayList;

public class TargetFunctions
{
    public static void main(String[] args)
    {
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

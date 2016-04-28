package C;

import java.util.ArrayList;

public class TargetFunctions
{
    public int Tritype(double a, double b, double c, ArrayList<VertexTF> executedPath)
    {
        
        int trityp = 0;
        if ((a + b > c) && (b + c > a) && (c + a > b))
        {
            VertexTF vertex1  = new VertexTF();
            vertex1.statement = "(a+b>c)&&(b+c>a)&&(c+a>b)";
            vertex1.decision  = "T"; 
            executedPath.add(vertex1);

            if ((a != b) && (b != c) && (c != a))
            {
                trityp = 1;      // Scalene
                VertexTF vertex2 = new VertexTF();
                vertex2.statement = "(a!=b)&&(b!=c)&&(c!=a)";
                vertex2.decision  = "T"; 
                executedPath.add(vertex2);
            }
            else
            {
                VertexTF vertex2 = new VertexTF();
                vertex2.statement = "(a!=b)&&(b!=c)&&(c!=a)";
                vertex2.decision  = "F"; 
                executedPath.add(vertex2);

                if (((a == b) && (b != c)) || ((b == c) && (c != a)) || ((c == a) && (a != b)))
                {
                    trityp = 2;  // Isosceles
                    VertexTF vertex3 = new VertexTF();
                    vertex3.statement = "((a==b)&&(b!=c))||((b==c)&&(c!=a))||((c==a)&&(a!=b))";
                    vertex3.decision  = "T"; 
                    executedPath.add(vertex3);
                }
                else
                {
                    trityp = 3;  // Equilateral
                    VertexTF vertex3 = new VertexTF();
                    vertex3.statement = "((a==b)&&(b!=c))||((b==c)&&(c!=a))||((c==a)&&(a!=b))";
                    vertex3.decision  = "F"; 
                    executedPath.add(vertex3);
                }
            }
        }
        else
        {
            trityp = -1;        // Not a triangle
            VertexTF vertex = new VertexTF();
            vertex.statement = "(a+b>c)&&(b+c>a)&&(c+a>b)";
            vertex.decision  = "F"; 
            executedPath.add(vertex);
        }
        
        return trityp;
    }
}

int tritypeBueno2002(double a, double b, double c, ArrayList<VertexTF> executedPath)
{
    int    type = -1; // Scalene
    double area = 0;
    
    if ((a < b) || (b < c))
    {
        return type;
    }
    
    if (a >= (b + c))
    {
        return type;
    }
    
    if ((a != b) && (b != c) ) /* escaleno */
    {
        double as = a*a;
        double bs = b*b;
        double cs = c*c;
        
        if (as == (bs + cs))  /* retangulo */
        {
            type = 2;// 'Rectangle';
            area = (b*c) / 2.0;
        }
        else
        {
            double s = (a+b+c) / 2.0;
            area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
            
            if ( as < bs + cs )
            {
                type = 3; /* agudo */
            }
            else
            {
                type = 4; /* obtuso */
            }
        }
    }
    else
    {
        if ((a == b) && (b == c))
        {
            type = 5;  /* equilatero */
            area = a*a*Math.sqrt(3.0)/4.0;
        }
        else
        {
            type = 6; /* isoceles */

            if (a == b)
            {
                area = c*Math.sqrt(4*a*b-c*c)/4.0;
            }
            else
            {
                area = a*Math.sqrt(4*b*c-a*c)/4.0;
            }
        }
    }
    
    return type;
}
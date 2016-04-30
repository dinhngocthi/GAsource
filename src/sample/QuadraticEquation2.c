void QuadraticEquation2(double a, double b, double c, ArrayList<VertexTF> executedPath)
{
	double root1, root2;

	if (a == 0)
	{
		if (b != 0)            
		{
			root1 = (-c)/b;
		}
		return;
	}

	double delta = ((b*b) - (4*a*c));
	
	if (delta < 0)
	{
		return;
	}
	else 
	{
		if (delta == 0)
		{
			root1 = (-b)/(a*2);
		}
		else
		{
			root1 = ((-b + Math.sqrt(delta))/(2*a));
			root2 = ((-b - Math.sqrt(delta))/(2*a));
		}
	}
}
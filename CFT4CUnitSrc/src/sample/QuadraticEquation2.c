void QuadraticEquation2(double a, double b, double c)
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
	
	if (((b*b) - (4*a*c)) < 0)
	{
		return;
	}
	else 
	{
		if (((b*b) - (4*a*c)) == 0)
		{
			root1 = (-b)/(a*2);
		}
		else
		{
			root1 = ((-b + Math.sqrt(((b*b) - (4*a*c))))/(2*a));
			root2 = ((-b - Math.sqrt(((b*b) - (4*a*c))))/(2*a));
		}
	}
}
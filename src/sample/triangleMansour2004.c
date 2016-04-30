int triangleMansour2004(double a, double b, double c)
{
	int type = 1; // Scalene
	
	if (a == b)
	{
		if (b == c)
		{
			type = 2; // Equilateral
		}
		else 
		{
			type = 3; // Isosceles
		}
	}
	else
	{
		if (b == c)
		{
			type = 3; // Isosceles
		}
	}
	if (a*a == (b*b + c*c))
	{
		type = 4; // Right
	}
	else
	{
		
	}
	
	return type;
}
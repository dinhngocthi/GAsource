double Tritype(double a, double b, double c)
{
	int type = -1;

	if (a + b > c && a + c > b && b + c > a)
	{
		if (a == b && b == c)
		{
			type = 3;  // Equilateral
		}
		else
		{
			if (a == b || b == c || c == a)
			{
				type = 2;  // Isosceles
			}
			else
			{
				type = 1;  // Scalene
			}
		}
	}
	else
	{
		type = -1; // Not a triangle
	}
	return type;
}

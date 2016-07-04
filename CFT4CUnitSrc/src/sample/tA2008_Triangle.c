int Tritype(double a, double b, double c)
{
	int trityp = 0;
	if ((a + b > c) && (b + c > a) && (c + a > b))
	{
		if ((a != b) && (b != c) && (c != a))
			trityp = 1;  	 // Scalene
		else
			if (((a == b) && (b != c)) || ((b == c) && (c != a)) || ((c == a) && (a != b)))
				trityp = 2;  // Isosceles
			else
				trityp = 3;  // Equilateral
	}
	else
		trityp = -1; 		// Not a triangle

	return trityp;
}

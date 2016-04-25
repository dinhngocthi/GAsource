void mmTriangle(double  arr[], int size)
{
	double min = arr[0];
	double max = arr[0];
	int i = 1;
	while (i < size)
	{
		if (min > arr[i]) min = arr[i];
		if (max < arr[i]) max = arr[i];
		i++;
	}
	
	double a = arr[0];
	double b = arr[1];
	double b = arr[2];

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
}
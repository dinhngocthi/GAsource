double Tritype(double aaa, double bbb, double ccc)
{
	int type = -1;

	if (aaa + bbb > ccc && aaa + ccc > bbb && bbb + ccc > aaa)
	{
		if (aaa == bbb && bbb == ccc)
		{
			type = 3;  // Equilateral
		}
		else
		{
			if (aaa == bbb || bbb == ccc || ccc == aaa)
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

int gcd(int[] number)
{
	int a = number[0];
	int b = number[1];

	if (a == 0)
	{
		return b;
	}
	else
	{
		while (b != 0)
		{
			if (a > b)
			{
				a = a - b;
			}
			else
			{
				b = b - a;
			}
		}
		return a;
	}
}
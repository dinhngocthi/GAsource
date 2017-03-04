double weak(double aaa, double bbb)
{
	int type = -1;

	double sum = aaa + bbb;
	//if (aaa + bbb > 10)
	if (sum > 10)
	{
		type = 1;
	}
	else
	{
		type = -1;
	}
	return type;
}

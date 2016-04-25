void getMinMax(int a[], int size)
{
	int min = a[0];
	int max = a[0];
	int i = 1;
	while (i < size)
	{
		if (min > a[i]) min = a[i];
		if (max < a[i]) max = a[i];
		i++;
	}
}
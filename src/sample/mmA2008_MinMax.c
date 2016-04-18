void getMinMax(int a[], int size)
{
	int min = a[0];
	int max = a[0];
	int i;
	for (i = 1; i < size; i++)
	{
		if (min > a[i]) min = a[i];
		if (max < a[i]) max = a[i];
	}
}
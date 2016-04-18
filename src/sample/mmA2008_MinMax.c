void getMinMax(int arr[], int n)
{
	int min = arr[0];
	int max = arr[0];
	int i;
	for (i = 1; i < n; i++)
	{
		if (min > arr[i]) min = arr[i];
		if (max < arr[i]) max = arr[i];
	}
}
int BinarySearch(int arr[], int value, int size) 
{
	int left  = 0;
	int right = size - 1; 

	while (left <= right) 
	{
		int middle = (left + right) / 2;
		if (arr[middle] == value)
			return middle;
		else if (arr[middle] > value)
			right = middle - 1;
		else
			left = middle + 1;
	}
	return -1;
}
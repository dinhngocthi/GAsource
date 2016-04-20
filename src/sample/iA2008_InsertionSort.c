void InsertionSort (int a[], int size)
{ 
	for (int i=1; i<size; i++) 
	{ 
		int j=i; 
		while (j>0 && a[j] < a[j-1]) 
		{ //swap 
			int temp=a[j-1]; 
			a[j-1]=a[j]; 
			a[j]=temp; 
			j--; 
		} 
	} 
}


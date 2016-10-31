package coverage;

import gov.nasa.jpf.symbc.Symbolic;

public class MyClassWithPathAnnotations {

	public void f(int x, int y) {
/*		
		int z = 2*y;
		if (x == 100000) {
			if (x < z) {
				assert(false); 
			}
		}
*/
		if (x > y)
			assert(false);
	}
	
    public int myMethod(int x, int y) {
        StringBuilder path = new StringBuilder();
        int z = x + y;
        if (z > 0) {
            path.append("z>0 ");
            z = 1;
        } else {
            path.append("z<=0 ");
            z = z - x;
        }

        if (x < 0) {
            path.append("x<0 ");
            z = z * 2;
        } else if (x < 10) {
            path.append("0<=x<10 ");
            z = z + 2;
        } else {
            path.append("x>=10 ");
            z = -z;
        }

        if (y < 5) {
            path.append("y<5 ");
            z = z - 12;
        } else {
            path.append("y>=5 ");
            z = z - 30;
        }
        CheckCoverage.processTestCase(path.toString());
        return z;
    }

    public void sample1(int a, int b, int c)
    {
    	int x = 0, y = 0, z = 0;
    	if (a >= 0)
    		x = -2;
    	if (b < 5){
    		if ((a < 0)  && (c >= 0))
    			y = 1;
    		z = 2;
    	}
    	System.out.println("Hello");
    }

    public void sample2(int n)
    {
    	int i=0;
    	while(n>0)
    	{
    		if(i==10) break;
    		i=i+1;
    		n=n-1;
    	}
    	if (i==10) assert false;
    }

    public void sample3(int n)
    {
    	int x = 0;
    	while(x < n)
    	{
    		x++;
    		System.out.println(x);
    		if (x > 10) assert false;
    	}

    	//if (x < 0) assert false;
    }

    public void sample4(int old)
    {
    	int lock = 0;
    	int neww = old + 1;
    	while(neww != old)
    	{
    		System.out.println(neww);
    		lock = 1;
    		old  = neww;
    		if(true)
    		{
    			lock = 0;
    			neww++;
    		}
    	}
    	if (lock == 0) assert false;
    }

    public void quickSort(int array[], int lowerIndex, int higherIndex)
    {
    	int i = lowerIndex;
    	int j = higherIndex;
    	// calculate pivot number, I am taking pivot as middle index number
    	int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
    	// Divide into two arrays
    	while (i <= j)
    	{
    		while (array[i] < pivot) { i++; }
    		while (array[j] > pivot) { j--; }
    		if (i <= j)
    		{
    			int temp = array[i];
    	        array[i] = array[j];
    	        array[j] = temp;
    			i++;
    			j--;
    		}
    	}
    	// call quickSort() method recursively
    	if (lowerIndex < j) quickSort(array, lowerIndex, j);
    	if (i < higherIndex) quickSort(array, i, higherIndex);

    	if ((i > j) && (array[i] > array[j])) assert false;
    }

    public void sample5(int a, int b, int c)
    {
    	int k = 0;
    	if (((a > 1) || (b > 2)) && (c > 3) )
    	{
    		k ++;
    	}
    	else
    	{
    		k = k +2;
    	}
    	//if (k > 1) assert false;
    }

    public void sample6(int x, int y) {
        int z = 2*y;
        if (x == 100000) {
            if (x < z) assert false;
        }
    }

    public void test1(int A[], int B[])
    {
    	int a = 0;
    	int b = 0;
    	for (int i = 0; i < 15; i++)
    	{
    		System.out.println(i);
    		if (A[i] == 1) a++;
    	}
    	for (int j = 0; j < 15; j++)
    	{
    		System.out.println(j);
    		if (B[j] == 1) b++;
    	}
    	if ((a > 12) && (a + b == 23)) assert false;
    }

    public int min(int a, int b, int c)
    {
    	if (a > b) a = b;
    	if (a > c) a = c;
    	//if ((a > b) || (a > c)) assert false;
    	return a;
    }

    public int sum(int a, int b, int c)
    {
    	int x, y, z;
    	x = a + b;
    	y = b + c;
    	z = x + y - b;
    	return z;
    }
}
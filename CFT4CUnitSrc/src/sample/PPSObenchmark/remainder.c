void remainder(int a, int b) 
{		
	int r = -1;
	int cy = 0;
	int ny = 0;

	if (a!=0)
	{
		if (b!=0)
		{
			if (a>0)
			{
				if (b>0)
				{
					while((a-ny)>=b)
					{
						ny=ny+b;
						r=a-ny;
						cy=cy+1;
					}
				}
				else
				{	// b<0
					while((a+ny)>=abs(b))
					{
						ny=ny+b;
						r=a+ny;
						cy=cy-1;
					}
				}
			}
			else
			{	// a<0
				if (b>0)
				{
					while(abs(a+ny)>=b)
					{
						ny=ny+b;
						r=a+ny;
						cy=cy-1;
					}
				}
				else
				{
					while((a-ny)<=b)
					{
						ny=ny+b;
						r=abs(a-ny);
						cy=cy+1;
					}
				}
			}
		}
	}
}
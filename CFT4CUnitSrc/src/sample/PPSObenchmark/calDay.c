int calDay (int id, int mm, int iyyy) 
{	
	int jul = 0;
    long jul1, IGREG= (15+31L*(10+12L*1582));
	
	int ja,jy=iyyy,jm;

	if (jy==0)
    { 
	    printf("Error: there is no year zero\n");
	}

	if (jy<0)
    {
		++jy;
	}
	if (mm>2) 
    {
		jm=mm+1;
	}
	else 
    {
		--jy;
		jm=mm+13;
	}

    jul1 =(long)((int)(365.25 * jy) + (int)(30.6001*jm) + id + 1720995);

	if (id+31L*(mm+12L*iyyy)>=(IGREG))
    {
		ja=(int)(0.01*jy);
		jul1 += 2-ja+(int) (0.25*ja);
	}
	
	printf("Julian Day: %d\n", jul1);	

	jul = (int)(jul1+1) % 7;

	if (jul==0)
    {
		printf("Sunday\n");
	}
	else if (jul==1)
    {
		printf("Monday\n");
	}
	else if (jul==2)
    {
		printf("Tuesday\n");
	}
	else if (jul==3)
    {
		printf("Wednesday\n");
	}
	else if (jul==4)
    {
		printf("Thursday\n");
	}
	else if (jul==5)
    {
		printf("Friday\n");
	}
	else if (jul==6)
    {
		printf("Saturday\n");
	}

    return jul;
}
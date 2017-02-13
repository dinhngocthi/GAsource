void calDay(int iyyy, int mm, int id) 
{
	
	int jm, ja, jy;
	int jul = -1;

	long IGREG= (15+31L*(10+12L*1582));
	
	
	jy=iyyy;
	

	if (jy==0)
	{ 
		//error("Error: there is no year zero\n");
	}
	else
	{

	}
/*
	if (jy<0){
		++jy;
	}
	if (mm>2) {
		jm=mm+1;
	}
	else {
		--jy;
		jm=mm+13;
	}
*/
/*
	//long jul1 =(long)(Math.floor(365.25*jy)+Math.floor(30.6001*jm)+id+1720995);
	long jul1 = 10;

	if (id+31L*(mm+12L*iyyy)>=(IGREG)){
		ja=(int)(0.01*jy);
		jul1 += 2-ja+(int) (0.25*ja);
	}
*/
/*	
	System.out.println("Julian Day:"+jul1);	

	jul = (int)(jul1+1) % 7;
	
	if (jul==0){
		System.out.println("Sunday\n");
	}
	else if (jul==1){
		System.out.println("Monday\n");
	}
	else if (jul==2){
		System.out.println("Tuesday\n");
	}
	else if (jul==3){
		System.out.println("Wednesday\n");
	}
	else if (jul==4){
		System.out.println("Thursday\n");
	}
	else if (jul==5){
		System.out.println("Friday\n");
	}
	else if (jul==6){
		System.out.println("Saturday\n");
	}
*/
}

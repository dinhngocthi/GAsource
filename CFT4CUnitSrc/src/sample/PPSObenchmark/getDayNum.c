int getDayNum(int year, int month) 
{	
    int maxDay=0;
    if(month >= 1 && month <= 12) //bch1: branch 1
    {
        if(month==2) //bch2: branch 2
        {
            if(year%400==0||(year%4==0&&year%100==0))
                //bch3: branch 3
                maxDay=29;
            else //bch4: branch 4
                maxDay=28;
        }
        else if(month==4||month==6||month==9||month==11)
            //bch5: branch 5
            maxDay=30;
        else //bch6: branch 6
            maxDay=31;
    }
    else //bch7: branch 7
        maxDay=-1;
    return maxDay;
}
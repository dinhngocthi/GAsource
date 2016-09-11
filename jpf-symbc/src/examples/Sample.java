public class Sample {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		//example(1, 1, 1);
		fisher(1, 1, 1);
	}

    static double fisher(int m, int n, double x) {
        int a, b, j;
        double w, y, z, zk, d, p;
        a = 2*(m/2)-m+2;      
        b = 2*(n/2)-n+2;
        w = (x*m)/n;
        z = 1.0/(1.0+w);
        if(a == 1){
          if(b == 1){
            p = Math.sqrt(w);
            y = 0.3183098862;
            d = y*z/p;
            p = 2.0*y*Math.atan(p);
          }
          else {
            p = Math.sqrt(w*z);
            d = 0.5*p*z/w;
          }
        }
        else if(b == 1) {
          p = Math.sqrt(z);
          d = 0.5*z*p;
          p = 1.0-p;
        }
        else {
          d = z*z;
          p = w*z;
        }
        y = 2.0*w/z;
        if(a == 1)
          for(j = b+2; j <= n; j += 2) {
            d *= (1.0+1.0/(j-2))*z;
            p += d*y/(j-1);
          }
        else {
          zk = Math.pow(z, (double)((n-1)/2));
          d *= (zk*n)/b;
          p = p*zk+w*z*(zk-1.0)/(z-1.0);
        }
        y = w*z;
        z = 2.0/z;
        b = n-2;
        for(int i = a+2; i <= m; i += 2) {
          j = i+b;
          d *= (y*j)/(i-2);
          p -= z*d/j;
        }
        
        return(p<0.0? 0.0: p>1.0? 1.0: p);
      }

    public static int example(int x, int y, double z) 
    {
    	int ret = 0;
        boolean flag = y > 1000;
        if (x + y == 1024)
        {
            if (flag)
            { 
                if ((Math.cos(z) - 0.95) < Math.exp(z))
                {
                	ret = 1;
                    System.out.println("path 1");
                }
                else
                {
                	ret = 2;
                	System.out.println("path 2");                    
                }
            }
            else
            {
            	ret = 3;
            	System.out.println("path 3");
            }
        }
        else
        {
        	ret = 4;
        	System.out.println("path 4");
        }

        return ret;
    }    
}

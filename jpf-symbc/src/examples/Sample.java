public class Sample {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		//example(1, 1, 1);
		//fisher(1, 1, 1);
		//line(1,2,3,5,6,7,8,9);
		//weak(1, 2);
		//test(1);
		//System.out.print(foo(9, 2));
		//foo(9, 2);
//		sample(1, 2, 3);
		//sin(1);
		
//		sample(10,11,11);
//		sample(-77,93,93);
//		sample(-2147483648,0,0);
//		sample(-2147483648,-77,86);
		foo1(0, 1503);
	}

	public static int foo1(int x, int y) {
		y =  y - 500;
		if (fun1(x, y) == 1002) {
			System.out.println("target");
			return 0;
		}

		return 1;
	}

	public static double fun1(int x, int y) {
		double ret = 0;
		x = x - 1;
		if (y != 0)
			ret = x + y + Math.sin(x + 1);
		System.out.println("fun11");
		return ret;
	}

	public static void sample(int x,int y,int z) { 
		if(y==z)
			if(y>0)
				if(x==10)
					System.out.println("OK");
	}
	
	public static double sin(double X) {
		double ret = 0;
		int type = 0;

		//instrument code start
		//if (Math.sin(X) == 1) ret = 0;
		//else ret = Math.abs(Math.sin(X) - 1) + 0.1;
		//instrument code end

		if (Math.sin(X) == 1)
			type = 1;
		return ret;
	}

	// motivation sample
	public static double foo(int X, int Y)
	{
		if(X<=0 || Y<=0)
			return 0;
		double Z;
		if ((X < Y/2)|| (Y==0))
		{
			Z= 1; //Target 1
			System.out.println("Target 1");
		}
		else if (Y > 3 * X)
		{
			Z=2; //Target 2
			System.out.println("Target 2");
		}
		else
		{
			Z = fun(X,Y);
			if ((Z >8) && (Y==10))
				if (Z==Y)
				{
					Z=3;//Target 3
					System.out.println("Target 3");
				}
		}

		return Z;
	}
	
	public static double fun(int x, int y)
	{
		double ret = 0;
		if (y != 0)
			ret = x * x / y;
		return ret;
	}
	
	static double  test(double  a)
	{
          int  type  =  -1;
          if  (f(a)  >  1000)
                    type  =  1;
          else  
                    type  =  0;
          return  type;
	}

	static double  f(double  a)
	{
	          return  (a + 100);
	}
	
	static double weak(double a, double b)
	{
		int type = -1;

		double c = a + b;
		if (c > 25)
		{
			type = 1;
		}

		return type;
	}
	
	static int line(int xr1, int xr2, int yr1, int yr2, int xl1, int xl2, int yl1, int yl2)
	{    
		int salida = -1;
	    int x, y;
	    // precondiciones
	    if(xr1 < xr2 && yr1 < yr2 && xl1 <= xl2)
	    {
	        // completamente cubierta
	        if(xl1 >= xr1 && xl1 <= xr2 && xl2 >= xr1 && xl2 <= xr2 && yl1 >= yr1 && yl1 <= yr2
	            && yl2 >= yr1 && yl2 <= yr2)
	        {
	            salida = 1;
	        }
	        else
	        {
	            // completamente fuera o parcialmente dentro
	            if(yl1 == yl2) // linea horizantal
	            {
	                if(yl1 < yr1)
	                    salida = 2;
	                else if(yl1 > yr2)
	                    salida = 2;
	                else
	                {
	                    if(xl1 > xr2 || xl2 < xr1)
	                        salida = 2;
	                    else
	                        salida = 3; // parcialmente cubierta
	                }
	            }
	            else if(xl1 == xl2) // linea vertical
	            {
	                if(xl1 < xr1)
	                    salida = 2;
	                else if(xl1 > xr2)
	                    salida = 2;
	                else
	                {
	                    if(yl1 > yr2 || yl2 < yr1)
	                        salida = 2;
	                    else
	                        salida = 3; // parcialmente cubierta
	                }
	            }
	            else // linea inclinada
	            {
	                if(yl1 < yr1 && yl2 < yr1) // parte superior del rectangulo
	                    salida = 2;
	                else if(yl1 > yr2 && yl2 > yr2) // parte inferior del rectangulo
	                    salida = 2;
	                else if(xl1 < xr1 && xl2 < xr1) // parte izquierda del rectangulo
	                    salida = 2;
	                else if(xl1 > xr2 && xl2 > xr2) // parte derecha del rectangulo
	                    salida = 2;
	                else
	                {
	                    // pto de corte con la parte superior del rectangulo
	                    x = (xl2 - xl1) / (yl2 - yl1) * (yr1 - yl1) + xl1;
	                    if(x >= xr1 && x <= xr2)
	                        salida = 3; // la linea entra en el rectangulo
	                    else
	                    {
	                        // pto de corte con la parte inferior del rectangulo
	                        x = (xl2 - xl1) / (yl2 - yl1) * (yr2 - yl1) + xl1;
	                        if(x >= xr1 && x <= xr2)
	                            salida = 3; // la linea corta el rectangulo
	                        else
	                        {
	                            // pto de corte con la parte izquieda del rectangulo
	                            y = (yl2 - yl1) / (xl2 - xl1) * (xr1 - xl1) + yl1;
	                            if(y >= yr1 && y <= yr2)
	                                salida = 3; // la linea corta el rectangulo
	                            else
	                            {
	                                // pto de corte con la parte derecha del rectangulo
	                                y = (yl2 - yl1) / (xl2 - xl1) * (xr2 - xl1) + yl1;
	                                if( y >= yr1 && y <= yr2)
	                                    salida = 3; // la linea corta el rectangulo
	                                else
	                                    salida = 2; // la linea no corta al rectangulo
	                            }
	                        }
	                    }
	                }
	            } // fin linea inclinada
	        }
	    }
	    System.out.print(salida);
	    return salida;
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
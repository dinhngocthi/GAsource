void line(int xr1, int xr2, int yr1, int yr2, int xl1, int xl2, int yl1, int yl2) 
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
                    //x = (xl2 - xl1) / (yl2 - yl1) * (yr1 - yl1) + xl1;
                    //if(x >= xr1 && x <= xr2)
					if((xl2 - xl1) / (yl2 - yl1) * (yr1 - yl1) + xl1 >= xr1 && (xl2 - xl1) / (yl2 - yl1) * (yr1 - yl1) + xl1 <= xr2)
                        salida = 3; // la linea entra en el rectangulo
                    else
                    {
                        // pto de corte con la parte inferior del rectangulo
                        //x = (xl2 - xl1) / (yl2 - yl1) * (yr2 - yl1) + xl1;
                        //if(x >= xr1 && x <= xr2)
						if((xl2 - xl1) / (yl2 - yl1) * (yr2 - yl1) + xl1 >= xr1 && (xl2 - xl1) / (yl2 - yl1) * (yr2 - yl1) + xl1 <= xr2)							
                            salida = 3; // la linea corta el rectangulo
                        else
                        {
                            // pto de corte con la parte izquieda del rectangulo
                            //y = (yl2 - yl1) / (xl2 - xl1) * (xr1 - xl1) + yl1;
                            //if(y >= yr1 && y <= yr2)
							if((yl2 - yl1) / (xl2 - xl1) * (xr1 - xl1) + yl1 >= yr1 && (yl2 - yl1) / (xl2 - xl1) * (xr1 - xl1) + yl1 <= yr2)
                                salida = 3; // la linea corta el rectangulo
                            else
                            {
                                // pto de corte con la parte derecha del rectangulo
                                //y = (yl2 - yl1) / (xl2 - xl1) * (xr2 - xl1) + yl1;
                                //if( y >= yr1 && y <= yr2)
								if((yl2 - yl1) / (xl2 - xl1) * (xr2 - xl1) + yl1 >= yr1 && (yl2 - yl1) / (xl2 - xl1) * (xr2 - xl1) + yl1 <= yr2)
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
    printf("salida = %d\n", salida);
}
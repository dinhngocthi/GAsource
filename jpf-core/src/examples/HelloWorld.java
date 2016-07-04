//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//

/**
 * you guess what that one does - just like a normal VM
 */
public class HelloWorld {
  public static void main(String[] args)
  {
      classify(3, 3, 3);
    System.out.println("I won't say it!");
  }
  
  static int classify (int a, int b, int c) 
  {
      if (a <=0 || b <=0 || c <=0) return 4;
      int type =0;
      if (a==b) type +=1;
      if (a==c) type +=2;
      if (b==c) type +=3;
      if ( type ==0) 
      { 
          if (a+b <=c || b+c <=a || a+c >=b) type =4;
          else type =1;
          return type ;
      }
      if (type >3) type =3;
      else if ( type ==1 && a+b>c) type =2;
      else if ( type ==2 && a+c>b) type =2;
      else if ( type ==3 && b+c>a) type =2;
      else type =4;
      System.out.println("type = " + type);
      return type ;
  }
}

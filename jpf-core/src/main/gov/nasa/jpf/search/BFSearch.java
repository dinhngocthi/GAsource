package gov.nasa.jpf.search;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.VM;

public class BFSearch extends Search
{
  public BFSearch (Config config, VM vm)
  {
    super(config,vm);
  }

  public void search ()
  {
    //notifySearchStarted();
    while (true)
    {
      if (forward())
      {
        //notifyStateAdvanced();
        System.out.println("D.N.Thi");
      }
      else
      {
        //notifyStateProcessed();
        break;
      }
    }
  }
}
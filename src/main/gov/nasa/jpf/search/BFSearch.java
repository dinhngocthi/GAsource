package gov.nasa.jpf.search;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.VM;

/**
 * D.N.Thi: standard breadth first model checking
 */

public class BFSearch extends Search 
{
  public BFSearch (Config config, VM vm) 
  {
  	super(config,vm);
  }
    
  public void search () 
  {
  	notifySearchStarted();
  	while (true)
  	{
  		if (!isEndState())
  		{
  			forward();
  			notifyStateAdvanced ();
  		}
  		else
  		{
  			notifyStateProcessed();
  			break;
  			//backtrack();
  			//notifyStateBacktracked();
  		}
  	}
		notifySearchFinished();
  }
}

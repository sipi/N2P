package structure;

import java.util.ArrayList;

public class BaseRegles extends ArrayList<Rule>
{

  //******************************************************************************
  //  METHODS  
  //******************************************************************************
  
  public ArrayList<Term> getConstantsOfConclusion()
  {
    ArrayList<Term> constants = new ArrayList<Term>();
    for(Rule r: this)
      constants.addAll(r.getConclusion().getAllConstants());
    
    return constants;
  }
  
  // ************************************************************************
  // OVERRIDE METHODS
  // ************************************************************************
  
  @Override
  public String toString()
  {
    String s = "";
    
    for(Rule r: this)
      s += r+"\n";
    
    return s;
  }
}

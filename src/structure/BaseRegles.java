package structure;

import java.util.ArrayList;

public class BaseRegles extends ArrayList<Regle>
{

  //******************************************************************************
  //  METHODS  
  //******************************************************************************
  
  public ArrayList<Terme> getConstantsOfConclusion()
  {
    ArrayList<Terme> constants = new ArrayList<Terme>();
    for(Regle r: this)
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
    
    for(Regle r: this)
      s += r+"\n";
    
    return s;
  }
}

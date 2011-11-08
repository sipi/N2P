package structure;

import pi.util.Pair;

public class CoupleTermes extends Pair<Terme,Terme>
{
  
  public CoupleTermes(Terme first, Terme second)
  {
    super(first,second);
  }
  
  /**
   * Attention, cette methode modifie t
   * @param t le terme que l'on veut substituer 
   * @return true si la substitution a pu être appliquée
   */
  public boolean substitue(Terme t)
  {
    if(t.equals(this.first))
    {
      t.set(second.getLabel(), second.isConstant());
      return true;
    }
      
    return false;
  }

}

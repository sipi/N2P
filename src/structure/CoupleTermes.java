package structure;

import pi.util.Pair;

public class CoupleTermes extends Pair<Term,Term>
{
  
  public CoupleTermes(Term first, Term second)
  {
    super(first,second);
  }
  
  /**
   * Attention, cette methode modifie t
   * @param t le terme que l'on veut substituer 
   * @return true si la substitution a pu être appliquée
   */
  public boolean substitue(Term t)
  {
    if(t.equals(this.first))
    {
      t.set(second.getLabel(), second.isConstant());
      return true;
    }
      
    return false;
  }
  
  public CoupleTermes clone()
  {
    return new CoupleTermes(this.first.clone(), this.second.clone());
  }

}

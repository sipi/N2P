package structure;

import java.util.ArrayList;

public class Substitution extends ArrayList<CoupleTermes>
{

  /**
   * applique la substitution à r
   * @param r
   */
  public void substitue(Regle r)
  {
    this.substitue(r.getEnsembleTermes());
  }
  
  /**
   * applique la substitution à l'ensemble des termes de list_terme
   * @param list_terme
   */
  public void substitue(ArrayList<Terme> list_terme)
  {
    for(Terme t: list_terme)
      this.substitue(t);    
  }
  
  /**
   * Applique la subsitution au terme T
   * @param t
   * @return true si la substitution a pu être appliquée
   */
  public boolean substitue(Terme t)
  {
    for(CoupleTermes ct : this)
      if(ct.substitue(t))
        return true;
    
    return false;
  }
  
  /**
   * Ajoute un couple de terme (a, b) à la substitution si 
   * il n'existe aucun couple (a, x) dans la substitution
   * retourne false ssi il existe un couple (a, x) avec x != b dans la substitution
   * @param ct
   * @return
   */
  public boolean ajoutSiCompatible(CoupleTermes ct)
  {
    boolean contains = false;
    for(CoupleTermes couple : this)
    {
      if(ct.first.equals(couple.first))
      {
        contains = true;
        if(!ct.second.equals(couple.second))
          return false;
      }
    }
    if(!contains)
      this.add(ct);
    
    return true;
  }
  
  public boolean ajoutSiCompatible(Substitution s)
  { 
    for(CoupleTermes param_ct : s)
    {
      boolean contains = false;
      for(CoupleTermes ct : this)
      {
        if(ct.first.equals(param_ct.first))
        {
          contains = true;
           if(!ct.second.equals(param_ct.second))
            return false;
        }
      }
      if(!contains)
        this.add(param_ct);
    }

    return true;
  }
  
  public String toString()
  {
    String s = "[ ";
    for(CoupleTermes ct : this)
      s+= ct + " ";
    
    return s+ "]";
  }
  
}

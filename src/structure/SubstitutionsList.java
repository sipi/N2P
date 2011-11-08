package structure;

import java.util.ArrayList;


public class SubstitutionsList extends ArrayList<Substitution>
{
 
  public SubstitutionsList()
  {}
  
  public SubstitutionsList(ArrayList<CoupleTermes> substitutions_unitaire)
  {
    Substitution s;
    for(CoupleTermes ct: substitutions_unitaire)
    {
      s = new Substitution();
      s.add(ct);
      this.add(s);
    }
  }
  
  /**
   * Retourne une SubstitutionsList contenant l'ensemble des substitutions possible 
   * entre les variable de la Regle courante et les Termes présents dans la liste
   * de terme
   * 
   * exemple:
   *    regle: p(x,y) -> p(y,x)
   *    terme: TOTO TATA
   *    
   *    res:
   *      p(TOTO,TOTO) -> p(TOTO, TOTO)
   *      p(TOTO,TATA) -> p(TOTO, TATA)
   *      p(TATA,TOTO) -> p(TATA, TOTO)
   *      p(TATA,TATA) -> p(TATA, TATA)
   *    
   * @param variables 
   * @param constants
   * @return 
   */
  public static SubstitutionsList generateSubstitutionsList(ArrayList<Terme> variables, ArrayList<Terme> constants)
  {
    SubstitutionsList substitutions_list = new SubstitutionsList();

    for(Terme variable: variables)
    {
      ArrayList<CoupleTermes> list_ct = new ArrayList<CoupleTermes>();
      
      for(Terme constant: constants)
      {
        if(!variable.isConstant() || variable.equals(constant))
          list_ct.add( new CoupleTermes(variable,constant) );
      }
      
      substitutions_list = substitutions_list.cartesianProductWidth(list_ct);
    }
    
    return substitutions_list;
    
  }
  
  /**
   * retourne la composition de l'ensemble courant avec les différentes substitiution de l'ensemble param
   * exemple:
   *  receveur courant : [ [[x, TOTO]], [[x, TITI]], [[x, TATA]]]
   *  param : [[y, TOTO], [y, TITI], [y, TATA]]
   *  
   *  alors l'ensemble de retour est:
   *  [ [[x, TOTO], [y, TOTO]],   [[x, TOTO], [y, TITI]],  [[x, TOTO], [y, TATA]],  [[x, TITI], [y, TOTO]], ... ]
   *  
   *  
   * Si le receveur courant est vide alors celle-ci retourne un ensemble constituer des elements de param
   * et inversement
   * @param param
   * @return 
   */
  public SubstitutionsList cartesianProductWidth(ArrayList<CoupleTermes> param)
  {
    if(this.isEmpty())
      return new SubstitutionsList(param);
    
    if(param.isEmpty())
      return this;
    
    
    SubstitutionsList new_ensemble_sub = new SubstitutionsList();
    for(Substitution sub: this)
    {
      for(CoupleTermes ct: param)
      {
        Substitution new_s = (Substitution)sub.clone();
        new_s.add( ct );
        new_ensemble_sub.add(new_s);
      }
    }
    return new_ensemble_sub;
  }
  
  
  public SubstitutionsList intersectionWidth(SubstitutionsList param)
  {
    
    SubstitutionsList new_ensemble_sub = new SubstitutionsList();
    for(Substitution sub: this)
    {
      for(Substitution param_sub : param)
      {
        Substitution new_s = (Substitution)sub.clone();
        if(new_s.ajoutSiCompatible(param_sub))
          new_ensemble_sub.add(new_s);
      }
    }
    return new_ensemble_sub;
  }
  
  public String toString(){
    String s= "";
    for(Substitution sub : this)
      s+= sub+"\n";
    
    return s;
  }

}



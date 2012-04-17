package structure;

import java.util.LinkedList;
import java.util.Set;

import pi.util.ArraySet;

public class RuleBasedSystemLib
{
  
  // ************************************************************************
  // METHODS
  // ************************************************************************
  
  public static LinkedList<Substitution> homomorphisme(AtomSet atomset_src, AtomSet atomset_dest)
  {
      
    renameVariables(atomset_src, atomset_dest);
    
    ArraySet<Term> vars = atomset_src.getTotalOrderOnVars();
    //Candidats
    CandidatesManagement cm = new CandidatesManagement(atomset_src, atomset_dest);

    //rang des atomes
    AtomSet[] atom_rank = getAtomRank(atomset_src, vars);
    
  
    return homomorphismeBT(atom_rank, atomset_dest, new Substitution(), vars, cm, 0); 
  }
  
  /**
   * renomme les variables de atomset1 et de atomset2 de façon à ce qu'aucune variables
   * n'apparaisse dans les deux ensembles.
   * @param atomset1
   * @param atomset2
   */
  public static void renameVariables(AtomSet atomset1, AtomSet atomset2)
  {
    atomset1.addSuffixOnAllVar("1");
    atomset2.addSuffixOnAllVar("2");
  }
  
  // ************************************************************************
  // PRIVATE METHODS
  // ************************************************************************
  
  /**
   * 
   * @param atomset_src un ensemble d'atomes A1
   * @param atomset_dest un ensemble d'atomes A2
   * @param s une substitution partiel
   * @param vars un ordre total sur les variables de A1
   * @param cm une classe de gestion des candidats pour une variables
   * @param n
   * @return
   */
  private static LinkedList<Substitution> homomorphismeBT(AtomSet[] atomrank, AtomSet atomset_dest, Substitution s, ArraySet<Term> vars, CandidatesManagement cm, int rank)
  {
    LinkedList<Substitution> list = new LinkedList<Substitution>();

    
    if(s.size() >= vars.size())
      list.add(s);
    else
    {
      Term x = vars.get(rank);
      for(Term c: cm.getCandidats(x))
      {
        Substitution s_clone = s.clone();
        s_clone.add(new CoupleTermes(x, c));

        //Test d'homomorphimse partiel sur les atomes de rang 'rank'
        if(estHomomorphisme(s_clone, atomrank[rank], atomset_dest))
          list.addAll(homomorphismeBT(atomrank, atomset_dest, s_clone, vars, cm, rank + 1));

    
      }
    }
    
    return list;

  }
  
  //@here faire un algo backtrack homomorphisme qui retroune une liste de Susbstitutions
  
  private static boolean estHomomorphisme(Substitution s, AtomSet atomset_src, AtomSet atomset_dest)
  {
    AtomSet src = atomset_src.clone();
    src.substitue(s);
    
    for(Atom a: src)
      if(!atomset_dest.contains(a))
        return false;
        
    return true;
  }

  
  private static AtomSet[] getAtomRank(AtomSet atomset, ArraySet<Term> vars)
  {
    //initialisation
    AtomSet[] atom_rank = new AtomSet[vars.size()];
    for(int i = 0; i<vars.size(); ++i)
      atom_rank[i] = new AtomSet();
    
    //
    for(Atom a: atomset)
    {
      int rank = 0;
      for(Term t: a.getListeTermes())
      {
        int tmp = vars.indexOf(t);
        if(rank < tmp)
          rank = tmp;
      }   
      atom_rank[rank].add(a);
    }
    
    return atom_rank;
  }
  

  
  private static class CandidatesManagement
  {
    Set<Term> constantes;
    
    CandidatesManagement(AtomSet atomset_src, AtomSet atomset_dest)
    {
      this.constantes = atomset_dest.getAllConsts();
    }
    
    Set<Term> getCandidats(Term var)
    {
      return this.constantes;
    }
    
  }
  
  //***************************************************************************
  //***************************************************************************
  //***************************************************************************
  //***************************************************************************

  
//@TODO ameliorer la machine à état :
  //  -> créer une structure récursive
  //  -> créer des constantes nommées pour les états.
  
  /**
   * Cette fonction permet de tester si la chaîne de caractère passé en paramètre peut
   * être considéré comme un fait.
   * 
   * Un fait doit être de la forme : 
   * MOT\(CONSTANTE(,CONSTANTE)*);?
   * 
   * avec     MOT = ([a-z]|[A-Z]|[0-1])+
   * et CONSTANTE = 'MOT'
   * 
   * @param string
   * @return true si string correspond au pattern d'un fait
   */
  public static boolean isFact(String string)
  {
    int state = 0;
    
    for(char c: string.toCharArray())
    {
      switch(state)
      {          
        case 0 :
          if(Character.isLetter(c) || Character.isDigit(c))
            state = 1;
          else
            return false;
          
          break;
          
        case 1 :
          if(Character.isLetter(c) || Character.isDigit(c))
          {}
          else if(c == '(')
            state = 2;
          else
            return false;
          
          break;  
          
        case 2:
          if(c == '\'')
            state = 3;
          else
            return false;
          
        case 3:
          if(Character.isLetter(c) || Character.isDigit(c))
          {}
          else if(c == '\'')
            state = 4;
          else
            return false;
          
          break;
          
        case 4:
          if(c == ')')
            state = 5;
          else if(c == ',')
            state = 2;
          
          break;
          
        case 5:
          if(c != ' ' && c != ';')
            return false;
          
          break;       
      }
    }

    return state == 5;
  }
  
   /**
   * Cette fonction permet de tester si la chaîne de caractère passé en paramètre peut
   * être considéré comme une règle.
   * 
   * Une règle doit être de la forme : 
   * ATOME(;ATOME)*;?
   * 
   * avec MOT = ([a-z]|[A-Z]|[0-1])+
   *    TERME = 'MOT'|MOT
   * et ATOME = MOT\(TERME(,TERME)*)
   * 
   * @param string
   * @return true si string correspond au pattern d'un fait
   */
  public static boolean isRule(String string)
  {
    int state = 0;
    
    for(char c: string.toCharArray())
    {
      switch(state)
      {          
        case 0 :
          if(Character.isLetter(c) || Character.isDigit(c))
            state = 1;
          else
            return false;

          break;

        case 1 :
          if(Character.isLetter(c) || Character.isDigit(c))
          {}
          else if(c == '(')
            state = 2;
          else
            return false;

          break;  

        case 2:
          if(Character.isLetter(c) || Character.isDigit(c) || c == '\'' || c == ' ' || c == ',')
          {}
          else if(c == ')')
            state = 3;
          else
            return false;

          break;

        case 3:
          if(c == ';')
            state = 4;
          else
            return false;

          break;  

        case 4 :
          if(Character.isLetter(c) || Character.isDigit(c))
            state = 1;
          else
            return false;

          break;
      }
    }

    return state == 4 || state == 3;
  }
  
  public static boolean isBlank(String string)
  {
    for(char c: string.toCharArray())
    {
      if(c != ' ' && c != '\t')
        return false;
    }
    return true;
  }
  
  
}

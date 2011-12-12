package structure;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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
    AtomSet[] atom_rank = new AtomSet[vars.size()];
    for(int i = 0; i<vars.size(); ++i)
      atom_rank[i] = new AtomSet();
    
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
  
  
}

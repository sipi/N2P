package structure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import pi.util.ArraySet;

public class AtomSet extends ArrayList<Atom>
{
  
  // ************************************************************************
  // METHODS
  // ************************************************************************
  
  public AtomSet()
  {
    super();
  }
  
  public AtomSet(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public void addSuffixOnAllVar(String suffix)
  {
    for(Atom a: this)
      a.addSuffixOnAllVar(suffix);
  }
  
  public ArraySet<Term> getTotalOrderOnVars()
  {
    ArraySet<Term> set = new ArraySet<Term>();
    for(Atom a: this)
      set.addAll(a.getAllVariables());

    
    return set;
  }
  
  public Set<Term> getAllConsts()
  {
    Set<Term> set = new ArraySet<Term>();
    for(Atom a: this)
      for(Term t: a.getAllConstants())
        set.add(t);
    
    return set;
  }
  
  public void substitue(Substitution s)
  {
    for(Atom a: this)
      a.substitue(s);
  }
  
  public AtomSet clone()
  {
    AtomSet atomset = new AtomSet();
    for(Atom a: this)
      atomset.add(a.clone());
        
    return atomset;
  }
  
 
}

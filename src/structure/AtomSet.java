/**
* Copyright (C) 2011 Thibaut Marmin
* Copyright (c) 2011 Cl�ment Sipieter
* 
* This file is part of N2P.
*
* N2P is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License.
*
* N2P is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with N2P. If not, see <http://www.gnu.org/licenses/>.
*/

package structure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
  
  /**
   * Constructeur
   * 
   * @param atomes
   *          les atomes, sous forme sous forme textuelle ; cette forme est
   *          "atome1;atome2;...atomek"
   */
  public AtomSet(String string)
  {
    super();

    StringTokenizer st = new StringTokenizer(string, ";");
    while (st.hasMoreTokens())
    {
      String s = st.nextToken(); // s repr�sente un atome
      this.add(new Atom(s));
    }
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

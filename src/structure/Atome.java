/**
* Copyright (C) 2011 Thibaut Marmin
* Copyright (c) 2011 Clément Sipieter
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


/** ****************************************************************************
 *  Class Atome
 * *****************************************************************************
 * Cette classe représente un atome composé d'un prédicat et d'une liste de termes
 * exemples : 
 *  humain('SOCRATE'), egal(x,y), inferieur(x,'A')...
 *
 */

package structure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Atome
{
  private String predicat_label; // le prédicat de l'atome
  //@FIXME use an set, because listeTermes shouldn't accept multiple equals element
  private ArrayList<Terme> listeTermes; // la liste de termes de cet atome

  // ************************************************************************
  // CONSTRUCTEURS
  // ************************************************************************
  
  public Atome(String label, ArrayList<Terme> termes)
  {
    this.predicat_label = label;
    this.listeTermes = termes;
  }
  
  /**
   * Constructeur de la classe Atome Crée un atome, avec ou sans termes
   * 
   * @param s l'atome, soit réduit à un nom de prédicat, soit de la forme
   *          prédicat(liste de termes), où les termes sont séparés par des
   *          points virgules
   * 
   * ATOME = MOT\(TERME(,TERME)*)
   * avec MOT = ([a-z]|[A-Z]|[0-1])+
   * et TERME = 'MOT'|MOT
   */
  public Atome(String s)
  {
    this.initFromString(s);
  }

  // ************************************************************************
  // METHODS
  // ************************************************************************
  
  /**
   * Ajoute le terme 't' à la liste de termes de l'atome, sans autre
   * vérification
   * 
   * @param t
   *          le terme à ajouter
   */
  public void addTerme(Terme t)
  {
    listeTermes.add(t);
  }
  
  public boolean isUnifiables(Atome a)
  {  
    if(!this.predicatEqualsPredicatOf(a))
      return false;
    
    Atome clone_a = a.clone();
    Atome clone_this = this.clone();
    
    clone_this.addSuffixOnAllVar("1");
    clone_a.addSuffixOnAllVar("2");
    
    for(int index = 0; index < this.listeTermes.size(); ++index)
    {
      Terme t1 = clone_this.listeTermes.get(index);
      Terme t2 = clone_a.listeTermes.get(index);
      
      if( (!t1.isConstant() && !t2.isConstant()) || (t1.isConstant() && !t2.isConstant()) )
      {
        Terme t = t2.clone();
        clone_a.substitue(t , t1);
        clone_this.substitue(t, t1);
      }
      else if(!t1.isConstant() && t2.isConstant())
      {
        Terme t = t1.clone();
        clone_a.substitue(t, t2);
        clone_this.substitue(t, t2);
      }

    }
    
    return clone_this.equals(clone_a);
  }
  
  public void addSuffixOnAllVar(String suffix)
  {
    Set<Terme> set = new LinkedHashSet<Terme>(this.listeTermes);
    for(Terme t: set)
      if(!t.isConstant())
        t.setLabel(t.getLabel()+suffix);
  }
  
  public void substitue(Terme a, Terme b)
  {
    int index = this.listeTermes.indexOf(a);
    if(index != -1)
      this.listeTermes.get(index).set(b.getLabel(), b.isConstant());
  }
  
  // ************************************************************************
  // GETTERS / SETTERS
  // ************************************************************************
  
  public ArrayList<Terme> getListeTermes()
  {
    return listeTermes;
  }
  
  public ArrayList<Terme> getAllConstants()
  {
    ArrayList<Terme> constants = new ArrayList<Terme>();
    for(Terme t: listeTermes)
    {
      if(t.isConstant())
        constants.add(t);
    }
    return constants;
  }

  public String getLabel()
  {
    return predicat_label;
  }

  /**
   * Teste l'egalité des prédicats de deux atomes avec le label et l'arité de
   * l'atome
   * 
   * @param a l'atome à tester par rapport à l'atome courant
   * @return vrai si les deux atomes ont même prédicat, faux sinon
   */
  public boolean predicatEqualsPredicatOf(Atome a)
  {
    return this.predicat_label.equals(a.predicat_label)
        && this.listeTermes.size() == a.listeTermes.size();
  }
  
  //******************************************************************************
  //  PRIVATE METHODS
  //******************************************************************************
  
  private void initFromString(String s)
  {
    listeTermes = new ArrayList<Terme>();
    
    if (s.charAt(s.length() - 1) != ')') // c'est donc un atome sans termes
      predicat_label = s;
    else
    {
      int cpt = 0;
      String nomAtome = "";// pour construire le label de l'atome
      String nomTerme = "";// pour construire le terme courant
      boolean constanteTerme;// le terme courant-il est une constante ou non

      while (s.charAt(cpt) != ')')
      {
        while (s.charAt(cpt) != '(')// On récupère le label de l'atome
        {
          nomAtome += s.charAt(cpt);
          cpt++;
        }
        predicat_label = nomAtome;
        cpt++;// Pour sauter le '('
        while (s.charAt(cpt) != ')')// On va désormais s'intéresser aux termes de l'atome
        {
          while (s.charAt(cpt) != ',' && s.charAt(cpt) != ')')// On récupère le label du terme
          {
            nomTerme += s.charAt(cpt);
            cpt++;
          }
          
          if (nomTerme.charAt(0) == '\'')// On teste si le terme est une constante
          {
            constanteTerme = true;
            nomTerme = nomTerme.substring(1, nomTerme.length() - 1);// Si c'est une constante alors on supprime les "'"
          } 
          else
          {
            constanteTerme = false;
          }
          Terme t = new Terme(nomTerme, constanteTerme);// On crée un nouveau terme
          
          int index = this.listeTermes.indexOf(t);
          if(index != -1)
            listeTermes.add(this.listeTermes.get(index));
          else
            listeTermes.add(t);
          
          nomTerme = "";
          if (s.charAt(cpt) == ',')
            cpt++;// Pour sauter le ','
        }

      }
    }
  }
  

  // ************************************************************************
  // OVERRIDE OBJECT
  // ************************************************************************

  /**
   * @return a new instance of Atome which is equals to this instance
   */
  @Override
  public Atome clone()
  {
    String label = new String(this.getLabel());
    
    // la methode clone d'ArrayList n'effectue pas une copie profonde donc :
    ArrayList<Terme> liste = new ArrayList<Terme>(this.getListeTermes().size());
    for(Terme t: this.getListeTermes())
      liste.add(t.clone());
    
    return new Atome(label, liste);
  }
  
  /**
   * Retourne la chaîne de caractères de cet atome
   * @return la chaîne décrivant l'atome (suivant l'écriture logique habituelle)
   */
  @Override
  public String toString()
  {
    String s = predicat_label + "(";
    for (int i = 0; i < listeTermes.size(); i++)
    {
      s += listeTermes.get(i);
      if (i < listeTermes.size() - 1)
        s += ",";
    }
    s += ")";
    return s;
  }

  /**
   * Teste l'egalité de deux atomes (même label et même liste de termes)
   * @param o l'atome à tester par rapport à l'atome courant
   * @return vrai si les deux atomes sont égaux, faux sinon
   */
  @Override
  public boolean equals(Object o)
  {
    if (!o.getClass().equals(this.getClass())) //@FIXME user instance of
      return false;

    Atome a = (Atome) o;
    if (!this.predicatEqualsPredicatOf(a))
      return false;

    for (int i = 0; i < listeTermes.size(); i++)
    {
      if (!listeTermes.get(i).equals(a.listeTermes.get(i)))
        return false;
    }

    return true;
  }

}
  
  

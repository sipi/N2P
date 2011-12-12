/*******************************************************
 *  CLASS REGLE
 * *****************************************************
 * 
 * representation d'une regle
 * exemples : 
 *  p1(x,y) . p2(y,z) -> p3(x,z) 
 *
 */

package structure;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Rule
{
  private AtomSet hypothese;// la liste des atomes hypothèses
  private Atom conclusion;// la conclusion de la règle
  private ArrayList<Term> ensembleTermes;// l'ensemble des termes présents dans la règle

  // ************************************************************************
  // CONSTRUCTEURS
  // ************************************************************************

  /**
   * Constructeur
   * 
   * @param regle
   *          la règle, sous forme sous forme textuelle ; cette forme est
   *          "atome1;atome2;...atomek", où les (k-1) premiers atomes forment
   *          l'hypothèse, et le dernier forme la conclusion
   */
  public Rule(String regle)
  {
    hypothese = new AtomSet();
    ensembleTermes = new ArrayList<Term>();

    StringTokenizer st = new StringTokenizer(regle, ";");
    while (st.hasMoreTokens())
    {
      String s = st.nextToken(); // s représente un atome
      this.hypotheseAdd(new Atom(s));
    }
    // on a mis tous les atomes créés en hypothèse
    // reste à tranférer le dernier en conclusion
    conclusion = hypothese.get(hypothese.size() - 1);
    hypothese.remove(hypothese.size() - 1);
  }
  
  // ************************************************************************
  // METHODS
  // ************************************************************************

  public boolean hypotheseContains(Atom atome)
  { 
    return hypothese.contains(atome);
  }
  
  public ArrayList<Rule> propositionnaliser(ArrayList<Term> constantList)
  {
    ArrayList<Rule> br = new ArrayList<Rule>();    
    SubstitutionsList sl = SubstitutionsList.generateSubstitutionsList(this.getEnsembleTermes(), constantList);

    for(Substitution s: sl)
    {
      Rule new_r = this.clone();
      s.substitue(new_r);
      br.add(new_r);
    }
    
    return br;
  }
  
  public boolean canTrigger(Rule r)
  {
    for(Atom a: r.hypothese)
      if(this.conclusion.isUnifiables(a))
        return true;
        
    return false;
  }
  
  // ************************************************************************
  // GETTERS / SETTERS
  // ************************************************************************


  /**
   * @return l'hypothèse de la règle
   */
  public AtomSet getHypothese()
  {
    return hypothese;
  }

  /**
   * @return la conclusion de la règle
   */
  public Atom getConclusion()
  {
    return conclusion;
  }

  /**
   * @return l'ensemble de termes de la règle
   */
  public ArrayList<Term> getEnsembleTermes()
  {
    return ensembleTermes;
  }

  // ************************************************************************
  // OVERRIDE OBJECT
  // ************************************************************************

    @Override
  public Rule clone()
  {
    Rule r = new Rule();
    
    // la methode clone d'ArrayList n'effectue pas une copie profonde donc :
    r.ensembleTermes = new ArrayList<Term>(this.getEnsembleTermes().size());

    r.hypothese = new AtomSet(this.getHypothese().size());
    for (Atom a : this.getHypothese())
      r.hypotheseAdd(a.clone());
    // fin copie profonde
    
    //Ajout de la conclusion en tant que atome de l'hypothèse afin de bénéficier du traitement sur les termes
    r.hypotheseAdd(this.conclusion.clone());

    //déclaration du dernière atome de l'hypothèse en tant que conclusion
    r.conclusion = r.hypothese.get(r.hypothese.size() - 1);
    r.hypothese.remove(r.hypothese.size() - 1);
    
    return r;
  }

    @Override
  public String toString()
  {
    String s = "";

    for (int i = 0; i < hypothese.size(); i++)
    {
      s += hypothese.get(i);
      if (i < hypothese.size() - 1)
        s += ",";
    }

    s += " --> ";
    s += this.conclusion;
    /*s += "\nliste des termes : ";

    for (int i = 0; i < ensembleTermes.size(); i++)
      s += ensembleTermes.get(i) + " ; ";*/

    return s;
  }


  
  // ************************************************************************
  // PRIVATE METHODS
  // ************************************************************************

  private Rule(){}
  
  /**
   * Ajoute un atome a l'hypothese et ses termes à la liste des termes
   */
  
  private void hypotheseAdd(Atom a)
  {
    this.hypothese.add(a);
    ArrayList<Term> termes = a.getListeTermes();
    for (int i = 0; i < termes.size(); i++)
    {
      // ajout à la liste des termes
      a.getListeTermes().set( i, addTerme(termes.get(i)) );
    }
  }
  
  /**
   * Ajoute un terme à la liste des termes s'il n'existe pas déjà
   * 
   * @param t
   *          le terme à potentiellement ajouter
   * @return un sommet terme, soit t s'il a été inséré, soit le sommet terme qui
   *         existait déjà dans la liste des sommets termes
   */
  private Term addTerme(Term t)
  // SI : dans le cas où le terme t n'existe pas déjà dans la liste des sommets
  // termes, on l'ajoute à la bonne place
  // et on lui donne comme voisin le sommet relation se trouvant à l'index
  // "index" dans la liste des sommets relations
  // Sinon, on ajoute le sommet relation se trouvant à l'index "index" dans la
  // liste des sommets relations au sommet terme déjà existant dans la liste des
  // sommets termes
  {
    int[] retour;

    retour = positionDichoTerme(t);// on recherche la position où ajouter t
    if (retour[0] != -1)
      ensembleTermes.add(retour[1], t);// Si t n'apparaissait pas auparavant, on
                                       // l'ajoute à la liste des termes
    return ensembleTermes.get(retour[1]);// On retourne le terme, soit t s'il a
                                         // été inséré, soit le terme qui
                                         // existait déjà dans la liste des
                                         // termes
  }

  /**
   * Cherche la position où insérer le sommet terme 't'
   * 
   * @param t
   *          le sommet terme à insérer
   * @return la position où doit être ajoutée le sommet terme
   */
  private int[] positionDichoTerme(Term t)
  // SE : si t se trouve dans la liste des termes, retourne son indice.
  // sinon retourne l'indice où il devrait être inséré
  // SI : appelle la méthode positionDichoRecursif en indiquant comme paramètre
  // de recherche les
  // indices de début et de fin de la liste des termes (à savoir : 0 et
  // ensembleTermes.size()-1)
  // tableauRéponses : la première cellule est à -1 si le terme apparaît déjà
  // la seconde à la place où doit être inséré le terme
  {
    int[] tableauReponses = new int[2];
    if (ensembleTermes.size() > 0)
      return positionDichoRecursifTerme(t.getLabel(), 0,
          ensembleTermes.size() - 1, tableauReponses);
    else
    {
      tableauReponses[0] = 0;
      tableauReponses[1] = 0;
      return tableauReponses;
    }
  }

  private int[] positionDichoRecursifTerme(String nom, int debut, int fin,
      int[] tabReponses)
  // SE : recherche nom, de façon récursive, entre les indices début et fin de
  // la liste des termes. début et fin
  // doivent obligatoirement être positifs et inférieurs à la taille de la liste
  // des termes.
  // tabReponses : la première cellule est à -1 si le terme apparaît déjà
  // la seconde à la place où doit être inséré le terme
  {
    if (debut > fin)
    {
      tabReponses[0] = debut;
      tabReponses[1] = debut;
      return tabReponses; // et on sort
    }
    int milieu = (debut + fin) / 2;
    int compare = ensembleTermes.get(milieu).getLabel().compareTo(nom);
    if (compare == 0)// Si le terme de nom "nom" existe déjà
    {
      tabReponses[0] = -1;
      tabReponses[1] = milieu;
      return tabReponses; // et on sort
    }
    if (compare > 0)
      return positionDichoRecursifTerme(nom, debut, milieu - 1, tabReponses);
    return positionDichoRecursifTerme(nom, milieu + 1, fin, tabReponses);
  }

  

}

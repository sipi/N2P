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

public class Regle
{
  private ArrayList<Atome> hypothese;// la liste des atomes hypoth�ses
  private Atome conclusion;// la conclusion de la r�gle
  private ArrayList<Terme> ensembleTermes;// l'ensemble des termes pr�sents dans la r�gle

  // ************************************************************************
  // CONSTRUCTEURS
  // ************************************************************************

  /**
   * Constructeur
   * 
   * @param regle
   *          la r�gle, sous forme sous forme textuelle ; cette forme est
   *          "atome1;atome2;...atomek", o� les (k-1) premiers atomes forment
   *          l'hypoth�se, et le dernier forme la conclusion
   */
  public Regle(String regle)
  {
    hypothese = new ArrayList<Atome>();
    ensembleTermes = new ArrayList<Terme>();

    StringTokenizer st = new StringTokenizer(regle, ";");
    while (st.hasMoreTokens())
    {
      String s = st.nextToken(); // s repr�sente un atome
      this.hypotheseAdd(new Atome(s));
    }
    // on a mis tous les atomes cr��s en hypoth�se
    // reste � tranf�rer le dernier en conclusion
    conclusion = hypothese.get(hypothese.size() - 1);
    hypothese.remove(hypothese.size() - 1);
  }
  
  public Regle(Regle regle)
  {
    this.hypothese = (ArrayList<Atome>) regle.hypothese.clone();
    this.conclusion = regle.conclusion.clone();
    this.ensembleTermes = (ArrayList<Terme>) regle.ensembleTermes.clone();
  }

  // ************************************************************************
  // METHODS
  // ************************************************************************

  public boolean hypotheseContains(Atome atome)
  { 
    return hypothese.contains(atome);
  }
  
  public ArrayList<Regle> propositionnaliser(ArrayList<Terme> constantList)
  {
    ArrayList<Regle> br = new ArrayList<Regle>();    
    SubstitutionsList sl = SubstitutionsList.generateSubstitutionsList(this.getEnsembleTermes(), constantList);

    for(Substitution s: sl)
    {
      Regle new_r = this.clone();
      s.substitue(new_r);
      br.add(new_r);
    }
    
    return br;
  }
  
  public boolean canTrigger(Regle r)
  {
    for(Atome a: r.hypothese)
      if(this.conclusion.isUnifiables(a))
        return true;
        
    return false;
  }
  
  // ************************************************************************
  // GETTERS / SETTERS
  // ************************************************************************


  /**
   * @return l'hypoth�se de la r�gle
   */
  public ArrayList<Atome> getHypothese()
  {
    return hypothese;
  }

  /**
   * @return la conclusion de la r�gle
   */
  public Atome getConclusion()
  {
    return conclusion;
  }

  /**
   * @return l'ensemble de termes de la r�gle
   */
  public ArrayList<Terme> getEnsembleTermes()
  {
    return ensembleTermes;
  }

  // ************************************************************************
  // OVERRIDE OBJECT
  // ************************************************************************

    @Override
  public Regle clone()
  {
    Regle r = new Regle();
    
    // la methode clone d'ArrayList n'effectue pas une copie profonde donc :
    r.ensembleTermes = new ArrayList<Terme>(this.getEnsembleTermes().size());

    r.hypothese = new ArrayList<Atome>(this.getHypothese().size());
    for (Atome a : this.getHypothese())
      r.hypotheseAdd(a.clone());
    // fin copie profonde
    
    //Ajout de la conclusion en tant que atome de l'hypoth�se afin de b�n�ficier du traitement sur les termes
    r.hypotheseAdd(this.conclusion.clone());

    //d�claration du derni�re atome de l'hypoth�se en tant que conclusion
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

  private Regle(){}
  
  /**
   * Ajoute un atome a l'hypothese et ses termes � la liste des termes
   */
  
  private void hypotheseAdd(Atome a)
  {
    this.hypothese.add(a);
    ArrayList<Terme> termes = a.getListeTermes();
    for (int i = 0; i < termes.size(); i++)
    {
      // ajout � la liste des termes
      a.getListeTermes().set( i, addTerme(termes.get(i)) );
    }
  }
  
  /**
   * Ajoute un terme � la liste des termes s'il n'existe pas d�j�
   * 
   * @param t
   *          le terme � potentiellement ajouter
   * @return un sommet terme, soit t s'il a �t� ins�r�, soit le sommet terme qui
   *         existait d�j� dans la liste des sommets termes
   */
  private Terme addTerme(Terme t)
  // SI : dans le cas o� le terme t n'existe pas d�j� dans la liste des sommets
  // termes, on l'ajoute � la bonne place
  // et on lui donne comme voisin le sommet relation se trouvant � l'index
  // "index" dans la liste des sommets relations
  // Sinon, on ajoute le sommet relation se trouvant � l'index "index" dans la
  // liste des sommets relations au sommet terme d�j� existant dans la liste des
  // sommets termes
  {
    int[] retour;

    retour = positionDichoTerme(t);// on recherche la position o� ajouter t
    if (retour[0] != -1)
      ensembleTermes.add(retour[1], t);// Si t n'apparaissait pas auparavant, on
                                       // l'ajoute � la liste des termes
    return ensembleTermes.get(retour[1]);// On retourne le terme, soit t s'il a
                                         // �t� ins�r�, soit le terme qui
                                         // existait d�j� dans la liste des
                                         // termes
  }

  /**
   * Cherche la position o� ins�rer le sommet terme 't'
   * 
   * @param t
   *          le sommet terme � ins�rer
   * @return la position o� doit �tre ajout�e le sommet terme
   */
  private int[] positionDichoTerme(Terme t)
  // SE : si t se trouve dans la liste des termes, retourne son indice.
  // sinon retourne l'indice o� il devrait �tre ins�r�
  // SI : appelle la m�thode positionDichoRecursif en indiquant comme param�tre
  // de recherche les
  // indices de d�but et de fin de la liste des termes (� savoir : 0 et
  // ensembleTermes.size()-1)
  // tableauR�ponses : la premi�re cellule est � -1 si le terme appara�t d�j�
  // la seconde � la place o� doit �tre ins�r� le terme
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
  // SE : recherche nom, de fa�on r�cursive, entre les indices d�but et fin de
  // la liste des termes. d�but et fin
  // doivent obligatoirement �tre positifs et inf�rieurs � la taille de la liste
  // des termes.
  // tabReponses : la premi�re cellule est � -1 si le terme appara�t d�j�
  // la seconde � la place o� doit �tre ins�r� le terme
  {
    if (debut > fin)
    {
      tabReponses[0] = debut;
      tabReponses[1] = debut;
      return tabReponses; // et on sort
    }
    int milieu = (debut + fin) / 2;
    int compare = ensembleTermes.get(milieu).getLabel().compareTo(nom);
    if (compare == 0)// Si le terme de nom "nom" existe d�j�
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

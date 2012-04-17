package structure;

import java.util.*;

/**
 * @author mugnier
 *
 */
public class BaseFaits
{
	private AtomSet listeAtomes;//l'ensemble des faits (atomes)
	private ArrayList<Term> ensembleTermes;//l'ensemble des termes présents
	
	
	//************************************************************************************
	// CONSTRUCTORS
  //************************************************************************************
	
	/**
	 * Constructeur de la classe 'BaseFaits'
	 * crée une base de faits vide
	 */
	public BaseFaits()
	{
		listeAtomes = new AtomSet();
		ensembleTermes = new ArrayList<Term>();
	}
	
	/**
	 * Constructeur de la classe 'BaseFaits'
	 * @param baseFaits les faits, passés sous la forme "atom1;...;atomk"
	 */
	public BaseFaits(String baseFaits)
	{
		listeAtomes = new AtomSet();
		ensembleTermes = new ArrayList<Term>();
		creerBaseFaits(baseFaits);	
	}
	
	//************************************************************************************
  // METHODS
  //************************************************************************************
	
	/**
   * Ajoute des faits à la base de faits s'ils n'apparaissaient pas déjà
   * @param faits les faits à ajouter (seuls ceux n'apparaissant pas dans la base seront ajoutés)
   */
  public void ajouterNouveauxFaits(ArrayList<Atom> faits)
  // Spec Interne : la liste des termes apparaissant dans la base est également
  // mise à jour
  {
    for(int i=0;i<faits.size();i++)
      ajouterNouveauFait(faits.get(i));
  }
  
  /**
   * Ajoute un fait à la base de faits s'il n'apparait pas déjà
   * @param fait le fait à ajouter (ne sera ajouté que s'il n'apparait pas déjà)
   */
  public void ajouterNouveauFait(Atom fait)
  // Spec Interne : la liste des termes apparaissant dans la base est également
  // mise à jour
  {
      if(!atomeExiste(fait))
      {
        listeAtomes.add(fait);
        for(int j=0;j<fait.getListeTermes().size();j++)
        {
          Term t = new Term(fait.getListeTermes().get(j).getLabel(),fait.getListeTermes().get(j).isConstant());//On crée un nouveau terme
          t = ajouterTerme(t); // ceci ajoute le terme dans la liste des termes de la base de faits s'il n'existait pas déjà
          listeAtomes.get(listeAtomes.size()-1).getListeTermes().set(j,t);
        }
      }
  }

  /**
   * Ajoute un terme à la liste des termes s'il n'existe pas déjà.
   * @param t le terme à potentiellement ajouter
   * @return un sommet terme, soit t s'il a été inséré, soit le sommet terme qui existait déjà dans la liste des sommets termes
   */
  public Term ajouterTerme(Term t)
  {
    for(Term terme : ensembleTermes)
    {
      if(terme.equals(t))
        return terme;
    }
   
    ensembleTermes.add(t);
    return t;
  } 
  
  
  public boolean contains(Atom a)
  {
    return listeAtomes.contains(a);
  }
	
  
	//************************************************************************
  // GETTERS / SETTERS
  // ************************************************************************

	public AtomSet getListeAtomes() {
		return listeAtomes;
	}

	public void setListeAtomes(AtomSet listeAtomes) {
		this.listeAtomes = listeAtomes;
	}

	public ArrayList<Term> getEnsembleTermes() {
		return (ArrayList<Term>)ensembleTermes.clone();
	}
  
  public void addAll(ArrayList<Atom> list)
  {
    for(Atom a: list)
      this.ajouterNouveauFait(a);
  }

	// ************************************************************************
  // OVERRIDE OBJECT
  // ************************************************************************
  
  /**
   * retourne une description de la base de faits
   * @return description de la base de faits
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String s = "nombre de faits : "+listeAtomes.size()+ "\n";
    
    s+="liste des faits : ";   
    for(Atom a : listeAtomes)
      s+=a+" ; ";
    
    s+="\nliste des termes : ";
    for(Term t : ensembleTermes)
      s+=t+" ; ";
    
    return s;
  }
	
	//************************************************************************
  // PRIVATES METHODS
  // ************************************************************************

	private void creerBaseFaits(String baseFaits)
	//Prérequis : le format de la base de faits est supposé correct
   	{
		Term t;
   		StringTokenizer st = new StringTokenizer(baseFaits,";");
   		while(st.hasMoreTokens())
   		{
   			String s = st.nextToken(); // s représente un atome
   			Atom a = new Atom(s);
   			ajouterAtome(a);//On ajoute un atome à la liste des atomes
   			ArrayList<Term> termes = a.getListeTermes();
   			for (int i = 0; i < termes.size(); i ++)
   			{
   				t = ajouterTerme(termes.get(i));
   				a.getListeTermes().set(i,t);
   			}
   		}
   	}
	
	
	/**
	 * Ajoute un atome à la base de faits sans autre vérification
	 * tous les termes de l'atome doivent déjà exister dans la base
	 * @param a l'atome à ajouter
	 * @return la position où l'atome 'a' a été ajouté (s'il existait déjà il est ajouté en double)
	 */
	private int ajouterAtome(Atom a)
	{
		listeAtomes.add(a);
		return listeAtomes.size()-1;
	}
	
	/**
	 * Teste l'existence d'un atome dans la base
	 * @param a l'atome dont on teste l'existence
	 * @return vrai si l'atome existe déjà, faux sinon
	 */
	public boolean atomeExiste(Atom a)
	{
	  return listeAtomes.contains(a);
	}
	
	
	
}

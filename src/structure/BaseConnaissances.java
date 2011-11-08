
package structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import pi.util.Pair;

public class BaseConnaissances
{

  private BaseFaits bf;//base de faits
  private BaseRegles br;//base de règles
  private boolean is_saturated;
  //************************************************************************************
  // CONSTRUCTORS
  //************************************************************************************ 
  
  /**
   * Création d'une base de connaissance vide
   */
  public BaseConnaissances()
  {
    this.init(new BaseFaits(), new BaseRegles());
  }

  /**
   * Création d'une base de connaissance initialisée avec la BF et la BR passées en paramètre
   * @param BF
   * @param BR
   */
  public BaseConnaissances(BaseFaits BF, BaseRegles BR)
  {
    this.init(BF, BR);
  }

  //************************************************************************************
  // METHODS
  //************************************************************************************
  
   /**
   * Chargement d'une base de connaissance depuis un fichier au format suivant:
   * nbr_faits
   * fait1
   * fait2
   * ...
   * faitn
   * nbr_regles
   * regle1
   * regle2
   * ...
   * reglem
   * 
   * @param path le chemin vers le fichier à charger
   * @return une instance de BaseConnaissances initialiser avec les faits et regles du fichier
   */
  public Boolean load(String path)
  {
    BufferedReader reader = null;
    int nbFaits;
    int nbRegles;

    this.is_saturated = false;

    try
    {
      reader = new BufferedReader(new FileReader(path));
      
      //Lecture des faits
      nbFaits = Integer.decode(reader.readLine());
      for (int i = 0; i < nbFaits; ++i)
        bf.ajouterNouveauFait(new Atome(reader.readLine()));

      //Lecture des regles
      nbRegles = Integer.decode(reader.readLine());
      for (int i = 0; i < nbRegles; ++i)
        br.add(new Regle(reader.readLine()));

    }
    catch (IOException e)
    {
      System.err.println("io exception : ");
      e.printStackTrace();
      return false;
    }

    return true;
  }
  
  /**
   * Réinitialisation de la base de connaissance
   */
  public void clear()
  {
    this.init(new BaseFaits(), new BaseRegles());
  }
  
  /**
   * Saturation de la base de faits en utilisant la méthode de propositionalisation
   */
  public void saturation()
  {
    this.propositionnalisation();

    ArrayList<Atome> a_traiter;
    ArrayList<Pair<Regle, Integer>> liste_regles = new ArrayList<Pair<Regle, Integer>>();

    //RegleWithCompteur rwc;
    Pair<Regle, Integer> rwc;
    for (Regle r : br)
    {
      rwc = new Pair<Regle, Integer>(r, new Integer(0));
      rwc.second = rwc.first.getHypothese().size();
      
      if (rwc.second == 0) // si le compteur est null
        bf.ajouterNouveauFait(r.getConclusion()); // on considere r comme un fait 
        // cela permet la gestion des règles du type tout est quelque chose
        // exemple : --> object(x)
      else
        liste_regles.add(rwc); // sinon comme une règles classique
    }
    
    a_traiter = new ArrayList<Atome>(bf.getListeAtomes());
    
    // *************************************************************************
    //  DATA PROCESSING
    while (!a_traiter.isEmpty())
    {
      Atome atome = a_traiter.remove(0);
      for (Pair<Regle, Integer> ref : liste_regles) //Pour toutes règles
      {
        if (!bf.contains(ref.first.getConclusion()) && ref.first.hypotheseContains(atome) && --ref.second == 0)
        {
          a_traiter.add(ref.first.getConclusion());
          bf.ajouterNouveauFait(ref.first.getConclusion());
        }
      }
    }

    this.is_saturated = true;
  }

  /**
   * @param a
   * @return true si la base de faits contiens un atome égal a "a", false sinon.
   */
  public boolean valueOf(Atome a)
  {
    if(!this.is_saturated)
      this.saturation();
    
    return bf.contains(a);
  }
  
  public SubstitutionsList instanceOf(Regle r){
    if(!this.is_saturated)
      this.saturation();
   
    //initialisation avec les substitutions possible sur la "conclusion"
    SubstitutionsList subList = new SubstitutionsList();
    SubstitutionsList sublisttmp;
    Substitution s ;
    ArrayList<CoupleTermes> listcouple;
    
    Atome at = r.getConclusion();
    for(Atome fa: bf.getListeAtomes()){
      boolean err = false;
      if(at.predicatEqualsPredicatOf(fa))
      {
        s = new Substitution();
        for(int i = 0; i< fa.getListeTermes().size(); ++i)
        {
          if(!s.ajoutSiCompatible(new CoupleTermes(at.getListeTermes().get(i), fa.getListeTermes().get(i))))
            err = true;
        }
        if(!err)
          subList.add(s);
      }
    }
    
    
    for(Atome ra : r.getHypothese()) // A(x,y), B(y,z)
    {
      sublisttmp = new SubstitutionsList();
      for(Atome fa: bf.getListeAtomes()){ 
        boolean err = false;
        if(ra.predicatEqualsPredicatOf(fa))
        {
          s = new Substitution();
          for(int i = 0; i< fa.getListeTermes().size(); ++i)
          {
              if(!s.ajoutSiCompatible(new CoupleTermes(ra.getListeTermes().get(i), fa.getListeTermes().get(i))))
                err = true;
          }
          if(!err)
            sublisttmp.add(s);
        }
      }
      
      subList = subList.intersectionWidth(sublisttmp);
    }
      
    
    return subList;
  }


  //******************************************************************************
  //  GETTERS / SETTERS
  //******************************************************************************
  public BaseFaits getBaseFaits()
  {
    return bf;
  }

  public BaseRegles getBaseRegles()
  {
    return br;
  }
  
   public void ajouterRegle(Regle r)
  {
    this.br.add(r);
  }

  public void ajouterRegle(ArrayList<Regle> list)
  {
    this.br.addAll(list);
  }

  public void ajouterFait(Atome a)
  {
    bf.ajouterNouveauFait(a);
  }

  public void ajouterFait(ArrayList<Atome> faits)
  {
    bf.ajouterNouveauxFaits(faits);
  }

  //************************************************************************************
  // OVERRIDE METHODS
  //************************************************************************************
  @Override
  public String toString()
  {
    return 
      "\n******Base de faits******\n" +
      bf + "\n" +
      "\n******Base de règles*****\n" + 
      br;
  }

  //************************************************************************************
  // PRIVATE METHODS
  //************************************************************************************
  private void init(BaseFaits BF, BaseRegles BR)
  {
    this.bf = BF;
    this.br = BR;
    this.is_saturated = false;
  }

  private void propositionnalisation()
  {
    //***********************************************************************
    // Récupération d'une liste des constantes présente dans la base de fait
    ArrayList<Terme> constantList = this.bf.getEnsembleTermes();
    constantList.addAll(br.getConstantsOfConclusion());

    //***********************************************************************
    //  Remplacement de la base de règles par la propositionalisation de celle-ci
    this.br = this.generateBrPropositionnaliser(constantList);

  }

  private BaseRegles generateBrPropositionnaliser(ArrayList<Terme> constantList)
  {
    BaseRegles br_prop = new BaseRegles();
    for (Regle r : this.br) // pour toutes les règles r de this.br
      // ajouter la propositionnalisation de r dans br_prop
      br_prop.addAll(r.propositionnaliser(constantList)); 

    return br_prop;
  }
}

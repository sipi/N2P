package structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import pi.util.Pair;

public class BaseConnaissances
{

  private BaseFaits bf;// base de faits
  private BaseRegles br;// base de règles
  private boolean is_saturated;

  // ************************************************************************************
  // CONSTRUCTORS
  // ************************************************************************************

  /**
   * Création d'une base de connaissance vide
   */
  public BaseConnaissances()
  {
    this.init(new BaseFaits(), new BaseRegles());
  }

  /**
   * Création d'une base de connaissance initialisée avec la BF et la BR passées
   * en paramètre
   * 
   * @param BF
   * @param BR
   */
  public BaseConnaissances(BaseFaits BF, BaseRegles BR)
  {
    this.init(BF, BR);
  }

  // ************************************************************************************
  // METHODS
  // ************************************************************************************

  /**
   * Chargement d'une base de connaissance depuis un fichier au format suivant:
   * nbr_faits fait1 fait2 ... faitn nbr_regles regle1 regle2 ... reglem
   * 
   * @param path
   *          le chemin vers le fichier à charger
   * @return une instance de BaseConnaissances initialiser avec les faits et
   *         regles du fichier
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

      // Lecture des faits
      nbFaits = Integer.decode(reader.readLine());
      for (int i = 0; i < nbFaits; ++i)
        bf.ajouterNouveauFait(new Atom(reader.readLine()));

      // Lecture des regles
      nbRegles = Integer.decode(reader.readLine());
      for (int i = 0; i < nbRegles; ++i)
        br.add(new Rule(reader.readLine()));

    } catch (IOException e)
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
   * Saturation de la base de faits
   */
  public void saturation()
  {
    AtomSet new_faits = new AtomSet();
    Atom a;
    Rule r;
    
    while(true)
    {
      new_faits.clear();
      for(Rule rule: this.br)
      {
        r = rule.clone();
        for(Substitution h : RuleBasedSystemLib.homomorphisme(r.getHypothese(), 
            this.bf.getListeAtomes()))
        {
          a = r.getConclusion();
          a.substitue(h);
          if(!new_faits.contains(a) && !this.bf.contains(a))
            new_faits.add(a);
        }
      }
      if(new_faits.size() == 0)
        break;
      else
        this.bf.addAll(new_faits);
    }
  }

  /**
   * @param a
   * @return true si la base de faits contiens un atome égal a "a", false sinon.
   */
  public boolean valueOf(Atom a)
  {
    if (!this.is_saturated)
      this.saturation();

    return bf.contains(a);
  }

  public SubstitutionsList instanceOf(Rule r)
  {
    if (!this.is_saturated)
      this.saturation();

    // initialisation avec les substitutions possible sur la "conclusion"
    SubstitutionsList subList = new SubstitutionsList();
    SubstitutionsList sublisttmp;
    Substitution s;
    ArrayList<CoupleTermes> listcouple;

    Atom at = r.getConclusion();
    for (Atom fa : bf.getListeAtomes())
    {
      boolean err = false;
      if (at.predicatEqualsPredicatOf(fa))
      {
        s = new Substitution();
        for (int i = 0; i < fa.getListeTermes().size(); ++i)
        {
          if (!s.ajoutSiCompatible(new CoupleTermes(at.getListeTermes().get(i),
              fa.getListeTermes().get(i))))
            err = true;
        }
        if (!err)
          subList.add(s);
      }
    }

    for (Atom ra : r.getHypothese()) // A(x,y), B(y,z)
    {
      sublisttmp = new SubstitutionsList();
      for (Atom fa : bf.getListeAtomes())
      {
        boolean err = false;
        if (ra.predicatEqualsPredicatOf(fa))
        {
          s = new Substitution();
          for (int i = 0; i < fa.getListeTermes().size(); ++i)
          {
            if (!s.ajoutSiCompatible(new CoupleTermes(ra.getListeTermes()
                .get(i), fa.getListeTermes().get(i))))
              err = true;
          }
          if (!err)
            sublisttmp.add(s);
        }
      }

      subList = subList.intersectionWidth(sublisttmp);
    }

    return subList;
  }

  // ******************************************************************************
  // GETTERS / SETTERS
  // ******************************************************************************
  public BaseFaits getBaseFaits()
  {
    return bf;
  }

  public BaseRegles getBaseRegles()
  {
    return br;
  }

  public void ajouterRegle(Rule r)
  {
    this.br.add(r);
  }

  public void ajouterRegle(ArrayList<Rule> list)
  {
    this.br.addAll(list);
  }

  public void ajouterFait(Atom a)
  {
    bf.ajouterNouveauFait(a);
  }

  public void ajouterFait(ArrayList<Atom> faits)
  {
    bf.ajouterNouveauxFaits(faits);
  }

  // ************************************************************************************
  // OVERRIDE METHODS
  // ************************************************************************************
  @Override
  public String toString()
  {
    return "\n******Base de faits******\n" + bf + "\n"
        + "\n******Base de règles*****\n" + br;
  }

  // ************************************************************************************
  // PRIVATE METHODS
  // ************************************************************************************
  private void init(BaseFaits BF, BaseRegles BR)
  {
    this.bf = BF;
    this.br = BR;
    this.is_saturated = false;
  }

  private void propositionnalisation()
  {
    // ***********************************************************************
    // Récupération d'une liste des constantes présente dans la base de fait
    ArrayList<Term> constantList = this.bf.getEnsembleTermes();
    constantList.addAll(br.getConstantsOfConclusion());

    // ***********************************************************************
    // Remplacement de la base de règles par la propositionalisation de celle-ci
    this.br = this.generateBrPropositionnaliser(constantList);

  }

  private BaseRegles generateBrPropositionnaliser(ArrayList<Term> constantList)
  {
    BaseRegles br_prop = new BaseRegles();
    for (Rule r : this.br)
      // pour toutes les règles r de this.br
      // ajouter la propositionnalisation de r dans br_prop
      br_prop.addAll(r.propositionnaliser(constantList));

    return br_prop;
  }
}

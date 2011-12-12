/*******************************************************
 *  CLASS TERME
 * *****************************************************
 * 
 * La classe terme repr�sente une constante ou une variable
 * exemples : 
 *  x, y, SOCRATE...
 *
 */

package structure;

public class Term
{
	private String label;//le nom du terme (par exemple : x, 'toto')
	private boolean is_constant;//vrai si le terme courant est une constante, faux sinon (c'est une variable)
		
	
	//************************************************************************
	// CONSTRUCTEURS
	//************************************************************************
	
	/**
   * Constructeur de la classe Terme pour cr�er une variable
   * @param label le label du terme (qui doit �tre une variable)
   */
  public Term(String label)
  {
    this.init(label, is_constant);
  }
	
	/**
	 * Constructeur de la classe Terme
	 * @param label le label du terme
	 * @param is_constant boolean qui indique si le terme est une constante ou pas (et alors c'est une variable)
	 */
	public Term(String label, boolean is_constant)
	{
		this.init(label, is_constant);
	}
	
	/**
   * @param label le label du terme
   * @param is_constant boolean qui indique si le terme est une constante ou pas (et alors c'est une variable)
   */
	private void init(String label, boolean is_constant)
	{
	  this.label = label;
	  this.is_constant = is_constant;
	}
	
	
	//************************************************************************
  // METHODS
  //************************************************************************
	
	
	
	//************************************************************************
  // GETTERS / SETTERS
  //************************************************************************
	
	 /**
   * Indique si le terme est une constante
   * @return vrai si le terme est une constante, faux sinon
   */
  public boolean isConstant()
  {
    return is_constant;
  }

  /**
   * Accesseur en lecture
   * @return le label du terme
   */
  public String getLabel()
  {
    return label;
  }
  
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  public void set(String label, boolean isConstant)
  {
    this.init(label, isConstant);
  }

  //************************************************************************
  // OVERRIDE OBJECT
  //************************************************************************
  
  @Override
  public Term clone()
  {
    return new Term(new String(this.getLabel()), this.isConstant());
  }
  
	/**
	 * Retourne la cha�ne de caract�res de ce terme
	 * @return la cha�ne d�crivant le terme 
	 */
  @Override
	public String toString()
	{
		if(is_constant) 
		  return "'"+label+"'";
		else 
		  return label;
	}
	
	
	/**
   * Teste l'�galite du terme 't' et du terme courant (constante, label)
   * @param o le terme � tester
   * @return vrai si 't' et le terme courant sont �gaux, faux sinon
   */
  @Override
  public boolean equals(Object o)
  {
    if(!o.getClass().equals(this.getClass()))
      return false;
    
    Term t = (Term)o;
    return t.is_constant == is_constant && t.label.equals(this.label);
  }
	
	
}

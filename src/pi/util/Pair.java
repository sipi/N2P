/** ****************************************************************************
 * Class Pair
 * *****************************************************************************
 * Description
 * 
 * 
 * *****************************************************************************
 *  @autor Clément SIPIETER
 *  @email c.sipieter@gmail.com
 *  @version 0.1
 *  @date October 15, 2011
 */
package pi.util;

/**
 *
 * @param A 
 * @param B 
 */
public class Pair<A, B>
{

  public A first;
  public B second;

  //******************************************************************************
  //  CONSTRUCTORS
  //******************************************************************************
  
  public Pair()
  {
  }

  public Pair(A first, B second)
  {
    this.first = first;
    this.second = second;
  }

  //******************************************************************************
  //  GETTERS / SETTERS
  //******************************************************************************
  public A getFirst()
  {
    return first;
  }

  public B getSecond()
  {
    return second;
  }

  public void setFirst(A first)
  {
    this.first = first;
  }

  public void setSecond(B second)
  {
    this.second = second;
  }

  //******************************************************************************
  //  OVERRIDE METHODS
  //******************************************************************************

  /**
   * 
   * @param obj
   * @return true if this object is the same as the obj argument; false otherwise.
   */
  @Override
  public boolean equals(Object obj)
  {
    if(!(obj instanceof Pair))
      return false;
    
    Pair p = (Pair)obj;
    return this.first.equals(p.first) && this.second.equals(p.second);
  }

  @Override
  public
  int hashCode()
  {
    int hash = 5;
    hash = 23 * hash + (this.first != null ? this.first.hashCode() : 0);
    hash = 23 * hash + (this.second != null ? this.second.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return '<'+this.first.toString()+", "+this.second.toString()+'>';
  }
}

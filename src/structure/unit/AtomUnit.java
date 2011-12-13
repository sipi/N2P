package structure.unit;

import structure.Atom;
import structure.Term;

import static org.junit.Assert.*;

import org.junit.Test;

public class AtomUnit
{
  
  @Test 
  public void testInitFromString()
  {
    Atom a = new Atom("p(x,x)");
    assertTrue( (a.getListeTermes().get(0)) == (a.getListeTermes().get(1)) );
  }

  @Test
  public void testAddSuffixOnAllVar()
  {
    try
    {
      Atom atome = new Atom("test(x,'B',x,z)");
      atome.addSuffixOnAllVar("1");
      assertTrue(atome.equals(new Atom("test(x1,'B',x1,z1)")));
      assertTrue(atome.getListeTermes().get(0) == atome.getListeTermes().get(2));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testIsUnifiable()
  {
    Atom a1 = new Atom("p(x,y,'A',x)");
    Atom a2 = new Atom("p('B',x,x,y)");
    assertTrue(a1.isUnifiables(a2));
      
    a1 = new Atom("p(x,'A',x)");
    a2 = new Atom("p(y,y,'B')");
    assertFalse(a1.isUnifiables(a2));
    
    a1 = new Atom("p(x,x)");
    a2 = new Atom("p('B','C')");
    assertFalse(a1.isUnifiables(a2));   
  }
  
  @Test
  public void testSubstitue()
  {
    Atom a = new Atom("p(x,x)");
    a.substitue(new Term("x"), new Term("A", true));
    assertTrue(a.equals(new Atom("p('A','A')")));
  }
  
  @Test 
  public void testClone()
  {
    Atom a = new Atom("p(x,x)");
    Atom b = a.clone();
    assertTrue(b.getListeTermes().get(0) == b.getListeTermes().get(1));
  }
  

}

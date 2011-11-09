package test;

import structure.Atome;
import structure.Terme;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AtomeUnit
{
  
  @Test 
  public void testInitFromString()
  {
    Atome a = new Atome("p(x,x)");
    assertTrue( (a.getListeTermes().get(0)) == (a.getListeTermes().get(1)) );
  }

  @Test
  public void testAddSuffixOnAllVar()
  {
    try
    {
      Atome atome = new Atome("test(x,'B',x,z)");
      atome.addSuffixOnAllVar("1");
      assertTrue(atome.equals(new Atome("test(x1,'B',x1,z1)")));
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
    Atome a1 = new Atome("p(x,y,'A',x)");
    Atome a2 = new Atome("p('B',x,x,y)");
    assertTrue(a1.isUnifiables(a2));
      
    a1 = new Atome("p(x,'A',x)");
    a2 = new Atome("p(y,y,'B')");
    assertFalse(a1.isUnifiables(a2));
    
    a1 = new Atome("p(x,x)");
    a2 = new Atome("p('B','C')");
    assertFalse(a1.isUnifiables(a2));   
  }
  
  @Test
  public void testSubstitue()
  {
    Atome a = new Atome("p(x,x)");
    a.substitue(new Terme("x"), new Terme("A", true));
    assertTrue(a.equals(new Atome("p('A','A')")));
  }
  
  @Test 
  public void testClone()
  {
    Atome a = new Atome("p(x,x)");
    Atome b = a.clone();
    assertTrue(b.getListeTermes().get(0) == b.getListeTermes().get(1));
  }
  

}

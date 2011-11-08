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
    
    System.out.println((a.getListeTermes().get(0)) +"/"+(a.getListeTermes().get(1)) );
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
    Atome clone_a1 = a1.clone();
    Atome a2 = new Atome("p('B',x,x,y)");
    assertTrue(a1.isUnifiables(a2));
    
    assertTrue(clone_a1.equals(a1));
    
    Atome a3 = new Atome("p(x,'A',x)");
    Atome a4 = new Atome("p(y,y,'B')");
    assertFalse(a3.isUnifiables(a4));
  }

}

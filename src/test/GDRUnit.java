package test;

import static org.junit.Assert.*;

import org.junit.Test;

import pi.util.AdjacencyListGraph;

import structure.Atome;
import structure.BaseConnaissances;
import structure.GDR;
import structure.Regle;

public class GDRUnit
{

  @Test
  public void test()
  {
    BaseConnaissances bc = new BaseConnaissances();
    bc.ajouterRegle(new Regle("q(x,x);p('B',x);p(x,'A')"));
    bc.ajouterRegle(new Regle("p(x,'B');p(y,'C');r(x,y,'A',x)"));
    bc.ajouterRegle(new Regle("r('B',x,x,y);q(x,y)"));
    bc.ajouterRegle(new Regle("p(x,y);p(y,z);q('B','C')"));
    
    GDR gdr = new GDR(bc);
    
    AdjacencyListGraph gExpected = new AdjacencyListGraph();
    gExpected.setNbrVertices(4);
    gExpected.addArc(0,0);
    gExpected.addArc(0,3);
    gExpected.addArc(1,2);
    gExpected.addArc(2,0);
    gExpected.addArc(3,0);
    
    assertTrue(gdr.getGraph().equals(gExpected));

  }

}

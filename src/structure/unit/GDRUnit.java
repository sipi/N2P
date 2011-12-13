package structure.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import pi.util.AdjacencyListGraph;

import structure.Atom;
import structure.BaseConnaissances;
import structure.GDR;
import structure.Rule;

public class GDRUnit
{

  @Test
  public void test()
  {
    BaseConnaissances bc = new BaseConnaissances();
    bc.ajouterRegle(new Rule("q(x,x);p('B',x);p(x,'A')"));
    bc.ajouterRegle(new Rule("p(x,'B');p(y,'C');r(x,y,'A',x)"));
    bc.ajouterRegle(new Rule("r('B',x,x,y);q(x,y)"));
    bc.ajouterRegle(new Rule("p(x,y);p(y,z);q('B','C')"));
    
    GDR gdr = new GDR(bc);
    
    AdjacencyListGraph gExpected = new AdjacencyListGraph();
    gExpected.setNbrVertices(4);
    gExpected.addArc(0,0);
    gExpected.addArc(0,3);
    gExpected.addArc(1,2);
    gExpected.addArc(2,0);
    
    assertTrue(gdr.getGraph().equals(gExpected));

  }

}

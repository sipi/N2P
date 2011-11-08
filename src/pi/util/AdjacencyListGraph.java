package pi.util;

import java.util.Set;
import java.util.HashSet;

public class AdjacencyListGraph implements Graph
{

  Set<Integer>[] adjacencyList;
 
  @SuppressWarnings("unchecked")
  @Override
  public void setNbrVertices(int nbrVertices)
  {
    this.adjacencyList = (HashSet<Integer>[]) new HashSet[nbrVertices];
    for(int i = 0; i < this.adjacencyList.length; ++i)
      this.adjacencyList[i] = new HashSet<Integer>();
    
  }

  @Override
  public void addArc(int idVertexSrc, int idVertexDest)
  {
    this.adjacencyList[idVertexSrc].add(idVertexDest);
  }
  
  // ************************************************************************
  // OVERRIDE OBJECT
  // ************************************************************************

  @Override
  public String toString()
  {
    String s = new String();
    for(int vertexSrc = 0; vertexSrc < this.adjacencyList.length; ++vertexSrc)
    {
      s += vertexSrc + " :";
      for(Integer vertexDest: this.adjacencyList[vertexSrc])
        s += " " + vertexDest + ",";
      
      s += "\n";
    }
    return s;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof AdjacencyListGraph))
      return false;

    AdjacencyListGraph g = (AdjacencyListGraph) obj;
    
    if(this.adjacencyList.length != g.adjacencyList.length)
      return false;
    
    for(int i = 0; i < this.adjacencyList.length; ++i )
    {
      if(this.adjacencyList != null && g.adjacencyList != null)
      {
        if(!this.adjacencyList[i].equals(g.adjacencyList[i]))
          return false;
      }
      else if(this.adjacencyList != null || g.adjacencyList != null)
      {
        return false;
      }
    }
      
    return true;
  }
  
  

}

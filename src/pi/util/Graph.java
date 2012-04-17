package pi.util;

public interface Graph
{
  
  public void setNbrVertices(int nbrVertices);
  public void addArc(int idVertexSrc, int idVertexDest);
  public String toString();
  public boolean equals(Object obj);
}

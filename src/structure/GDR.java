/**
* Copyright (C) 2011 Thibaut Marmin
* Copyright (c) 2011 Clément Sipieter
* This file is part of N2P.
*
* N2P is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License.
*
* N2P is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with N2P. If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * @Date November 8, 2011
 */

package structure;

import pi.util.AdjacencyListGraph;
import pi.util.Graph;

public class GDR
{
  private Graph g;
  private Rule[] map;
  
  
  // ************************************************************************
  // CONSTRUCTEURS
  // ************************************************************************
  
  public GDR(BaseConnaissances bc)
  {
    this.g = GDR.generateGDR(bc);
    this.map = new Rule[bc.getBaseRegles().size()];
    this.map = (Rule[]) bc.getBaseRegles().toArray(this.map);
  }
  
  // ************************************************************************
  // GETTERS / SETTERS
  // ************************************************************************
 
  public Graph getGraph()
  {
    return this.g;
  }
  
  
  //******************************************************************************
  //  PRIVATE METHODS
  //******************************************************************************
 
  private static Graph generateGDR(BaseConnaissances bc)
  {
    Graph g = new AdjacencyListGraph();
    BaseRegles br = bc.getBaseRegles();
    g.setNbrVertices(br.size());
    
    for(int idVertexSrc = 0; idVertexSrc < br.size(); ++idVertexSrc)
      for(int idVertexDest = 0; idVertexDest < br.size(); ++idVertexDest)
        if(br.get(idVertexSrc).canTrigger(br.get(idVertexDest)))
          g.addArc(idVertexSrc, idVertexDest);
    
    return g;
  }


  
  // ************************************************************************
  // OVERRIDE OBJECT
  // ************************************************************************

  @Override
  public String toString()
  {
    String s = new String();
    
    s += "Mapping:\n";
    for(int i = 0; i < this.map.length; ++i)
      s += i + ": " + map[i] + "\n";
    
    s += "\nGraph (Adjacency List):\n" + g.toString();
    
    return s;
  }
  
}

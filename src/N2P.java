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
 * @Date October 18, 2011
 */

public class N2P
{
  
  //************************************************************************************
	// MAIN
  //************************************************************************************
  
  public static void main(String[] args) 
  {
    new N2PCore().run();
    System.gc(); //assure l'appel à finalize()
    System.exit(0);
  }
  
}

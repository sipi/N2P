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
 * @author Clément SIPIETER
 * @Date October 18, 2011
 * @email c.sipieter@gmail.com
 */

import java.util.LinkedList;
import java.util.Scanner;
import structure.*;

public class N2PCore {

  private BaseConnaissances bc;
  
  //************************************************************************************
	// METHODS
  //************************************************************************************
  
  public void run()
  {  
    bc = new BaseConnaissances(); 
    printBanner();
    mainLoop();  
  }
  
  //************************************************************************************
	// PROTECTED METHODS
  //************************************************************************************
  
  @Override
  protected void finalize()
  {
    System.out.println("Bye");
  }
  
  //************************************************************************************
	// MAIN LOOP
  public void mainLoop()
  {
    Scanner scan = new Scanner(System.in);
    String line;
    boolean exit = false;
    
    do
    {
      printPrompt();
      line = scan.nextLine();
      
      if(line.equals("quit") || line.equals("exit") || line.equals("bye"))
        exit = true;
      
      else if(line.equals("help"))
        printHelp();
      
      else if(line.equals("printBC") || line.equals("print"))
        System.out.println(bc);
      
      else if(line.equals("printBF"))
        System.out.println(bc.getBaseFaits());
      
      else if(line.equals("printBR"))
        System.out.println(bc.getBaseRegles());
      
      else if(line.equals("printGDR"))
        System.out.println(new GDR(bc));
      
      else if(line.split(" ")[0].equals("load"))
        load(line.split(" ")[1]);
      
      else if(line.equals("clear"))
        bc.clear();
      
      else if(line.equals("saturer") || line.equals("sat"))
        bc.saturation();
      
      else if(isQuestion(line))
      {
        if(RuleBasedSystemLib.isFact(line.substring(0,line.length()-1)))
        {
          System.out.println(bc.valueOf(new Atom(line.substring(0,line.length()-1))));
        }
        else if(RuleBasedSystemLib.isRule(line.substring(0,line.length()-1)))
        {
          LinkedList<Substitution> list = bc.instanceOf(new AtomSet(line.substring(0,line.length()-1)));
          for(Substitution sub : list)
            System.out.println(sub);
        }
        else
          System.out.println("Question incorrect");
          
      }
      else if(RuleBasedSystemLib.isFact(line))
      {
        if(line.charAt(line.length()-1) != ')')
          line = line.substring(0,line.length()-1);
        
        bc.ajouterFait(new Atom(line));
      }
      else if(RuleBasedSystemLib.isRule(line))
        bc.ajouterRegle(new Rule(line));
      
      else
       printMisunderstoodRequest();
      
    }while(!exit);
  }
 
  //************************************************************************************
	// PRIVATE METHODS
  //************************************************************************************
  
  private void load(String filepath)
  {
    if(this.bc.load(filepath))
      System.out.println("Le fichier a été chargé avec succès.");
    else
      System.out.println("Une erreur s'est produite pendant le chargement du fichier");
  }
    
  //************************************************************************************
	// PRINT METHODS
  private void printPrompt()
  {
    System.out.print("> ");
  }
  
  private void printBanner()
  {
     System.out.println(
      "*************************************************************************\n" +
      "*                                N2P                                    *\n" +
      "*                          NNP's Not Prolog                             *\n" +
      "*************************************************************************\n"
      );
  }
  
  private void printMisunderstoodRequest()
  {
     System.out.println(
       "Je ne comprends pas votre demande!\n" +
       "Si besoin, vous pouvez consulter l'aide en saisissant le mot \"help\"."
       );
  }
  
  private void printHelp()
  {
    System.out.println(
      "*************************************************************************\n" +
      "*  HELP                                                                 *\n" +
      "*************************************************************************\n" +
      "load file_path  : charge le fichier dans la BC \n" +
      "          clear : réinitialiser la base de connaissances\n" +
      "        printBC : impression de la base de connaissances\n" +
      "          print : alias de print BC \n" +
      "        printBF : impression de la base de faits \n" +
      "        printBR : impression de la base de règles \n" +
      "       printGDR : affiche le graphe de dépendance des règles \n" +
      "        saturer : saturer la base de connaissance \n" +
      "            sat : alias de saturer \n" +
      "           exit : quitter l'application \n" +
      "           quit : alias de exit \n" +
      "            bye : alias de exit \n" +
      "\n" +
      "*************************************************************************\n" +
      " l'interrogation de la base de connaissance s'éffectuer par la saisi \n" +
      " d'un atome ou d'un suite d'atomes suivi d'un '?'.       \n" +
      " Exemple : \n" +
      "   p('a','b')? \n" +
      "   p('a',x)? \n" +
      "   p('a',x);p(x,'c')? \n" +
      "\n" +
      "*************************************************************************\n" +
      "Vous pouvez également saisir un fait ou une règle afin de l'ajouter \n" +
      "dynamiquement à la base de connaissance. \n" +
      " Exemple de fait : \n" +
      "   egal('a','A')  \n" +
      " Exemple de règle \n" +
      "   egal(x,y);egal(y,x) \n\n" 
      );
  }
  
  //************************************************************************************
	// StateEngine METHODS
  private boolean isQuestion(String line)
  {
    return line.charAt(line.length()-1) == '?';
  }
  
  
   
}

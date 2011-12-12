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
      
      else if(line.split(" ")[0].equals("load"))
        load(line.split(" ")[1]);
      
      else if(line.equals("clear"))
        bc.clear();
      
      else if(line.equals("saturer") || line.equals("sat"))
        bc.saturation();
      
      else if(isQuestion(line))
      {
        if(isFait(line.substring(0,line.length()-1)))
          System.out.println(bc.valueOf(new Atom(line.substring(0,line.length()-1))));
        else if(isRegle(line.substring(0,line.length()-1)))
          System.out.println(bc.instanceOf(new Rule(line.substring(0,line.length()-1))));
        else
          System.out.println("Question incorrect");
          
      }
      else if(isFait(line))
      {
        if(line.charAt(line.length()-1) != ')')
          line = line.substring(0,line.length()-1);
        
        bc.ajouterFait(new Atom(line));
      }
      else if(isRegle(line))
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
      " load file_path : charge le fichier dans la BC \n" +
      "          clear : réinitialiser la base de connaissances\n" +
      "        printBC : impression de la base de connaissances\n" +
      "          print : alias de print BC \n" +
      "        printBF : impression de la base de faits \n" +
      "        printBR : impression de la base de règles \n" +
      "        saturer : saturer la base de connaissance \n" +
      "            sat : alias de saturer \n" +
      "           exit : quitter l'application \n" +
      "           quit : alias de exit \n" +
      "            bye : alias de exit \n" +
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
  
  //@TODO ameliorer la machine à état :
  //  -> créer une structure récursive
  //  -> créer des constantes nommées pour les états.
  
  /**
   * Cette fonction permet de tester si la chaîne de caractère passé en paramètre peut
   * être considéré comme un fait.
   * 
   * Un fait doit être de la forme : 
   * MOT\(CONSTANTE(,CONSTANTE)*);?
   * 
   * avec     MOT = ([a-z]|[A-Z]|[0-1])+
   * et CONSTANTE = 'MOT'
   * 
   * @param string
   * @return true si string correspond au pattern d'un fait
   */
  private boolean isFait(String string)
  {
    int state = 0;
    
    for(char c: string.toCharArray())
    {
      switch(state)
      {          
        case 0 :
          if(Character.isLetter(c) || Character.isDigit(c))
            state = 1;
          else
            return false;
          
          break;
          
        case 1 :
          if(Character.isLetter(c) || Character.isDigit(c))
          {}
          else if(c == '(')
            state = 2;
          else
            return false;
          
          break;  
          
        case 2:
          if(c == '\'')
            state = 3;
          else
            return false;
          
        case 3:
          if(Character.isLetter(c) || Character.isDigit(c))
          {}
          else if(c == '\'')
            state = 4;
          else
            return false;
          
          break;
          
        case 4:
          if(c == ')')
            state = 5;
          else if(c == ',')
            state = 2;
          
          break;
          
        case 5:
          if(c != ' ' && c != ';')
            return false;
          
          break;       
      }
    }

    return state == 5;
  }
  
   /**
   * Cette fonction permet de tester si la chaîne de caractère passé en paramètre peut
   * être considéré comme une règle.
   * 
   * Une règle doit être de la forme : 
   * ATOME(;ATOME)*;?
   * 
   * avec MOT = ([a-z]|[A-Z]|[0-1])+
   *    TERME = 'MOT'|MOT
   * et ATOME = MOT\(TERME(,TERME)*)
   * 
   * @param string
   * @return true si string correspond au pattern d'un fait
   */
  private boolean isRegle(String string)
  {
    int state = 0;
    
    for(char c: string.toCharArray())
    {
      switch(state)
      {          
        case 0 :
          if(Character.isLetter(c) || Character.isDigit(c))
            state = 1;
          else
            return false;

          break;

        case 1 :
          if(Character.isLetter(c) || Character.isDigit(c))
          {}
          else if(c == '(')
            state = 2;
          else
            return false;

          break;  

        case 2:
          if(Character.isLetter(c) || Character.isDigit(c) || c == '\'' || c == ' ' || c == ',')
          {}
          else if(c == ')')
            state = 3;
          else
            return false;

          break;

        case 3:
          if(c == ';')
            state = 4;
          else
            return false;

          break;  

        case 4 :
          if(Character.isLetter(c) || Character.isDigit(c))
            state = 1;
          else
            return false;

          break;
      }
    }

    return state == 4 || state == 3;
  }
   
}

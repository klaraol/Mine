/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miny;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author klara olsakova
 */

public class Miny {
    
    private static int openBoxCount = 0;//count of opened coordinates to end the game in right time
    
    /**
     * Asks the player size of field and with how many mines player wants to play with executes functions fillInMine() and game()
     * @param args - unused
     */
    public static void main(String[] args)
    {
        //firstOut();
        int width = sizeOfField();
        int mine = numberOfMine();
        int[][][] ar = new int[width][width][2];
        ar = newArray(width);
        
        fillInMine(ar, mine);
        
        game(ar, mine);
    }
    
    /**
     * Asks the player coordinates and executes them
     * @param ar - field
     * @param number - number of mines in game
     */
    public static void game(int[][][] ar, int number)
    {
        printUser(ar);
        String str;
        int x, y;
        Scanner sc = new Scanner(System.in);
        System.out.println("Pro označení bomby využij ^\n Pro označení ? použij ?\n Pro otevření čísla zadej O");
        while(openBoxCount < (Math.pow(ar.length, 2) - number))
        {
            System.out.print("Zadej x, y, operace ");
            str = sc.nextLine();
            
            String[] parts = inputRestruckt(str);
            if(inputCheck(parts)) 
            {
                x = Integer.parseInt(parts[0]);
                y = Integer.parseInt(parts[1]);
                if(x < ar.length && y < ar.length)
                {
                    switch(parts[2])
                    {
                        case "o":
                            openWindow(ar, x, y);
                            break;
                        case "^":
                            if(ar[x][y][1] == 1)
                                openBoxCount--;
                            ar[x][y][1] = 2;
                            break;
                        case "?":
                            if(ar[x][y][1] == 1)
                                openBoxCount--;
                            ar[x][y][1] = 3;
                            break;
                        default:
                            System.out.println("zadal jsi špatně vstup");
                            break;
                    }

                    System.out.println("X: " + x + " Y: " + y + " Other: " + parts[2]);

                    for (String part : parts) 
                    {

                        System.out.print(part + "+");
                    }
                    System.out.println();
                    printUser(ar);
                }else System.out.println("Zadaná souřadnice se nenachází v hracím poli");
            } else System.out.println("Zadal jsi špatně input. Zadej ho znovu.");
        }
        System.out.println("VÝHRA :-)");
    }
    
    /**
     * Restructures string to usable array of strings
     * 
     * @param str String 
     * @return array of string that isn't longer than 3
     */
    public static String[] inputRestruckt(String str)
    {
       str = str.toLowerCase();
       String[] parts = str.split(",| ");
       String[] par = new String[3];
       
       if(parts.length > 3)
       {
           int count = 0;
           int countPar = 0;
           while(countPar != 3)
           {
               if(!parts[count].isEmpty())
               {
                   par[countPar] = parts[count];
                   countPar++;
               }
               count++;
           }
       }
       else
       {
           return parts;
       }
       return par;
    }
    
    /**
     * Determines if input from user is walid
     * 
     * @param parts array of three or less Strings from inputRestruct() function
     * @return boolean
     */
    public static Boolean inputCheck(String[] parts)
    {
        int x;
        if(parts.length < 3)
            return false;
       for(int i = 0; i < 2; i++)
       {
           for(int j = 0; j < parts[i].length(); j++)
           {
               x = parts[i].charAt(j);
               if(x < 48 || x > 57)
               {
                   return false;
               }
           }
       }
       return "o".equals(parts[2]) || "^".equals(parts[2]) || "?".equals(parts[2]);
    }
      
    /**
     * It prints a field for a player
     * If a coordinate is hidden it print "s"
     * @param ar - field
     */
    public static void printUser(int[][][] ar)
    {
        for (int[][] ar1 : ar) {
            for (int j = 0; j < ar.length; j++) {
                switch (ar1[j][1]) {
                    case 0:
                        System.out.print("s ");
                        break;
                    case 1:
                        System.out.print(ar1[j][0] + " ");
                        break;
                    case 2:
                        System.out.print("^ ");
                        break;
                    case 3:
                        System.out.print("? ");
                        break;
                    default:
                        System.out.print("% ");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * It opens a coordinates in field
     * If a mine is in coordinates will exit the program and tell the player that he lost 
     * If  coordinates are zero it opens up surounding windows.
     * @param ar - field
     * @param x - x coordinat that the playes wants to open
     * @param y - y coordinat that the playes wants to open
     */
    public static void openWindow(int[][][] ar, int x, int y)
    {
        if(ar[x][y][1] != 1)
        {
            ar[x][y][1] = 1;
            openBoxCount++;
            if(ar[x][y][0] == 0)
            {
                for(int i = -1; i < 2; i++)
                {
                    for(int j = -1; j < 2; j++)
                    {
                        if(x + i >= 0 && x + i < ar.length && y + j >= 0 && y + j < ar.length)
                        {
                            if(ar[x + i][y + j][1] == 0)
                            {
                                openWindow(ar, x + i, y + j);
                            }
                        }
                    }
                }
            }
            else if(ar[x][y][0] == -1)
            {
                for (int[][] ar1 : ar) 
                {
                    for (int j = 0; j < ar.length; j++) 
                    {
                        if(ar1[j][0] == -1)
                        {
                            System.out.print("* ");
                        }
                        else
                        {
                            System.out.print(ar1[j][0] + " ");
                        }
                    }
                    System.out.println();
                }
                System.out.println("PROHRA :-(");
                System.exit(0);
            }
        } else {
        }
    }
    
    /**
     * It randomly distributes mines in field and fills in numbers acordingly
     * @param ar - field
     * @param number - number of mines in field
     */
    public static void fillInMine(int ar[][][], int number)
    {
        Random rand = new Random();
        int num = number;
        int x;
        int y;
        
        while(num != 0)
        {
            x = rand.nextInt(ar.length);
            y = rand.nextInt(ar.length);
            
            if(ar[x][y][0] != -1)
            {
                ar[x][y][0] = -1;
                num -= 1;
            
                for(int j = -1; j < 2; j++)
                {
                    for (int k = -1; k < 2; k++)
                    {
                        if(y + k >= 0 && y + k < ar.length && x + j >= 0 && x + j < ar.length)
                        {
                            if(ar[x + j][y + k][0] != -1)
                            {
                                ar[x + j][y + k][0] = ar[x + j][y + k][0] + 1;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Divelopers functin that print out every item in field
     * @param ar field
     */
    public static void printArray(int[][][] ar)
    {
        for (int[][] ar1 : ar) {
            for (int j = 0; j < ar.length; j++) {
                if (ar1[j][0] < 10 && ar1[j][0] > -1) {
                    System.out.print("   " + ar1[j][0]);
                } else if ((ar1[j][0] < 0 && ar1[j][0] > -10) || (ar1[j][0] < 100 && ar1[j][0] > 9)) {
                    System.out.print("  " + ar1[j][0]);
                } else {
                    System.out.print(" " + ar1[j][0]);
                }
            }
            System.out.println();
        }
        
        System.out.println();
        
        for (int[][] ar1 : ar) {
            for (int j = 0; j < ar.length; j++) {
                System.out.print(" " + ar1[j][1]);
            }
            System.out.println();
        }
    }
    
    /**
     * Creates a 3 dimentional array (field) filled with zeros
     * @param a - widht and height of field
     * @return 3dimentional array
     */
    public static int[][][] newArray(int a)
    {
        int[][][] ar = new int[a][a][2];
        
        for (int i = 0; i < a; i++)
        {
            for (int j = 0; j < a; j++)
            {
                Arrays.fill(ar[i][j], 0);
            }
        }
        return ar;
    }
    
    /**
     * Function that asks the player how big the fild shoud be 
     * @return intiger that will be a width and height of field of mines
     */
    public static int sizeOfField()
    {
        System.out.println("Zadejte rozměry pole: ");
        Scanner sc = new Scanner(System.in);
        int pole;
        pole = sc.nextInt();
        return pole;
    }
    
    /**
     * It will ask player with how many mines player wants to play with 
     * @return int of number of mines
     */
    public static int numberOfMine()
    {
        Scanner sc = new Scanner(System.in); 
        System.out.println("Počet min");
        int miny;
        miny = sc.nextInt();
        return miny;
    }
    
}

// Main method

import java.io.*;
import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class SoundUp {
    public static void main(String[] args) throws FileNotFoundException{

        Chrono chrono = new Chrono();
        chrono.start();
        
        Scanner keyboard = new Scanner(System.in);
        System.out.print("\033\143");
        System.out.println("//////////////////////////////////WELCOME TO SoundUp///////////////////////////////////////////");
        
        System.out.print("What is your user name?  ");
        String nameUser1 = keyboard.nextLine();
        
        User user1 = new User (nameUser1);

        System.out.print("What is your password?  ");
        String psw = keyboard.nextLine();
        System.out.print("\033\143");
        System.out.print("Welcome " + nameUser1 + "!\n\n");

        System.out.println("What is your objective for today's run? (in km) ");
        String objective = keyboard.nextLine();
        System.out.println("\n                   Press to start a new race");
        try{System.in.read();}
        catch(Exception e){}
        System.out.println("Let's go !");


        user1.displayBpm();
        user1.updateBpm();

        System.out.println("Good job !");

    }
    
}

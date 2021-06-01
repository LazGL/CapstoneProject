// Main method

import java.io.*;
import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class SoundUp {
    public static void main(String[] args) throws FileNotFoundException{
        User charlotte = new User ("Charlotte");
        //charlotte.displayBpm();
        // charlotte.updateBpm();
        
        ArrayList<Music> musicArray = new ArrayList<Music>();
        try
        {
            
            File musicFile = new File ("musics.txt");
            Scanner input = new Scanner(musicFile);
            while(input.hasNextLine())
            {
              //  System.out.println("PAS DE PROBLEME");
                String nameMusic = input.nextLine();
                //System.out.println(nameMusic);
                String authorMusic = input.nextLine();
                //System.out.println(authorMusic);
                String tpm = input.nextLine();
                int bpmMusic = Integer.parseInt(tpm);
                //System.out.println(bpmMusic);
                Music music = new Music (nameMusic, bpmMusic, authorMusic);
                System.out.println(music.toString());
                musicArray.add(music);
                System.out.println();
            }
            input.close();
        }
        catch(FileNotFoundException e )
        {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i<musicArray.size() ; i++)
        {
            musicArray.get(i).displayMusic();
        }

        //ERIFE
        int toPlayMusicBpm;  //to get the bpm of the user, so i can use to detemine the song
        toPlayMusicBpm = charlotte.sendBpm();

        
        System.out.println("This is charlotte's bpm "  + toPlayMusicBpm);
        Random random = new Random();

        if (toPlayMusicBpm<100){
            int index = random.nextInt(musicArray.size());
            for(int i = 0; i <= musicArray.size(); i++){
                if(i == index){
                    System.out.println("this song is " +  musicArray.get(i).getName()); 
                }
            }
        }else if (toPlayMusicBpm>=100 && toPlayMusicBpm<140){
            //pick at random from middle bpm list
            int index = random.nextInt(musicArray.size());
            for(int i = 0; i <= musicArray.size(); i++){
                if(i == index){
                    System.out.println("this song is " +  musicArray.get(i).getName()); 
                }
            }
        }else if (toPlayMusicBpm>=140 && toPlayMusicBpm<223){
            //pick at random from high bpm list
            int index = random.nextInt(musicArray.size());
            for(int i = 0; i <= musicArray.size(); i++){
                if(i == index){
                    System.out.println("this song is " +  musicArray.get(i).getName()); 
                }
            }
        }

        Chrono chrono = new Chrono();
        chrono.start(); // démarrage du chrono
        try
            {
                Thread.sleep(2000);
            }
            catch(InterruptedException ie)
            {
                System.out.println(ie.getMessage());
            }
        
        System.out.println(chrono.getDureeTxt());
        try
        {
            Thread.sleep(3000);
        }
        catch(InterruptedException ie)
        {
            System.out.println(ie.getMessage());
        }
        System.out.println(chrono.getDureeTxt());

    }
    
}

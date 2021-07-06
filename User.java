// Class User

import java.util.Random;

// package SoundUp;

public class User{
    private String name;
    private int bpm;
    private int age;
    private int indexAverageBpm=0;
    private int averageLastBpm;
    private int[] bpmMatrix = new int[5];

    public User (String name)
    {
        this.name = name;
        bpm = createBpm();
    }

    public User()
    {
        User user = new User( "no name") ;
    }

    //generates the bpm of the user when he starts the run
    public int createBpm()
    {
        Random random = new Random();
        int randomBpm;
        randomBpm = 65 +random.nextInt(90-65); // generates a bpm between 50 and 90
        return randomBpm;
    }

    //udpates the bpm of the user as the race progresses
    public int updateBpmOneTime(int lowerBound, int upperBound)
    {
        Random random = new Random();
        int randomRateOfChange;

        //int lowerBound = -1;
        //int upperBound = 5;

        randomRateOfChange = lowerBound +random.nextInt(upperBound - lowerBound); // generates a bpm 
        
        return bpm+randomRateOfChange;
    }

    public void displayBpm()
    {
        System.out.print("Your bpm : " + bpm);
    }
    public void displayAverage()
    {
        System.out.print("    Average : " + averageLastBpm);
    }

    //want to return an int/the number of the users bpm
    public int sendBpm(){
        return bpm;
    }

    public void updateBpm()
    {
        int millis = 500;
        for (int i = 0; i< 100; i++)
        {
            try
            {
                Thread.sleep(millis);
            }
            catch(InterruptedException ie)
            {
                System.out.println(ie.getMessage());
            }

            if (bpm<100)
            {
                bpm = updateBpmOneTime(-1, 8);
            }
            if (bpm>=100 && bpm<140)
            {
                bpm = updateBpmOneTime(-5, 10);
            }
            if (bpm>=140 && bpm<223-age)
            {
                bpm = updateBpmOneTime(-5, 3);
            }
            
            displayBpm();
            updateAverageBpm(bpm);

            // if (i>5)
            // {
            //     displayAverage();
            // }
            System.out.print("\n");

            if(i%10 == 0) {
                Playlist p = new Playlist();
                System.out.print("\n");
                p.chooseMusic(averageLastBpm);
                System.out.print("\n");

                double realKm = (double)i*0.04;
                System.out.printf("You already ran :  %.2f  kms", realKm);
                System.out.print("\n\n");
            }
                
        }
    }

    public void updateAverageBpm(int newBpm)
    {
        bpmMatrix[indexAverageBpm] = newBpm;

        int sum=0;
        for (int i= 0; i<5; i++)
        {
            sum += bpmMatrix[i];
        }
        averageLastBpm =(int) sum/5;

        indexAverageBpm = indexAverageBpm+1;
        indexAverageBpm = indexAverageBpm%5;
    }
    
}
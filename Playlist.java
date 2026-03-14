import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist {
    private ArrayList<Music> playlist1; // high BPM (>=140)
    private ArrayList<Music> playlist2; // middle BPM (100-139)
    private ArrayList<Music> playlist3; // low BPM (<100)

    public Playlist() {
        playlist1 = readPlaylist("HighBpmMusic.txt");
        playlist2 = readPlaylist("MiddleBpmMusic.txt");
        playlist3 = readPlaylist("LowBpmMusic.txt");
    }

    private ArrayList<Music> readPlaylist(String fileName) {
        ArrayList<Music> musicArray = new ArrayList<>();
        File file = new File(System.getProperty("user.dir"), fileName);
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String name = input.nextLine().trim();
                if (!input.hasNextLine()) break;
                String author = input.nextLine().trim();
                if (!input.hasNextLine()) break;
                int bpm = Integer.parseInt(input.nextLine().trim());
                musicArray.add(new Music(name, bpm, author));
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("Music file not found: " + fileName);
        } catch (NumberFormatException e) {
            System.out.println("Bad BPM value in: " + fileName);
        }
        return musicArray;
    }

    public Music chooseMusic(int average) {
        ArrayList<Music> selected;
        if (average < 100) {
            selected = playlist3;
        } else if (average < 140) {
            selected = playlist2;
        } else {
            selected = playlist1;
        }
        if (selected.isEmpty()) return null;
        int index = (int) (Math.random() * selected.size());
        return selected.get(index);
    }
}

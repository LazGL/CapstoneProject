// Class Music

public class Music {
    private String name;
    private int bpm;
    private String author;
    // constructor
    public Music (String name, int bpm, String author)
    {
        this.name = name;
        this.bpm = bpm;
        this.author = author;
        
    }
    
    public void setName(String newName)
    {
        name = newName;
    }            
    
    public String getName()
    {
        return name;
    }
    
    public void setAuthor(String newAuthor)
    {
        author = newAuthor;
    }            
    
    public String getAuthor()
    {
        return author;
    }
    
    public void setBpm(int newBpm)
    {
        bpm = newBpm;
    }            
    
    public int getBpm()
    {
        return bpm;
    }

    public void displayMusic()
    {
        System.out.println("name : "+ name);
        System.out.println("bpm : "+ bpm);
        System.out.println("author : "+ author);
    }
}

import java.util.Random;

public class User {
    private String name;
    private int bpm;
    private int age;
    private int indexAverageBpm = 0;
    private int averageLastBpm;
    private int[] bpmMatrix = new int[5];

    public User(String name, int age) {
        this.name = name;
        this.age = age;
        bpm = createBpm();
    }

    public User(String name) {
        this(name, 25);
    }

    public int createBpm() {
        Random random = new Random();
        return 65 + random.nextInt(25); // 65–89 bpm at rest
    }

    public int updateBpmOneTime(int lowerBound, int upperBound) {
        Random random = new Random();
        int change = lowerBound + random.nextInt(upperBound - lowerBound);
        return bpm + change;
    }

    public void displayBpm() {
        System.out.print("Your bpm : " + bpm);
    }

    public int sendBpm() {
        return bpm;
    }

    public void updateAverageBpm(int newBpm) {
        bpmMatrix[indexAverageBpm] = newBpm;
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += bpmMatrix[i];
        }
        averageLastBpm = sum / 5;
        indexAverageBpm = (indexAverageBpm + 1) % 5;
    }

    /**
     * Runs the race simulation for the given distance.
     * Calls listener.onUpdate() every 500ms and listener.onFinished() when done.
     * Supports interruption for early stop.
     */
    public void updateBpm(double distanceKm, Playlist playlist, RaceListener listener) {
        int iterations = Math.max(10, (int) (distanceKm / 0.04));
        int millis = 500;
        int maxBpm = 223 - age;
        Music lastSong = null;

        for (int i = 0; i < iterations; i++) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ie) {
                double km = i * 0.04;
                listener.onFinished(km, (long) i * millis / 1000L);
                return;
            }

            if (bpm < 100) {
                bpm = updateBpmOneTime(-1, 8);
            } else if (bpm < 140) {
                bpm = updateBpmOneTime(-5, 10);
            } else if (bpm < maxBpm) {
                bpm = updateBpmOneTime(-5, 3);
            }

            updateAverageBpm(bpm);

            if (i % 10 == 0) {
                lastSong = playlist.chooseMusic(averageLastBpm);
            }

            double km = i * 0.04;
            listener.onUpdate(bpm, averageLastBpm, km, lastSong);
        }

        double totalKm = iterations * 0.04;
        long elapsedSeconds = (long) iterations * millis / 1000L;
        listener.onFinished(totalKm, elapsedSeconds);
    }
}

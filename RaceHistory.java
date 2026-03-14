import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RaceHistory {

    public static class RaceRecord {
        public final double distanceKm;
        public final long durationSeconds;
        public final double speedKmh;

        public RaceRecord(double distanceKm, long durationSeconds, double speedKmh) {
            this.distanceKm = distanceKm;
            this.durationSeconds = durationSeconds;
            this.speedKmh = speedKmh;
        }
    }

    private final File file;

    public RaceHistory() {
        this.file = new File(System.getProperty("user.dir"), "races.csv");
    }

    public void save(double distanceKm, long durationSeconds) {
        double speedKmh = durationSeconds > 0
                ? distanceKm / (durationSeconds / 3600.0)
                : 0;
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.printf("%.2f,%d,%.1f%n", distanceKm, durationSeconds, speedKmh);
        } catch (IOException e) {
            System.out.println("Could not save race: " + e.getMessage());
        }
    }

    public List<RaceRecord> load() {
        List<RaceRecord> records = new ArrayList<>();
        if (!file.exists()) return records;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    records.add(new RaceRecord(
                            Double.parseDouble(parts[0]),
                            Long.parseLong(parts[1]),
                            Double.parseDouble(parts[2])
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load race history: " + e.getMessage());
        }
        return records;
    }
}

public interface RaceListener {
    void onUpdate(int bpm, int avgBpm, double distanceKm, Music currentSong);
    void onFinished(double totalDistanceKm, long elapsedSeconds);
}

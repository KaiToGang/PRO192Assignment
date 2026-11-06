package Data;

public class Episode {

    private String episodeId;
    private String parentSeriesId;
    private String title;

    private int seasonNumber;
    private int episodeNumber;
    private int runtimeMinutes;

    private double rating;
    private int numVotes;

    // Constructor
    public Episode(String episodeId, String parentSeriesId,
            String title, int seasonNumber, int episodeNumber,
            int runtimeMinutes, double rating, int numVotes) {

        this.episodeId = episodeId;
        this.parentSeriesId = parentSeriesId;
        this.title = title;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.runtimeMinutes = runtimeMinutes;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    // Getter va Setter
    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getParentSeriesId() {
        return parentSeriesId;
    }

    public void setParentSeriesId(String parentSeriesId) {
        this.parentSeriesId = parentSeriesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    // Hien thi thong tin Episode
    @Override
    public String toString() {

        String runtime = runtimeMinutes > 0
                ? runtimeMinutes + " min" : "Unknown";

        return String.format(
                "%-12s | S%02dE%02d | %-40s | %-10s | %-5.1f | %-10d",
                episodeId,
                seasonNumber,
                episodeNumber,
                title,
                runtime,
                rating,
                numVotes
        );
    }
}
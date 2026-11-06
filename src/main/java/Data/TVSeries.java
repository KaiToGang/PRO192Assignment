package Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TVSeries extends Media {

    private int endYear;

    // Danh sach cac tap phim
    private List<Episode> episodes;

    // Constructor
    public TVSeries(String id, String title, int startYear,
            String genres, double rating, int votes, int endYear) {

        super(id, title, startYear, genres, rating, votes);

        this.endYear = endYear;
        this.episodes = new ArrayList<>();
    }

    // Getter va Setter
    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        if (episodes == null) {
            this.episodes = new ArrayList<>();
        } else {
            this.episodes = episodes;
        }
    }

    // Them Episode vao TVSeries
    public boolean addEpisode(Episode episode) {

        if (episode == null) {
            return false;
        }

        // Kiem tra Episode da ton tai chua
        for (Episode e : episodes) {

            if (e.getEpisodeId().equalsIgnoreCase(
                    episode.getEpisodeId())) {
                return false;
            }
        }

        episodes.add(episode);
        return true;
    }

    // Tim Episode bang IMDb ID
    public Episode findEpisode(String episodeId) {

        if (episodeId == null) {
            return null;
        }

        for (Episode e : episodes) {

            if (e.getEpisodeId().equalsIgnoreCase(episodeId)) {
                return e;
            }
        }

        return null;
    }

    // Lay cac Episode theo Season
    public List<Episode> getEpisodesBySeason(int seasonNumber) {

        List<Episode> result = new ArrayList<>();

        for (Episode e : episodes) {

            if (e.getSeasonNumber() == seasonNumber) {
                result.add(e);
            }
        }

        return result;
    }

    // Sap xep Episode theo Season va Episode Number
    public List<Episode> sortedEpisodes() {

        List<Episode> result = new ArrayList<>(episodes);

        Collections.sort(result, new Comparator<Episode>() {

            @Override
            public int compare(Episode a, Episode b) {

                if (a.getSeasonNumber() != b.getSeasonNumber()) {
                    return Integer.compare(
                            a.getSeasonNumber(),
                            b.getSeasonNumber()
                    );
                }

                return Integer.compare(
                        a.getEpisodeNumber(),
                        b.getEpisodeNumber()
                );
            }
        });

        return result;
    }

    // Dem tong so Episode
    public int getEpisodeCount() {
        return episodes.size();
    }

    // Xac dinh loai Media
    @Override
    public String getMediaType() {
        return "TV Series";
    }

    // Thong tin bo sung
    @Override
    public String getExtraInfo() {

        String years = endYear > 0
                ? getStartYear() + " - " + endYear
                : getStartYear() + " - Ongoing";

        return years + " | " + getEpisodeCount() + " Episodes";
    }

    // Hien thi thong tin TVSeries
    @Override
    public String toString() {

        return super.toString()
                + " | End Year: "
                + (endYear > 0 ? endYear : "Unknown")
                + " | Episodes: " + getEpisodeCount();
    }
}

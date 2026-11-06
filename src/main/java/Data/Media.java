package Data;

import Filter.AlternativeTitle;
import java.util.ArrayList;
import java.util.List;

public abstract class Media implements Searchable {

    private String imdbId;
    private String title;
    private String genres;
    private int startYear;
    private int numVotes;
    private double rating;

    // Danh sach ten phim thay the
    private List<AlternativeTitle> alternativeTitles;

    public Media(String id, String title, int year, String genres,
            double rating, int votes) {

        this.imdbId = id;
        this.title = title;
        this.startYear = year;
        this.genres = genres;
        this.rating = rating;
        this.numVotes = votes;

        this.alternativeTitles = new ArrayList<>();
    }

    // Phuong thuc truu tuong
    public abstract String getMediaType();

    public abstract String getExtraInfo();

    // Getter va Setter
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
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

    // Lay danh sach ten thay the
    public List<AlternativeTitle> getAlternativeTitles() {
        return alternativeTitles;
    }

    // Cap nhat danh sach ten thay the
    public void setAlternativeTitles(List<AlternativeTitle> alternativeTitles) {
        if (alternativeTitles == null) {
            this.alternativeTitles = new ArrayList<>();
        } else {
            this.alternativeTitles = alternativeTitles;
        }
    }

    // Them ten thay the
    public void addAlternativeTitle(AlternativeTitle alternativeTitle) {
        if (alternativeTitle != null) {
            alternativeTitles.add(alternativeTitle);
        }
    }

    // Tim kiem theo ten thay the
    public boolean matchesAlternativeTitle(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        keyword = keyword.toLowerCase();

        for (AlternativeTitle alternativeTitle : alternativeTitles) {

            if (alternativeTitle.getTitle() != null
                    && alternativeTitle.getTitle()
                            .toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    // Tim kiem theo IMDb ID, ten chinh hoac ten thay the
    @Override
    public boolean matches(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        keyword = keyword.toLowerCase();

        return (imdbId != null && imdbId.toLowerCase().contains(keyword))
                || (title != null && title.toLowerCase().contains(keyword))
                || matchesAlternativeTitle(keyword);
    }

    // Hien thi thong tin Media
    @Override
    public String toString() {

        return String.format(
                "%-12s | %-45s | %-4d | %-35s | %-5.1f | %-10d | %-20s",
                imdbId != null ? imdbId : "",
                title != null ? title : "",
                startYear,
                genres != null ? genres : "",
                rating,
                numVotes,
                getExtraInfo()
        );
    }
}

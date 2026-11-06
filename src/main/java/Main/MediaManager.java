package Main;

import Data.Media;
import Data.Person;
import Data.TVSeries;
import Data.Movie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MediaManager {

    // Danh sach quan ly chung Movie va TVSeries
    private List<Media> mediaList = new ArrayList<>();

    // Danh sach dien vien va dao dien
    private List<Person> peopleList = new ArrayList<>();

    // Lay danh sach Media
    public List<Media> getMediaList() {
        return mediaList;
    }

    // Lay danh sach nhan su
    public List<Person> getPeopleList() {
        return peopleList;
    }

    // Them Movie hoac TVSeries
    public boolean addMedia(Media m) {

        if (m == null || findById(m.getImdbId()) != null) {
            return false;
        }

        mediaList.add(m);
        return true;
    }

    // Tim Media bang IMDb ID
    public Media findById(String id) {

        if (id == null) {
            return null;
        }

        for (Media m : mediaList) {

            if (m.getImdbId() != null
                    && m.getImdbId().equalsIgnoreCase(id)) {
                return m;
            }
        }

        return null;
    }

    // Cap nhat thong tin Media
    public boolean updateMedia(String id, String title, int year,
            String genre, double rating, int votes) {

        Media m = findById(id);

        if (m == null) {
            return false;
        }

        m.setTitle(title);
        m.setStartYear(year);
        m.setGenres(genre);
        m.setRating(rating);
        m.setNumVotes(votes);

        return true;
    }

    // Xoa Media
    public boolean deleteMedia(String id) {

        Media m = findById(id);

        return m != null && mediaList.remove(m);
    }

    // Tim kiem theo ten, IMDb ID hoac ten thay the
    public List<Media> searchMedia(String keyword) {

        List<Media> result = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }

        for (Media m : mediaList) {

            if (m.matches(keyword)) {
                result.add(m);
            }
        }

        return result;
    }

    // Loc Media theo nam bat dau
    public List<Media> filterYear(int year) {

        List<Media> result = new ArrayList<>();

        for (Media m : mediaList) {

            if (m.getStartYear() == year) {
                result.add(m);
            }
        }

        return result;
    }

    // Loc Media theo the loai
    public List<Media> filterGenre(String genre) {

        List<Media> result = new ArrayList<>();

        if (genre == null || genre.trim().isEmpty()) {
            return result;
        }

        for (Media m : mediaList) {

            if (m.getGenres() != null
                    && m.getGenres().toLowerCase()
                            .contains(genre.toLowerCase())) {

                result.add(m);
            }
        }

        return result;
    }

    // Loc rieng Movie
    public List<Movie> getMovies() {

        List<Movie> result = new ArrayList<>();

        for (Media m : mediaList) {

            if (m instanceof Movie) {
                result.add((Movie) m);
            }
        }

        return result;
    }

    // Loc rieng TVSeries
    public List<TVSeries> getTVSeries() {

        List<TVSeries> result = new ArrayList<>();

        for (Media m : mediaList) {

            if (m instanceof TVSeries) {
                result.add((TVSeries) m);
            }
        }

        return result;
    }

    // Sap xep Media theo diem IMDb
    public List<Media> sortedRating(boolean desc) {

        List<Media> result = new ArrayList<>(mediaList);

        Collections.sort(result, new Comparator<Media>() {

            @Override
            public int compare(Media a, Media b) {
                return Double.compare(a.getRating(), b.getRating());
            }
        });

        if (desc) {
            Collections.reverse(result);
        }

        return result;
    }

    // Lay Top 10 Media co diem IMDb cao nhat
    public List<Media> top10() {

        List<Media> result = sortedRating(true);

        return result.size() > 10
                ? new ArrayList<>(result.subList(0, 10))
                : result;
    }

    // Them dien vien hoac dao dien
    public void addPerson(Person p) {

        if (p == null) {
            return;
        }

        for (Person x : peopleList) {

            if (x.getPersonId() != null
                    && x.getPersonId().equals(p.getPersonId())) {
                return;
            }
        }

        peopleList.add(p);
    }

    // Tim kiem dien vien hoac dao dien
    public List<Person> searchPeople(String keyword, String role) {

        List<Person> result = new ArrayList<>();

        for (Person p : peopleList) {

            boolean matchRole = role == null
                    || p.getRoles().contains(role);

            boolean matchKeyword = keyword == null
                    || keyword.trim().isEmpty()
                    || p.matches(keyword);

            if (matchRole && matchKeyword) {
                result.add(p);
            }
        }

        return result;
    }

    // Lay danh sach Media co su tham gia cua mot nhan su
    public List<Media> mediaOf(Person p) {

        List<Media> result = new ArrayList<>();

        if (p == null) {
            return result;
        }

        for (String id : p.getCredits()) {

            Media m = findById(id);

            if (m != null) {
                result.add(m);
            }
        }

        return result;
    }
}

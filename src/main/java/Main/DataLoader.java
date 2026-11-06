
package Main;

import Data.Media;
import Data.Movie;
import Data.Person;
import Data.TVSeries;
import Data.Episode;
import Filter.AlternativeTitle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DataLoader {

    public static void load(String folder, MediaManager manager)
            throws IOException {

        Path titles = Paths.get(folder, "mcu_titles_rated.tsv");
        Path people = Paths.get(folder, "mcu_people.tsv");
        Path alternativeTitles = Paths.get(folder, "title.akas.tsv.gz");

        Path episodes = Paths.get(folder, "title.episode.tsv.gz");
        Path basics = Paths.get(folder, "title.basics.tsv.gz");
        Path ratings = Paths.get(folder, "title.ratings.tsv.gz");

        if (!Files.exists(titles)) {
            throw new IOException(
                    "Khong tim thay " + titles.toAbsolutePath()
            );
        }

        // Luu Media theo IMDb ID
        Map<String, Media> byId = new HashMap<>();

        // 1. Doc danh sach Movie va TVSeries
        try (BufferedReader br = Files.newBufferedReader(
                titles, StandardCharsets.UTF_8)) {

            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split("\\t", -1);

                if (d.length < 9) {
                    continue;
                }

                String id = clean(d[0]);
                String type = clean(d[1]);
                String title = clean(d[2]);
                String genre = clean(d[8]);

                int year = num(d[5]);
                int end = num(d[6]);
                int runtime = num(d[7]);

                double rating = d.length > 9
                        ? decimal(d[9]) : 0;

                int votes = d.length > 10
                        ? num(d[10]) : 0;

                if (id.isEmpty()) {
                    continue;
                }

                Media m;

                if (type.equals("movie")) {

                    m = new Movie(
                            id, title, year, genre,
                            rating, votes, runtime
                    );

                } else if (type.equals("tvSeries")
                        || type.equals("tvMiniSeries")) {

                    m = new TVSeries(
                            id, title, year, genre,
                            rating, votes, end
                    );

                } else {
                    continue;
                }

                if (manager.addMedia(m)) {
                    byId.put(id, m);
                }
            }
        }

        System.out.println("\n===== MEDIA DATA =====");
        System.out.println("Tong so Media: " + byId.size());
        System.out.println("So Movie: " + manager.getMovies().size());
        System.out.println("So TVSeries: " + manager.getTVSeries().size());

        // 2. Doc danh sach Episode
        if (Files.exists(episodes)) {

            Map<String, Episode> episodeById = new HashMap<>();

            try (BufferedReader br = openGzipReader(episodes)) {

                br.readLine();

                String line;

                while ((line = br.readLine()) != null) {

                    String[] d = line.split("\\t", -1);

                    if (d.length < 4) {
                        continue;
                    }

                    String episodeId = clean(d[0]);
                    String parentId = clean(d[1]);

                    int season = num(d[2]);
                    int episodeNumber = num(d[3]);

                    if (episodeId.isEmpty()) {
                        continue;
                    }

                    // Chi lay Episode thuoc TVSeries dang quan ly
                    Media media = byId.get(parentId);

                    if (!(media instanceof TVSeries)) {
                        continue;
                    }

                    Episode episode = new Episode(
                            episodeId,
                            parentId,
                            "",
                            season,
                            episodeNumber,
                            0,
                            0,
                            0
                    );

                    episodeById.put(episodeId, episode);
                }
            }

            // 3. Doc title.basics.tsv.gz
            // Lay ten va thoi luong Episode
            if (Files.exists(basics)) {

                int basicCount = 0;

                try (BufferedReader br = openGzipReader(basics)) {

                    br.readLine();

                    String line;

                    while ((line = br.readLine()) != null) {

                        String[] d = line.split("\\t", -1);

                        if (d.length < 8) {
                            continue;
                        }

                        String id = clean(d[0]);

                        Episode episode = episodeById.get(id);

                        if (episode == null) {
                            continue;
                        }

                        if (!clean(d[1]).equals("tvEpisode")) {
                            continue;
                        }

                        episode.setTitle(clean(d[2]));
                        episode.setRuntimeMinutes(num(d[7]));

                        basicCount++;
                    }
                }

                System.out.println(
                        "Episode co thong tin basics: " + basicCount
                );

            } else {

                System.out.println(
                        "CANH BAO: Khong tim thay title.basics.tsv.gz"
                );
            }

            // 4. Doc title.ratings.tsv.gz
            // Lay diem va so luot binh chon
            if (Files.exists(ratings)) {

                int ratingCount = 0;

                try (BufferedReader br = openGzipReader(ratings)) {

                    br.readLine();

                    String line;

                    while ((line = br.readLine()) != null) {

                        String[] d = line.split("\\t", -1);

                        if (d.length < 3) {
                            continue;
                        }

                        String id = clean(d[0]);

                        Episode episode = episodeById.get(id);

                        if (episode == null) {
                            continue;
                        }

                        episode.setRating(decimal(d[1]));
                        episode.setNumVotes(num(d[2]));

                        ratingCount++;
                    }
                }

                System.out.println(
                        "Episode co thong tin ratings: " + ratingCount
                );

            } else {

                System.out.println(
                        "CANH BAO: Khong tim thay title.ratings.tsv.gz"
                );
            }

            // 5. Gan Episode vao TVSeries
            int totalEpisodes = 0;

            for (Episode episode : episodeById.values()) {

                String parentId = episode.getParentSeriesId();

                Media media = byId.get(parentId);

                if (media instanceof TVSeries) {

                    TVSeries series = (TVSeries) media;

                    if (series.addEpisode(episode)) {
                        totalEpisodes++;
                    }
                }
            }

            System.out.println(
                    "Episode lien ket thanh cong: " + totalEpisodes
            );

        } else {

            System.out.println(
                    "CANH BAO: Khong tim thay title.episode.tsv.gz"
            );
        }

        // 6. Doc danh sach dien vien va dao dien
        if (Files.exists(people)) {

            Map<String, Person> peopleById = new HashMap<>();

            try (BufferedReader br = Files.newBufferedReader(
                    people, StandardCharsets.UTF_8)) {

                br.readLine();

                String line;

                while ((line = br.readLine()) != null) {

                    String[] d = line.split("\\t", -1);

                    if (d.length < 7
                            || !byId.containsKey(clean(d[0]))) {
                        continue;
                    }

                    String pid = clean(d[2]);
                    String pname = clean(d[3]);
                    String role = clean(d[4]);

                    if (pid.isEmpty()) {
                        continue;
                    }

                    Person p = peopleById.get(pid);

                    if (p == null) {
                        p = new Person(pid, pname);
                        peopleById.put(pid, p);
                    }

                    p.addRole(role);
                    p.addCredit(clean(d[0]));
                }
            }

            for (Person p : peopleById.values()) {
                manager.addPerson(p);
            }

        } else {

            System.out.println("Khong tim thay mcu_people.tsv");
        }

        // 7. Doc Alternative Titles
        if (Files.exists(alternativeTitles)) {

            try (BufferedReader br = openGzipReader(
                    alternativeTitles)) {

                br.readLine();

                String line;

                while ((line = br.readLine()) != null) {

                    String[] d = line.split("\\t", -1);

                    if (d.length < 8) {
                        continue;
                    }

                    String titleId = clean(d[0]);

                    Media media = byId.get(titleId);

                    if (media == null) {
                        continue;
                    }

                    int ordering = num(d[1]);
                    String title = clean(d[2]);
                    String region = clean(d[3]);
                    String language = clean(d[4]);
                    String types = clean(d[5]);
                    String attributes = clean(d[6]);

                    boolean originalTitle =
                            clean(d[7]).equals("1");

                    if (title.isEmpty()) {
                        continue;
                    }

                    AlternativeTitle alternativeTitle =
                            new AlternativeTitle(
                                    titleId,
                                    ordering,
                                    title,
                                    region,
                                    language,
                                    types,
                                    attributes,
                                    originalTitle
                            );

                    media.addAlternativeTitle(alternativeTitle);
                }
            }

        } else {

            System.out.println(
                    "Khong tim thay title.akas.tsv.gz"
            );
        }
    }

    // Mo file TSV nen bang GZIP
    private static BufferedReader openGzipReader(Path path)
            throws IOException {

        return new BufferedReader(
                new InputStreamReader(
                        new GZIPInputStream(
                                Files.newInputStream(path)
                        ),
                        StandardCharsets.UTF_8
                )
        );
    }

    // Xu ly du lieu rong hoac ky hieu \N
    private static String clean(String s) {

        if (s == null) {
            return "";
        }

        s = s.replace("\uFEFF", "").trim();

        return s.equals("\\N") ? "" : s;
    }

    // Chuyen String thanh int
    private static int num(String s) {

        try {
            return Integer.parseInt(clean(s));
        } catch (Exception e) {
            return 0;
        }
    }

    // Chuyen String thanh double
    private static double decimal(String s) {

        try {
            return Double.parseDouble(clean(s));
        } catch (Exception e) {
            return 0;
        }
    }
}

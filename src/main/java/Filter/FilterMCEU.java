package Filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class FilterMCEU {

    // Lop phu de luu thong tin phim phuc vu viec sap xep
    static class MovieInfo {
        String imdbId;
        String type;
        String title;
        int year;
        String rawLine;

        public MovieInfo(String imdbId, String type, String title,
                int year, String rawLine) {
            this.imdbId = imdbId;
            this.type = type;
            this.title = title;
            this.year = year;
            this.rawLine = rawLine;
        }

        public int getYear() {
            return year;
        }
    }

    public static void main(String[] args) {

        // Khai bao duong dan cac file
        Path idFile = Paths.get("mcu_ids.txt");
        Path dataFile = Paths.get("title.basics.tsv.gz");
        Path outputFile = Paths.get("mcu_titles.tsv");

        // Collection luu danh sach IMDb ID cua MCU
        Set<String> mcuIds = new HashSet<>();

        // Danh sach luu cac bo phim tim thay
        List<MovieInfo> foundMovies = new ArrayList<>();

        String header = "";

        try {

            // BUOC 1: Doc danh sach MCU ID
            try (BufferedReader idReader = Files.newBufferedReader(
                    idFile, StandardCharsets.UTF_8)) {

                String id;

                while ((id = idReader.readLine()) != null) {
                    id = id.trim();

                    if (!id.isEmpty()) {
                        mcuIds.add(id);
                    }
                }
            }

            System.out.println("So luong MCU ID: " + mcuIds.size());

            // BUOC 2: Doc du lieu IMDb tu file GZ
            try (BufferedReader dataReader = new BufferedReader(
                    new InputStreamReader(
                            new GZIPInputStream(Files.newInputStream(dataFile)),
                            StandardCharsets.UTF_8))) {

                // Doc dong tieu de
                header = dataReader.readLine();

                String line;

                // BUOC 3: Duyet tung dong du lieu IMDb
                while ((line = dataReader.readLine()) != null) {

                    // Tach cac cot bang ky tu TAB
                    String[] data = line.split("\t", -1);

                    // Kiem tra dong du lieu hop le
                    if (data.length < 6) {
                        continue;
                    }

                    // Lay IMDb ID
                    String imdbId = data[0];

                    // Kiem tra ID co thuoc MCU hay khong
                    if (mcuIds.contains(imdbId)) {

                        String type = data[1];
                        String title = data[2];

                        int year = 0;

                        try {
                            if (!data[5].equals("\\N")) {
                                year = Integer.parseInt(data[5]);
                            }
                        } catch (NumberFormatException e) {
                            year = 0;
                        }

                        // Them vao danh sach cho sap xep
                        foundMovies.add(
                                new MovieInfo(imdbId, type, title, year, line)
                        );
                    }
                }
            }

            // BUOC 4: Sap xep theo nam phat hanh tang dan
            foundMovies.sort(
                    Comparator.comparingInt(MovieInfo::getYear)
            );

            // BUOC 5: Ghi ket qua ra file
            try (BufferedWriter writer = Files.newBufferedWriter(
                    outputFile, StandardCharsets.UTF_8)) {

                // Ghi dong tieu de
                if (header != null) {
                    writer.write(header);
                    writer.newLine();
                }

                int count = 0;

                for (MovieInfo movie : foundMovies) {

                    // Hien thi thong tin
                    System.out.println("--------------------------");
                    System.out.println("IMDb ID: " + movie.imdbId);
                    System.out.println("Loai: " + movie.type);
                    System.out.println("Ten phim: " + movie.title);
                    System.out.println("Nam phat hanh: " + movie.year);

                    // Ghi toan bo dong du lieu
                    writer.write(movie.rawLine);
                    writer.newLine();

                    count++;
                }

                // BUOC 6: Thong bao ket qua
                System.out.println("==========================");
                System.out.println("Tong so phim tim thay: " + count);
                System.out.println("File created: " + outputFile.toAbsolutePath());
            }

        } catch (IOException e) {
            System.out.println("Loi khi doc hoac ghi file!");
            System.out.println(e.getMessage());
        }
    }
}

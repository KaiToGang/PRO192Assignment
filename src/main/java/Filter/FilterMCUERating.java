package Filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class FilterMCUERating {

    // Ham mo va doc file GZ
    private static BufferedReader openGzipReader(Path file)
            throws IOException {

        return new BufferedReader(
                new InputStreamReader(
                        new GZIPInputStream(Files.newInputStream(file)),
                        StandardCharsets.UTF_8
                )
        );
    }

    public static void main(String[] args) {

        // Khai bao duong dan cac file
        Path movieFile = Paths.get("mcu_titles.tsv");
        Path ratingFile = Paths.get("title.ratings.tsv.gz");
        Path outputFile = Paths.get("mcu_titles_rated.tsv");

        // Collection luu IMDb ID va thong tin Rating
        Map<String, String> ratings = new HashMap<>();

        // Collection luu IMDb ID cua cac phim MCU
        Set<String> mcuIds = new HashSet<>();

        int count = 0;
        int missingRating = 0;

        try {

            // BUOC 1: Doc danh sach IMDb ID tu mcu_titles.tsv
            try (BufferedReader reader = Files.newBufferedReader(
                    movieFile, StandardCharsets.UTF_8)) {

                // Bo qua dong tieu de
                reader.readLine();

                String line;

                while ((line = reader.readLine()) != null) {

                    String[] data = line.split("\t", -1);

                    if (data.length < 1 || data[0].isEmpty()) {
                        continue;
                    }

                    String imdbId = data[0];

                    mcuIds.add(imdbId);
                }
            }

            System.out.println("So phim MCU: " + mcuIds.size());


            // BUOC 2: Doc du lieu Rating tu IMDb file GZ
            try (BufferedReader reader = openGzipReader(ratingFile)) {

                // Bo qua dong tieu de
                reader.readLine();

                String line;

                while ((line = reader.readLine()) != null) {

                    String[] data = line.split("\t", -1);

                    if (data.length < 3) {
                        continue;
                    }

                    String imdbId = data[0];

                    // Chi luu Rating cua phim MCU
                    if (mcuIds.contains(imdbId)) {

                        String averageRating = data[1];
                        String numVotes = data[2];

                        ratings.put(
                                imdbId,
                                averageRating + "\t" + numVotes
                        );
                    }
                }
            }

            System.out.println("So phim co Rating: " + ratings.size());


            // BUOC 3: Tao file moi va ket hop du lieu
            try (
                BufferedReader reader = Files.newBufferedReader(
                        movieFile, StandardCharsets.UTF_8
                );

                BufferedWriter writer = Files.newBufferedWriter(
                        outputFile, StandardCharsets.UTF_8
                )
            ) {

                // Doc header cua file phim
                String header = reader.readLine();

                // Them hai cot Rating va Votes
                writer.write(header + "\taverageRating\tnumVotes");
                writer.newLine();

                String line;

                while ((line = reader.readLine()) != null) {

                    String[] data = line.split("\t", -1);

                    if (data.length < 1 || data[0].isEmpty()) {
                        continue;
                    }

                    String imdbId = data[0];

                    // Tim thong tin Rating
                    if (ratings.containsKey(imdbId)) {

                        writer.write(
                                line + "\t" + ratings.get(imdbId)
                        );

                        count++;

                    } else {

                        // Phim chua co du lieu Rating
                        writer.write(line + "\t\\N\t\\N");

                        missingRating++;
                    }

                    writer.newLine();
                }
            }


            // BUOC 4: Thong bao ket qua
            System.out.println("==============================");
            System.out.println("Tong so phim MCU: " + mcuIds.size());
            System.out.println("Phim co Rating: " + count);
            System.out.println("Phim chua co Rating: " + missingRating);
            System.out.println("Da tao file mcu_titles_rated.tsv");

        } catch (IOException e) {

            System.out.println("Loi khi doc hoac ghi file!");
            System.out.println(e.getMessage());

        }
    }
}

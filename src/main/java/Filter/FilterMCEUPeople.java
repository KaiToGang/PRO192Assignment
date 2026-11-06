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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class FilterMCEUPeople {

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
        Path principalFile = Paths.get("title.principals.tsv.gz");
        Path nameFile = Paths.get("name.basics.tsv.gz");
        Path outputFile = Paths.get("mcu_people.tsv");

        // Luu IMDb ID va ten phim
        Map<String, String> movieTitles = new HashMap<>();

        // Luu danh sach nconst can tim
        Set<String> peopleIds = new HashSet<>();

        // Luu thong tin dien vien va dao dien
        ArrayList<String[]> peopleList = new ArrayList<>();

        // Luu ten nguoi theo nconst
        Map<String, String> peopleNames = new HashMap<>();

        int count = 0;

        try {

            // BUOC 1: Doc danh sach phim MCU
            try (BufferedReader movieReader = Files.newBufferedReader(
                    movieFile, StandardCharsets.UTF_8)) {

                String line;

                // Bo qua dong tieu de
                movieReader.readLine();

                while ((line = movieReader.readLine()) != null) {

                    String[] data = line.split("\t", -1);

                    if (data.length > 2) {

                        String imdbId = data[0];
                        String title = data[2];

                        movieTitles.put(imdbId, title);
                    }
                }
            }

            System.out.println("So luong phim MCU: "
                    + movieTitles.size());


            // BUOC 2: Doc danh sach dien vien va dao dien tu file GZ
            try (BufferedReader principalReader =
                    openGzipReader(principalFile)) {

                String line;

                // Bo qua dong tieu de
                principalReader.readLine();

                while ((line = principalReader.readLine()) != null) {

                    String[] data = line.split("\t", -1);

                    if (data.length < 6) {
                        continue;
                    }

                    String imdbId = data[0];
                    String personId = data[2];
                    String category = data[3];

                    // Kiem tra phim co thuoc MCU hay khong
                    if (movieTitles.containsKey(imdbId)) {

                        // Chi lay dien vien va dao dien
                        if (category.equals("actor")
                                || category.equals("actress")
                                || category.equals("director")) {

                            peopleIds.add(personId);

                            peopleList.add(data);
                        }
                    }
                }
            }

            System.out.println("So luong ID nguoi tim thay: "
                    + peopleIds.size());


            // BUOC 3: Tim ten trong name.basics.tsv.gz
            try (BufferedReader nameReader = openGzipReader(nameFile)) {

                String line;

                // Bo qua dong tieu de
                nameReader.readLine();

                while ((line = nameReader.readLine()) != null) {

                    String[] data = line.split("\t", -1);

                    if (data.length < 2) {
                        continue;
                    }

                    String personId = data[0];
                    String personName = data[1];

                    // Chi lay nhung nguoi co trong danh sach
                    if (peopleIds.contains(personId)) {

                        peopleNames.put(personId, personName);
                    }
                }
            }


            // BUOC 4: Ghi ket qua ra file moi
            try (BufferedWriter writer = Files.newBufferedWriter(
                    outputFile, StandardCharsets.UTF_8)) {

                // Ghi dong tieu de
                writer.write("tconst\tprimaryTitle\tnconst"
                        + "\tprimaryName\tcategory\tjob\tcharacters");
                writer.newLine();

                for (String[] data : peopleList) {

                    String imdbId = data[0];
                    String personId = data[2];
                    String category = data[3];
                    String job = data[4];
                    String characters = data[5];

                    // Lay ten phim va ten nguoi
                    String movieTitle = movieTitles.get(imdbId);
                    String personName = peopleNames.get(personId);

                    // Kiem tra da tim thay ten hay chua
                    if (personName != null) {

                        System.out.println("--------------------------");
                        System.out.println("Ten phim: " + movieTitle);
                        System.out.println("IMDb ID: " + imdbId);
                        System.out.println("Ten nguoi: " + personName);
                        System.out.println("nconst: " + personId);
                        System.out.println("Vai tro: " + category);
                        System.out.println("Nhan vat/Cong viec: "
                                + (job.equals("\\N")
                                        ? characters : job));

                        // Ghi thong tin vao file
                        writer.write(imdbId + "\t"
                                + movieTitle + "\t"
                                + personId + "\t"
                                + personName + "\t"
                                + category + "\t"
                                + job + "\t"
                                + characters);

                        writer.newLine();

                        count++;
                    }
                }
            }


            // BUOC 5: Thong bao ket qua
            System.out.println("==========================");
            System.out.println("Tong so ket qua: " + count);
            System.out.println("Da tao file mcu_people.tsv");

        } catch (IOException e) {

            System.out.println("Loi khi doc hoac ghi file!");
            System.out.println(e.getMessage());

        }
    }
}

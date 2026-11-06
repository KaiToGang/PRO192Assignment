package Main;

import Data.Person;
import Data.Movie;
import Data.Media;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static MediaManager manager = new MediaManager();

    public static void main(String[] args) {
        System.out.println("=== MARVEL MANAGEMENT SYSTEM ===");
        System.out.print("Thu muc chua TSV (Enter neu cung thu muc pom.xml): ");
        String folder = sc.nextLine().trim();

        if (folder.isEmpty()) {
            folder = ".";
        }

        try {
            DataLoader.load(folder, manager);
            System.out.println("Da nap " + manager.getMediaList().size()
                    + " phim/series va "
                    + manager.getPeopleList().size() + " nguoi.");
        } catch (IOException e) {
            System.out.println("Loi nap du lieu: " + e.getMessage());
        }

        int choice;

        do {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Hien thi phim/series");
            System.out.println("2. Them phim");
            System.out.println("3. Sua phim");
            System.out.println("4. Xoa phim");
            System.out.println("5. Tim theo ten/IMDb ID");
            System.out.println("6. Loc theo nam");
            System.out.println("7. Loc theo the loai");
            System.out.println("8. Sap xep theo Rating");
            System.out.println("9. Top 10 IMDb");
            System.out.println("10. Dien vien/dao dien");
            System.out.println("0. Thoat");

            choice = integer("Lua chon: ");

            switch (choice) {
                case 1:
                    show(manager.getMediaList());
                    break;

                case 2:
                    add();
                    break;

                case 3:
                    update();
                    break;

                case 4:
                    remove();
                    break;

                case 5:
                    show(manager.searchMedia(input("Tu khoa: ")));
                    break;

                case 6:
                    show(manager.filterYear(integer("Nam: ")));
                    break;

                case 7:
                    show(manager.filterGenre(input("The loai: ")));
                    break;

                case 8:
                    int order = integer("1 giam dan, 2 tang dan: ");
                    show(manager.sortedRating(order != 2));
                    break;

                case 9:
                    show(manager.top10());
                    break;

                case 10:
                    people();
                    break;

                case 0:
                    System.out.println("Tam biet!");
                    break;

                default:
                    System.out.println("Lua chon khong hop le.");
            }

        } while (choice != 0);
    }

    private static void add() {
        String id = input("IMDb ID: ");
        String title = input("Ten phim: ");
        int year = integer("Nam: ");
        String genres = input("The loai: ");
        double rating = decimal("Rating: ");
        int votes = integer("Votes: ");
        int runtime = integer("Thoi luong (phut): ");

        boolean success = manager.addMedia(
                new Movie(id, title, year, genres, rating, votes, runtime)
        );

        System.out.println(success ? "Da them." : "ID da ton tai.");
    }

    private static void update() {
        String id = input("IMDb ID can sua: ");

        if (manager.findById(id) == null) {
            System.out.println("Khong tim thay.");
            return;
        }

        manager.updateMedia(
                id,
                input("Ten moi: "),
                integer("Nam moi: "),
                input("The loai moi: "),
                decimal("Rating moi: "),
                integer("Votes moi: ")
        );

        System.out.println("Da cap nhat.");
    }

    private static void remove() {
        String id = input("IMDb ID can xoa: ");

        boolean success = manager.deleteMedia(id);

        System.out.println(success ? "Da xoa." : "Khong tim thay.");
    }

    private static void people() {
        System.out.println("1. Dien vien");
        System.out.println("2. Dao dien");
        System.out.println("3. Tim theo ten/nconst");

        int choice = integer("Lua chon: ");

        String role = choice == 1 ? "actor"
                : choice == 2 ? "director" : null;

        if (choice < 1 || choice > 3) {
            System.out.println("Lua chon khong hop le.");
            return;
        }

        String keyword = choice == 3
                ? input("Ten hoac nconst: ") : "";

        List<Person> peopleList = manager.searchPeople(keyword, role);

        if (peopleList.isEmpty()) {
            System.out.println("Khong co du lieu.");
            return;
        }

        for (Person p : peopleList) {
            System.out.println("\n" + p);

            for (Media m : manager.mediaOf(p)) {
                System.out.println("  - " + m.getTitle()
                        + " (" + m.getStartYear() + ")");
            }
        }
    }

    // Hien thi danh sach phim voi cac cot duoc can chinh
    private static void show(List<Media> list) {
        if (list.isEmpty()) {
            System.out.println("Khong co ket qua.");
            return;
        }

        System.out.println();

        System.out.printf(
                "%-12s | %-45s | %-4s | %-35s | %-5s | %-10s | %-15s%n",
                "IMDb ID",
                "Title",
                "Year",
                "Genres",
                "Rate",
                "Votes",
                "Extra"
        );

        System.out.println(
                "-------------+-----------------------------------------------+------+-------------------------------------+-------+------------+----------------"
        );

        for (Media m : list) {
            System.out.printf(
                    "%-12s | %-45s | %-4d | %-35s | %-5.1f | %-10d | %-15s%n",
                    m.getImdbId(),
                    m.getTitle(),
                    m.getStartYear(),
                    m.getGenres(),
                    m.getRating(),
                    m.getNumVotes(),
                    m.getExtraInfo()
            );
        }

        System.out.println("\nTong so ket qua: " + list.size());
    }

    private static String input(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int integer(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(input(prompt));
            } catch (Exception e) {
                System.out.println("Nhap so nguyen hop le.");
            }
        }
    }

    private static double decimal(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(input(prompt));
            } catch (Exception e) {
                System.out.println("Nhap so hop le.");
            }
        }
    }
}
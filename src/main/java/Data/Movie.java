
package Data;

import Data.Media;

public class Movie extends Media {

    private int runtimeMinutes;

    // Constructor
    public Movie(String id, String title, int year, String genres,
            double rating, int votes, int runtimeMinutes) {

        super(id, title, year, genres, rating, votes);
        this.runtimeMinutes = runtimeMinutes;
    }

    // Getter và Setter
    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    // Xác định loại nội dung
    @Override
    public String getMediaType() {
        return "Movie";
    }

    // Lấy thông tin bổ sung
    @Override
    public String getExtraInfo() {
        return runtimeMinutes > 0
                ? runtimeMinutes + " min"
                : "Unknown runtime";
    }

    // Hiển thị thông tin Movie
    @Override
    public String toString() {
        return super.toString();
    }
}

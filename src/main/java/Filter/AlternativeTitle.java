package Filter;

// Class luu thong tin ten goi thay the cua phim
public class AlternativeTitle {

    private String titleId;
    private int ordering;
    private String title;
    private String region;
    private String language;
    private String types;
    private String attributes;
    private boolean originalTitle;

    // Constructor
    public AlternativeTitle(String titleId, int ordering, String title,
            String region, String language, String types,
            String attributes, boolean originalTitle) {

        this.titleId = titleId;
        this.ordering = ordering;
        this.title = title;
        this.region = region;
        this.language = language;
        this.types = types;
        this.attributes = attributes;
        this.originalTitle = originalTitle;
    }

    // Getter va Setter
    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public boolean isOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(boolean originalTitle) {
        this.originalTitle = originalTitle;
    }

    // Kiem tra ten thay the
    public boolean matches(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        if (title == null) {
            return false;
        }

        return title.toLowerCase().contains(
                keyword.trim().toLowerCase()
        );
    }

    // Hien thi thong tin
    @Override
    public String toString() {

        return "AlternativeTitle{"
                + "titleId='" + titleId + '\''
                + ", ordering=" + ordering
                + ", title='" + title + '\''
                + ", region='" + region + '\''
                + ", language='" + language + '\''
                + ", types='" + types + '\''
                + ", attributes='" + attributes + '\''
                + ", originalTitle=" + originalTitle
                + '}';
    }
}

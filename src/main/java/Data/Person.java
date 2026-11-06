
package Data;

import java.util.LinkedHashSet;
import java.util.Set;

public class Person implements Searchable {

    private String personId;
    private String name;
    private Set<String> roles = new LinkedHashSet<>();
    private Set<String> credits = new LinkedHashSet<>();

    // Constructor
    public Person(String id, String name) {
        this.personId = id;
        this.name = name;
    }

    // Getter và Setter

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles != null
                ? new LinkedHashSet<>(roles)
                : new LinkedHashSet<String>();
    }

    public Set<String> getCredits() {
        return credits;
    }

    public void setCredits(Set<String> credits) {
        this.credits = credits != null
                ? new LinkedHashSet<>(credits)
                : new LinkedHashSet<String>();
    }

    // Thêm vai trò (Actor, Actress, Director...)
    public void addRole(String role) {
        if (role != null && !role.trim().isEmpty()) {
            roles.add(role);
        }
    }

    // Thêm IMDb ID của phim mà người này tham gia
    public void addCredit(String credit) {
        if (credit != null && !credit.trim().isEmpty()) {
            credits.add(credit);
        }
    }

    // Tìm kiếm theo IMDb ID hoặc tên nhân sự
    @Override
    public boolean matches(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        keyword = keyword.toLowerCase();

        return personId.toLowerCase().contains(keyword)
                || name.toLowerCase().contains(keyword);
    }

    // Hiển thị thông tin nhân sự
    @Override
    public String toString() {
        return personId + " | " + name
                + " | " + roles
                + " | MCU titles: " + credits.size();
    }
}

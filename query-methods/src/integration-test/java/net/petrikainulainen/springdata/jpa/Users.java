package net.petrikainulainen.springdata.jpa;

/**
 * @author Petri Kainulainen
 */
public enum Users {

    USER("user", "password", "ROLE_USER");

    private String password;
    private String role;
    private String username;

    Users(String username, String password, String role) {
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}

package net.petrikainulainen.springdata.jpa;

/**
 * @author Petri Kainulainen
 */
public enum Users {

    USER("user");

    private String username;

    Users(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

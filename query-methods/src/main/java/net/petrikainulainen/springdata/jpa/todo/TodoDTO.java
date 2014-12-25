package net.petrikainulainen.springdata.jpa.todo;

import java.time.ZonedDateTime;

/**
 * @author Petri Kainulainen
 */
public class TodoDTO {

    private ZonedDateTime creationTime;

    private String description;

    private Long id;

    private ZonedDateTime modificationTime;

    private String title;

    public TodoDTO() {}

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getModificationTime() {
        return modificationTime;
    }

    public String getTitle() {
        return title;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModificationTime(ZonedDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

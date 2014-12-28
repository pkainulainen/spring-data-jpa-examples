package net.petrikainulainen.springdata.jpa.todo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * @author Petri Kainulainen
 */
public final class TodoDTO {

    private ZonedDateTime creationTime;

    @Size(max = Todo.MAX_LENGTH_DESCRIPTION)
    private String description;

    private Long id;

    private ZonedDateTime modificationTime;

    @NotEmpty
    @Size(max = Todo.MAX_LENGTH_TITLE)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("creationTime", this.creationTime)
                .append("description", this.description)
                .append("id", this.id)
                .append("modificationTime", this.modificationTime)
                .append("title", this.title)
                .toString();
    }
}

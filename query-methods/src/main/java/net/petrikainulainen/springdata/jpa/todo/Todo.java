package net.petrikainulainen.springdata.jpa.todo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Date;

import static net.petrikainulainen.springdata.jpa.todo.PreCondition.isTrue;
import static net.petrikainulainen.springdata.jpa.todo.PreCondition.notEmpty;
import static net.petrikainulainen.springdata.jpa.todo.PreCondition.notNull;

/**
 * This entity class contains the information of a single todo entry
 * and the methods that are used to create new todo entries and to modify
 * the information of an existing todo entry.
 *
 * @author Petri Kainulainen
 */
@Entity
@Table(name = "todos")
final class Todo {

    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "description", length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modification_time")
    private Date modificationTime;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    @Version
    private long version;

    /**
     * Required by Hibernate.
     */
    private Todo() {}

    private Todo(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
    }

    static Builder getBuilder() {
        return new Builder();
    }

    Long getId() {
        return id;
    }

    LocalDateTime getCreationTime() {
        LocalDateTime creationTime = null;

        if (this.creationTime != null) {
            creationTime = LocalDateTime.from(this.creationTime.toInstant());
        }

        return creationTime;
    }

    String getDescription() {
        return description;
    }

    LocalDateTime getModificationTime() {
        LocalDateTime modificationTime = null;

        if (this.modificationTime != null) {
            modificationTime = LocalDateTime.from(this.modificationTime.toInstant());
        }

        return modificationTime;
    }

    String getTitle() {
        return title;
    }

    long getVersion() {
        return version;
    }

    /**
     * This entity is so simple that you don't really need to use the builder pattern
     * (use a constructor instead). I use the builder pattern here because it makes
     * the code a bit more easier to read.
     */
    static class Builder {
        private String description;
        private String title;

        private Builder() {}

        Builder description(String description) {
            this.description = description;
            return this;
        }

        Builder title(String title) {
            this.title = title;
            return this;
        }

        Todo build() {
            Todo build = new Todo(this);

            notNull(build.getTitle(), "Title cannot be null.");
            notEmpty(build.getTitle(), "Title cannot be empty.");
            isTrue(build.getTitle().length() <= MAX_LENGTH_TITLE,
                    "The maximum length of the title is <%d> characters.",
                    MAX_LENGTH_TITLE
            );
            String description = build.getDescription();
            isTrue((description == null) || (description.length() <= MAX_LENGTH_DESCRIPTION),
                    "The maximum length of the description is <%d> characters.",
                    MAX_LENGTH_DESCRIPTION
            );

            return build;
        }
    }
}

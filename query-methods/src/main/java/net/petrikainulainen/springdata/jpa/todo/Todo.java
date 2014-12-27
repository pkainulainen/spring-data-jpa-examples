package net.petrikainulainen.springdata.jpa.todo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.ZonedDateTime;

import static net.petrikainulainen.springdata.jpa.common.PreCondition.isTrue;
import static net.petrikainulainen.springdata.jpa.common.PreCondition.notEmpty;
import static net.petrikainulainen.springdata.jpa.common.PreCondition.notNull;

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

    static final int MAX_LENGTH_DESCRIPTION = 500;
    static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    private ZonedDateTime creationTime;

    @Column(name = "description", length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modification_time")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    private ZonedDateTime modificationTime;

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

    ZonedDateTime getCreationTime() {
        return creationTime;
    }

    String getDescription() {
        return description;
    }

    ZonedDateTime getModificationTime() {
        return modificationTime;
    }

    String getTitle() {
        return title;
    }

    long getVersion() {
        return version;
    }

    @PrePersist
    public void prePersist() {
        ZonedDateTime now = ZonedDateTime.now();
        this.creationTime = now;
        this.modificationTime = now;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("creationTime", this.creationTime)
                .append("description", this.description)
                .append("id", this.id)
                .append("modificationTime", this.modificationTime)
                .append("title", this.title)
                .append("version", this.version)
                .toString();
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

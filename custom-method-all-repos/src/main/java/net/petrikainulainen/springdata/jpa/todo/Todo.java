package net.petrikainulainen.springdata.jpa.todo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
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
@EntityListeners(AuditingEntityListener.class)
@NamedNativeQuery(name = "Todo.findBySearchTermNamedNative",
        query="SELECT * FROM todos t WHERE " +
                "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
                "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
                "ORDER BY t.title ASC",
        resultClass = Todo.class
)
@NamedQuery(name = "Todo.findBySearchTermNamed",
        query = "SELECT t FROM Todo t WHERE " +
                "LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
                "ORDER BY t.title ASC"
)
@Table(name = "todos")
final class Todo {

    static final int MAX_LENGTH_DESCRIPTION = 500;
    static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_by_user", nullable = false)
    @CreatedBy
    private String createdByUser;

    @Column(name = "creation_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    @CreatedDate
    private ZonedDateTime creationTime;

    @Column(name = "description", length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modified_by_user", nullable = false)
    @LastModifiedBy
    private String modifiedByUser;

    @Column(name = "modification_time")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    @LastModifiedDate
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

    String getCreatedByUser() {
        return createdByUser;
    }

    ZonedDateTime getCreationTime() {
        return creationTime;
    }

    String getDescription() {
        return description;
    }

    String getModifiedByUser() {
        return modifiedByUser;
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

    void update(String newTitle, String newDescription) {
        requireValidTitleAndDescription(newTitle, newDescription);

        this.title = newTitle;
        this.description = newDescription;
    }

    private void requireValidTitleAndDescription(String title, String description) {
        notNull(title, "Title cannot be null.");
        notEmpty(title, "Title cannot be empty.");
        isTrue(title.length() <= MAX_LENGTH_TITLE,
                "The maximum length of the title is <%d> characters.",
                MAX_LENGTH_TITLE
        );

        isTrue((description == null) || (description.length() <= MAX_LENGTH_DESCRIPTION),
                "The maximum length of the description is <%d> characters.",
                MAX_LENGTH_DESCRIPTION
        );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("createdByUser", this.createdByUser)
                .append("creationTime", this.creationTime)
                .append("description", this.description)
                .append("id", this.id)
                .append("modifiedByUser", this.modifiedByUser)
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

            build.requireValidTitleAndDescription(build.getTitle(), build.getDescription());

            return build;
        }
    }
}

package net.petrikainulainen.springdata.jpa.todo;

import java.time.ZonedDateTime;

/**
 * @author Petri Kainulainen
 */
public class TodoDTOBuilder {

    private ZonedDateTime creationTime;
    private String description;
    private Long id;
    private ZonedDateTime modificationTime;
    private String title = "NOT_IMPORTANT";

    public TodoDTOBuilder() {}

    public TodoDTOBuilder creationTime(String creationTime) {
        this.creationTime = TestUtil.parseDateTime(creationTime);
        return this;
    }

    public TodoDTOBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TodoDTOBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TodoDTOBuilder modificationTime(String modificationTime) {
        this.modificationTime = TestUtil.parseDateTime(modificationTime);
        return this;
    }

    public TodoDTOBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TodoDTO build() {
        TodoDTO build = new TodoDTO();

        build.setCreationTime(creationTime);
        build.setDescription(description);
        build.setId(id);
        build.setModificationTime(modificationTime);
        build.setTitle(title);

        return build;
    }
}

package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;

/**
 * @author Petri Kainulainen
 */
class TodoBuilder {

    private Long id;
    private ZonedDateTime creationTime;
    private String description;
    private ZonedDateTime modificationTime;
    private String title;

    TodoBuilder() {}

    TodoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    TodoBuilder creationTime(String creationTime) {
        this.creationTime = TestUtil.parseDateTime(creationTime);
        return this;
    }

    TodoBuilder description(String description) {
        this.description = description;
        return this;
    }

    TodoBuilder modificationTime(String modificationTime) {
        this.modificationTime = TestUtil.parseDateTime(modificationTime);
        return this;
    }

    TodoBuilder title(String title) {
        this.title = title;
        return this;
    }

    Todo build() {
        Todo build = Todo.getBuilder()
                .title(title)
                .description(description)
                .build();

        ReflectionTestUtils.setField(build, "creationTime", creationTime);
        ReflectionTestUtils.setField(build, "id", id);
        ReflectionTestUtils.setField(build, "modificationTime", modificationTime);

        return build;
    }
}

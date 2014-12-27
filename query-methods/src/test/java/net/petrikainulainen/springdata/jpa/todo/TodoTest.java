package net.petrikainulainen.springdata.jpa.todo;

import org.junit.Test;

import java.time.ZonedDateTime;

import static net.petrikainulainen.springdata.jpa.todo.TodoAssert.assertThatTodoEntry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class TodoTest {

    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    @Test(expected = NullPointerException.class)
    public void build_TitleIsNull_ShouldThrowException() {
        Todo.getBuilder()
                .title(null)
                .description(DESCRIPTION)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TitleIsEmpty_ShouldThrowException() {
        Todo.getBuilder()
                .title("")
                .description(DESCRIPTION)
                .build();
    }

    @Test
    public void build_MaxLengthTitleGiven_ShouldCreateNewObject() {
        String maxLengthTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE);

        Todo build = Todo.getBuilder()
                .title(maxLengthTitle)
                .description(DESCRIPTION)
                .build();

        assertThatTodoEntry(build)
                .hasTitle(maxLengthTitle)
                .hasDescription(DESCRIPTION)
                .hasNoCreationTime()
                .hasNoId()
                .hasNoModificationTime();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TooLongTitleGiven_ShouldThrowException() {
        String maxLengthTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);

        Todo.getBuilder()
                .title(maxLengthTitle)
                .description(DESCRIPTION)
                .build();
    }

    @Test
    public void build_NoDescriptionGiven_ShouldCreateNewObject() {
        Todo build = Todo.getBuilder()
                .title(TITLE)
                .build();

        assertThatTodoEntry(build)
                .hasTitle(TITLE)
                .hasNoId()
                .hasNoCreationTime()
                .hasNoDescription()
                .hasNoModificationTime();
    }

    @Test
    public void build_MaxLengthDescriptionGiven_ShouldCreateNewObject() {
        String maxLengthDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

        Todo build = Todo.getBuilder()
                .title(TITLE)
                .description(maxLengthDescription)
                .build();

        assertThatTodoEntry(build)
                .hasTitle(TITLE)
                .hasDescription(maxLengthDescription)
                .hasNoId()
                .hasNoCreationTime()
                .hasNoModificationTime();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TooLongDescriptionGiven_ShouldThrowException() {
        String tooLongDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);

        Todo.getBuilder()
                .title(TITLE)
                .description(tooLongDescription)
                .build();
    }

    @Test
    public void prePersist_ShouldUseSameTimeAsCreationTimeAndModificationTime() {
        Todo newTodoEntry = Todo.getBuilder()
                .title(TITLE)
                .build();

        newTodoEntry.prePersist();

        ZonedDateTime creationTime = newTodoEntry.getCreationTime();
        ZonedDateTime modificationTime = newTodoEntry.getModificationTime();

        assertThat(creationTime).isNotNull();
        assertThat(modificationTime).isNotNull();
        assertThat(creationTime).isEqualTo(modificationTime);
    }
}

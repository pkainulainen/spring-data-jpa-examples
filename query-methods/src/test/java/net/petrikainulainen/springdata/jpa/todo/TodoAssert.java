package net.petrikainulainen.springdata.jpa.todo;

import org.assertj.core.api.AbstractAssert;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class provides a fluent API that can be used for writing assertions
 * to {@link net.petrikainulainen.springdata.jpa.todo.Todo} objects.
 *
 * @author Petri Kainulainen
 */
public class TodoAssert extends AbstractAssert<TodoAssert, Todo> {

    private TodoAssert(Todo actual) {
        super(actual, TodoAssert.class);
    }

    public static TodoAssert assertThatTodoEntry(Todo actual) {
        return new TodoAssert(actual);
    }

    public TodoAssert hasDescription(String expectedDescription) {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage(String.format(
                        "Expected description to be <%s> but was <%s>.",
                        expectedDescription,
                        actualDescription
                ))
                .isEqualTo(expectedDescription);

        return this;
    }

    public TodoAssert hasNoCreationTime() {
        isNotNull();

        LocalDateTime actualCreationTime = actual.getCreationTime();
        assertThat(actualCreationTime)
                .overridingErrorMessage(
                        "Expected creation time to be <null> but was <%s>",
                        actualCreationTime
                )
                .isNull();

        return this;
    }

    public TodoAssert hasNoDescription() {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage("Expected description to be <null> but was <%s>", actualDescription)
                .isNull();

        return this;
    }

    public TodoAssert hasNoId() {
        isNotNull();

        Long actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%d>.", actualId)
                .isNull();

        return this;
    }

    public TodoAssert hasNoModificationTime() {
        isNotNull();

        LocalDateTime actualModificationTime = actual.getModificationTime();
        assertThat(actualModificationTime)
                .overridingErrorMessage(
                        "Expected modification time to be <null> but was <%s>.",
                        actualModificationTime
                )
                .isNull();

        return this;
    }

    public TodoAssert hasTitle(String expectedTitle) {
        isNotNull();

        String actualTitle = actual.getTitle();
        assertThat(actualTitle)
                .overridingErrorMessage(
                        "Expected title to be <%s> but was <%s>.",
                        expectedTitle,
                        actualTitle
                )
                .isEqualTo(actualTitle);

        return this;
    }
}

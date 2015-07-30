package net.petrikainulainen.springdata.jpa.todo;

import org.assertj.core.api.AbstractAssert;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class provides a fluent API that can be used for writing assertions
 * to {@link net.petrikainulainen.springdata.jpa.todo.Todo} objects.
 *
 * @author Petri Kainulainen
 */
final class TodoAssert extends AbstractAssert<TodoAssert, Todo> {

    private TodoAssert(Todo actual) {
        super(actual, TodoAssert.class);
    }

    static TodoAssert assertThatTodoEntry(Todo actual) {
        return new TodoAssert(actual);
    }

    TodoAssert hasDescription(String expectedDescription) {
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

    TodoAssert hasNoCreationAuditFieldValues() {
        isNotNull();

        ZonedDateTime actualCreationTime = actual.getCreationTime();
        assertThat(actualCreationTime)
                .overridingErrorMessage(
                        "Expected creationTime to be <null> but was <%s>",
                        actualCreationTime
                )
                .isNull();

        String actualCreatedByUser = actual.getCreatedByUser();
        assertThat(actualCreatedByUser)
                .overridingErrorMessage(
                        "Expected createdByUser to be <null> but was <%s>",
                        actualCreatedByUser
                )
                .isNull();

        return this;
    }

    TodoAssert hasNoDescription() {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage("Expected description to be <null> but was <%s>", actualDescription)
                .isNull();

        return this;
    }

    TodoAssert hasId(Long expectedId) {
        isNotNull();

        Long actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <%d> but was <%d>",
                        expectedId,
                        actualId
                )
                .isEqualTo(expectedId);

        return this;
    }

    TodoAssert hasNoId() {
        isNotNull();

        Long actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%d>.", actualId)
                .isNull();

        return this;
    }

    TodoAssert hasNoModificationAuditFieldValues() {
        isNotNull();

        ZonedDateTime actualModificationTime = actual.getModificationTime();
        assertThat(actualModificationTime)
                .overridingErrorMessage(
                        "Expected modificationTime to be <null> but was <%s>.",
                        actualModificationTime
                )
                .isNull();

        String actualModifiedByUser = actual.getModifiedByUser();
        assertThat(actualModifiedByUser)
                .overridingErrorMessage(
                        "Expected modificationTime to be <null> but was <%s>",
                        actualModificationTime
                )
                .isNull();

        return this;
    }

    TodoAssert hasTitle(String expectedTitle) {
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

    public TodoAssert wasCreatedAt(String creationTime) {
        isNotNull();

        ZonedDateTime expectedCreationTime = TestUtil.parseDateTime(creationTime);
        ZonedDateTime actualCreationTime = actual.getCreationTime();

        assertThat(actualCreationTime)
                .overridingErrorMessage(
                        "Expected creation time to be <%s> but was <%s>",
                        expectedCreationTime,
                        actualCreationTime
                )
                .isEqualTo(expectedCreationTime);

        return this;
    }

    public TodoAssert wasCreatedByUser(String expectedCreatedByUser) {
        isNotNull();

        String actualCreatedByUser = actual.getCreatedByUser();
        assertThat(actualCreatedByUser)
                .overridingErrorMessage(
                        "Expected createdByUser to be <%s> but was <%s>",
                        expectedCreatedByUser,
                        actualCreatedByUser
                )
                .isEqualTo(expectedCreatedByUser);

        return this;
    }

    public TodoAssert wasModifiedAt(String modificationTime) {
        isNotNull();

        ZonedDateTime expectedModificationTime = TestUtil.parseDateTime(modificationTime);
        ZonedDateTime actualModificationTime = actual.getModificationTime();

        assertThat(actualModificationTime)
                .overridingErrorMessage(
                        "Expected modification time to be <%s> but was <%s>",
                        expectedModificationTime,
                        actualModificationTime
                )
                .isEqualTo(actualModificationTime);

        return this;
    }

    public TodoAssert wasModifiedByUser(String expectedModifiedByUser) {
        isNotNull();

        String actualModifiedByUser = actual.getModifiedByUser();
        assertThat(actualModifiedByUser)
                .overridingErrorMessage(
                        "Expected modifiedByUser to be <%s> but was <%s>",
                        expectedModifiedByUser,
                        actualModifiedByUser
                )
                .isEqualTo(expectedModifiedByUser);

        return this;
    }
}

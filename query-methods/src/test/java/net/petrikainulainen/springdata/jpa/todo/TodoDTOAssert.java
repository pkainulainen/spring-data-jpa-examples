package net.petrikainulainen.springdata.jpa.todo;

import org.assertj.core.api.AbstractAssert;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
final class TodoDTOAssert extends AbstractAssert<TodoDTOAssert, TodoDTO> {

    private TodoDTOAssert(TodoDTO actual) {
        super(actual, TodoDTOAssert.class);
    }

    static TodoDTOAssert assertThatTodoDTO(TodoDTO actual) {
        return new TodoDTOAssert(actual);
    }

    TodoDTOAssert hasDescription(String expectedDescription) {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage(
                        "Expected description to be <%s> but was <%s>",
                        expectedDescription,
                        actualDescription
                )
                .isEqualTo(expectedDescription);

        return this;
    }

    TodoDTOAssert hasId(Long expectedId) {
        isNotNull();

        Long actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage(
                        "Expected id to be <%d> but was <%d>",
                        actualId,
                        expectedId
                )
                .isEqualTo(expectedId);

        return this;
    }

    TodoDTOAssert hasTitle(String expectedTitle) {
        isNotNull();

        String actualTitle = actual.getTitle();
        assertThat(actualTitle)
                .overridingErrorMessage(
                        "Expected title to be <%s> but was <%s>",
                        expectedTitle,
                        actualTitle
                )
                .isEqualTo(expectedTitle);

        return this;
    }

    TodoDTOAssert wasCreatedAt(String creationTime) {
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

    TodoDTOAssert wasModifiedAt(String modificationTime) {
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
}

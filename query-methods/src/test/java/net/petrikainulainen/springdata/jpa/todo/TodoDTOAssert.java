package net.petrikainulainen.springdata.jpa.todo;

import org.assertj.core.api.AbstractAssert;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public final class TodoDTOAssert extends AbstractAssert<TodoDTOAssert, TodoDTO> {

    private TodoDTOAssert(TodoDTO actual) {
        super(actual, TodoDTOAssert.class);
    }

    public static TodoDTOAssert assertThatTodoDTO(TodoDTO actual) {
        return new TodoDTOAssert(actual);
    }

    public TodoDTOAssert hasDescription(String expectedDescription) {
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

    public TodoDTOAssert hasId(Long expectedId) {
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

    public TodoDTOAssert hasNoCreationAuditFieldValues() {
        isNotNull();

        String actualCreatedByUser = actual.getCreatedByUser();
        assertThat(actualCreatedByUser)
                .overridingErrorMessage(
                        "Expected createdByUser to be <null> but was <%s>",
                        actualCreatedByUser
                )
                .isNull();

        ZonedDateTime actualCreationTime = actual.getCreationTime();
        assertThat(actualCreationTime)
                .overridingErrorMessage("Expected creationTime to be <null> but was <%s>", actualCreationTime)
                .isNull();

        return this;
    }

    public TodoDTOAssert hasNoId() {
        isNotNull();

        Long actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%d>", actualId)
                .isNull();

        return this;
    }

    public TodoDTOAssert hasNoModificationAuditFieldValues() {
        isNotNull();

        String actualModifiedByUser = actual.getModifiedByUser();
        assertThat(actualModifiedByUser)
                .overridingErrorMessage(
                        "Expected modifiedByUser to be <null> but was <%s>",
                        actualModifiedByUser
                )
                .isNull();

        ZonedDateTime actualModificationTime = actual.getModificationTime();
        assertThat(actualModificationTime)
                .overridingErrorMessage("Expected modification time to be <null> but was <%d>", actualModificationTime)
                .isNull();

        return this;
    }

    public TodoDTOAssert hasTitle(String expectedTitle) {
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

    public TodoDTOAssert wasCreatedAt(String creationTime) {
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

    public TodoDTOAssert wasCreatedByUser(String expectedCreatedByUser) {
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

    public TodoDTOAssert wasModifiedAt(String modificationTime) {
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

    public TodoDTOAssert wasModifiedByUser(String expectedModifiedByUser) {
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

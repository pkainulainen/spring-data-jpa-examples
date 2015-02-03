package net.petrikainulainen.springdata.jpa.todo;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZonedDateTime;

import static net.petrikainulainen.springdata.jpa.todo.TodoAssert.assertThatTodoEntry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class TodoTest {

    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    private static final String DESCRIPTION = "description";
    private static final String TITLE = "title";

    private static final String UPDATED_DESCRIPTION = "updatedDescription";
    private static final String UPDATED_TITLE = "updatedTitle";

    public class Build {

        public class WhenTitleIsInvalid {

            @Test(expected = NullPointerException.class)
            public void shouldThrowExceptionWhenTitleIsNull() {
                Todo.getBuilder()
                        .title(null)
                        .description(DESCRIPTION)
                        .build();
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenTitleIsEmpty() {
                Todo.getBuilder()
                        .title("")
                        .description(DESCRIPTION)
                        .build();
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenTitleIsTooLong() {
                String tooLongTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);

                Todo.getBuilder()
                        .title(tooLongTitle)
                        .description(DESCRIPTION)
                        .build();
            }
        }

        public class WhenDescriptionIsTooLong {

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowException() {
                String tooLongDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);

                Todo.getBuilder()
                        .title(TITLE)
                        .description(tooLongDescription)
                        .build();
            }
        }

        public class WhenTitleAndDescriptionAreValid {

            @Test
            public void shouldCreateNewObjectWhenMaxLengthTitleIsGiven() {
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

            @Test
            public void shouldCreateNewObjectWhenMaxLengthDescriptionIsGiven() {
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

            @Test
            public void shouldCreateNewObjectWhenNoDescriptionIsGiven() {
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
        }
    }

    public class PrePersist {

        @Test
        public void shouldUseSameTimeAsCreationTimeAndModificationTime() {
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

    public class Update {

        public class WhenNewTitleIsInvalid {

            @Test(expected = NullPointerException.class)
            public void shouldThrowExceptionWhenNewTitleIsNull() {
                Todo updated = Todo.getBuilder()
                        .title(TITLE)
                        .build();

                updated.update(null, UPDATED_DESCRIPTION);
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenNewTitleIsEmpty() {
                Todo updated = Todo.getBuilder()
                        .title(TITLE)
                        .build();

                updated.update("", UPDATED_DESCRIPTION);
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenNewTitleIsTooLong() {
                String tooLongTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);

                Todo updated = Todo.getBuilder()
                        .title(TITLE)
                        .build();

                updated.update(tooLongTitle, UPDATED_DESCRIPTION);
            }
        }

        public class WhenNewDescriptionIsTooLong {
            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowException() {
                String tooLongDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);

                Todo updated = Todo.getBuilder()
                        .description(DESCRIPTION)
                        .title(TITLE)
                        .build();

                updated.update(UPDATED_TITLE, tooLongDescription);
            }
        }

        public class WhenNewTitleAndNewDescriptionAreValid {

            public class WhenMaxLengthTitleAndNewDescriptionAreGiven {

                @Test
                public void shouldUpdateTitle() {
                    String maxLengthTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE);

                    Todo updated = Todo.getBuilder()
                            .title(TITLE)
                            .build();

                    updated.update(maxLengthTitle, UPDATED_DESCRIPTION);

                    assertThatTodoEntry(updated)
                            .hasTitle(maxLengthTitle);
                }

                @Test
                public void shouldUpdateDescription() {
                    String maxLengthTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE);

                    Todo updated = Todo.getBuilder()
                            .title(TITLE)
                            .build();

                    updated.update(maxLengthTitle, UPDATED_DESCRIPTION);

                    assertThatTodoEntry(updated)
                            .hasDescription(UPDATED_DESCRIPTION);
                }
            }

            public class WhenNewTitleIsGivenAndNewDescriptionIsNull {

                @Test
                public void shouldUpdateTitle() {
                    Todo updated = Todo.getBuilder()
                            .description(DESCRIPTION)
                            .title(TITLE)
                            .build();

                    updated.update(UPDATED_TITLE, null);

                    assertThatTodoEntry(updated)
                            .hasTitle(UPDATED_TITLE);
                }

                @Test
                public void shouldRemoveDescription() {
                    Todo updated = Todo.getBuilder()
                            .description(DESCRIPTION)
                            .title(TITLE)
                            .build();

                    updated.update(UPDATED_TITLE, null);

                    assertThatTodoEntry(updated)
                            .hasNoDescription();
                }
            }

            public class WhenNewTitleIsGivenAndMaxLengthDescriptionAreGiven {

                @Test
                public void shouldUpdateTitle() {
                    String maxLengthDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

                    Todo updated = Todo.getBuilder()
                            .description(DESCRIPTION)
                            .title(TITLE)
                            .build();

                    updated.update(UPDATED_TITLE, maxLengthDescription);

                    assertThatTodoEntry(updated)
                            .hasTitle(UPDATED_TITLE);
                }

                @Test
                public void shouldUpdateDescription() {
                    String maxLengthDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

                    Todo updated = Todo.getBuilder()
                            .description(DESCRIPTION)
                            .title(TITLE)
                            .build();

                    updated.update(UPDATED_TITLE, maxLengthDescription);

                    assertThatTodoEntry(updated)
                            .hasDescription(maxLengthDescription);
                }
            }
        }
    }
}

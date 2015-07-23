package net.petrikainulainen.springdata.jpa.todo;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static net.petrikainulainen.springdata.jpa.todo.TodoAssert.assertThatTodoEntry;

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

            public class WhenTitleIsNull {

                @Test(expected = NullPointerException.class)
                public void shouldThrowException() {
                    Todo.getBuilder()
                            .title(null)
                            .description(DESCRIPTION)
                            .build();
                }
            }

            public class WhenTitleIsEmpty {

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    Todo.getBuilder()
                            .title("")
                            .description(DESCRIPTION)
                            .build();
                }
            }

            public class WhenTitleIsTooLong {

                private String tooLongTitle;

                @Before
                public void createTooLongTitle() {
                    tooLongTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                }

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    Todo.getBuilder()
                            .title(tooLongTitle)
                            .description(DESCRIPTION)
                            .build();
                }
            }
        }

        public class WhenDescriptionIsTooLong {

            private String tooLongDescription;

            @Before
            public void createTooLongDescription() {
                tooLongDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
            }


            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowException() {
                Todo.getBuilder()
                        .title(TITLE)
                        .description(tooLongDescription)
                        .build();
            }
        }

        public class WhenTitleAndDescriptionAreValid {

            @Test
            public void shouldNotSetId() {
                Todo build = Todo.getBuilder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .build();

                assertThatTodoEntry(build)
                        .hasNoId();
            }

            @Test
            public void shouldNotSetCreationAuditFieldValues() {
                Todo build = Todo.getBuilder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .build();

                assertThatTodoEntry(build)
                        .hasNoCreationAuditFieldValues();
            }

            @Test
            public void shouldNotSetModificationAuditFieldValues() {
                Todo build = Todo.getBuilder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .build();

                assertThatTodoEntry(build)
                        .hasNoModificationAuditFieldValues();
            }

            @Test
            public void shouldSetDescription() {
                Todo build = Todo.getBuilder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .build();

                assertThatTodoEntry(build)
                        .hasDescription(DESCRIPTION);
            }

            @Test
            public void shouldSetTitle() {
                Todo build = Todo.getBuilder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .build();

                assertThatTodoEntry(build)
                        .hasTitle(TITLE);
            }

            public class WhenMaxLengthTitleIsGiven {

                private String maxLengthTitle;

                @Before
                public void createMaxLengthTitle() {
                   maxLengthTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                }

                @Test
                public void shouldCreateNewObjectAndSetTitle() {
                    Todo build = Todo.getBuilder()
                            .title(maxLengthTitle)
                            .description(DESCRIPTION)
                            .build();

                    assertThatTodoEntry(build)
                            .hasTitle(maxLengthTitle);
                }
            }

            public class WhenMaxLengthDescriptionIsGiven {

                private String maxLengthDescription;

                @Before
                public void createMaxLengthDescription() {
                    maxLengthDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

                }

                @Test
                public void shouldCreateNewObjectAndSetDescription() {
                    Todo build = Todo.getBuilder()
                            .title(TITLE)
                            .description(maxLengthDescription)
                            .build();

                    assertThatTodoEntry(build)
                            .hasDescription(maxLengthDescription);
                }
            }

            public class WhenNoDescriptionIsGiven {

                @Test
                public void shouldCreateNewObjectWithoutDescription() {
                    Todo build = Todo.getBuilder()
                            .title(TITLE)
                            .build();

                    assertThatTodoEntry(build)
                            .hasNoDescription();
                }
            }
        }
    }

    public class Update {

        private Todo updated;

        @Before
        public void createUpdatedTodoEntry() {
            updated = Todo.getBuilder()
                    .description(DESCRIPTION)
                    .title(TITLE)
                    .build();
        }

        public class WhenNewTitleIsInvalid {

            public class WhenTitleIsNull {

                @Test(expected = NullPointerException.class)
                public void shouldThrowException() {
                    updated.update(null, UPDATED_DESCRIPTION);
                }
            }

            public class WhenTitleIsEmpty {

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    updated.update("", UPDATED_DESCRIPTION);
                }
            }

            public class WhenTitleIsTooLong {

                private String tooLongTitle;

                @Before
                public void createTooLongTitle() {
                    tooLongTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                }

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    updated.update(tooLongTitle, UPDATED_DESCRIPTION);
                }
            }
        }

        public class WhenNewDescriptionIsTooLong {

            private String tooLongDescription;

            @Before
            public void createTooLongDescription() {
                tooLongDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);

            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowException() {
                updated.update(UPDATED_TITLE, tooLongDescription);
            }
        }

        public class WhenNewTitleAndNewDescriptionAreValid {

            public class WhenMaxLengthTitleAndNewDescriptionAreGiven {

                private String maxLengthTitle;

                @Before
                public void createMaxLengthTitle() {
                    maxLengthTitle = TestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                }

                @Test
                public void shouldUpdateTitle() {
                    updated.update(maxLengthTitle, UPDATED_DESCRIPTION);

                    assertThatTodoEntry(updated)
                            .hasTitle(maxLengthTitle);
                }

                @Test
                public void shouldUpdateDescription() {
                    updated.update(maxLengthTitle, UPDATED_DESCRIPTION);

                    assertThatTodoEntry(updated)
                            .hasDescription(UPDATED_DESCRIPTION);
                }
            }

            public class WhenNewTitleIsGivenAndNewDescriptionIsNull {

                @Test
                public void shouldUpdateTitle() {
                    updated.update(UPDATED_TITLE, null);

                    assertThatTodoEntry(updated)
                            .hasTitle(UPDATED_TITLE);
                }

                @Test
                public void shouldRemoveDescription() {
                    updated.update(UPDATED_TITLE, null);

                    assertThatTodoEntry(updated)
                            .hasNoDescription();
                }
            }

            public class WhenNewTitleAndMaxLengthDescriptionAreGiven {

                private String maxLengthDescription;

                @Before
                public void createMaxLengthDescription() {
                    maxLengthDescription = TestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                }

                @Test
                public void shouldUpdateTitle() {
                    updated.update(UPDATED_TITLE, maxLengthDescription);

                    assertThatTodoEntry(updated)
                            .hasTitle(UPDATED_TITLE);
                }

                @Test
                public void shouldUpdateDescription() {
                    updated.update(UPDATED_TITLE, maxLengthDescription);

                    assertThatTodoEntry(updated)
                            .hasDescription(maxLengthDescription);
                }
            }
        }
    }
}

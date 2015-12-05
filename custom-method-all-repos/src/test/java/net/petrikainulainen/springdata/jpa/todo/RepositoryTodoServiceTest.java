package net.petrikainulainen.springdata.jpa.todo;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static net.petrikainulainen.springdata.jpa.todo.TodoAssert.assertThatTodoEntry;
import static net.petrikainulainen.springdata.jpa.todo.TodoDTOAssert.assertThatTodoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class RepositoryTodoServiceTest {

    private static final String CREATED_BY_USER = "createdByUser";
    private static final String CREATION_TIME = "2014-12-24T22:28:39+02:00";
    private static final String DESCRIPTION = "description";
    private static final Long ID = 20L;
    private static final String MODIFIED_BY_USER = "modifiedByUser";
    private static final String MODIFICATION_TIME = "2014-12-24T22:29:05+02:00";
    private static final String TITLE = "title";

    private static final String UPDATED_DESCRIPTION = "updatedDescription";
    private static final String UPDATED_TITLE = "updatedTitle";

    private TodoRepository repository;

    private RepositoryTodoService service;

    @Before
    public void setUp() {
        repository = mock(TodoRepository.class);
        service = new RepositoryTodoService(repository);
    }

    public class Create {

        @Before
        public void returnNewTodoEntry() {
            given(repository.save(isA(Todo.class))).willAnswer(
                    invocationOnMock -> new TodoBuilder()
                            .createdByUser(CREATED_BY_USER)
                            .creationTime(CREATION_TIME)
                            .description(DESCRIPTION)
                            .id(ID)
                            .modifiedByUser(MODIFIED_BY_USER)
                            .modificationTime(MODIFICATION_TIME)
                            .title(TITLE)
                            .build()
            );
        }

        @Test
        public void shouldPersistNewTodoEntryWithCorrectInformation() {
            TodoDTO newTodoEntry = new TodoDTOBuilder()
                    .description(DESCRIPTION)
                    .title(TITLE)
                    .build();

            service.create(newTodoEntry);

            verify(repository, times(1)).save(
                    assertArg(persisted ->  assertThatTodoEntry(persisted)
                            .hasNoCreationAuditFieldValues()
                            .hasDescription(DESCRIPTION)
                            .hasNoId()
                            .hasNoModificationAuditFieldValues()
                            .hasTitle(TITLE)
                    )
            );
            verifyNoMoreInteractions(repository);
        }

        @Test
        public void shouldReturnTheInformationOfPersistedTodoEntry() {
            TodoDTO newTodoEntry = new TodoDTOBuilder()
                    .description(DESCRIPTION)
                    .title(TITLE)
                    .build();

            TodoDTO created = service.create(newTodoEntry);
            assertThatTodoDTO(created)
                    .hasDescription(DESCRIPTION)
                    .hasId(ID)
                    .hasTitle(TITLE)
                    .wasCreatedAt(CREATION_TIME)
                    .wasCreatedByUser(CREATED_BY_USER)
                    .wasModifiedAt(MODIFICATION_TIME)
                    .wasModifiedByUser(MODIFIED_BY_USER);
        }
    }

    public class Delete {

        public class WhenTodoEntryIsNotFound {

            @Before
            public void returnNoTodoEntry() {
                given(repository.deleteById(ID)).willReturn(Optional.empty());

            }

            @Test
            public void shouldThrowExceptionWithCorrectId() {
                Throwable thrown = catchThrowable(() -> service.delete(ID));

                assertThat(thrown).isExactlyInstanceOf(TodoNotFoundException.class);

                TodoNotFoundException ex = (TodoNotFoundException) thrown;
                assertThat(ex.getId()).isEqualTo(ID);
            }
        }

        public class WhenTodoEntryIsFound {

            private Todo deleted;

            @Before
            public void returnDeletedTodoEntry() {
                deleted = new TodoBuilder()
                        .createdByUser(CREATED_BY_USER)
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .id(ID)
                        .modifiedByUser(MODIFIED_BY_USER)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build();

                given(repository.deleteById(ID)).willReturn(Optional.of(deleted));
            }

            @Test
            public void shouldDeleteFoundTodoEntry() {
                service.delete(ID);

                verify(repository, times(1)).deleteById(ID);
            }

            @Test
            public void shouldReturnTheInformationOfDeletedTodoEntry() {
                TodoDTO deleted = service.delete(ID);

                assertThatTodoDTO(deleted)
                        .hasDescription(DESCRIPTION)
                        .hasId(ID)
                        .hasTitle(TITLE)
                        .wasCreatedAt(CREATION_TIME)
                        .wasCreatedByUser(CREATED_BY_USER)
                        .wasModifiedAt(MODIFICATION_TIME)
                        .wasModifiedByUser(MODIFIED_BY_USER);
            }
        }
    }

    public class FindAll {

        public class WhenNoTodoEntryAreFound {

            @Before
            public void returnNoTodoEntries() {
                given(repository.findAll()).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldReturnEmptyList() {
                List<TodoDTO> todoEntries = service.findAll();

                assertThat(todoEntries).isEmpty();
            }
        }

        public class WhenOneTodoEntryIsFound {

            @Before
            public void returnOneTodoEntry() {
                Todo found = new TodoBuilder()
                        .id(ID)
                        .createdByUser(CREATED_BY_USER)
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .modifiedByUser(MODIFIED_BY_USER)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build();

                given(repository.findAll()).willReturn(Arrays.asList(found));
            }

            @Test
            public void shouldReturnOneTodoEntry() {
                List<TodoDTO> todoEntries = service.findAll();

                assertThat(todoEntries).hasSize(1);
            }

            @Test
            public void shouldReturnInformationOfFoundTodoEntry() {
                TodoDTO todoEntry = service.findAll().get(0);

                assertThatTodoDTO(todoEntry)
                        .hasId(ID)
                        .hasTitle(TITLE)
                        .hasDescription(DESCRIPTION)
                        .wasCreatedAt(CREATION_TIME)
                        .wasCreatedByUser(CREATED_BY_USER)
                        .wasModifiedAt(MODIFICATION_TIME)
                        .wasModifiedByUser(MODIFIED_BY_USER);
            }
        }
    }

    public class FindOne {

        public class WhenTodoEntryIsNotFound {

            @Before
            public void returnNoTodoEntry() {
                given(repository.findOne(ID)).willReturn(Optional.empty());
            }

            @Test
            public void shouldThrowExceptionWithCorrectId() {
                Throwable thrown = catchThrowable(() -> service.findById(ID));

                assertThat(thrown).isExactlyInstanceOf(TodoNotFoundException.class);

                TodoNotFoundException exception = (TodoNotFoundException) thrown;
                assertThat(exception.getId()).isEqualTo(ID);
            }
        }

        public class WhenTodoEntryIsFound {

            @Before
            public void returnFoundTodoEntry() {
                Todo found = new TodoBuilder()
                        .id(ID)
                        .createdByUser(CREATED_BY_USER)
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .modifiedByUser(MODIFIED_BY_USER)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build();

                given(repository.findOne(ID)).willReturn(Optional.of(found));
            }

            @Test
            public void shouldReturnInformationOfFoundTodoEntry() {
                TodoDTO returned = service.findById(ID);

                assertThatTodoDTO(returned)
                        .hasDescription(DESCRIPTION)
                        .hasId(ID)
                        .hasTitle(TITLE)
                        .wasCreatedAt(CREATION_TIME)
                        .wasCreatedByUser(CREATED_BY_USER)
                        .wasModifiedAt(MODIFICATION_TIME)
                        .wasModifiedByUser(MODIFIED_BY_USER);
            }
        }
    }

    public class Update {

        public class WhenTodoEntryIsNotFound {

            @Before
            public void returnNoTodoEntry() {
                given(repository.findOne(ID)).willReturn(Optional.empty());
            }

            @Test
            public void shouldThrowExceptionWithCorrectId() {
                TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                        .id(ID)
                        .build();

                Throwable thrown = catchThrowable(() -> service.update(updatedTodoEntry));

                assertThat(thrown).isExactlyInstanceOf(TodoNotFoundException.class);

                TodoNotFoundException exception = (TodoNotFoundException) thrown;
                assertThat(exception.getId()).isEqualTo(ID);
            }
        }

        public class WhenTodoEntryIsFound {

            private Todo updated;

            @Before
            public void returnUpdatedTodoEntry() {
                updated = new TodoBuilder()
                        .createdByUser(CREATED_BY_USER)
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .id(ID)
                        .modifiedByUser(MODIFIED_BY_USER)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build();

                given(repository.findOne(ID)).willReturn(Optional.of(updated));
            }

            @Test
            public void shouldUpdateTitleAndDescription() {
                TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                        .id(ID)
                        .description(UPDATED_DESCRIPTION)
                        .title(UPDATED_TITLE)
                        .build();

                service.update(updatedTodoEntry);

                assertThatTodoEntry(updated)
                        .hasDescription(UPDATED_DESCRIPTION)
                        .hasTitle(UPDATED_TITLE);
            }

            @Test
            public void shouldNotUpdateIdOrAuditInformation() {
                TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                        .id(ID)
                        .description(UPDATED_DESCRIPTION)
                        .title(UPDATED_TITLE)
                        .build();

                service.update(updatedTodoEntry);

                assertThatTodoEntry(updated)
                        .hasId(ID)
                        .wasCreatedAt(CREATION_TIME)
                        .wasCreatedByUser(CREATED_BY_USER)
                        .wasModifiedAt(MODIFICATION_TIME)
                        .wasModifiedByUser(MODIFIED_BY_USER);
            }

            @Test
            public void shouldReturnInformationOfUpdatedTodoEntry() {
                TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                        .id(ID)
                        .description(UPDATED_DESCRIPTION)
                        .title(UPDATED_TITLE)
                        .build();

                TodoDTO returnedTodoEntry = service.update(updatedTodoEntry);

                assertThatTodoDTO(returnedTodoEntry)
                        .hasDescription(UPDATED_DESCRIPTION)
                        .hasId(ID)
                        .hasTitle(UPDATED_TITLE)
                        .wasCreatedAt(CREATION_TIME)
                        .wasCreatedByUser(CREATED_BY_USER)
                        .wasModifiedAt(MODIFICATION_TIME)
                        .wasModifiedByUser(MODIFIED_BY_USER);
            }
        }
    }
}

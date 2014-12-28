package net.petrikainulainen.springdata.jpa.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.petrikainulainen.springdata.jpa.common.ThrowableCaptor.thrown;
import static net.petrikainulainen.springdata.jpa.todo.TodoAssert.assertThatTodoEntry;
import static net.petrikainulainen.springdata.jpa.todo.TodoDTOAssert.assertThatTodoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Petri Kainulainen
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryTodoServiceTest {

    private static final String CREATION_TIME = "2014-12-24T22:28:39+02:00";
    private static final String DESCRIPTION = "description";
    private static final Long ID = 20L;
    private static final String MODIFICATION_TIME = "2014-12-24T22:29:05+02:00";
    private static final String TITLE = "title";

    private static final String UPDATED_DESCRIPTION = "updatedDescription";
    private static final String UPDATED_TITLE = "updatedTitle";

    @Mock
    private TodoRepository repository;

    private RepositoryTodoService service;

    @Before
    public void setUp() {
        service = new RepositoryTodoService(repository);
    }

    @Test
    public void create_ShouldPersistNewTodoEntry() {
        given(repository.save(isA(Todo.class))).willAnswer(
                invocationOnMock -> invocationOnMock.getArguments()[0]
        );

        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(DESCRIPTION)
                .title(TITLE)
                .build();

        service.create(newTodoEntry);

        ArgumentCaptor<Todo> persistedArgument = ArgumentCaptor.forClass(Todo.class);
        verify(repository, times(1)).save(persistedArgument.capture());
        verifyNoMoreInteractions(repository);

        Todo persisted = persistedArgument.getValue();
        assertThatTodoEntry(persisted)
                .hasNoCreationTime()
                .hasDescription(DESCRIPTION)
                .hasNoId()
                .hasNoModificationTime()
                .hasTitle(TITLE);
    }

    @Test
    public void create_ShouldReturnInformationOfPersistedTodoEntry() {
        given(repository.save(isA(Todo.class))).willAnswer(
                invocationOnMock -> new TodoBuilder()
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .id(ID)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build()
        );

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
                .wasModifiedAt(MODIFICATION_TIME);
    }

    @Test
    public void findAll_NoTodoEntriesFound_ShouldReturnEmptyList() {
        given(repository.findAll()).willReturn(new ArrayList<>());

        List<TodoDTO> todoEntries = service.findAll();

        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findAll_OneTodoEntryFound_ShouldReturnInformationOfFoundTodoEntry() {
        Todo found = new TodoBuilder()
                .id(ID)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .modificationTime(MODIFICATION_TIME)
                .title(TITLE)
                .build();

        given(repository.findAll()).willReturn(Arrays.asList(found));

        List<TodoDTO> todoEntries = service.findAll();

        assertThat(todoEntries).hasSize(1);
        TodoDTO todoEntry = todoEntries.iterator().next();

        assertThatTodoDTO(todoEntry)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .wasCreatedAt(CREATION_TIME)
                .wasModifiedAt(MODIFICATION_TIME);
    }

    @Test
    public void findOne_TodoEntryNotFound_ShouldThrowExceptionWithCorrectId() {
        given(repository.findOne(ID)).willReturn(Optional.empty());

        Throwable thrown = thrown(() -> service.findById(ID));

        assertThat(thrown).isExactlyInstanceOf(TodoNotFoundException.class);

        TodoNotFoundException exception = (TodoNotFoundException) thrown;
        assertThat(exception.getId()).isEqualTo(ID);
    }

    @Test
    public void findOne_TodoEntryIsFound_ShouldReturnTheInformationOfFoundTodoEntry() {
        Todo found = new TodoBuilder()
                .id(ID)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .modificationTime(MODIFICATION_TIME)
                .title(TITLE)
                .build();

        given(repository.findOne(ID)).willReturn(Optional.of(found));

        TodoDTO returned = service.findById(ID);

        assertThatTodoDTO(returned)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME)
                .wasModifiedAt(MODIFICATION_TIME);
    }

    @Test
    public void update_TodoEntryIsNotFound_ShouldThrowExceptionWithCorrectId() {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(ID)
                .build();

        given(repository.findOne(ID)).willReturn(Optional.empty());

        Throwable thrown = thrown(() -> service.update(updatedTodoEntry));

        assertThat(thrown).isExactlyInstanceOf(TodoNotFoundException.class);

        TodoNotFoundException exception = (TodoNotFoundException) thrown;
        assertThat(exception.getId()).isEqualTo(ID);
    }

    @Test
    public void update_TodoEntryIsFound_ShouldUpdateOnlyTitleAndDescription() {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(ID)
                .description(UPDATED_DESCRIPTION)
                .title(UPDATED_TITLE)
                .build();

        Todo updated = new TodoBuilder()
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .title(TITLE)
                .build();

        given(repository.findOne(ID)).willReturn(Optional.of(updated));

        service.update(updatedTodoEntry);

        assertThatTodoEntry(updated)
                .hasDescription(UPDATED_DESCRIPTION)
                .hasId(ID)
                .hasTitle(UPDATED_TITLE)
                .wasCreatedAt(CREATION_TIME)
                .wasModifiedAt(MODIFICATION_TIME);
    }

    @Test
    public void update_TodoEntryIsFound_ShouldReturnTheInformationOfUpdatedTodoEntry() {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(ID)
                .description(UPDATED_DESCRIPTION)
                .title(UPDATED_TITLE)
                .build();

        Todo updated = new TodoBuilder()
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .title(TITLE)
                .build();

        given(repository.findOne(ID)).willReturn(Optional.of(updated));

        TodoDTO returnedTodoEntry = service.update(updatedTodoEntry);

        assertThatTodoDTO(returnedTodoEntry)
                .hasDescription(UPDATED_DESCRIPTION)
                .hasId(ID)
                .hasTitle(UPDATED_TITLE)
                .wasCreatedAt(CREATION_TIME)
                .wasModifiedAt(MODIFICATION_TIME);
    }
}

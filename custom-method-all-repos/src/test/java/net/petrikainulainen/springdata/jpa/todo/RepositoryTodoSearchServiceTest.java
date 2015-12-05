package net.petrikainulainen.springdata.jpa.todo;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.petrikainulainen.springdata.jpa.todo.TodoDTOAssert.assertThatTodoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class RepositoryTodoSearchServiceTest {

    private static final String SEARCH_TERM = "itl";

    private TodoRepository repository;
    private RepositoryTodoSearchService service;

    @Before
    public void setUp() {
        repository = mock(TodoRepository.class);
        service = new RepositoryTodoSearchService(repository);
    }

    public class FindBySearchTerm {

        public class WhenNoTodoEntriesAreFound {

            @Before
            public void returnZeroTodoEntries() {
                given(repository.findBySearchTerm(SEARCH_TERM)).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldReturnEmptyList() {
                List<TodoDTO> searchResults = service.findBySearchTerm(SEARCH_TERM);
                assertThat(searchResults).isEmpty();
            }
        }

        public class WhenOneTodoEntryIsFound {

            private final String CREATED_BY_USER = "createdByUser";
            private final String CREATION_TIME = "2014-12-24T22:28:39+02:00";
            private final String DESCRIPTION = "description";
            private final Long ID = 20L;
            private final String MODIFIED_BY_USER = "modifiedByUser";
            private final String MODIFICATION_TIME = "2014-12-24T22:29:05+02:00";
            private final String TITLE = "title";

            @Before
            public void returnOneTodoEntry() {
                Todo found = new TodoBuilder()
                        .createdByUser(CREATED_BY_USER)
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .id(ID)
                        .modifiedByUser(MODIFIED_BY_USER)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build();

                given(repository.findBySearchTerm(SEARCH_TERM)).willReturn(Arrays.asList(found));
            }

            @Test
            public void shouldReturnOneTodoEntry() {
                List<TodoDTO> searchResults = service.findBySearchTerm(SEARCH_TERM);
                assertThat(searchResults).hasSize(1);
            }

            @Test
            public void shouldReturnTheInformationOfOneTodoEntry() {
                TodoDTO found = service.findBySearchTerm(SEARCH_TERM).get(0);

                assertThatTodoDTO(found)
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
}

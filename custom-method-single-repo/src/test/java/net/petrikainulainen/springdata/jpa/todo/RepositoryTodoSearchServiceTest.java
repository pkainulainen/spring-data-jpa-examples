package net.petrikainulainen.springdata.jpa.todo;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                List<TodoSearchResultDTO> searchResults = service.findBySearchTerm(SEARCH_TERM);
                assertThat(searchResults).isEmpty();
            }
        }

        public class WhenOneTodoEntryIsFound {

            private final Long ID = 20L;
            private final String TITLE = "title";

            @Before
            public void returnOneTodoEntry() {
                TodoSearchResultDTO found = new TodoSearchResultDTO();
                found.setId(ID);
                found.setTitle(TITLE);

                given(repository.findBySearchTerm(SEARCH_TERM)).willReturn(Arrays.asList(found));
            }

            @Test
            public void shouldReturnOneTodoEntry() {
                List<TodoSearchResultDTO> searchResults = service.findBySearchTerm(SEARCH_TERM);
                assertThat(searchResults).hasSize(1);
            }

            @Test
            public void shouldReturnTheInformationOfOneTodoEntry() {
                TodoSearchResultDTO found = service.findBySearchTerm(SEARCH_TERM).get(0);

                assertThat(found.getId()).isEqualTo(ID);
                assertThat(found.getTitle()).isEqualTo(TITLE);
            }
        }
    }
}

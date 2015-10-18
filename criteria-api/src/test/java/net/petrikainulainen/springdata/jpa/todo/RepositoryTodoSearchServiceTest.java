package net.petrikainulainen.springdata.jpa.todo;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.PageBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;

import static net.petrikainulainen.springdata.jpa.todo.TodoDTOAssert.assertThatTodoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
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

        private final int PAGE_NUMBER = 1;
        private final int PAGE_SIZE = 5;
        private final String SORT_PROPERTY = "title";

        private Pageable pageRequest;

        @Before
        public void createPageRequest() {
            Sort sort = new Sort(Sort.Direction.ASC, SORT_PROPERTY);
            pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, sort);

            Page<Todo> emptyPage = new PageBuilder<Todo>()
                    .elements(new ArrayList<>())
                    .pageRequest(pageRequest)
                    .totalElements(0)
                    .build();
            given(repository.findAll(isA(Specification.class), eq(pageRequest))).willReturn(emptyPage);
        }

        @Test
        public void shouldReturnPageWithRequestedPageNumber() {
            Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
            assertThat(searchResultPage.getNumber()).isEqualTo(PAGE_NUMBER);
        }

        @Test
        public void shouldReturnPageWithRequestedPageSize() {
            Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
            assertThat(searchResultPage.getSize()).isEqualTo(PAGE_SIZE);
        }

        @Test
        public void shouldReturnPageThatIsSortedInAscendingOrderByUsingSortProperty() {
            Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
            assertThat(searchResultPage.getSort().getOrderFor(SORT_PROPERTY).getDirection())
                    .isEqualTo(Sort.Direction.ASC);
        }

        public class WhenNoTodoEntriesAreFound {

            @Before
            public void returnZeroTodoEntries() {
                Page<Todo> emptyPage = new PageBuilder<Todo>()
                        .elements(new ArrayList<>())
                        .pageRequest(pageRequest)
                        .totalElements(0)
                        .build();
                given(repository.findAll(isA(Specification.class), eq(pageRequest))).willReturn(emptyPage);
            }

            @Test
            public void shouldReturnEmptyPage() {
                Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
                assertThat(searchResultPage).isEmpty();
            }

            @Test
            public void shouldReturnPageWithTotalElementCountZero() {
                Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
                assertThat(searchResultPage.getTotalElements()).isEqualTo(0);
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

                Page<Todo> resultPage = new PageBuilder<Todo>()
                        .elements(Arrays.asList(found))
                        .pageRequest(pageRequest)
                        .totalElements(1)
                        .build();

                given(repository.findAll(isA(Specification.class), eq(pageRequest))).willReturn(resultPage);
            }

            @Test
            public void shouldReturnPageThatHasOneTodoEntry() {
                Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
                assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);
            }

            @Test
            public void shouldReturnPageThatHasCorrectInformation() {
                TodoDTO found = service.findBySearchTerm(SEARCH_TERM, pageRequest).getContent().get(0);

                assertThatTodoDTO(found)
                        .hasId(ID)
                        .hasTitle(TITLE)
                        .hasDescription(DESCRIPTION)
                        .wasCreatedAt(CREATION_TIME)
                        .wasCreatedByUser(CREATED_BY_USER)
                        .wasModifiedAt(MODIFICATION_TIME)
                        .wasModifiedByUser(MODIFIED_BY_USER);
            }

            @Test
            public void shouldReturnPageWithTotalElementCountOne() {
                Page<TodoDTO> searchResultPage = service.findBySearchTerm(SEARCH_TERM, pageRequest);
                assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
            }
        }
    }
}

package net.petrikainulainen.springdata.jpa.web;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.PageBuilder;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import net.petrikainulainen.springdata.jpa.todo.TodoDTOBuilder;
import net.petrikainulainen.springdata.jpa.todo.TodoSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class TodoSearchControllerTest {

    private MockMvc mockMvc;

    private TodoSearchService searchService;

    @Before
    public void setUp() {
        searchService = mock(TodoSearchService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new TodoSearchController(searchService))
                .setMessageConverters(WebTestConfig.jacksonDateTimeConverter())
                .setCustomArgumentResolvers(WebTestConfig.pageRequestArgumentResolver())
                .build();
    }

    public class FindBySearchTerm {

        private final int PAGE_NUMBER = 1;
        private final String PAGE_NUMBER_STRING = "1";
        private final int PAGE_SIZE = 5;
        private final String PAGE_SIZE_STRING = "5";
        private final String SEARCH_TERM = "itl";

        private Pageable pageRequest;

        @Before
        public void setUp() {
            Sort sort = new Sort(Sort.Direction.ASC, WebTestConstants.FIELD_NAME_TITLE);
            pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, sort);

            Page<TodoDTO> emptyPage = new PageBuilder<TodoDTO>()
                    .elements(new ArrayList<>())
                    .pageRequest(pageRequest)
                    .totalElements(0)
                    .build();
            given(searchService.findBySearchTerm(eq(SEARCH_TERM), isA(Pageable.class))).willReturn(emptyPage);
        }

        @Test
        public void shouldReturnHttpResponseStatusOk() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            )
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldReturnPageNumberAndPageSizeAsJson() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            )
                    .andExpect(jsonPath("$.number", is(PAGE_NUMBER)))
                    .andExpect(jsonPath("$.size", is(PAGE_SIZE)));
        }

        @Test
        public void shouldReturnSortInformationAsJson() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            )
                    .andExpect(jsonPath("$.sort[*].direction[0]", is(WebTestConstants.SORT_DIRECTION_ASC)))
                    .andExpect(jsonPath("$.sort[*].property[0]", is(WebTestConstants.FIELD_NAME_TITLE)));
        }

        @Test
        public void shouldReturnPTotalElementInformationAsJson() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            )
                    .andExpect(jsonPath("$.totalElements", is(0)));
        }

        @Test
        public void shouldPassSearchTermForwardToSearchService() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            );

            verify(searchService, times(1)).findBySearchTerm(eq(SEARCH_TERM), isA(Pageable.class));
        }

        @Test
        public void shouldPassPageSizeAndNumberForwardToSearchService() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            );

            verify(searchService, times(1)).findBySearchTerm(isA(String.class), assertArg(
                    pageRequest -> {
                        assertThat(pageRequest.getPageNumber()).isEqualTo(PAGE_NUMBER);
                        assertThat(pageRequest.getPageSize()).isEqualTo(PAGE_SIZE);
                    }
            ));
        }

        @Test
        public void shouldPassSortForwardToSearchService() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, PAGE_NUMBER_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                            .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
            );

            verify(searchService, times(1)).findBySearchTerm(isA(String.class), assertArg(
                            pageRequest -> assertThat(
                                    pageRequest.getSort().getOrderFor(WebTestConstants.FIELD_NAME_TITLE).getDirection())
                                    .isEqualTo(Sort.Direction.ASC)
                    )
            );
        }

        public class WhenNoTodoEntriesAreFound {

            @Before
            public void returnEmptyPage() {
                Sort sort = new Sort(Sort.Direction.ASC, WebTestConstants.FIELD_NAME_TITLE);
                pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, sort);

                Page<TodoDTO> emptyPage = new PageBuilder<TodoDTO>()
                        .elements(new ArrayList<>())
                        .pageRequest(pageRequest)
                        .totalElements(0)
                        .build();
                given(searchService.findBySearchTerm(eq(SEARCH_TERM), isA(Pageable.class))).willReturn(emptyPage);
            }

            @Test
            public void shouldReturnPageAsJson() throws Exception {
                mockMvc.perform(get("/api/todo/search")
                                .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                                .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
                )
                        .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.content", hasSize(0)))
                        .andExpect(jsonPath("$.totalElements", is(0)));
            }
        }


        public class WhenOneTodoEntryIsFound {

            private final Long ID= 1L;
            private final String CREATED_BY_USER = "createdByUser";
            private final String CREATION_TIME = "2014-12-24T22:28:39+02:00";
            private final String DESCRIPTION = "description";
            private final String MODIFIED_BY_USER = "modifiedByUser";
            private final String MODIFICATION_TIME = "2014-12-24T14:28:39+02:00";
            private final String TITLE = "title";

            @Before
            public void returnOneTodoEntry() {
                TodoDTO found = new TodoDTOBuilder()
                        .id(ID)
                        .createdByUser(CREATED_BY_USER)
                        .creationTime(CREATION_TIME)
                        .description(DESCRIPTION)
                        .modifiedByUser(MODIFIED_BY_USER)
                        .modificationTime(MODIFICATION_TIME)
                        .title(TITLE)
                        .build();

                Sort sort = new Sort(Sort.Direction.ASC, WebTestConstants.FIELD_NAME_TITLE);
                pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, sort);

                Page<TodoDTO> pageWithOneTodoEntry = new PageBuilder<TodoDTO>()
                        .elements(Arrays.asList(found))
                        .pageRequest(pageRequest)
                        .totalElements(1)
                        .build();
                given(searchService.findBySearchTerm(eq(SEARCH_TERM), isA(Pageable.class))).willReturn(pageWithOneTodoEntry);
            }

            @Test
            public void shouldReturnOneTodoEntryAsJson() throws Exception {
                mockMvc.perform(get("/api/todo/search")
                                .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                                .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
                )
                        .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.content", hasSize(1)))
                        .andExpect(jsonPath("$.content[0].id", is(ID.intValue())))
                        .andExpect(jsonPath("$.content[0].createdByUser", is(CREATED_BY_USER)))
                        .andExpect(jsonPath("$.content[0].creationTime", is(CREATION_TIME)))
                        .andExpect(jsonPath("$.content[0].description", is(DESCRIPTION)))
                        .andExpect(jsonPath("$.content[0].modifiedByUser", is(MODIFIED_BY_USER)))
                        .andExpect(jsonPath("$.content[0].modificationTime", is(MODIFICATION_TIME)))
                        .andExpect(jsonPath("$.content[0].title", is(TITLE)));
            }
        }
    }
}

package net.petrikainulainen.springdata.jpa.web;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.todo.TodoSearchResultDTO;
import net.petrikainulainen.springdata.jpa.todo.TodoSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
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

    private static final String SEARCH_TERM = "itl";

    private MockMvc mockMvc;

    private TodoSearchService searchService;

    @Before
    public void setUp() {
        searchService = mock(TodoSearchService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new TodoSearchController(searchService))
                .setMessageConverters(WebTestConfig.jacksonDateTimeConverter())
                .setCustomArgumentResolvers(WebTestConfig.sortArgumentResolver())
                .build();
    }

    public class FindBySearchTerm {

        @Test
        public void shouldReturnHttpResponseStatusOk() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
            )
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldPassSearchTermForwardToSearchService() throws Exception {
            mockMvc.perform(get("/api/todo/search")
                            .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
            );

            verify(searchService, times(1)).findBySearchTerm(eq(SEARCH_TERM));
        }

        public class WhenNoTodoEntriesAreFound {

            @Before
            public void returnZeroTodoEntries() {
                given(searchService.findBySearchTerm(SEARCH_TERM)).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldReturnEmptyListAsJson() throws Exception {
                mockMvc.perform(get("/api/todo/search")
                                .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$", hasSize(0)));
            }
        }

        public class WhenOneTodoEntryIsFound {

            private final Long ID= 1L;
            private final String TITLE = "title";

            @Before
            public void returnOneTodoEntry() {
                TodoSearchResultDTO found = new TodoSearchResultDTO();
                found.setId(ID);
                found.setTitle(TITLE);

                given(searchService.findBySearchTerm(SEARCH_TERM)).willReturn(Arrays.asList(found));
            }

            @Test
            public void shouldReturnOneTodoEntryAsJson() throws Exception {
                mockMvc.perform(get("/api/todo/search")
                                .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].id", is(ID.intValue())))
                        .andExpect(jsonPath("$[0].title", is(TITLE)));
            }
        }
    }
}

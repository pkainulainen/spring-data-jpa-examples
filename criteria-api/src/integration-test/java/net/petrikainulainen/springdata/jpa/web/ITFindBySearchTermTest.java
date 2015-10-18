package net.petrikainulainen.springdata.jpa.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.petrikainulainen.springdata.jpa.TodoConstants;
import net.petrikainulainen.springdata.jpa.config.ExampleApplicationContext;
import net.petrikainulainen.springdata.jpa.config.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
@DbUnitConfiguration(dataSetLoader = ColumnSensingReplacementDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@WebAppConfiguration
@DatabaseSetup("two-todo-entries.xml")
public class ITFindBySearchTermTest {

    private static final int FIRST_PAGE = 0;
    private static final String FIRST_PAGE_STRING = "0";

    private static final int PAGE_SIZE = 1;
    private static final String PAGE_SIZE_STRING = "1";

    private static final String SEARCH_TERM = "tIo";
    private static final int SECOND_PAGE = 1;
    private static final String SECOND_PAGE_STRING = "1";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }

    /* Response status tests */
    @Test
    public void findBySearchTerm_AsAnonymous_ShouldReturnHttpResponseStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenNoTodoEntriesAreFoundWithSearchTerm_ShouldReturnHttpResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_NO_MATCH)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTodoEntriesAreFoundWithSearchTerm_ShouldReturnHttpResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(status().isOk());
    }


    /* No results found */
    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenNoTodoEntriesAreFoundWithSearchTerm_ShouldReturnAnEmptyPageAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_NO_MATCH)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.numberOfElements", is(0)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenNoTodoEntriesAreFoundWithSearchTerm_ShouldReturnAnPageThatHasZeroTotalElementsAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_NO_MATCH)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    /* One todo entry found */
    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenDescriptionOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnPageThatHasOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.numberOfElements", is(1)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenDescriptionOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnPageThatHasOneTotalElementAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenDescriptionOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnTheFoundTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].createdByUser", is(TodoConstants.TodoEntries.First.CREATED_BY_USER)))
                .andExpect(jsonPath("$.content[0].creationTime", is(TodoConstants.TodoEntries.First.CREATION_TIME)))
                .andExpect(jsonPath("$.content[0].description", is(TodoConstants.TodoEntries.First.DESCRIPTION)))
                .andExpect(jsonPath("$.content[0].id", is(TodoConstants.TodoEntries.First.ID.intValue())))
                .andExpect(jsonPath("$.content[0].modifiedByUser", is(TodoConstants.TodoEntries.First.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$.content[0].modificationTime", is(TodoConstants.TodoEntries.First.MODIFICATION_TIME)))
                .andExpect(jsonPath("$.content[0].title", is(TodoConstants.TodoEntries.First.TITLE)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTitleOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnPageThatHasOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.numberOfElements", is(1)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTitleOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnPageThatHasOneTotalElementsAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTitleOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnTheFoundTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].createdByUser", is(TodoConstants.TodoEntries.First.CREATED_BY_USER)))
                .andExpect(jsonPath("$.content[0].creationTime", is(TodoConstants.TodoEntries.First.CREATION_TIME)))
                .andExpect(jsonPath("$.content[0].description", is(TodoConstants.TodoEntries.First.DESCRIPTION)))
                .andExpect(jsonPath("$.content[0].id", is(TodoConstants.TodoEntries.First.ID.intValue())))
                .andExpect(jsonPath("$.content[0].modifiedByUser", is(TodoConstants.TodoEntries.First.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$.content[0].modificationTime", is(TodoConstants.TodoEntries.First.MODIFICATION_TIME)))
                .andExpect(jsonPath("$.content[0].title", is(TodoConstants.TodoEntries.First.TITLE)));
    }

    /* Pagination tests */
    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndFirstPageIsRequestedWithPageSizeOne_ShouldReturnPageThatHasOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, FIRST_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.numberOfElements", is(1)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndFirstPageIsRequestedWithPageSizeOne_ShouldReturnPageThatHasTwoTotalElementsAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, FIRST_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndFirstPageIsRequestedWithPageSizeOne_ShouldReturnFirstPageJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, FIRST_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.number", is(FIRST_PAGE)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.last", is(false)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndFirstPageIsRequestedWithPageSizeOne_ShouldSortTodoEntriesByTitleAscAndReturnSecondTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, FIRST_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].createdByUser", is(TodoConstants.TodoEntries.Second.CREATED_BY_USER)))
                .andExpect(jsonPath("$.content[0].creationTime", is(TodoConstants.TodoEntries.Second.CREATION_TIME)))
                .andExpect(jsonPath("$.content[0].description", is(TodoConstants.TodoEntries.Second.DESCRIPTION)))
                .andExpect(jsonPath("$.content[0].id", is(TodoConstants.TodoEntries.Second.ID.intValue())))
                .andExpect(jsonPath("$.content[0].modifiedByUser", is(TodoConstants.TodoEntries.Second.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$.content[0].modificationTime", is(TodoConstants.TodoEntries.Second.MODIFICATION_TIME)))
                .andExpect(jsonPath("$.content[0].title", is(TodoConstants.TodoEntries.Second.TITLE)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndSecondPageIsRequestedWithPageSizeOne_ShouldReturnPageThatHasOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, SECOND_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.numberOfElements", is(1)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndSecondPageIsRequestedWithPageSizeOne_ShouldReturnPageThatHasTwoTotalElementsAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, SECOND_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndSecondPageIsRequestedWithPageSizeOne_ShouldReturnLastPageJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, SECOND_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.number", is(SECOND_PAGE)))
                .andExpect(jsonPath("$.first", is(false)))
                .andExpect(jsonPath("$.last", is(true)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTermAndSecondPageIsRequestedWithPageSizeOne_ShouldSortTodoEntriesByTitleAscAndReturnFirstTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_NUMBER, SECOND_PAGE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_PAGE_SIZE, PAGE_SIZE_STRING)
                        .param(WebTestConstants.REQUEST_PARAM_SORT, WebTestConstants.FIELD_NAME_TITLE)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].createdByUser", is(TodoConstants.TodoEntries.First.CREATED_BY_USER)))
                .andExpect(jsonPath("$.content[0].creationTime", is(TodoConstants.TodoEntries.First.CREATION_TIME)))
                .andExpect(jsonPath("$.content[0].description", is(TodoConstants.TodoEntries.First.DESCRIPTION)))
                .andExpect(jsonPath("$.content[0].id", is(TodoConstants.TodoEntries.First.ID.intValue())))
                .andExpect(jsonPath("$.content[0].modifiedByUser", is(TodoConstants.TodoEntries.First.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$.content[0].modificationTime", is(TodoConstants.TodoEntries.First.MODIFICATION_TIME)))
                .andExpect(jsonPath("$.content[0].title", is(TodoConstants.TodoEntries.First.TITLE)));
    }
}

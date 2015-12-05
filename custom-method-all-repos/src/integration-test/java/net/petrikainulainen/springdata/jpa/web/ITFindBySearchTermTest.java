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

    private static final String SECOND_TODO_CREATED_BY_USER = "createdByUser";
    private static final String SECOND_TODO_CREATION_TIME = "2014-12-24T13:13:28+02:00";
    private static final String SECOND_TODO_DESCRIPTION = "tiscription";
    private static final Long SECOND_TODO_ID = 2L;
    private static final String SECOND_TODO_MODIFIED_BY_USER = "modifiedByUser";
    private static final String SECOND_TODO_MODIFICATION_TIME = "2014-12-25T13:13:28+02:00";
    private static final String SECOND_TODO_TITLE = "First";

    private static final String SEARCH_TERM = "tIo";


    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }

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
    public void findBySearchTerm_AsUser_WhenNoTodoEntriesAreFoundWithSearchTerm_ShouldReturnZeroTodoEntriesAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_NO_MATCH)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenDescriptionOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnHttpResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenDescriptionOfOneTodoEntryContainsTheGivenSearchTerm_ShouldOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].createdByUser", is(TodoConstants.CREATED_BY_USER)))
                .andExpect(jsonPath("$[0].creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$[0].id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$[0].modifiedByUser", is(TodoConstants.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[0].modificationTime", is(TodoConstants.MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(TodoConstants.TITLE)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTitleOfOneTodoEntryContainsTheGivenSearchTerm_ShouldReturnHttpResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTitleOfOneTodoEntryContainsTheGivenSearchTerm_ShouldOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, TodoConstants.SEARCH_TERM_TITLE_MATCHES)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].createdByUser", is(TodoConstants.CREATED_BY_USER)))
                .andExpect(jsonPath("$[0].creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$[0].id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$[0].modifiedByUser", is(TodoConstants.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[0].modificationTime", is(TodoConstants.MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(TodoConstants.TITLE)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTerm_ShouldReturnHttpResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenTwoTodoEntriesMatchesWithSearchTerm_ShouldTwoTodoEntriesAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, SEARCH_TERM)
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].createdByUser", is(SECOND_TODO_CREATED_BY_USER)))
                .andExpect(jsonPath("$[0].creationTime", is(SECOND_TODO_CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(SECOND_TODO_DESCRIPTION)))
                .andExpect(jsonPath("$[0].id", is(SECOND_TODO_ID.intValue())))
                .andExpect(jsonPath("$[0].modifiedByUser", is(SECOND_TODO_MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[0].modificationTime", is(SECOND_TODO_MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(SECOND_TODO_TITLE)))
                .andExpect(jsonPath("$[1].createdByUser", is(TodoConstants.CREATED_BY_USER)))
                .andExpect(jsonPath("$[1].creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$[1].description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$[1].id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$[1].modifiedByUser", is(TodoConstants.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[1].modificationTime", is(TodoConstants.MODIFICATION_TIME)))
                .andExpect(jsonPath("$[1].title", is(TodoConstants.TITLE)));
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenSearchTermIsEmpty_ShouldReturnHttpResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, "")
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void findBySearchTerm_AsUser_WhenSearchTermIsEmpty_ShouldTwoTodoEntriesAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                        .param(WebTestConstants.REQUEST_PARAM_SEARCH_TERM, "")
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].createdByUser", is(SECOND_TODO_CREATED_BY_USER)))
                .andExpect(jsonPath("$[0].creationTime", is(SECOND_TODO_CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(SECOND_TODO_DESCRIPTION)))
                .andExpect(jsonPath("$[0].id", is(SECOND_TODO_ID.intValue())))
                .andExpect(jsonPath("$[0].modifiedByUser", is(SECOND_TODO_MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[0].modificationTime", is(SECOND_TODO_MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(SECOND_TODO_TITLE)))
                .andExpect(jsonPath("$[1].createdByUser", is(TodoConstants.CREATED_BY_USER)))
                .andExpect(jsonPath("$[1].creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$[1].description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$[1].id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$[1].modifiedByUser", is(TodoConstants.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[1].modificationTime", is(TodoConstants.MODIFICATION_TIME)))
                .andExpect(jsonPath("$[1].title", is(TodoConstants.TITLE)));
    }
}
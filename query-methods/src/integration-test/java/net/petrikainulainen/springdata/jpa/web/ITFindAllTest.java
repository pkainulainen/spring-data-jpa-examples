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
public class ITFindAllTest {

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
    public void findAll_AsAnonymous_ShouldReturnResponseStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user")
    public void findAll_AsUser_ShouldReturnResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    @WithUserDetails("user")
    public void findAll_AsUser_WhenTodoEntriesAreNotFound_ShouldReturnEmptyListAsJson() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DatabaseSetup("one-todo-entry.xml")
    @WithUserDetails("user")
    public void findAll_AsUser_WhenOneTodoEntryIsFound_ShouldReturnInformationOfOneTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].createdByUser", is(TodoConstants.TodoEntries.First.CREATED_BY_USER)))
                .andExpect(jsonPath("$[0].creationTime", is(TodoConstants.TodoEntries.First.CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(TodoConstants.TodoEntries.First.DESCRIPTION)))
                .andExpect(jsonPath("$[0].id", is(TodoConstants.TodoEntries.First.ID.intValue())))
                .andExpect(jsonPath("$[0].modifiedByUser", is(TodoConstants.TodoEntries.First.MODIFIED_BY_USER)))
                .andExpect(jsonPath("$[0].modificationTime", is(TodoConstants.TodoEntries.First.MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(TodoConstants.TodoEntries.First.TITLE)));
    }
}

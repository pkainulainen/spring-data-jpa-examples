package net.petrikainulainen.springdata.jpa.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import net.petrikainulainen.springdata.jpa.config.ExampleApplicationContext;
import net.petrikainulainen.springdata.jpa.config.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class ITDeleteTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    public void delete_TodoEntryNotFound_ShouldReturnResponseStatusBadRequest() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", TodoConstants.ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    public void delete_TodoEntryNotFound_ShouldReturnErrorMessageAsJson() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", TodoConstants.ID))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_TODO_ENTRY_NOT_FOUND)))
                .andExpect(jsonPath("$.message", is(TodoConstants.ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND)));
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    @ExpectedDatabase("no-todo-entries.xml")
    public void delete_TodoEntryNotFound_ShouldNotMakeAnyChangesToDatabase() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", TodoConstants.ID))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_TODO_ENTRY_NOT_FOUND)))
                .andExpect(jsonPath("$.message", is(TodoConstants.ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND)));
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void delete_TodoEntryFound_ShouldReturnInformationOfDeletedTodoEntry() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", TodoConstants.ID))
                .andExpect(jsonPath("$.creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$.id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$.modificationTime", is(TodoConstants.MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(TodoConstants.TITLE)));
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    @ExpectedDatabase("delete-todo-entry-expected.xml")
    public void delete_TodoEntryFound_ShouldDeleteTodoEntryFromDatabase() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", TodoConstants.ID));
    }
}

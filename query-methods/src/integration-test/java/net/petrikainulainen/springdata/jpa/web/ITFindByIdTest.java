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

import static org.hamcrest.Matchers.is;
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
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class ITFindByIdTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    public void findById_TodoEntryNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", TodoConstants.ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    public void findById_TodoEntryNotFound_ShouldReturnErrorMessageAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", TodoConstants.ID))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_TODO_ENTRY_NOT_FOUND)))
                .andExpect(jsonPath("$.message", is(TodoConstants.ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND)));

    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void findById_TodoEntryFound_ShouldReturnResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", TodoConstants.ID))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void findById_TodoEntryFound_ShouldReturnInformationOfFoundTodoEntryAsJson() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", TodoConstants.ID))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$.id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$.modificationTime", is(TodoConstants.MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(TodoConstants.TITLE)));
    }
}

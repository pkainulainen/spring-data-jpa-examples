package net.petrikainulainen.springdata.jpa.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import net.petrikainulainen.springdata.jpa.TodoConstants;
import net.petrikainulainen.springdata.jpa.common.ConstantDateTimeService;
import net.petrikainulainen.springdata.jpa.config.ExampleApplicationContext;
import net.petrikainulainen.springdata.jpa.config.Profiles;
import net.petrikainulainen.springdata.jpa.todo.TestUtil;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import net.petrikainulainen.springdata.jpa.todo.TodoDTOBuilder;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@WebAppConfiguration
public class ITUpdateTest {

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
    public void update_TodoEntryNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(TodoConstants.ID)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    public void update_TodoEntryNotFound_ShouldReturnErrorMessageAsJson() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(TodoConstants.ID)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_TODO_ENTRY_NOT_FOUND)))
                .andExpect(jsonPath("message", is(TodoConstants.ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND)));
    }

    @Test
    @DatabaseSetup("no-todo-entries.xml")
    @ExpectedDatabase("no-todo-entries.xml")
    public void update_TodoEntryNotFound_ShouldNotMakeAnyChangesToDatabase() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(TodoConstants.ID)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        );
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void update_TitleAndDescriptionAreMissing_ShouldReturnResponseStatusBadRequest() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(null)
                .id(TodoConstants.ID)
                .title(null)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void update_TitleAndDescriptionAreMissing_ShouldReturnValidationErrorAboutMissingTitleAsJson() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(null)
                .id(TodoConstants.ID)
                .title(null)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_VALIDATION_FAILED)))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is(WebTestConstants.FIELD_NAME_TITLE)))
                .andExpect(jsonPath("$.fieldErrors[0].message", is(TodoConstants.ERROR_MESSAGE_MISSING_TITLE)));
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    @ExpectedDatabase("todo-entries.xml")
    public void update_TitleAndDescriptionAreMissing_ShouldNotUpdateTodoEntry() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(null)
                .id(TodoConstants.ID)
                .title(null)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        );
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void update_TooLongTitleAndDescription_ShouldReturnResponseStatusBadRequest() throws Exception {
        String tooLongDescription = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_DESCRIPTION + 1);
        String tooLongTitle = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_TITLE + 1);

        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .id(TodoConstants.ID)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void update_TooLongTitleAndDescription_ShouldReturnValidationErrorsAboutTitleAndDescriptionAsJson() throws Exception {
        String tooLongDescription = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_DESCRIPTION + 1);
        String tooLongTitle = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_TITLE + 1);

        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .id(TodoConstants.ID)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_VALIDATION_FAILED)))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder(
                        WebTestConstants.FIELD_NAME_DESCRIPTION,
                        WebTestConstants.FIELD_NAME_TITLE
                )))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        TodoConstants.ERROR_MESSAGE_TOO_LONG_DESCRIPTION,
                        TodoConstants.ERROR_MESSAGE_TOO_LONG_TITLE
                )));
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    @ExpectedDatabase("todo-entries.xml")
    public void update_TooLongTitleAndDescription_ShouldNotUpdateTodoEntry() throws Exception {
        String tooLongDescription = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_DESCRIPTION + 1);
        String tooLongTitle = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_TITLE + 1);

        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .id(TodoConstants.ID)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        );
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void update_ValidTitleAndDescription_ShouldReturnResponseStatusOk() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(TodoConstants.UPDATED_DESCRIPTION)
                .id(TodoConstants.ID)
                .title(TodoConstants.UPDATED_TITLE)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    public void update_ValidTitleAndDescription_ShouldReturnInformationOfUpdatedTodoEntryAsJson() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(TodoConstants.UPDATED_DESCRIPTION)
                .id(TodoConstants.ID)
                .title(TodoConstants.UPDATED_TITLE)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.creationTime", is(TodoConstants.CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(TodoConstants.UPDATED_DESCRIPTION)))
                .andExpect(jsonPath("$.id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$.modificationTime", is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(jsonPath("$.title", is(TodoConstants.UPDATED_TITLE)));
    }

    @Test
    @DatabaseSetup("todo-entries.xml")
    @ExpectedDatabase(value = "update-todo-entry-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update_ValidTitleAndDescription_ShouldUpdateTodoEntry() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(TodoConstants.UPDATED_DESCRIPTION)
                .id(TodoConstants.ID)
                .title(TodoConstants.UPDATED_TITLE)
                .build();

        mockMvc.perform(put("/api/todo/{id}", TodoConstants.ID)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        );
    }
}

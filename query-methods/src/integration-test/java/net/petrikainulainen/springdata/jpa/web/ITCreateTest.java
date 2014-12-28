package net.petrikainulainen.springdata.jpa.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@DatabaseSetup("no-todo-entries.xml")
public class ITCreateTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws SQLException {
        DbTestUtil.resetAutoIncrementColumns(webAppContext, "todos");

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    public void create_EmptyTodoEntry_ShouldReturnResponseStatusBadRequest() throws Exception {
        TodoDTO emptyTodoEntry = new TodoDTO();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(emptyTodoEntry))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_EmptyTodoEntry_ShouldReturnValidationErrorAboutMissingTitleAsJson() throws Exception {
        TodoDTO emptyTodoEntry = new TodoDTO();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(emptyTodoEntry))
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(WebTestConstants.ERROR_CODE_VALIDATION_FAILED)))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is(WebTestConstants.FIELD_NAME_TITLE)))
                .andExpect(jsonPath("$.fieldErrors[0].message", is(TodoConstants.ERROR_MESSAGE_MISSING_TITLE)));
    }

    @Test
    @ExpectedDatabase("no-todo-entries.xml")
    public void create_EmptyTodoEntry_ShouldNotSaveTodoEntry() throws Exception {
        TodoDTO emptyTodoEntry = new TodoDTO();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(emptyTodoEntry))
        );
    }

    @Test
    public void create_TooLongTitleAndDescription_ShouldReturnResponseStatusBadRequest() throws Exception {
        String tooLongDescription = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_DESCRIPTION + 1);
        String tooLongTitle = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_TITLE + 1);

        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_TooLongTitleAndDescription_ShouldReturnValidationErrorsAboutTitleAndDescriptionAsJson() throws Exception {
        String tooLongDescription = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_DESCRIPTION + 1);
        String tooLongTitle = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_TITLE + 1);

        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
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
    @ExpectedDatabase("no-todo-entries.xml")
    public void create_TooLongTitleAndDescription_ShouldNotSaveTodoEntry() throws Exception {
        String tooLongDescription = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_DESCRIPTION + 1);
        String tooLongTitle = TestUtil.createStringWithLength(WebTestConstants.MAX_LENGTH_TITLE + 1);

        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        );
    }

    @Test
    public void create_ValidTitleAndDescription_ShouldReturnResponseStatusCreated() throws Exception {
        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(TodoConstants.DESCRIPTION)
                .title(TodoConstants.TITLE)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void create_ValidTitleAndDescription_ShouldReturnInformationOfCreatedTodoEntryAsJson() throws Exception {
        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(TodoConstants.DESCRIPTION)
                .title(TodoConstants.TITLE)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        )
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.creationTime", isA(String.class)))
                .andExpect(jsonPath("$.description", is(TodoConstants.DESCRIPTION)))
                .andExpect(jsonPath("$.id", is(TodoConstants.ID.intValue())))
                .andExpect(jsonPath("$.modificationTime", isA(String.class)))
                .andExpect(jsonPath("$.title", is(TodoConstants.TITLE)));
    }

    @Test
    @ExpectedDatabase(value = "create-todo-entry-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void create_ValidTitleAndDescription_ShouldSaveTodoEntry() throws Exception {
        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(TodoConstants.DESCRIPTION)
                .title(TodoConstants.TITLE)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        );
    }
}

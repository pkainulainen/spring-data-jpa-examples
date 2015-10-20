package net.petrikainulainen.springdata.jpa.todo;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.petrikainulainen.springdata.jpa.TodoConstants;
import net.petrikainulainen.springdata.jpa.config.ExampleApplicationContext;
import net.petrikainulainen.springdata.jpa.config.Profiles;
import net.petrikainulainen.springdata.jpa.web.ColumnSensingReplacementDataSetLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Iterator;

import static net.petrikainulainen.springdata.jpa.todo.TodoPredicates.titleOrDescriptionContainsIgnoreCase;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
@DbUnitConfiguration(dataSetLoader = ColumnSensingReplacementDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@WebAppConfiguration
@DatabaseSetup("todo-entries.xml")
public class ITTodoRepositoryTest {

    @Autowired
    private TodoRepository repository;

    @Test
    public void findBySearchTerm_SearchTermIsNull_ShouldReturnTwoTodoEntries() {
        Iterable<Todo> todoEntries = repository.findAll(titleOrDescriptionContainsIgnoreCase(null));

        assertThat(todoEntries).hasSize(2);

        Iterator<Todo> searchResults = todoEntries.iterator();

        Todo firstTodoEntry = searchResults.next();
        assertThat(firstTodoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);

        Todo secondTodoEntry = searchResults.next();
        assertThat(secondTodoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);
    }

    @Test
    public void findBySearchTerm_SearchTermIsEmpty_ShouldReturnTwoTodoEntries() {
        Iterable<Todo> todoEntries = repository.findAll(titleOrDescriptionContainsIgnoreCase(""));

        assertThat(todoEntries).hasSize(2);

        Iterator<Todo> searchResults = todoEntries.iterator();

        Todo firstTodoEntry = searchResults.next();
        assertThat(firstTodoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);

        Todo secondTodoEntry = searchResults.next();
        assertThat(secondTodoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);
    }

    @Test
    public void findBySearchTerm_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        Iterable<Todo> todoEntries = repository.findAll(titleOrDescriptionContainsIgnoreCase(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES));

        assertThat(todoEntries).hasSize(1);

        Iterator<Todo> searchResults = todoEntries.iterator();

        Todo todoEntry = searchResults.next();
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTerm_NoMatch_ShouldReturnEmptyList() {
        Iterable<Todo> todoEntries = repository.findAll(titleOrDescriptionContainsIgnoreCase(TodoConstants.SEARCH_TERM_NO_MATCH));
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTerm_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        Iterable<Todo> todoEntries = repository.findAll(titleOrDescriptionContainsIgnoreCase(TodoConstants.SEARCH_TERM_TITLE_MATCHES));

        assertThat(todoEntries).hasSize(1);

        Iterator<Todo> searchResults = todoEntries.iterator();

        Todo todoEntry = searchResults.next();
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }
}

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

import java.util.List;

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
public class ITNamedQueryTest {

    private static final String SEARCH_TERM = "tIo";
    private static final Long SECOND_TODO_ID = 2L;

    @Autowired
    private TodoRepository repository;

    @Test
    public void findBySearchTermNamed_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamed_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTermNamed_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamed_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamed(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNative_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNative(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNative_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNative(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTermNamedNativeSorted_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNative(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNative_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNative(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedFile_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedFile_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTermNamedFile_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedFile_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedFile(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNativeFile_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeFile(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNativeFile_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeFile(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTermNamedNativeFile_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeFile(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNativeFile_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeFile(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedOrmXml_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedOrmXml_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTermNamedOrmXml_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedOrmXml_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedOrmXml(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNativeOrmXml_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeOrmXml(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNativeOrmXml_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeOrmXml(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTermNamedNativeOrmXml_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeOrmXml(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTermNamedNativeOrmXml_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeOrmXml(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }
}

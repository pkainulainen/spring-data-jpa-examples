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
public class ITTodoRepositoryTest {

    private static final String SEARCH_TERM = "tIo";
    private static final Long SECOND_TODO_ID = 2L;

    @Autowired
    private TodoRepository repository;

    @Test
    public void findBySearchTerm_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<TodoSearchResultDTO> todoEntries = repository.findBySearchTerm(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        TodoSearchResultDTO todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTerm_NoMatch_ShouldReturnEmptyList() {
        List<TodoSearchResultDTO> todoEntries = repository.findBySearchTerm(TodoConstants.SEARCH_TERM_NO_MATCH);
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findBySearchTerm_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<TodoSearchResultDTO> todoEntries = repository.findBySearchTerm(TodoConstants.SEARCH_TERM_TITLE_MATCHES);
        assertThat(todoEntries).hasSize(1);

        TodoSearchResultDTO todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.ID);
    }

    @Test
    public void findBySearchTerm_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<TodoSearchResultDTO> todoEntries = repository.findBySearchTerm(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        TodoSearchResultDTO first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(SECOND_TODO_ID);

        TodoSearchResultDTO second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.ID);
    }
}

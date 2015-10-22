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
import org.springframework.data.domain.Sort;
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
public class ITMethodNameTest {

    private static final String SEARCH_TERM = "tIo";

    @Autowired
    private TodoRepository repository;

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCase_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCase(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES,
                TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES,
                orderByTitleAsc()
        );
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCase_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCase(TodoConstants.SEARCH_TERM_NO_MATCH,
                TodoConstants.SEARCH_TERM_NO_MATCH,
                orderByTitleAsc()
        );
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCase_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCase(TodoConstants.SEARCH_TERM_TITLE_MATCHES,
                TodoConstants.SEARCH_TERM_TITLE_MATCHES,
                orderByTitleAsc()
        );
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCase_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCase(SEARCH_TERM,
                SEARCH_TERM,
                orderByTitleAsc()
        );
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    private Sort orderByTitleAsc() {
        return new Sort(new Sort.Order(Sort.Direction.ASC, "title"));
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES,
                TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES
        );
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc_NoMatches_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc(TodoConstants.SEARCH_TERM_NO_MATCH,
                TodoConstants.SEARCH_TERM_NO_MATCH
        );
        assertThat(todoEntries).isEmpty();
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc_TitleOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc(TodoConstants.SEARCH_TERM_TITLE_MATCHES,
                TodoConstants.SEARCH_TERM_TITLE_MATCHES
        );
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc(SEARCH_TERM,
                SEARCH_TERM
        );
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }
}

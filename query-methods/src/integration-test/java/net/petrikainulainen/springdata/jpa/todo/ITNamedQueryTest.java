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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private static final int PAGE_NUMBER_ONE = 0;
    private static final int PAGE_NUMBER_TWO = 1;
    private static final int PAGE_SIZE_ONE = 1;
    private static final int PAGE_SIZE_TWO = 2;

    private static final String SEARCH_TERM = "tIo";

    @Autowired
    private TodoRepository repository;


    @Test
    public void findBySearchTermNamed_DescriptionOfOneTodoEntryMatches_ShouldReturnPageWithTotalElementCountOne() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findBySearchTermNamed_DescriptionOfFirstTodoEntryMatches_ShouldReturnPageThatHasOneTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo todoEntry = searchResultPage.getContent().get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamed_NoMatch_ShouldReturnPageWithTotalElementCountZero() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_NO_MATCH, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void findBySearchTermNamed_NoMatch_ShouldReturnEmptyPage() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_NO_MATCH, pageRequest);
        assertThat(searchResultPage).isEmpty();
    }

    @Test
    public void findBySearchTermNamed_TitleOfOneTodoEntryMatches_ShouldReturnPageWithTotalElementCountOne() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_TITLE_MATCHES, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findBySearchTermNamed_TitleOfFirstTodoEntryMatches_ShouldReturnPageThatHasOneTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(TodoConstants.SEARCH_TERM_TITLE_MATCHES, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo todoEntry = searchResultPage.getContent().get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamed_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageWithTotalElementCountTwo() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void findFirstPageBySearchTermNamedWithPageSizeOne_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTheSecondTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_ONE);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);
    }

    @Test
    public void findSecondPageBySearchTermNamedWithPageSizeOne_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTheFirstTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_TWO, PAGE_SIZE_ONE);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findFirstPageBySearchTermNamedWithPageSizeTwo_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTwoTodoEntries() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamed(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(2);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = searchResultPage.getContent().get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    private Pageable createPageRequest(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber, pageSize);
    }

    @Test
    public void findBySearchTermNamedNative_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNative(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
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
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedNative_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNative(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedFile_DescriptionOfOneTodoEntryMatches_ShouldReturnPageWithTotalElementCountOne() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findBySearchTermNamedFile_DescriptionOfFirstTodoEntryMatches_ShouldReturnPageThatHasOneTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo todoEntry = searchResultPage.getContent().get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedFile_NoMatch_ShouldReturnPageWithTotalElementCountZero() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_NO_MATCH, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void findBySearchTermNamedFile_NoMatch_ShouldReturnEmptyPage() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_NO_MATCH, pageRequest);
        assertThat(searchResultPage).isEmpty();
    }

    @Test
    public void findBySearchTermNamedFile_TitleOfOneTodoEntryMatches_ShouldReturnPageWithTotalElementCountOne() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_TITLE_MATCHES, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findBySearchTermNamedFile_TitleOfFirstTodoEntryMatches_ShouldReturnPageThatHasOneTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(TodoConstants.SEARCH_TERM_TITLE_MATCHES, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo todoEntry = searchResultPage.getContent().get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedFile_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageWithTotalElementCountTwo() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void findFirstPageBySearchTermNamedFileWithPageSizeOne_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTheSecondTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_ONE);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);
    }

    @Test
    public void findSecondPageBySearchTermNamedFileWithPageSizeOne_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTheFirstTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_TWO, PAGE_SIZE_ONE);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findFirstPageBySearchTermNamedFileWithPageSizeTwo_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTwoTodoEntries() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedFile(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(2);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = searchResultPage.getContent().get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedNativeFile_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeFile(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
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
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedNativeFile_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeFile(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedOrmXml_DescriptionOfOneTodoEntryMatches_ShouldReturnPageWithTotalElementCountOne() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findBySearchTermNamedOrmXml_DescriptionOfFirstTodoEntryMatches_ShouldReturnPageThatHasOneTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo todoEntry = searchResultPage.getContent().get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedOrmXml_NoMatch_ShouldReturnPageWithTotalElementCountZero() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_NO_MATCH, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void findBySearchTermNamedOrmXml_NoMatch_ShouldReturnEmptyPage() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_NO_MATCH, pageRequest);
        assertThat(searchResultPage).isEmpty();
    }

    @Test
    public void findBySearchTermNamedOrmXml_TitleOfOneTodoEntryMatches_ShouldReturnPageWithTotalElementCountOne() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_TITLE_MATCHES, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findBySearchTermNamedOrmXml_TitleOfFirstTodoEntryMatches_ShouldReturnPageThatHasOneTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(TodoConstants.SEARCH_TERM_TITLE_MATCHES, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo todoEntry = searchResultPage.getContent().get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedOrmXml_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageWithTotalElementCountTwo() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void findFirstPageBySearchTermNamedOrmXmlWithPageSizeOne_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTheSecondTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_ONE);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);
    }

    @Test
    public void findSecondPageBySearchTermNamedOrmXmlWithPageSizeOne_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTheFirstTodoEntry() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_TWO, PAGE_SIZE_ONE);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(1);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findFirstPageBySearchTermNamedOrmXmlWithPageSizeTwo_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnPageThatHasTwoTodoEntries() {
        Pageable pageRequest = createPageRequest(PAGE_NUMBER_ONE, PAGE_SIZE_TWO);

        Page<Todo> searchResultPage = repository.findBySearchTermNamedOrmXml(SEARCH_TERM, pageRequest);
        assertThat(searchResultPage.getNumberOfElements()).isEqualTo(2);

        Todo first = searchResultPage.getContent().get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = searchResultPage.getContent().get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedNativeOrmXml_DescriptionOfOneTodoEntryMatches_ShouldReturnListThatHasOneTodoEntry() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeOrmXml(TodoConstants.SEARCH_TERM_DESCRIPTION_MATCHES);
        assertThat(todoEntries).hasSize(1);

        Todo todoEntry = todoEntries.get(0);
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
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
        assertThat(todoEntry.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }

    @Test
    public void findBySearchTermNamedNativeOrmXml_TwoTodoEntriesMatchesWithSearchTerm_ShouldReturnSortedListThatHasTwoTodoEntries() {
        List<Todo> todoEntries = repository.findBySearchTermNamedNativeOrmXml(SEARCH_TERM);
        assertThat(todoEntries).hasSize(2);

        Todo first = todoEntries.get(0);
        assertThat(first.getId()).isEqualTo(TodoConstants.TodoEntries.Second.ID);

        Todo second = todoEntries.get(1);
        assertThat(second.getId()).isEqualTo(TodoConstants.TodoEntries.First.ID);
    }
}

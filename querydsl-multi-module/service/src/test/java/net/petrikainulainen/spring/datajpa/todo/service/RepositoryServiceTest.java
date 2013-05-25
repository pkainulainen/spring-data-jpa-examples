package net.petrikainulainen.spring.datajpa.todo.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.petrikainulainen.spring.datajpa.context.ExampleApplicationContext;
import net.petrikainulainen.spring.datajpa.todo.model.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("todoData.xml")
public class RepositoryServiceTest {

    @Autowired
    private TodoService service;

    @Test
    public void search_NoMatchesFound_ShouldReturnEmptyList() {
        List<Todo> todos = service.search("NotFound");
        assertThat(todos, hasSize(0));
    }

    @Test
    public void search_TwoTodoEntriesFound_ShouldReturnTodoEntries() {
        List<Todo> todos = service.search("Foo");
        assertThat(todos, hasSize(2));
        assertThat(todos, contains(
                allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("title", is("Foo")),
                        hasProperty("description", is("Lorem ipsum"))
                ),
                allOf(
                        hasProperty("id", is(2L)),
                        hasProperty("title", is("Bar")),
                        hasProperty("description", is("Description foo"))
                )
        ));
    }

    @Test
    public void search_OneEntryFound_ShouldReturnTodoEntry() {
        List<Todo> todos = service.search("bar");
        assertThat(todos, hasSize(1));
        assertThat(todos, contains(
                allOf(
                        hasProperty("id", is(2L)),
                        hasProperty("title", is("Bar")),
                        hasProperty("description", is("Description foo"))
                )
        ));
    }
}

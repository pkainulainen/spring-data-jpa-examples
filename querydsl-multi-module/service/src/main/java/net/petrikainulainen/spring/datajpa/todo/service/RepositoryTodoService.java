package net.petrikainulainen.spring.datajpa.todo.service;

import com.google.common.collect.Lists;
import net.petrikainulainen.spring.datajpa.todo.model.Todo;
import net.petrikainulainen.spring.datajpa.todo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.petrikainulainen.spring.datajpa.todo.repository.predicates.TodoPredicates.titleOrDescriptionContains;

/**
 * @author Petri Kainulainen
 */
@Service
public class RepositoryTodoService implements TodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoService.class);

    private TodoRepository repository;

    @Autowired
    public RepositoryTodoService(TodoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Todo> search(String searchTerm) {
        LOGGER.debug("Searching todo entries by using search term: {}", searchTerm);
        Iterable<Todo> todoEntries = repository.findAll(titleOrDescriptionContains(searchTerm));
        return Lists.newArrayList(todoEntries);
    }
}

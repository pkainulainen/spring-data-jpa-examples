package net.petrikainulainen.springdata.jpa.todo;

import com.mysema.query.types.OrderSpecifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.petrikainulainen.springdata.jpa.todo.TodoPredicates.titleOrDescriptionContainsIgnoreCase;


/**
 * @author Petri Kainulainen
 */
@Service
final class RepositoryTodoSearchService implements TodoSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoSearchService.class);

    private final TodoRepository repository;

    @Autowired
    public RepositoryTodoSearchService(TodoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TodoDTO> findBySearchTerm(String searchTerm) {
        LOGGER.info("Finding todo entries by search term: {}", searchTerm);

        Iterable<Todo> searchResults = repository.findAll(titleOrDescriptionContainsIgnoreCase(searchTerm), orderByTitleAsc());
        List<TodoDTO> dtos = TodoMapper.mapEntitiesIntoDTOs(searchResults);
        LOGGER.info("Found {} todo entries", dtos.size());

        return dtos;
    }

    private OrderSpecifier<String> orderByTitleAsc() {
        return QTodo.todo.title.asc();
    }
}

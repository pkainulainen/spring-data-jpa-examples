package net.petrikainulainen.springdata.jpa.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        List<Todo> searchResults = repository.findBySearchTerm(searchTerm);
        LOGGER.info("Found {} todo entries", searchResults.size());

        return TodoMapper.mapEntitiesIntoDTOs(searchResults);
    }
}

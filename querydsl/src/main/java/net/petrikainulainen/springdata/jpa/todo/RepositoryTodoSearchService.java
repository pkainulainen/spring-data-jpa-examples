package net.petrikainulainen.springdata.jpa.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<TodoDTO> findBySearchTerm(String searchTerm, Pageable pageRequest) {
        LOGGER.info("Finding todo entries by search term: {} and page request: {}", searchTerm, pageRequest);

        Page<Todo> searchResultPage = repository.findAll(titleOrDescriptionContainsIgnoreCase(searchTerm), pageRequest);

        LOGGER.info("Found {} todo entries. Returned page {} contains {} todo entries",
                searchResultPage.getTotalElements(),
                searchResultPage.getNumber(),
                searchResultPage.getNumberOfElements()
        );

        return TodoMapper.mapEntityPageIntoDTOPage(pageRequest, searchResultPage);
    }
}

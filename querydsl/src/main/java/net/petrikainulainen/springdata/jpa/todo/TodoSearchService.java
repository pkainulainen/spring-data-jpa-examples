package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This service provides finder methods for {@link net.petrikainulainen.springdata.jpa.todo.Todo} objects.
 *
 * @author Petri Kainulainen
 */
public interface TodoSearchService {

    /**
     * Finds todo entries whose title or description contains the given search term.
     * This search is case insensitive.
     * @param searchTerm    The search term.
     * @param pageRequest   The information of the requested page.
     * @return
     */
    Page<TodoDTO> findBySearchTerm(String searchTerm, Pageable pageRequest);
}

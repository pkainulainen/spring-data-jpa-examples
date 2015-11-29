package net.petrikainulainen.springdata.jpa.todo;

import java.util.List;

/**
 * This interface declares the custom methods that can be added into a Spring
 * Data JPA repository interface by extending this interface.
 *
 * @author Petri Kainulainen
 */
interface CustomTodoRepository {

    /**
     * Finds todo entries by using the search term given as a method parameter.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains with the given search term.
     */
    List<TodoSearchResultDTO> findBySearchTerm(String searchTerm);
}

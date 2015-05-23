package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * This repository provides CRUD operations for {@link net.petrikainulainen.springdata.jpa.todo.Todo}
 * objects.
 *
 * @author Petri Kainulainen
 */
interface TodoRepository extends Repository<Todo, Long> {

    void delete(Todo deleted);

    List<Todo> findAll();

    /**
     * Finds todo entries whose description or title contains the given search terms. This search is case insensitive.
     * @param descriptionPart   The part that must be found from the description of the todo entry.
     * @param titlePart         The part that must be found from the title of the todo entry.
     * @return  A list of todo entries whose title or description matches with the given search criteria.
     */
    List<Todo> findByDescriptionContainsOrTitleContainsAllIgnoreCase(String descriptionPart,
                                                                     String titlePart);
    /**
     * Finds todo entries whose description of title contains the given search term. This search is case insensitive.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    List<Todo> findByDescriptionOrTitle(@Param("searchTerm") String searchTerm);

    /**
     * Finds todo entries whose description of title contains the given search term. This search is case insensitive.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
    List<Todo> findBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM todos t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))",
            nativeQuery = true
    )
    List<Todo> findBySearchTermNative(@Param("searchTerm") String searchTerm);

    Optional<Todo> findOne(Long id);

    Todo save(Todo persisted);
}

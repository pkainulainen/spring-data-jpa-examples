package net.petrikainulainen.springdata.jpa.todo;

import net.petrikainulainen.springdata.jpa.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * This repository provides CRUD operations for {@link net.petrikainulainen.springdata.jpa.todo.Todo}
 * objects.
 *
 * @author Petri Kainulainen
 */
interface TodoRepository extends BaseRepository<Todo, Long> {

    List<Todo> findAll();

    /**
     * Finds todo entries by using the search term given as a method parameter.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains with the given search term.
     */
    @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
            "ORDER BY t.title ASC")
    List<Todo> findBySearchTerm(@Param("searchTerm") String searchTerm);

    Optional<Todo> findOne(Long id);

    void flush();

    Todo save(Todo persisted);
}

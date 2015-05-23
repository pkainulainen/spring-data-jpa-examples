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
     * This query method creates the invoked query method by parsing it from the method name of the query method.
     * @param descriptionPart   The part that must be found from the description of the todo entry.
     * @param titlePart         The part that must be found from the title of the todo entry.
     * @return  A list of todo entries whose title or description matches with the given search criteria.
     */
    List<Todo> findByDescriptionContainsOrTitleContainsAllIgnoreCase(String descriptionPart,
                                                                     String titlePart);
    /**
     * This query method invokes the named JPQL query that is configured in the {@code Todo} class by using the
     * {@code @NamedQuery} annotation. The name of the named query is: {@code Todo.findByDescriptionOrTitle}.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    List<Todo> findByDescriptionOrTitle(@Param("searchTerm") String searchTerm);

    /**
     * This query method reads the named JPQL query from the {@code META-INF/jpa-named-queries.properties} file.
     * The name of the invoked query is: {@code Todo.findByDescriptionOrTitleFile}.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    List<Todo> findByDescriptionOrTitleFile(@Param("searchTerm") String searchTerm);

    /**
     * This query method invokes the named SQL query that is configured in the {@code Todo} class by using
     * the {@code @NamedNativeQuery} annotation. The name of the named native query is: {@code Todo.findByDescriptionOrTitleNative}.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    List<Todo> findByDescriptionOrTitleNative(@Param("searchTerm") String searchTerm);

    /**
     * This query method invokes the JPQL query that is configured by using the {@code @Query} annotation.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
    List<Todo> findBySearchTerm(@Param("searchTerm") String searchTerm);

    /**
     * This query method invokes the SQL query that is configured by using the {@code @Query} annotation.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description matches with the given search term.
     */
    @Query(value = "SELECT * FROM todos t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))",
            nativeQuery = true
    )
    List<Todo> findBySearchTermNative(@Param("searchTerm") String searchTerm);

    Optional<Todo> findOne(Long id);

    Todo save(Todo persisted);
}

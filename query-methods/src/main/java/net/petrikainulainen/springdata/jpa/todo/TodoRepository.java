package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
     * @param pageRequest       The information of the requested page.
     * @return  A page of todo entries whose title or description contains with the given search term. The content of
     *          the returned page depends from the page request given as a method parameter.
     */
    Page<Todo> findByDescriptionContainsOrTitleContainsAllIgnoreCase(String descriptionPart,
                                                                     String titlePart,
                                                                     Pageable pageRequest);

    /**
     * This query method creates the invoked query method by parsing it from the method name of the query method.
     * @param descriptionPart   The part that must be found from the description of the todo entry.
     * @param titlePart         The part that must be found from the title of the todo entry.
     * @return  A list of todo entries whose title or description contains with the given search criteria. The returned
     *          todo entries are sorted in alphabetical order by using the title of the todo entry.
     */
    List<Todo> findByDescriptionContainsOrTitleContainsAllIgnoreCaseOrderByTitleAsc(String descriptionPart,
                                                                                    String titlePart);

    /**
     * This query method invokes the named JPQL query that is configured in the {@code Todo} class by using the
     * {@code @NamedQuery} annotation. The name of the named query is: {@code Todo.findBySearchTermNamed}.
     * @param searchTerm    The given search term.
     * @param pageRequest   The information of the given page.
     * @return  A page of todo entries whose title or description contains with the given search term. The content of
     *          the returned page depends from the page request given as a method parameter.
     */
    Page<Todo> findBySearchTermNamed(@Param("searchTerm") String searchTerm, Pageable pageRequest);

    /**
     * This query method invokes the named SQL query that is configured in the {@code Todo} class by using
     * the {@code @NamedNativeQuery} annotation. The name of the named native query is: {@code Todo.findBySearchTermNamedNative}.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains with the given search term. The returned
     *          todo entries are sorted in alphabetical order by using the title of the todo entry.
     */
    List<Todo> findBySearchTermNamedNative(@Param("searchTerm") String searchTerm);

    /**
     * This query method reads the named JPQL query from the {@code META-INF/jpa-named-queries.properties} file.
     * The name of the invoked query is: {@code Todo.findBySearchTermNamedFile}.
     * @param searchTerm    The given search term.
     * @param pageRequest   The information of the given page.
     * @return  A page of todo entries whose title or description contains with the given search term. The content of
     *          the returned page depends from the page request given as a method parameter.
     */
    Page<Todo> findBySearchTermNamedFile(@Param("searchTerm") String searchTerm, Pageable pageRequest);

    /**
     * This query method reads the named native query from the {@code META-INF/jpa-named-queries.properties} file.
     * The name of the invoked query is: {@code Todo.findBySearchTermNamedNativeFile}.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains with the given search term. The returned
     *          todo entries are sorted in alphabetical order by using the title of the todo entry.
     */
    @Query(nativeQuery = true)
    List<Todo> findBySearchTermNamedNativeFile(@Param("searchTerm") String searchTerm);

    /**
     * This query method reads the named from the {@code META-INF/orm.xml} file. The name of the invoked query
     * is: {@code Todo.findBySearchTermNamedOrmXml}.
     * @param searchTerm    The given search term.
     * @param pageRequest   The information of the given page.
     * @return  A page of todo entries whose title or description contains with the given search term. The content of
     *          the returned page depends from the page request given as a method parameter.
     */
    Page<Todo> findBySearchTermNamedOrmXml(@Param("searchTerm") String searchTerm, Pageable pageRequest);

    /**
     * This query method reads the named from the {@code META-INF/orm.xml} file. The name of the invoked query
     * is: {@code Todo.findBySearchTermNamedNativeOrmXml}.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains the given search term. The returned
     *          todo entries are sorted in alphabetical order by using the title of the todo entry.
     */
    @Query(nativeQuery = true)
    List<Todo> findBySearchTermNamedNativeOrmXml(@Param("searchTerm") String searchTerm);

    /**
     * This query method invokes the JPQL query that is configured by using the {@code @Query} annotation.
     * @param searchTerm    The given search term.
     * @param pageRequest   The information of the requested page.
     * @return  A page of todo entries whose title or description contains with the given search term. The content of
     *          the returned page depends from the page request given as a method parameter.
     */
    @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
    Page<Todo> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageRequest);

    /**
     * This query method invokes the JPQL query that is configured by using the {@code @Query} annotation.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains with the given search term. The
     *          returned todo entries are sorted in alphabetical order by using the title of a todo entry.
     */
    @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
            "ORDER BY t.title ASC")
    List<Todo> findBySearchTermSortedInQuery(@Param("searchTerm") String searchTerm);

    /**
     * This query method invokes the SQL query that is configured by using the {@code @Query} annotation.
     * @param searchTerm    The given search term.
     * @return  A list of todo entries whose title or description contains with the given search term. The
     *          returned todo entries are sorted in alphabetical order by using the title of a todo entry.
     */
    @Query(value = "SELECT * FROM todos t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
            "ORDER BY t.title ASC",
            nativeQuery = true
    )
    List<Todo> findBySearchTermNative(@Param("searchTerm") String searchTerm);

    Optional<Todo> findOne(Long id);

    void flush();

    Todo save(Todo persisted);
}

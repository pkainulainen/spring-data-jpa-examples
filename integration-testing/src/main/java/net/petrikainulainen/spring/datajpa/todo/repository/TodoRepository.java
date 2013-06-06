package net.petrikainulainen.spring.datajpa.todo.repository;

import net.petrikainulainen.spring.datajpa.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query(
            "Select t FROM Todo t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
    )
    public List<Todo> search(@Param("searchTerm") String searchTerm);
}

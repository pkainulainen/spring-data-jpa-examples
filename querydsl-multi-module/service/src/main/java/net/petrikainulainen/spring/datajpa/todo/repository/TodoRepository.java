package net.petrikainulainen.spring.datajpa.todo.repository;

import net.petrikainulainen.spring.datajpa.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author Petri Kainulainen
 */
public interface TodoRepository extends JpaRepository<Todo, Long>, QueryDslPredicateExecutor<Todo> {
}

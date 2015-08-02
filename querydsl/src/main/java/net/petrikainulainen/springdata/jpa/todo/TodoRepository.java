package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This repository provides CRUD operations for {@link net.petrikainulainen.springdata.jpa.todo.Todo}
 * objects.
 *
 * @author Petri Kainulainen
 */
interface TodoRepository extends Repository<Todo, Long>, QueryDslPredicateExecutor<Todo> {

    void delete(Todo deleted);

    List<Todo> findAll();

    Optional<Todo> findOne(Long id);

    void flush();

    Todo save(Todo persisted);
}

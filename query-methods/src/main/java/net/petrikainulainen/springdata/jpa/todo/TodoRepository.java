package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This repository provides CRUD operations for {@link net.petrikainulainen.springdata.jpa.todo.Todo}
 * objects.
 *
 * @author Petri Kainulainen
 */
interface TodoRepository extends Repository<Todo, Long> {

    List<Todo> findAll();

    Optional<Todo> findOne(Long id);
}

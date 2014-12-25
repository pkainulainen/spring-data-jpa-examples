package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
interface TodoRepository extends Repository<Todo, Long> {

    List<Todo> findAll();
}

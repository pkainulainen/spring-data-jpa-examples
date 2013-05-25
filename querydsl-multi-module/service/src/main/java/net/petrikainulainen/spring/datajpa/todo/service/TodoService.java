package net.petrikainulainen.spring.datajpa.todo.service;

import net.petrikainulainen.spring.datajpa.todo.model.Todo;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoService {

    public List<Todo> search(String searchTerm);
}

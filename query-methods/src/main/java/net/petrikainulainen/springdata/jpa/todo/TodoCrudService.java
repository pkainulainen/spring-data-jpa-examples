package net.petrikainulainen.springdata.jpa.todo;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoCrudService {

    /**
     * Finds all todo entries that are saved to the database.
     * @return
     */
    public List<TodoDTO> findAll();

    /**
     * Finds a todo entry by using the id given as a method parameter.
     * @param id    The id of the wanted todo entry.
     * @return      The information of the requested todo entry.
     * @throws net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException if no todo entry is found with the given id.
     */
    public TodoDTO findById(Long id);
}

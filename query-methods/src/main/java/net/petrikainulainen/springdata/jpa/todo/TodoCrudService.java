package net.petrikainulainen.springdata.jpa.todo;

import java.util.List;

/**
 * This service provides CRUD operations for {@link net.petrikainulainen.springdata.jpa.todo.Todo}
 * objects.
 *
 * @author Petri Kainulainen
 */
public interface TodoCrudService {

    /**
     * Creates a new todo entry.
     * @param newTodoEntry   The information of the created todo entry.
     * @return               The information of the created todo entry.
     */
    public TodoDTO create(TodoDTO newTodoEntry);

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

    /**
     * Updates the information of an existing information.
     * @param updatedTodoEntry  The new information of an existing todo entry.
     * @return                  The information of the updated todo entry.
     * @throws net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException if no todo entry is found with the given id.
     */
    public TodoDTO update(TodoDTO updatedTodoEntry);
}

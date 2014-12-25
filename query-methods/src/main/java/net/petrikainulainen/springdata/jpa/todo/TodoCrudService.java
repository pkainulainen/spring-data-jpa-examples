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
}

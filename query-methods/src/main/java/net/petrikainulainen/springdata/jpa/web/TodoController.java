package net.petrikainulainen.springdata.jpa.web;

import net.petrikainulainen.springdata.jpa.todo.TodoCrudService;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller provides the public API that is used to perform
 * CRUD operations for todo entries.
 *
 * @author Petri Kainulainen
 */
@RestController
@RequestMapping("/api/todo")
final class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private final TodoCrudService crudService;

    @Autowired
    TodoController(TodoCrudService crudService) {
        this.crudService = crudService;
    }

    /**
     * Create a new todo entry.
     * @param newTodoEntry  The information of the created todo entry.
     * @return              The information of the created todo entry.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    TodoDTO create(@RequestBody @Valid TodoDTO newTodoEntry) {
        LOGGER.info("Creating a new todo entry by using information: {}", newTodoEntry);

        TodoDTO created = crudService.create(newTodoEntry);
        LOGGER.info("Created a new todo entry: {}", created);

        return created;
    }

    /**
     * Deletes a todo entry.
     * @param id    The id of the deleted todo entry.
     * @return      The information of the deleted todo entry.
     * @throws net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException if the deleted todo entry is not found.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public TodoDTO delete(@PathVariable("id") Long id) {
        LOGGER.info("Deleting a todo entry with id: {}", id);

        TodoDTO deleted = crudService.delete(id);
        LOGGER.info("Deleted the todo entry: {}", deleted);

        return deleted;
    }

    /**
     * Finds all todo entries.
     *
     * @return The information of all todo entries.
     */
    @RequestMapping(method = RequestMethod.GET)
    List<TodoDTO> findAll() {
        LOGGER.info("Finding all todo entries");

        List<TodoDTO> todoEntries = crudService.findAll();
        LOGGER.info("Found {} todo entries.", todoEntries.size());

        return todoEntries;
    }

    /**
     * Finds a single todo entry.
     * @param id    The id of the requested todo entry.
     * @return      The information of the requested todo entry.
     * @throws net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException if no todo entry is found by using the given id.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    TodoDTO findById(@PathVariable("id") Long id) {
        LOGGER.info("Finding todo entry by using id: {}", id);

        TodoDTO todoEntry = crudService.findById(id);
        LOGGER.info("Found todo entry: {}", todoEntry);

        return todoEntry;
    }

    /**
     * Updates the information of an existing todo entry.
     * @param updatedTodoEntry  The new information of the updated todo entry.
     * @return                  The updated information of the updated todo entry.
     * @throws net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException if no todo entry is found by using the given id.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    TodoDTO update(@RequestBody @Valid TodoDTO updatedTodoEntry) {
        LOGGER.info("Updating the information of a todo entry by using information: {}", updatedTodoEntry);

        TodoDTO updated = crudService.update(updatedTodoEntry);
        LOGGER.info("Updated the information of the todo entrY: {}", updated);

        return updated;
    }
}

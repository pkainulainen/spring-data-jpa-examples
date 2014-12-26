package net.petrikainulainen.springdata.jpa.web;

import net.petrikainulainen.springdata.jpa.todo.TodoCrudService;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * Finds all todo entries.
     *
     * @return The information of all todo entries.
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<TodoDTO> findAll() {
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
    public TodoDTO findById(@PathVariable("id") Long id) {
        LOGGER.info("Finding todo entry by using id: {}", id);

        TodoDTO todoEntry = crudService.findById(id);
        LOGGER.info("Found todo entry: {}", todoEntry);

        return todoEntry;
    }
}

package net.petrikainulainen.springdata.jpa.web;

import net.petrikainulainen.springdata.jpa.todo.TodoCrudService;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(method = RequestMethod.GET)
    public List<TodoDTO> findAll() {
        LOGGER.info("Finding all todo entries");

        List<TodoDTO> todoEntries = crudService.findAll();
        LOGGER.info("Found {} todo entries.", todoEntries.size());

        return todoEntries;
    }
}

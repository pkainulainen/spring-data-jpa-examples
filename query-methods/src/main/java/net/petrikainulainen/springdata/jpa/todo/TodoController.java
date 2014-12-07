package net.petrikainulainen.springdata.jpa.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Petri Kainulainen
 */
@RestController
@RequestMapping("/api/todo")
final class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    @RequestMapping(method = RequestMethod.GET)
    String hello() {
        LOGGER.info("Returning: Hello");

        return "Hello";
    }
}

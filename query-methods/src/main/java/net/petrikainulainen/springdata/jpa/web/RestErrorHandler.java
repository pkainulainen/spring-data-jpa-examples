package net.petrikainulainen.springdata.jpa.web;

import net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

/**
 * This class handles the exceptions thrown by our REST API.
 *
 * @author Petri Kainulainen
 */
@ControllerAdvice
final class RestErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    private static final String ERROR_CODE_TODO_ENTRY_NOT_FOUND = "error.todo.entry.not.found";

    private final MessageSource messageSource;

    @Autowired
    RestErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorDTO handleTodoEntryNotFound(TodoNotFoundException ex, Locale currentLocale) {
        LOGGER.error("Todo entry was not found by using id: {}", ex.getId());

        MessageSourceResolvable errorMessageRequest = createSingleErrorMessageRequest(
                ERROR_CODE_TODO_ENTRY_NOT_FOUND,
                ex.getId()
        );

        String errorMessage = messageSource.getMessage(errorMessageRequest, currentLocale);
        return new ErrorDTO(HttpStatus.NOT_FOUND.name(), errorMessage);
    }

    private DefaultMessageSourceResolvable createSingleErrorMessageRequest(String errorMessageCode, Object... params) {
        return new DefaultMessageSourceResolvable(new String[] {errorMessageCode}, params);
    }
}

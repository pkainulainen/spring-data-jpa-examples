package net.petrikainulainen.springdata.jpa.web.error;

import net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;

/**
 * This class handles the exceptions thrown by our REST API.
 *
 * @author Petri Kainulainen
 */
@ControllerAdvice
public final class RestErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    private static final String ERROR_CODE_TODO_ENTRY_NOT_FOUND = "error.todo.entry.not.found";

    private final MessageSource messageSource;

    @Autowired
    public RestErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Processes an error that occurs when the requested todo entry is not found.
     * @param ex                The exception that was thrown when the todo entry was not found.
     * @param currentLocale     The current locale.
     * @return                  An error object that contains the error code and message.
     */
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

    /**
     * Processes an error that occurs when the validation of an object fails.
     *
     * @param ex    The exception that was thrown when the validation failed.
     * @return      An error object that describes all validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO handleValidationErrors(MethodArgumentNotValidException ex, Locale currentLocale) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        LOGGER.error("Found {} validation errors", fieldErrors.size());

        return constructValidationErrors(fieldErrors, currentLocale);
    }

    private ValidationErrorDTO constructValidationErrors(List<FieldError> fieldErrors, Locale currentLocale) {
        ValidationErrorDTO dto = new ValidationErrorDTO();

        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = getValidationErrorMessage(fieldError, currentLocale);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    private String getValidationErrorMessage(FieldError fieldError, Locale currentLocale) {
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }

        return localizedErrorMessage;
    }
}

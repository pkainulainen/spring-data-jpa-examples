package net.petrikainulainen.springdata.jpa.web.error;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the information of validation errors that are found
 * from a controller method parameter that is annotated with the
 * {@link javax.validation.Valid} annotation.
 *
 * @author Petri Kainulainen
 */
final class ValidationErrorDTO {

    private final String code = HttpStatus.BAD_REQUEST.name();

    private final List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    ValidationErrorDTO() {
    }

    void addFieldError(String field, String message) {
        FieldErrorDTO error = new FieldErrorDTO(field, message);
        fieldErrors.add(error);
    }

    public String getCode() {
        return code;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}

package net.petrikainulainen.springdata.jpa.web.error;

import static net.petrikainulainen.springdata.jpa.common.PreCondition.notEmpty;
import static net.petrikainulainen.springdata.jpa.common.PreCondition.notNull;

/**
 * This class contains the information of a single field error.
 *
 * @author Petri Kainulainen
 */
final class FieldErrorDTO {

    private final String field;

    private final String message;

    FieldErrorDTO(String field, String message) {
        notNull(field, "Field cannot be null.");
        notEmpty(field, "Field cannot be empty");

        notNull(message, "Message cannot be null.");
        notEmpty(message, "Message cannot be empty.");

        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}

package net.petrikainulainen.springdata.jpa.web.error;

import static net.petrikainulainen.springdata.jpa.common.PreCondition.notEmpty;
import static net.petrikainulainen.springdata.jpa.common.PreCondition.notNull;

/**
 * This class contains the information of an error that occurred when the API tried
 * to perform the operation requested by the client.
 *
 * @author Petri Kainulainen
 */
final class ErrorDTO {

    private final String code;
    private final String message;

    ErrorDTO(String code, String message) {
        notNull(code, "Code cannot be null.");
        notEmpty(code, "Code cannot be empty.");

        notNull(message, "Message cannot be null.");
        notEmpty(message, "Message cannot be empty");

        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

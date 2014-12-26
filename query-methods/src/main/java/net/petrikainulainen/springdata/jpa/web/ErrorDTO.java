package net.petrikainulainen.springdata.jpa.web;

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

package net.petrikainulainen.springdata.jpa.web;

/**
 * This class contains the constants that are used in our integration tests, DbUnit datasets,
 * and the localization file.
 *
 * @author Petri Kainulainen
 */
final class TodoConstants {

    static final String CREATION_TIME = "2014-12-24T13:13:28+02:00";
    static final String DESCRIPTION = "description";
    static final Long ID = 1L;
    static final String MODIFICATION_TIME = "2014-12-25T13:13:28+02:00";
    static final String TITLE = "title";

    static final String ERROR_CODE_TODO_ENTRY_NOT_FOUND = "NOT_FOUND";

    static final String ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND = "No todo entry was found by using id: 1";
    static final String ERROR_MESSAGE_MISSING_TITLE = "The title cannot be empty";
    static final String ERROR_MESSAGE_TOO_LONG_DESCRIPTION = "The maximum length of description is 500 characters";
    static final String ERROR_MESSAGE_TOO_LONG_TITLE = "The maximum length of title is 100 characters";

    /**
     * Prevents instantiation
     */
    private TodoConstants() {}
}

package net.petrikainulainen.springdata.jpa.web;

/**
 * This class contains the constants that are used in our integration tests and
 * in our DbUnit datasets.
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

    /**
     * Prevents instantiation
     */
    private TodoConstants() {}
}

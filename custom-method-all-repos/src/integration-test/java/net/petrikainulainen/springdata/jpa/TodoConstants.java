package net.petrikainulainen.springdata.jpa;

/**
 * This class contains the constants that are used in our integration tests, DbUnit datasets,
 * and the localization file.
 *
 * @author Petri Kainulainen
 */
public final class TodoConstants {

    public static final String CREATED_BY_USER = "createdByUser";
    public static final String CREATION_TIME = "2014-12-24T13:13:28+02:00";
    public static final String DESCRIPTION = "description";
    public static final Long ID = 1L;
    public static final String MODIFIED_BY_USER = "modifiedByUser";
    public static final String MODIFICATION_TIME = "2014-12-25T13:13:28+02:00";
    public static final String TITLE = "title";

    public static final String SEARCH_TERM_DESCRIPTION_MATCHES = "esC";
    public static final String SEARCH_TERM_NO_MATCH = "NO MATCH";
    public static final String SEARCH_TERM_TITLE_MATCHES = "It";

    public static final String UPDATED_DESCRIPTION = "updatedDescription";
    public static final String UPDATED_TITLE = "updatedTitle";

    public static final String ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND = "No todo entry was found by using id: 1";
    public static final String ERROR_MESSAGE_MISSING_TITLE = "The title cannot be empty";
    public static final String ERROR_MESSAGE_TOO_LONG_DESCRIPTION = "The maximum length of description is 500 characters";
    public static final String ERROR_MESSAGE_TOO_LONG_TITLE = "The maximum length of title is 100 characters";

    /**
     * Prevents instantiation
     */
    private TodoConstants() {}
}

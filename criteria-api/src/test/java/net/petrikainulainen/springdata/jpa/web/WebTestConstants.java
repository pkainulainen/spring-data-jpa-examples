package net.petrikainulainen.springdata.jpa.web;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * @author Petri Kainulainen
 */
public final class WebTestConstants {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    static final String ERROR_CODE_TODO_ENTRY_NOT_FOUND = "NOT_FOUND";
    static final String ERROR_CODE_VALIDATION_FAILED = "BAD_REQUEST";

    static final String FIELD_NAME_DESCRIPTION = "description";
    static final String FIELD_NAME_TITLE = "title";

    static final int MAX_LENGTH_DESCRIPTION = 500;
    static final int MAX_LENGTH_TITLE = 100;

    static final String REQUEST_PARAM_PAGE_NUMBER = "page";
    static final String REQUEST_PARAM_PAGE_SIZE = "size";
    static final String REQUEST_PARAM_SEARCH_TERM = "searchTerm";
    static final String REQUEST_PARAM_SORT = "sort";

    static final String SORT_DIRECTION_ASC = "ASC";

    /**
     * Prevents instantiation.
     */
    private WebTestConstants() {}
}

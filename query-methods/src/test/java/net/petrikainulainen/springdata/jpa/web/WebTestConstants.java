package net.petrikainulainen.springdata.jpa.web;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * @author Petri Kainulainen
 */
final class WebTestConstants {

    static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    /**
     * Prevents instantiation.
     */
    private WebTestConstants() {}
}

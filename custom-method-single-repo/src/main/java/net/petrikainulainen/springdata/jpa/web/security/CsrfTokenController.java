package net.petrikainulainen.springdata.jpa.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Petri Kainulainen
 */
@RestController
public class CsrfTokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenController.class);

    @RequestMapping(value = "/api/csrf", method = RequestMethod.HEAD)
    public void getCsrfToken() {
        LOGGER.info("Getting CSRF token.");
    }
}

package net.petrikainulainen.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller is responsible of starting the frontend application.
 * @author Petri Kainulainen
 */
@Controller
public class FrontendLoaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendLoaderController.class);

    private static final String FRONTEND_APPLICATION_VIEW = "frontend/client";

    /**
     * Starts the AngularJS application.
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String startAngularJSApplication() {
        LOGGER.debug("Starting frontend single page application.");
        return FRONTEND_APPLICATION_VIEW;
    }
}

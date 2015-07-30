package net.petrikainulainen.springdata.jpa.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This authentication failure handler returns the HTTP status code 403.
 * @author Petri Kainulainen
 */
public final class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        LOGGER.info("Authentication failed with message: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed.");
    }
}

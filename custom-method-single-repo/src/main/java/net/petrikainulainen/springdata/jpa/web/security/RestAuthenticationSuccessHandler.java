package net.petrikainulainen.springdata.jpa.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This authentication success handler returns the information of the authenticated
 * user as JSON.
 *
 * @author Petri Kainulainen
 */
public final class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        LOGGER.info("Authentication was successful");
        response.sendRedirect(response.encodeRedirectURL("/api/authenticated-user"));
    }
}

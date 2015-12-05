package net.petrikainulainen.springdata.jpa.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the public API that is used to return the information
 * of the authenticated user.
 *
 * @author Petri Kainulainen
 */
@RestController
final class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * Returns the information of the authenticated user as JSON. The returned information
     * contains the username and the user role of the authenticated user.
     *
     * @param authenticatedUser The information of the authenticated user.
     * @return
     */
    @RequestMapping(value = "/api/authenticated-user", method = RequestMethod.GET)
    public UserDTO getAuthenticatedUser(@AuthenticationPrincipal User authenticatedUser) {
        LOGGER.info("Getting authenticated user.");

        if (authenticatedUser == null) {
            //If anonymous users can access this controller method, someone has changed
            //the security configuration and it must be fixed.
            LOGGER.error("Authenticated user is not found.");
            throw new AccessDeniedException("Anonymous users cannot request the information of the authenticated user.");
        }
        else {
            LOGGER.info("User with username: {} is authenticated", authenticatedUser.getUsername());
            return new UserDTO(authenticatedUser.getUsername(), authenticatedUser.getAuthorities());
        }
    }
}

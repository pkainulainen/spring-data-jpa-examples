package net.petrikainulainen.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * This component returns the username of the authenticated user.
 *
 * @author Petri Kainulainen
 */
public class UsernameAuditorAware implements AuditorAware<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameAuditorAware.class);

    @Override
    public String getCurrentAuditor() {
        LOGGER.debug("Getting the username of authenticated user.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.debug("Current user is anonymous. Returning null.");
            return null;
        }

        String username = ((User) authentication.getPrincipal()).getUsername();
        LOGGER.debug("Returning username: {}", username);

        return username;
    }
}

package net.petrikainulainen.springdata.jpa.web.security;

import net.petrikainulainen.springdata.jpa.common.PreCondition;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * This class contains the information of the authenticated user.
 *
 * @author Petri Kainulainen
 */
public final class UserDTO {

    private final String username;

    private final UserRole role;

    UserDTO(String username, Collection<GrantedAuthority> authorities) {
        PreCondition.isTrue(!username.isEmpty(), "Username cannot be empty.");
        PreCondition.isTrue(authorities.size() == 1, "User must have only one granted authority.");
        this.username = username;

        GrantedAuthority authority = authorities.iterator().next();
        this.role = UserRole.valueOf(authority.getAuthority());
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}

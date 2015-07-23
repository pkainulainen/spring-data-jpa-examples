package net.petrikainulainen.springdata.jpa.web.security;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class UserDTOTest {

    public class CreateNew {

        private final String ROLE_USER = UserRole.ROLE_USER.name();

        public class WhenUsernameIsEmpty {

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowException() {
                Collection<GrantedAuthority> authorities = createAuthorities(ROLE_USER);
                new UserDTO("", authorities);
            }
        }

        public class WhenUserNameIsNotEmpty {

            private final String USERNAME = "username";

            public class WhenUserHasNoGrantedAuthorities {

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    new UserDTO(USERNAME, new ArrayList<>());
                }
            }

            public class WhenUserHasOneGrantedAuthority {

                public class WhenGrantedAuthorityIsKnown {

                    private Collection<GrantedAuthority> authorities;

                    @Before
                    public void createKnownAuthority() {
                        authorities = createAuthorities(ROLE_USER);
                    }

                    @Test
                    public void shouldSetUsername() {
                        UserDTO user = new UserDTO(USERNAME, authorities);
                        assertThat(user.getUsername()).isEqualTo(USERNAME);
                    }

                    @Test
                    public void shouldSetRole() {
                        UserDTO user = new UserDTO(USERNAME, authorities);
                        assertThat(user.getRole()).isEqualTo(UserRole.ROLE_USER);
                    }
                }

                public class WhenGrantedAuthorityIsUnknown {

                    @Test(expected = IllegalArgumentException.class)
                    public void shouldThrowException() {
                        Collection<GrantedAuthority> authorities = createAuthorities("UNKNOWN_ROLE");
                        new UserDTO(USERNAME, authorities);
                    }
                }
            }

            public class WhenUserHasMoreThanOneGrantedAuthority {

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    Collection<GrantedAuthority> authorities = createAuthorities(ROLE_USER, "ANOTHER_ROLE");
                    new UserDTO(USERNAME, authorities);
                }
            }
        }
    }

    private Collection<GrantedAuthority> createAuthorities(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role: roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            authorities.add(authority);
        }

        return authorities;
    }
}

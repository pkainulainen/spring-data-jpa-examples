package net.petrikainulainen.springdata.jpa.web.security;

import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.petrikainulainen.springdata.jpa.Users;
import net.petrikainulainen.springdata.jpa.config.ExampleApplicationContext;
import net.petrikainulainen.springdata.jpa.config.Profiles;
import net.petrikainulainen.springdata.jpa.web.ColumnSensingReplacementDataSetLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
@DbUnitConfiguration(dataSetLoader = ColumnSensingReplacementDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
@WebAppConfiguration
public class ITLoginTest {

    private static final String INVALID_PASSWORD = "invalidPassword";
    private static final String INVALID_USERNAME = "invalidUsername";

    private static final String PARAM_NAME_PASSWORD = "password";
    private static final String PARAM_NAME_USERNAME = "username";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void logIn_WhenUsernameIsIncorrect_ShouldReturnResponseStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_NAME_USERNAME, INVALID_USERNAME)
                        .param(PARAM_NAME_PASSWORD, Users.USER.getPassword())
                        .with(csrf())
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void logIn_WhenPasswordIsIncorrect_ShouldReturnResponseStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_NAME_USERNAME, Users.USER.getUsername())
                        .param(PARAM_NAME_PASSWORD, INVALID_PASSWORD)
                        .with(csrf())
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void logIn_WhenUsernameAndPasswordAreCorrect_ShouldReturnResponseStatusFound() throws Exception {
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(PARAM_NAME_USERNAME, Users.USER.getUsername())
                .param(PARAM_NAME_PASSWORD, Users.USER.getPassword())
                .with(csrf())
        )
                .andExpect(status().isFound());
    }

    @Test
    public void logIn_WhenUsernameAndPasswordAreCorrect_ShouldRedirectClientToControllerMethodThatReturnsAuthenticatedUser() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(PARAM_NAME_USERNAME, Users.USER.getUsername())
                        .param(PARAM_NAME_PASSWORD, Users.USER.getPassword())
                        .with(csrf())
        )
                .andExpect(redirectedUrl("/api/authenticated-user"));
    }
}

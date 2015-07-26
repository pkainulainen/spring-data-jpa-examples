package net.petrikainulainen.springdata.jpa.web.security;

import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.petrikainulainen.springdata.jpa.config.ExampleApplicationContext;
import net.petrikainulainen.springdata.jpa.config.Profiles;
import net.petrikainulainen.springdata.jpa.web.ColumnSensingReplacementDataSetLoader;
import net.petrikainulainen.springdata.jpa.web.WebTestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class ITGetAuthenticatedUserTest {

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
    public void getAuthenticatedUser_AsAnonymous_ShouldReturnResponseStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/authenticated-user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user")
    public void getAuthenticatedUser_AsUser_ShouldReturnResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/authenticated-user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void getAuthenticatedUser_AsUser_ShouldReturnUserInformationAsJSON() throws Exception {
        mockMvc.perform(get("/api/authenticated-user"))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", is("user")))
                .andExpect(jsonPath("$.role", is(UserRole.ROLE_USER.name())));
    }
}

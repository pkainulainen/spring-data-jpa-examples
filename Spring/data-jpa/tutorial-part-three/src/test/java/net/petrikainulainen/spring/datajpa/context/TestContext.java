package net.petrikainulainen.spring.datajpa.context;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * A test context which is used for unit testing controllers.
 * @author Petri Kainulainen
 */
@Configuration
public class TestContext {

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
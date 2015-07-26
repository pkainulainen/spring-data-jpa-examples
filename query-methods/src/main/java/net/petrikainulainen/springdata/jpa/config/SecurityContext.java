package net.petrikainulainen.springdata.jpa.config;

import net.petrikainulainen.springdata.jpa.web.security.CsrfHeaderFilter;
import net.petrikainulainen.springdata.jpa.web.security.RestAuthenticationEntryPoint;
import net.petrikainulainen.springdata.jpa.web.security.RestAuthenticationFailureHandler;
import net.petrikainulainen.springdata.jpa.web.security.RestAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

/**
 * @author Petri Kainulainen
 */
@Configuration
@EnableWebSecurity
class SecurityContext extends WebSecurityConfigurerAdapter {

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                    .withUser("user")
                        .password("password")
                        .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Use the custom authentication entry point.
                .exceptionHandling()
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .and()
                //Configure form login.
                .formLogin()
                    .loginProcessingUrl("/api/login")
                    .failureHandler(authenticationFailureHandler())
                    .successHandler(authenticationSuccessHandler())
                    .permitAll()
                    .and()
                //Configure logout function.
                .logout()
                    .deleteCookies("JSESSIONID")
                    .logoutUrl("/api/logout")
                    .logoutSuccessUrl("/")
                    .and()
                //Configure url based authorization
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/api/csrf"
                    ).permitAll()
                    .anyRequest().hasRole("USER")
                    .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                //Spring Security ignores request to static resources such as CSS or JS files.
                .ignoring()
                .antMatchers(
                        "/favicon.ico",
                        "/css/**",
                        "/i18n/**",
                        "/js/**"
                    );
    }
}

package net.petrikainulainen.springdata.jpa.web.security;

import org.slf4j.Logger;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter reads the {@link org.springframework.security.web.csrf.CsrfToken} from the {@link HttpServletRequest} and
 * sets its content to the {@link HttpServletResponse} headers.
 *
 * I borrowed this idea from this <a href="http://stackoverflow.com/questions/20862299/with-spring-security-3-2-0-release-how-can-i-get-the-csrf-token-in-a-page-that">StackOverflow question</a>.
 *
 * @author Petri Kainulainen
 */
public class CsrfHeaderFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CsrfHeaderFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        LOGGER.trace("Reading CSRF token from the request.");

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (token != null) {
            LOGGER.trace("CSRF token was found. Creating HTTP response headers.");
            response.setHeader("X-CSRF-HEADER", token.getHeaderName());
            response.setHeader("X-CSRF-PARAM", token.getParameterName());
            response.setHeader("X-CSRF-TOKEN", token.getToken());
        }
        else {
            LOGGER.trace("CSRF Token was not found. Doing nothing.");
        }

        filterChain.doFilter(request, response);
    }
}

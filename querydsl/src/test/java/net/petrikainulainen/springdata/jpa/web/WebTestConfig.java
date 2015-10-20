package net.petrikainulainen.springdata.jpa.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import net.petrikainulainen.springdata.jpa.web.error.RestErrorHandler;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

/**
 * This factory class provides methods that can be used to create objects that are useful
 * when we are writing unit tests for our controller methods by using the Spring MVC Test
 * framework.
 *
 * @author Petri Kainulainen
 */
final class WebTestConfig {

    private WebTestConfig() {}

    /**
     * Configures a {@link org.springframework.web.servlet.LocaleResolver} that always returns the
     * configured {@link java.util.Locale}.
     *
     * @return
     */
    static LocaleResolver fixedLocaleResolver(Locale fixedLocale) {
        return new FixedLocaleResolver(fixedLocale);
    }

    /**
     * This method creates a custom {@link org.springframework.http.converter.HttpMessageConverter} which ensures that:
     *
     * <ul>
     *     <li>Null values are ignored.</li>
     *     <li>
     *         The new Java 8 date objects are serialized in standard
     *         <a href="http://en.wikipedia.org/wiki/ISO_8601" target="_blank">ISO-8601</a> string representation.
     *     </li>
     * </ul>
     *
     * @return
     */
    static MappingJackson2HttpMessageConverter jacksonDateTimeConverter() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JSR310Module());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    /**
     * This method ensures that the {@link RestErrorHandler} class
     * is used to handle the exceptions thrown by the tested controller. I borrowed this idea from
     * <a href="http://stackoverflow.com/a/27195332/313554" target="_blank">this StackOverflow answer</a>.
     *
     * @return an error handler component that delegates relevant exceptions forward to the {@link RestErrorHandler} class.
     */
    static ExceptionHandlerExceptionResolver restErrorHandler(MessageSource messageSource) {
        final ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(final HandlerMethod handlerMethod,
                                                                              final Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestErrorHandler.class).resolveMethod(exception);
                if (method != null) {
                    return new ServletInvocableHandlerMethod(new RestErrorHandler(messageSource), method);
                }
                return super.getExceptionHandlerMethod(handlerMethod, exception);
            }
        };
        exceptionResolver.setMessageConverters(Arrays.asList(jacksonDateTimeConverter()));
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    /**
     * This method returns a {@link org.springframework.web.method.support.HandlerMethodArgumentResolver} that can
     * construct {@link org.springframework.data.domain.Sort} objects by using the request params of the
     * incoming request.
     * @return
     */
    static SortHandlerMethodArgumentResolver sortArgumentResolver() {
        return new SortHandlerMethodArgumentResolver();
    }

    /**
     * This method returns a {@link org.springframework.web.method.support.HandlerMethodArgumentResolver} that can
     * construct {@link org.springframework.data.domain.Pageable} objects by using the request params of the
     * incoming request.
     * @return
     */
    static PageableHandlerMethodArgumentResolver pageRequestArgumentResolver() {
        return new PageableHandlerMethodArgumentResolver(sortArgumentResolver());
    }

    /**
     * This method creates a validator object that adds support for bean validation API 1.0 and 1.1.
     *
     * @return  The created validator object.
     */
    static LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}

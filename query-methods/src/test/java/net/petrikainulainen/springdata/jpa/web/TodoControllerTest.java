package net.petrikainulainen.springdata.jpa.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import net.petrikainulainen.springdata.jpa.todo.TodoCrudService;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import net.petrikainulainen.springdata.jpa.todo.TodoDTOBuilder;
import net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Petri Kainulainen
 */
@RunWith(MockitoJUnitRunner.class)
public class TodoControllerTest {

    private static final Locale CURRENT_LOCALE = Locale.US;
    private static final String CREATION_TIME = "2014-12-24T22:28:39+02:00";
    private static final String DESCRIPTION = "description";
    private static final String ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND = "No todo entry was found by using id: 1";
    private static final Long ID = 1L;
    private static final String MODIFICATION_TIME = "2014-12-24T14:28:39+02:00";
    private static final String TITLE = "title";

    private MockMvc mockMvc;

    @Mock
    private TodoCrudService crudService;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoController(crudService))
                .setHandlerExceptionResolvers(restErrorHandler())
                .setLocaleResolver(fixedLocaleResolver())
                .setMessageConverters(jacksonDateTimeConverter())
                .build();
    }

    /**
     * This method ensures that the {@link net.petrikainulainen.springdata.jpa.web.RestErrorHandler} class
     * is used to handle the exceptions thrown by the tested controller. I borrowed this idea from
     * <a href="http://stackoverflow.com/a/27195332/313554" target="_blank">this StackOverflow answer</a>.
     *
     * @return an error handler component that delegates relevant exceptions forward to the {@link net.petrikainulainen.springdata.jpa.web.RestErrorHandler} class.
     */
    private ExceptionHandlerExceptionResolver restErrorHandler() {
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
     * Configures a {@link org.springframework.web.servlet.LocaleResolver} that always returns the
     * configured {@link java.util.Locale}.
     *
     * @return
     */
    private LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(CURRENT_LOCALE);
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
    private MappingJackson2HttpMessageConverter jacksonDateTimeConverter() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JSR310Module());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Test
    public void findAll_ShouldReturnResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAll_NoTodoEntriesFound_ShouldReturnEmptyListAsJson() throws Exception {
        given(crudService.findAll()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/todo"))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void findAll_OneTodoEntryFound_ShouldReturnOneTodoEntryAsJson() throws Exception {
        TodoDTO found = new TodoDTOBuilder()
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .title(TITLE)
                .build();

        given(crudService.findAll()).willReturn(Arrays.asList(found));

        mockMvc.perform(get("/api/todo"))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].creationTime", is(CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$[0].id", is(ID.intValue())))
                .andExpect(jsonPath("$[0].modificationTime", is(MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(TITLE)));
    }

    @Test
    public void findById_TodoEntryNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        given(crudService.findById(ID)).willThrow(new TodoNotFoundException(ID));

        mockMvc.perform(get("/api/todo/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findId_TodoEntryNotFound_ShouldReturnErrorMessageAsJson() throws Exception {
        given(crudService.findById(ID)).willThrow(new TodoNotFoundException(ID));
        given(messageSource.getMessage(
                isA(MessageSourceResolvable.class),
                isA(Locale.class))
        ).willReturn(ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND);

        mockMvc.perform(get("/api/todo/{id}", ID))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("NOT_FOUND")))
                .andExpect(jsonPath("message", is(ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND)));
    }

    @Test
    public void findById_TodoEntryFound_ShouldReturnInformationOfFoundTodoEntryAsJson() throws Exception {
        TodoDTO found = new TodoDTOBuilder()
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .title(TITLE)
                .build();

        given(crudService.findById(ID)).willReturn(found);

        mockMvc.perform(get("/api/todo/{id}", ID))
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.creationTime", is(CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.modificationTime", is(MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(TITLE)));
    }
}

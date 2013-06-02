package net.petrikainulainen.spring.datajpa.controller;

import net.petrikainulainen.spring.datajpa.context.TestContext;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * An abstract base class for all controller unit tests.
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public abstract class AbstractTestController {

    protected static final String ERROR_MESSAGE = "errorMessage";
    protected static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private static final String FLASH_ERROR_MESSAGE = "errorMessage";
    private static final String FLASH_FEEDBACK_MESSAGE = "feedbackMessage";
    
    private static final String VIEW_REDIRECT_PREFIX = "redirect:";

    private MessageSource messageSourceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        messageSourceMock = mock(MessageSource.class);
        setUpTest();
    }

    protected abstract void setUpTest();

    /**
     * Asserts that an error message is present.
     * @param model The model which is used to store the error message.
     * @param messageCode   The message code of the expected error message.
     */
    protected void assertErrorMessage(RedirectAttributes model, String messageCode) {
        assertFlashMessages(model, messageCode, FLASH_ERROR_MESSAGE);
    }

    /**
     * Asserts that a feedback message is present.
     * @param model The model which is used to store the feedback message.
     * @param messageCode
     */
    protected void assertFeedbackMessage(RedirectAttributes model, String messageCode) {
        assertFlashMessages(model, messageCode, FLASH_FEEDBACK_MESSAGE);
    }

    private void assertFlashMessages(RedirectAttributes model, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = model.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);
        assertNotNull(message);
        flashMessages.remove(message);
        assertTrue(flashMessages.isEmpty());

        verify(messageSourceMock, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
        verifyNoMoreInteractions(messageSourceMock);
    }

    /**
     * Asserts that the binding result contains specified field errors.
     * @param result    The binding result
     * @param fieldNames    The names which should have validation errors.
     */
    protected void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }

    /**
     * Binds and validates the given form object.
     * @param request   The http servlet request object.
     * @param formObject    A form object.
     * @return  A binding result containing the outcome of binding and validation.
     */
    protected BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }

    /**
     * Creates an expected redirect view path.
     * @param path  The path to the requested view.
     * @return  The expected redirect view path.
     */
    protected String createExpectedRedirectViewPath(String path) {
        StringBuilder builder = new StringBuilder();
        builder.append(VIEW_REDIRECT_PREFIX);
        builder.append(path);
        return builder.toString();
    }

    /**
     * Initializes the message source mock to return an error message when
     * the error message code given as a a parameter is used to get message
     * from message source.
     * @param errorMessageCode  The wanted error message code.
     */
    protected void initMessageSourceForErrorMessage(String errorMessageCode) {
        when(messageSourceMock.getMessage(eq(errorMessageCode), any(Object[].class), any(Locale.class))).thenReturn(ERROR_MESSAGE);
    }

    /**
     * Initializes the message source mock to return a feedback message when
     * the feedback message code given as a parameter is used to get message
     * from message source.
     * @param feedbackMessageCode   The wanted feedback message code.
     */
    protected void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }

    /**
     * Returns the message source mock.
     * @return
     */
    protected MessageSource getMessageSourceMock() {
        return messageSourceMock;
    }
}

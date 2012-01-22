package net.petrikainulainen.spring.datajpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * An abstract controller class which provides utility methods useful
 * to actual controller classes.
 * @author Petri Kainulainen
 */
public abstract class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    private static final String FLASH_ERROR_MESSAGE = "errorMessage";
    private static final String FLASH_FEEDBACK_MESSAGE = "feedbackMessage";

    private static final String VIEW_REDIRECT_PREFIX = "redirect:";

    @Resource
    private MessageSource messageSource;

    /**
     * Adds a new error message
     * @param model A model which stores the the error message.
     * @param code  A message code which is used to fetch the correct message from the message source.
     * @param params    The parameters attached to the actual error message.
     */
    protected void addErrorMessage(RedirectAttributes model, String code, Object... params) {
        LOGGER.debug("adding error message with code: " + code + " and params: " + params);
        Locale current = LocaleContextHolder.getLocale();
        LOGGER.debug("Current locale is " + current);
        String localizedErrorMessage = messageSource.getMessage(code, params, current);
        LOGGER.debug("Localized message is: " + localizedErrorMessage);
        model.addFlashAttribute(FLASH_ERROR_MESSAGE, localizedErrorMessage);
    }

    /**
     * Adds a new feedback message.
     * @param model A model which stores the feedback message.
     * @param code  A message code which is used to fetch the actual message from the message source.
     * @param params    The parameters which are attached to the actual feedback message.
     */
    protected void addFeedbackMessage(RedirectAttributes model, String code, Object... params) {
        LOGGER.debug("Adding feedback message with code: " + code + " and params: " + params);
        Locale current = LocaleContextHolder.getLocale();
        LOGGER.debug("Current locale is " + current);
        String localizedFeedbackMessage = messageSource.getMessage(code, params, current);
        LOGGER.debug("Localized message is: " + localizedFeedbackMessage);
        model.addFlashAttribute(FLASH_FEEDBACK_MESSAGE, localizedFeedbackMessage);
    }

    /**
     * Creates a redirect view path for a specific controller action
     * @param path  The path processed by the controller method.
     * @return  A redirect view path to the given controller method.
     */
    protected String createRedirectViewPath(String path) {
        StringBuilder builder = new StringBuilder();
        builder.append(VIEW_REDIRECT_PREFIX);
        builder.append(path);
        return builder.toString();
    }

    /**
     * This method should only be used by unit tests.
     * @param messageSource
     */
    protected void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}

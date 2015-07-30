package net.petrikainulainen.springdata.jpa.web.error;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.todo.TodoDTO;
import net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Locale;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class RestErrorHandlerTest {

    private static final Locale CURRENT_LOCALE = Locale.US;

    private static final Long TODO_ID = 99L;

    private MessageSource messageSource;

    private RestErrorHandler errorHandler;

    @Before
    public void setUp() {
        messageSource = mock(MessageSource.class);
        this.errorHandler = new RestErrorHandler(messageSource);
    }

    public class HandleTodoEntryNotFound {

        private static final String ERROR_CODE_TODO_ENTRY_NOT_FOUND = "NOT_FOUND";

        private static final String ERROR_MESSAGE_CODE_TODO_ENTRY_NOT_FOUND = "error.todo.entry.not.found";
        private static final String ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND = "No todo entry was found by using id: 99";

        @Before
        public void returnErrorMessageNotFound() {
            given(messageSource.getMessage(
                            isA(MessageSourceResolvable.class),
                            isA(Locale.class))
            ).willReturn(ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND);
        }

        @Test
        public void shouldFindErrorMessageByUsingCurrentLocale() {
            errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

            verify(messageSource, times(1)).getMessage(isA(MessageSourceResolvable.class), eq(CURRENT_LOCALE));
        }

        @Test
        public void shouldFindErrorMessageByUsingCorrectId() {
            errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

            verify(messageSource, times(1)).getMessage(
                    assertArg(messageRequest -> assertThat(messageRequest.getArguments())
                                    .containsOnly(TODO_ID)
                    ),
                    eq(CURRENT_LOCALE)
            );
        }

        @Test
        public void shouldFindErrorMessageByUsingCorrectMessageCode() {
            errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

            verify(messageSource, times(1)).getMessage(
                    assertArg(messageRequest -> assertThat(messageRequest.getCodes())
                                    .containsOnly(ERROR_MESSAGE_CODE_TODO_ENTRY_NOT_FOUND)
                    ),
                    eq(CURRENT_LOCALE)
            );
        }

        @Test
        public void shouldReturnErrorThatHasCorrectErrorCode() {
            ErrorDTO error = errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

            assertThat(error.getCode()).isEqualTo(ERROR_CODE_TODO_ENTRY_NOT_FOUND);
        }

        @Test
        public void shouldReturnErrorThatHasCorrectMessage() {
            ErrorDTO error = errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

            assertThat(error.getMessage()).isEqualTo(ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND);
        }
    }

    public class HandleValidationErrors {

        private static final String ERROR_CODE_VALIDATION_ERROR = "BAD_REQUEST";
        private static final String ERROR_MESSAGE_VALIDATION_ERROR = "validationError";

        private static final String FIELD_DEFAULT_MESSAGE = "DefaultMessage";
        private static final String FIELD_WITH_VALIDATION_ERROR = "field";
        private static final String OBJECT_WITH_VALIDATION_ERROR = "todoDTO";

        private static final String VALIDATION_ERROR_CODE_ACCURATE = "Error";
        private static final String VALIDATION_ERROR_CODE_LESS_ACCURATE = "Maybe";

        public class WhenOneValidationErrorIsFound {

            public class WhenMessageIsFound {

                private MethodArgumentNotValidException ex;

                @Before
                public void createValidationErrorAndReturnErrorMessage() {
                    FieldError fieldError = new FieldErrorBuilder()
                            .defaultMessage(FIELD_DEFAULT_MESSAGE)
                            .fieldName(FIELD_WITH_VALIDATION_ERROR)
                            .build();
                    given(messageSource.getMessage(fieldError, CURRENT_LOCALE)).willReturn(ERROR_MESSAGE_VALIDATION_ERROR);

                    ex = createExceptionWithFieldErrors(fieldError);
                }

                @Test
                public void shouldReturnErrorThatHasCorrectCode() {
                    ValidationErrorDTO validationErrors = errorHandler.handleValidationErrors(ex, CURRENT_LOCALE);

                    assertThat(validationErrors.getCode()).isEqualTo(ERROR_CODE_VALIDATION_ERROR);
                }

                @Test
                public void shouldReturnErrorThatHasCorrectFieldErrorWithMessage() {
                    ValidationErrorDTO validationErrors = errorHandler.handleValidationErrors(ex, CURRENT_LOCALE);

                    List<FieldErrorDTO> fieldErrors = validationErrors.getFieldErrors();
                    assertThat(fieldErrors).hasSize(1);

                    FieldErrorDTO actualFieldError = fieldErrors.iterator().next();
                    assertThat(actualFieldError.getField()).isEqualTo(FIELD_WITH_VALIDATION_ERROR);
                    assertThat(actualFieldError.getMessage()).isEqualTo(ERROR_MESSAGE_VALIDATION_ERROR);
                }
            }

            public class WhenMessageIsNotFound {

                private MethodArgumentNotValidException ex;

                @Before
                public void createValidationErrorAndReturnDefaultErrorMessage() {
                    FieldError fieldError = new FieldErrorBuilder()
                            .defaultMessage(FIELD_DEFAULT_MESSAGE)
                            .errorCodes(VALIDATION_ERROR_CODE_ACCURATE, VALIDATION_ERROR_CODE_LESS_ACCURATE)
                            .fieldName(FIELD_WITH_VALIDATION_ERROR)
                            .build();
                    given(messageSource.getMessage(fieldError, CURRENT_LOCALE)).willReturn(FIELD_DEFAULT_MESSAGE);

                    ex = createExceptionWithFieldErrors(fieldError);
                }

                @Test
                public void shouldReturnErrorThatHasCorrectCode() {
                    ValidationErrorDTO validationErrors = errorHandler.handleValidationErrors(ex, CURRENT_LOCALE);

                    assertThat(validationErrors.getCode()).isEqualTo(ERROR_CODE_VALIDATION_ERROR);
                }

                @Test
                public void shouldReturnErrorThatHasFieldErrorWithMostAccurateFieldErrorCode() {
                    ValidationErrorDTO validationErrors = errorHandler.handleValidationErrors(ex, CURRENT_LOCALE);

                    List<FieldErrorDTO> fieldErrors = validationErrors.getFieldErrors();
                    assertThat(fieldErrors).hasSize(1);

                    FieldErrorDTO actualFieldError = fieldErrors.iterator().next();
                    assertThat(actualFieldError.getField()).isEqualTo(FIELD_WITH_VALIDATION_ERROR);
                    assertThat(actualFieldError.getMessage()).isEqualTo(VALIDATION_ERROR_CODE_ACCURATE);
                }
            }
        }

        private MethodArgumentNotValidException createExceptionWithFieldErrors(FieldError... fieldErrors) {
            BindingResult bindingResult = new BeanPropertyBindingResult(new TodoDTO(), OBJECT_WITH_VALIDATION_ERROR);

            for (FieldError fieldError: fieldErrors) {
                bindingResult.addError(fieldError);
            }

            return new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult);
        }


        private final class FieldErrorBuilder {

            private String defaultMessage;
            private String[] errorCodes;
            private String fieldName;

            private FieldErrorBuilder() {}

            private FieldErrorBuilder defaultMessage(String defaultMessage) {
                this.defaultMessage = defaultMessage;
                return this;
            }

            private FieldErrorBuilder errorCodes(String... errorCodes) {
                this.errorCodes = errorCodes;
                return this;
            }

            private FieldErrorBuilder fieldName(String fieldName) {
                this.fieldName = fieldName;
                return this;
            }

            private FieldError build() {
                return new FieldError(OBJECT_WITH_VALIDATION_ERROR,
                        fieldName,
                        null,
                        false,
                        errorCodes,
                        new Object[]{},
                        defaultMessage
                );
            }
        }
    }
}

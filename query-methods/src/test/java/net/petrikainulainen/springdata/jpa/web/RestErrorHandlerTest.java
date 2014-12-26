package net.petrikainulainen.springdata.jpa.web;

import net.petrikainulainen.springdata.jpa.todo.TodoNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Petri Kainulainen
 */
@RunWith(MockitoJUnitRunner.class)
public class RestErrorHandlerTest {

    private static final Locale CURRENT_LOCALE = Locale.US;
    private static final String ERROR_CODE_TODO_ENTRY_NOT_FOUND = "NOT_FOUND";
    private static final String ERROR_MESSAGE_CODE_TODO_ENTRY_NOT_FOUND = "error.todo.entry.not.found";
    private static final String ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND = "No todo entry was found by using id: 99";
    private static final Long TODO_ID = 99L;

    @Mock
    private MessageSource messageSource;

    private RestErrorHandler errorHandler;

    @Before
    public void setUp() {
        this.errorHandler = new RestErrorHandler(messageSource);
    }

    @Test
    public void handleTodoEntryNotFound_ShouldFindErrorMessageByUsingCurrentLocale() {
        errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

        verify(messageSource, times(1)).getMessage(isA(MessageSourceResolvable.class), eq(CURRENT_LOCALE));
    }

    @Test
    public void handleTodoEntryNotFound_ShouldFindErrorMessageByUsingCorrectMessageCode() {
        errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

        ArgumentCaptor<MessageSourceResolvable> messageRequestArgument = ArgumentCaptor.forClass(MessageSourceResolvable.class);
        verify(messageSource, times(1)).getMessage(messageRequestArgument.capture(), eq(CURRENT_LOCALE));

        MessageSourceResolvable messageRequest = messageRequestArgument.getValue();
        assertThat(messageRequest.getCodes())
                .containsOnly(ERROR_MESSAGE_CODE_TODO_ENTRY_NOT_FOUND);
    }

    @Test
    public void handleTodoEntryNotFound_ShouldFindErrorMessageByUsingCorrectId() {
        errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

        ArgumentCaptor<MessageSourceResolvable> messageRequestArgument = ArgumentCaptor.forClass(MessageSourceResolvable.class);
        verify(messageSource, times(1)).getMessage(messageRequestArgument.capture(), eq(CURRENT_LOCALE));

        MessageSourceResolvable messageRequest = messageRequestArgument.getValue();
        assertThat(messageRequest.getArguments())
                .containsOnly(TODO_ID);
    }

    @Test
    public void handleTodoEntryNotFound_ShouldReturnErrorThatHasCorrectErrorCodeAndMessage() {
        given(messageSource.getMessage(
                isA(MessageSourceResolvable.class),
                isA(Locale.class)
        )).willReturn(ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND);

        ErrorDTO error = errorHandler.handleTodoEntryNotFound(new TodoNotFoundException(TODO_ID), CURRENT_LOCALE);

        assertThat(error.getCode()).isEqualTo(ERROR_CODE_TODO_ENTRY_NOT_FOUND);
        assertThat(error.getMessage()).isEqualTo(ERROR_MESSAGE_TODO_ENTRY_NOT_FOUND);
    }
}

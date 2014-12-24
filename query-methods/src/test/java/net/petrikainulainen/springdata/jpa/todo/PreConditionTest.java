package net.petrikainulainen.springdata.jpa.todo;

import org.junit.Test;

import static net.petrikainulainen.springdata.jpa.todo.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class PreConditionTest {

    private static final String STATIC_ERROR_MESSAGE = "static error message";

    @Test
    public void isTrueWithDynamicErrorMessage_ExpressionIsTrue_ShouldNotThrowException() {
        PreCondition.isTrue(true, "Dynamic error message with parameter: %d", 1L);
    }

    @Test
    public void isTrueWithDynamicErrorMessage_ExpressionIsFalse_ShouldThrowException() {
        Throwable thrown = thrown(() -> PreCondition.isTrue(false, "Dynamic error message with parameter: %d", 1L));

        assertThat(thrown)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dynamic error message with parameter: 1");
    }

    @Test
    public void isTrueWithStaticErrorMessage_ExpressionIsTrue_ShouldNotThrowException() {
        PreCondition.isTrue(true, STATIC_ERROR_MESSAGE);
    }

    @Test
    public void isTrueWithStaticErrorMessage_ExpressionIsFalse_ShouldThrowException() {
        Throwable thrown = thrown(() -> PreCondition.isTrue(false, STATIC_ERROR_MESSAGE));

        assertThat(thrown)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notEmpty_StringIsNotEmpty_ShouldNotThrowException() {
        PreCondition.notEmpty("  ", STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notEmpty_StringIsEmpty_ShouldThrowException() {
        Throwable thrown = thrown(() -> PreCondition.notEmpty("", STATIC_ERROR_MESSAGE));

        assertThat(thrown)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notNull_ObjectIsNotNull_ShouldNotThrowException() {
        PreCondition.notNull(new Object(), STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notNull_ObjectIsNull_ShouldThrowException() {
        Throwable thrown = thrown(() -> PreCondition.notNull(null, STATIC_ERROR_MESSAGE));

        assertThat(thrown)
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage(STATIC_ERROR_MESSAGE);
    }
}

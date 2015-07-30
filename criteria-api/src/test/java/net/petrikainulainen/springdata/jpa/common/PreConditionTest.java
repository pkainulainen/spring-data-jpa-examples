package net.petrikainulainen.springdata.jpa.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> PreCondition.isTrue(false, "Dynamic error message with parameter: %d", 1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dynamic error message with parameter: 1");
    }

    @Test
    public void isTrueWithStaticErrorMessage_ExpressionIsTrue_ShouldNotThrowException() {
        PreCondition.isTrue(true, STATIC_ERROR_MESSAGE);
    }

    @Test
    public void isTrueWithStaticErrorMessage_ExpressionIsFalse_ShouldThrowException() {
        assertThatThrownBy(() -> PreCondition.isTrue(false, STATIC_ERROR_MESSAGE))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notEmpty_StringIsNotEmpty_ShouldNotThrowException() {
        PreCondition.notEmpty("  ", STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notEmpty_StringIsEmpty_ShouldThrowException() {
        assertThatThrownBy(() -> PreCondition.notEmpty("", STATIC_ERROR_MESSAGE))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notNull_ObjectIsNotNull_ShouldNotThrowException() {
        PreCondition.notNull(new Object(), STATIC_ERROR_MESSAGE);
    }

    @Test
    public void notNull_ObjectIsNull_ShouldThrowException() {
        assertThatThrownBy(() -> PreCondition.notNull(null, STATIC_ERROR_MESSAGE))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage(STATIC_ERROR_MESSAGE);
    }
}

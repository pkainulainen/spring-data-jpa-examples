package net.petrikainulainen.springdata.jpa.web;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class ErrorDTOTest {

    private static final String CODE = "code";
    private static final String MESSAGE = "message";

    @Test(expected = NullPointerException.class)
    public void createNew_CodeIsNull_ShouldThrowException() {
        new ErrorDTO(null, MESSAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNew_CodeIsEmpty_ShouldThrowException() {
        new ErrorDTO("", MESSAGE);
    }

    @Test(expected = NullPointerException.class)
    public void createNew_MessageIsNull_ShouldThrowException() {
        new ErrorDTO(CODE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNew_MessageIsEmpty_ShouldThrowException() {
        new ErrorDTO(CODE, "");
    }

    @Test
    public void createNew_ValidCodeAndMessageGiven_ShouldCreateNewObjectAndSetCode() {
        ErrorDTO error = new ErrorDTO(CODE, MESSAGE);
        assertThat(error.getCode()).isEqualTo(CODE);
    }

    @Test
    public void createNew_ValidCodeAndMessageGiven_ShouldCreateNewObjectAndSetMessage() {
        ErrorDTO error = new ErrorDTO(CODE, MESSAGE);
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }
}

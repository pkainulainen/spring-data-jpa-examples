package net.petrikainulainen.springdata.jpa.web;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class FieldErrorDTOTest {

    private static final String FIELD = "field";
    private static final String MESSAGE = "message";

    @Test(expected = NullPointerException.class)
    public void createNew_FieldIsNull_ShouldThrowException() {
        new FieldErrorDTO(null, MESSAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNew_FieldIsEmpty_ShouldThrowException() {
        new FieldErrorDTO("", MESSAGE);
    }

    @Test(expected = NullPointerException.class)
    public void createNew_MessageIsNull_ShouldThrowException() {
        new FieldErrorDTO(FIELD, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNew_MessageIsEmpty_ShouldThrowException() {
        new FieldErrorDTO(FIELD, "");
    }

    @Test
    public void createNew_LegalFieldAndMessageGiven_ShouldCreateNewObjectAndSetField() {
        FieldErrorDTO fieldError = new FieldErrorDTO(FIELD, MESSAGE);

        assertThat(fieldError.getField()).isEqualTo(FIELD);
    }

    @Test
    public void createNew_LegalFieldAndMessageGiven_ShouldCreateNewObjectAndSetMessage() {
        FieldErrorDTO fieldError = new FieldErrorDTO(FIELD, MESSAGE);

        assertThat(fieldError.getMessage()).isEqualTo(MESSAGE);
    }
}

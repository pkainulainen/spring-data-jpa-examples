package net.petrikainulainen.springdata.jpa.web;

import org.junit.Test;

import java.util.List;

import static net.petrikainulainen.springdata.jpa.common.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class ValidationErrorDTOTest {

    private static final String FIELD = "field";
    private static final String MESSAGE = "message";

    @Test(expected = NullPointerException.class)
    public void addFieldError_FieldIsNull_ShouldThrowException() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();
        validationErrors.addFieldError(null, MESSAGE);
    }

    @Test
    public void addFieldError_FieldIsNull_ShouldNotCreateNewFieldError() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();

        thrown(() -> validationErrors.addFieldError(null, MESSAGE));

        assertThat(validationErrors.getFieldErrors()).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFieldError_FieldIsEmpty_ShouldThrowException() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();
        validationErrors.addFieldError("", MESSAGE);
    }

    @Test
    public void addFieldError_FieldIsEmpty_ShouldNotCreateNewFieldError() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();

        thrown(() -> validationErrors.addFieldError("", MESSAGE));

        assertThat(validationErrors.getFieldErrors()).isEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void addFieldError_MessageIsNull_ShouldThrowException() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();
        validationErrors.addFieldError(FIELD, null);
    }

    @Test
    public void addFieldError_MessageIsNull_ShouldNotCreateNewFieldError() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();

        thrown(() -> validationErrors.addFieldError(FIELD, null));

        assertThat(validationErrors.getFieldErrors()).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFieldError_MessageIsEmpty_ShouldThrowException() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();
        validationErrors.addFieldError(FIELD, "");
    }

    @Test
    public void addFieldError_MessageIsEmpty_ShouldNotCreateNewFieldError() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();

        thrown(() -> validationErrors.addFieldError(FIELD, ""));

        assertThat(validationErrors.getFieldErrors()).isEmpty();
    }

    @Test
    public void addFieldError_LegalFieldAndMessageGiven_ShouldCreateNewFieldError() {
        ValidationErrorDTO validationErrors = new ValidationErrorDTO();
        validationErrors.addFieldError(FIELD, MESSAGE);

        List<FieldErrorDTO> fieldErrors = validationErrors.getFieldErrors();
        assertThat(fieldErrors).hasSize(1);

        FieldErrorDTO fieldError = fieldErrors.iterator().next();

        assertThat(fieldError.getField()).isEqualTo(FIELD);
        assertThat(fieldError.getMessage()).isEqualTo(MESSAGE);
    }
}

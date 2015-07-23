package net.petrikainulainen.springdata.jpa.web.error;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.web.error.FieldErrorDTO;
import net.petrikainulainen.springdata.jpa.web.error.ValidationErrorDTO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class ValidationErrorDTOTest {

    private static final String FIELD = "field";
    private static final String MESSAGE = "message";

    public class AddFieldError {

        public class WhenFieldIsInvalid {

            public class WhenFieldIsNull {

                @Test(expected = NullPointerException.class)
                public void shouldThrowException() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();
                    validationErrors.addFieldError(null, MESSAGE);
                }

                @Test
                public void shouldNotCreateNewFieldError() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();

                    catchThrowable(() -> validationErrors.addFieldError(null, MESSAGE));

                    assertThat(validationErrors.getFieldErrors()).isEmpty();
                }
            }

            public class WhenFieldIsEmpty {

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();
                    validationErrors.addFieldError("", MESSAGE);
                }

                @Test
                public void shouldNotCreateNewFieldError() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();

                    catchThrowable(() -> validationErrors.addFieldError("", MESSAGE));

                    assertThat(validationErrors.getFieldErrors()).isEmpty();
                }
            }
        }

        public class WhenMessageIsInvalid {

            public class WhenMessageIsNull {

                @Test(expected = NullPointerException.class)
                public void shouldThrowException() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();
                    validationErrors.addFieldError(FIELD, null);
                }

                @Test
                public void shouldNotCreateNewFieldError() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();

                    catchThrowable(() -> validationErrors.addFieldError(FIELD, null));

                    assertThat(validationErrors.getFieldErrors()).isEmpty();
                }
            }

            public class WhenMessageIsEmpty {

                @Test(expected = IllegalArgumentException.class)
                public void shouldThrowException() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();
                    validationErrors.addFieldError(FIELD, "");
                }

                @Test
                public void shouldNotCreateNewFieldError() {
                    ValidationErrorDTO validationErrors = new ValidationErrorDTO();

                    catchThrowable(() -> validationErrors.addFieldError(FIELD, ""));

                    assertThat(validationErrors.getFieldErrors()).isEmpty();
                }
            }
        }

        public class WhenFieldAndMessageAreValid {

            @Test
            public void shouldCreateNewFieldErrorAndSetField() {
                ValidationErrorDTO validationErrors = new ValidationErrorDTO();
                validationErrors.addFieldError(FIELD, MESSAGE);

                List<FieldErrorDTO> fieldErrors = validationErrors.getFieldErrors();
                assertThat(fieldErrors).hasSize(1);

                FieldErrorDTO fieldError = fieldErrors.iterator().next();

                assertThat(fieldError.getField()).isEqualTo(FIELD);
            }

            @Test
            public void shouldCreateNewFieldErrorAndSetMessage() {
                ValidationErrorDTO validationErrors = new ValidationErrorDTO();
                validationErrors.addFieldError(FIELD, MESSAGE);

                List<FieldErrorDTO> fieldErrors = validationErrors.getFieldErrors();
                assertThat(fieldErrors).hasSize(1);

                FieldErrorDTO fieldError = fieldErrors.iterator().next();

                assertThat(fieldError.getMessage()).isEqualTo(MESSAGE);
            }
        }
    }
}

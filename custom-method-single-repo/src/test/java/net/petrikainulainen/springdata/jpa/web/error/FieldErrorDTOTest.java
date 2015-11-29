package net.petrikainulainen.springdata.jpa.web.error;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.web.error.FieldErrorDTO;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class FieldErrorDTOTest {

    private static final String FIELD = "field";
    private static final String MESSAGE = "message";

    public class CreateNew {

        public class WhenFieldIsInvalid {

            @Test(expected = NullPointerException.class)
            public void shouldThrowExceptionWhenFieldIsNull() {
                new FieldErrorDTO(null, MESSAGE);
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenFieldIsEmpty() {
                new FieldErrorDTO("", MESSAGE);
            }
        }

        public class WhenMessageIsInvalid {

            @Test(expected = NullPointerException.class)
            public void shouldThrowExceptionWhenMessageIsNull() {
                new FieldErrorDTO(FIELD, null);
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenMessageIsEmpty() {
                new FieldErrorDTO(FIELD, "");
            }
        }

        public class WhenFieldAndMessageAreValid {

            @Test
            public void shouldCreateNewObjectAndSetField() {
                FieldErrorDTO fieldError = new FieldErrorDTO(FIELD, MESSAGE);

                assertThat(fieldError.getField()).isEqualTo(FIELD);
            }

            @Test
            public void shouldCreateNewObjectAndSetMessage() {
                FieldErrorDTO fieldError = new FieldErrorDTO(FIELD, MESSAGE);

                assertThat(fieldError.getMessage()).isEqualTo(MESSAGE);
            }
        }
    }
}

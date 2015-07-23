package net.petrikainulainen.springdata.jpa.web.error;

import com.nitorcreations.junit.runners.NestedRunner;
import net.petrikainulainen.springdata.jpa.web.error.ErrorDTO;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(NestedRunner.class)
public class ErrorDTOTest {

    private static final String CODE = "code";
    private static final String MESSAGE = "message";

    public class CreateNew {

        public class WhenCodeIsInvalid {
            @Test(expected = NullPointerException.class)
            public void shouldThrowExceptionWhenCodeIsNull() {
                new ErrorDTO(null, MESSAGE);
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenCodeIsEmpty() {
                new ErrorDTO("", MESSAGE);
            }
        }

        public class WhenMessageIsInvalid {

            @Test(expected = NullPointerException.class)
            public void shouldThrowExceptionWhenMessageIsNull() {
                new ErrorDTO(CODE, null);
            }

            @Test(expected = IllegalArgumentException.class)
            public void shouldThrowExceptionWhenMessageIsEmpty() {
                new ErrorDTO(CODE, "");
            }
        }

        public class WhenCodeAndMessageAreValid {

            @Test
            public void shouldCreateNewObjectAndSetCode() {
                ErrorDTO error = new ErrorDTO(CODE, MESSAGE);
                assertThat(error.getCode()).isEqualTo(CODE);
            }

            @Test
            public void shouldCreateNewObjectAndSetMessage() {
                ErrorDTO error = new ErrorDTO(CODE, MESSAGE);
                assertThat(error.getMessage()).isEqualTo(MESSAGE);
            }
        }
    }
}

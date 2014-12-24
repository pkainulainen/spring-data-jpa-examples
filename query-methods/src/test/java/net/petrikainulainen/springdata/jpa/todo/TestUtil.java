package net.petrikainulainen.springdata.jpa.todo;

/**
 * @author Petri Kainulainen
 */
public final class TestUtil {

    private TestUtil() {}

    public static String createStringWithLength(int length) {
        StringBuilder string = new StringBuilder();

        for (int index = 0; index < length; index++) {
            string.append("a");
        }

        return string.toString();
    }
}

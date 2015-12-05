package net.petrikainulainen.springdata.jpa.todo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public static ZonedDateTime parseDateTime(String dateAndTime) {
        return ZonedDateTime.parse(dateAndTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}

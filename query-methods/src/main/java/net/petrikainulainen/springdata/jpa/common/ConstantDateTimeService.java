package net.petrikainulainen.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used in our integration tests and it always returns the
 * same time. This gives us the possibility to verify that the correct
 * timestamps are saved to the database.
 *
 * @author Petri Kainulainen
 */
public class ConstantDateTimeService implements DateTimeService {

    public static final String CURRENT_DATE_AND_TIME = "2015-07-19T12:52:28+03:00[Europe/Helsinki]";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantDateTimeService.class);

    @Override
    public ZonedDateTime getCurrentDateAndTime() {
        ZonedDateTime constantDateAndTime = ZonedDateTime.from(FORMATTER.parse(CURRENT_DATE_AND_TIME));

        LOGGER.info("Returning constant date and time: {}", constantDateAndTime);

        return constantDateAndTime;
    }
}

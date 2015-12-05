package net.petrikainulainen.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

/**
 * This class returns the current time.
 *
 * @author Petri Kainulainen
 */
public class CurrentTimeDateTimeService implements DateTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentTimeDateTimeService.class);

    @Override
    public ZonedDateTime getCurrentDateAndTime() {
        ZonedDateTime currentDateAndTime =  ZonedDateTime.now();

        LOGGER.info("Returning current date and time: {}", currentDateAndTime);

        return currentDateAndTime;
    }
}

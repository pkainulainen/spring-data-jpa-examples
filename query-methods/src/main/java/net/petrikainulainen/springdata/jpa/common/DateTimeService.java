package net.petrikainulainen.springdata.jpa.common;

import java.time.ZonedDateTime;

/**
 * This interface defines the methods used to get the current
 * date and time.
 *
 * @author Petri Kainulainen
 */
public interface DateTimeService {

    /**
     * Returns the current date and time.
     * @return
     */
    ZonedDateTime getCurrentDateAndTime();
}

package net.petrikainulainen.springdata.jpa.common;

import org.springframework.data.auditing.DateTimeProvider;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class obtains the current time by using a {@link DateTimeService}
 * object. The reason for this is that we can use a different implementation in our integration tests.
 *
 * In other words:
 * <ul>
 *     <li>
 *         Our application always returns the correct time because it uses the
 *         {@link CurrentTimeDateTimeService} class.
 *     </li>
 *     <li>
 *         When our integration tests are running, we can return a constant time which gives us the possibility
 *         to assert the creation and modification times saved to the database.
 *     </li>
 * </ul>
 *
 * @author Petri Kainulainen
 */
public class AuditingDateTimeProvider implements DateTimeProvider {

    private final DateTimeService dateTimeService;

    public AuditingDateTimeProvider(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public Calendar getNow() {
        return GregorianCalendar.from(dateTimeService.getCurrentDateAndTime());
    }
}

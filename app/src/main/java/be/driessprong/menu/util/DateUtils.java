package be.driessprong.menu.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Simon Raes on 26/01/2015.
 */
public class DateUtils {

    /**
     * Returns the minValue date of the current week.
     */
    public static Date getFirstDateOfWeek() {
        Calendar c = getAdjustedDate();
        // Set calendar to first day of the week.
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return c.getTime();
    }

    /**
     * Returns the maxValue date of the current week.
     */
    public static Date getLastDateOfWeek() {
        Calendar c = getAdjustedDate();
        // Set calendar to last day of the week.
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return c.getTime();
    }


    private static Calendar getAdjustedDate() {
        Calendar c = Calendar.getInstance();
        if (isWeekend()) {
            c.add(Calendar.DATE, 3);
        }
        return c;
    }

    private static boolean isWeekend() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
}

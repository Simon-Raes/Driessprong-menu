package be.driessprong.menu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static String getFormattedDayTitle(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE dd/MM", new Locale("nl-BE"));
        return dayFormat.format(c.getTime());
    }
}

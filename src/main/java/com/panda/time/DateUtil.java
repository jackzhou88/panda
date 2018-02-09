package com.panda.time;

import java.util.Calendar;

/**
 * @author: dave01.zhou  Time: 2018/2/9 13:54
 */
public class DateUtil {
    /**
     * Compute wall time [hour]:[minute]:[second] after specific base time.
     *
     * @param baseDate The base date
     * @param hour     The hour(0-23)
     * @param minute   The minute(0-59)
     * @param second   The second(0-59)
     * @return Calendar - Wall time returned
     */
    public static Calendar computeTime(Calendar baseDate, int hour, int minute, int second) {
        if (baseDate == null || hour < 0
                || minute < 0 || second < 0) {
            return baseDate;
        }

        int currentDayOfWeek = baseDate.get(Calendar.DAY_OF_WEEK);
        int currentHour = baseDate.get(Calendar.HOUR_OF_DAY);
        int currentMinute = baseDate.get(Calendar.MINUTE);
        int currentSecond = baseDate.get(Calendar.SECOND);

        boolean dayLater = false;
        boolean hourLater = false;
        boolean minuteLater = false;
        if (hour < currentHour) {
            dayLater = true;
        }
        if (hour == currentHour && minute < currentMinute) {
            hourLater = true;
        }
        if (hour == currentHour && minute == currentMinute && second < currentSecond) {
            minuteLater = true;
        }

        baseDate.set(Calendar.DAY_OF_WEEK, currentDayOfWeek + (dayLater ? 1 : 0));
        baseDate.set(Calendar.HOUR_OF_DAY, hour + (hourLater ? 1 : 0));
        baseDate.set(Calendar.MINUTE, minute + (minuteLater ? 1 : 0));
        baseDate.set(Calendar.SECOND, second);
        return baseDate;
    }
}

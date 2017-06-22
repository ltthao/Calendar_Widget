package com.example.lttha.a14110180_lethithao_calendarwidget.Untils;

import android.content.Context;
import android.support.v4.util.Pools;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Utility class for {@link Calendar} and date time related operations
 */
public class CalendarUtils {


    public static final long NO_TIME_MILLIS = -1;

    public static final String TIMEZONE_UTC = "UTC";

    public static final String PREF_WEEK_START = "weekStart";
    public static final String PREF_CALENDAR_EXCLUSIONS = "calendarExclusions";

    public static int sWeekStart = Calendar.SUNDAY;// Ngày bắt đầu của tuần là sunday


    public static boolean isNotTime(long timeMillis) {
        return timeMillis == NO_TIME_MILLIS;
    }


    public static long today() {
        DateOnlyCalendar calendar = DateOnlyCalendar.today();
        long timeMillis = calendar.getTimeInMillis();
        calendar.recycle();
        return timeMillis;
    }


    public static String toDayString(Context context, long timeMillis) {
        return DateUtils.formatDateTime(context, timeMillis,
                DateUtils.FORMAT_SHOW_WEEKDAY |
                        DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_NO_YEAR);
    }


    public static String toMonthString(Context context, long timeMillis) {
        return DateUtils.formatDateRange(context, timeMillis, timeMillis,
                DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_NO_MONTH_DAY |
                        DateUtils.FORMAT_SHOW_YEAR);
    }

    public static String toTimeString(Context context, long timeMillis) {
        return DateUtils.formatDateTime(context, timeMillis, DateUtils.FORMAT_SHOW_TIME);
    }


    public static boolean sameMonth(long first, long second) {
        if (isNotTime(first) || isNotTime(second)) {
            return false; // not comparable
        }
        DateOnlyCalendar firstCalendar = DateOnlyCalendar.fromTime(first);
        DateOnlyCalendar secondCalendar = DateOnlyCalendar.fromTime(second);
        boolean same = firstCalendar.sameMonth(secondCalendar);
        firstCalendar.recycle();
        secondCalendar.recycle();
        return same;
    }


    public static int dayOfMonth(long timeMillis) {
        if (isNotTime(timeMillis)) {
            return -1;
        }
        DateOnlyCalendar calendar = DateOnlyCalendar.fromTime(timeMillis);
        //noinspection ConstantConditions
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.recycle();
        return day;
    }


    public static boolean monthBefore(long first, long second) {
        if (isNotTime(first) || isNotTime(second)) {
            return false;
        }
        DateOnlyCalendar firstCalendar = DateOnlyCalendar.fromTime(first);
        DateOnlyCalendar secondCalendar = DateOnlyCalendar.fromTime(second);
        boolean before = firstCalendar.monthBefore(secondCalendar);
        firstCalendar.recycle();
        secondCalendar.recycle();
        return before;
    }


    public static boolean monthAfter(long first, long second) {
        if (isNotTime(first) || isNotTime(second)) {
            return false;
        }
        DateOnlyCalendar firstCalendar = DateOnlyCalendar.fromTime(first);
        DateOnlyCalendar secondCalendar = DateOnlyCalendar.fromTime(second);
        boolean after = firstCalendar.monthAfter(secondCalendar);
        firstCalendar.recycle();
        secondCalendar.recycle();
        return after;
    }

    public static long addMonths(long timeMillis, int months) {
        if (isNotTime(timeMillis)) {
            return NO_TIME_MILLIS;
        }
        DateOnlyCalendar calendar = DateOnlyCalendar.fromTime(timeMillis);
        //noinspection ConstantConditions
        calendar.add(Calendar.MONTH, months);
        long result = calendar.getTimeInMillis();
        calendar.recycle();
        return result;
    }


    public static long monthFirstDay(long monthMillis) {
        if (isNotTime(monthMillis)) {
            return NO_TIME_MILLIS;
        }
        DateOnlyCalendar calendar = DateOnlyCalendar.fromTime(monthMillis);
        //noinspection ConstantConditions
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long result = calendar.getTimeInMillis();
        calendar.recycle();
        return result;
    }

    public static long monthLastDay(long monthMillis) {
        if (isNotTime(monthMillis)) {
            return NO_TIME_MILLIS;
        }
        DateOnlyCalendar calendar = DateOnlyCalendar.fromTime(monthMillis);
        //noinspection ConstantConditions
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long result = calendar.getTimeInMillis();
        calendar.recycle();
        return result;
    }

    public static int monthSize(long monthMillis) {
        if (isNotTime(monthMillis)) {
            return 0;
        }
        DateOnlyCalendar calendar = DateOnlyCalendar.fromTime(monthMillis);
        //noinspection ConstantConditions
        int size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.recycle();
        return size;
    }

//    Bắt đầu bù giờ đầu tiên cho một tháng nhất định,
//    Hoặc số ngày 'trống' trong tuần đầu tiên cho đến ngày đầu tiên của tháng
//    Ví dụ. Nếu ngày đầu tiên của tuần là chủ nhật, và ngày đầu tiên của tháng thứ ba, bù đắp là 2
//    Ví dụ. Nếu ngày đầu tiên của tuần là Thứ Hai, và ngày đầu tiên của tháng là chủ nhật, thì bù đắp là 6
//    trả về ngày đầu tiên bù đắp hoặc 0 nếu tháng không hợp lệ

    public static int monthFirstDayOffset(long monthMillis) {
        if (isNotTime(monthMillis)) {
            return 0;
        }
        DateOnlyCalendar calendar = DateOnlyCalendar.fromTime(monthMillis);
        //noinspection ConstantConditions
        int offset = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        if (offset < 0) {
            offset = 7 + offset;
        }
        calendar.recycle();
        return offset;
    }


    public static long toLocalTimeZone(long utcTimeMillis) {
        return convertTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC), TimeZone.getDefault(),
                utcTimeMillis);
    }


    public static long toUtcTimeZone(long localTimeMillis) {
        return convertTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone(TIMEZONE_UTC),
                localTimeMillis);
    }

    private static long convertTimeZone(TimeZone fromTimeZone, TimeZone toTimeZone, long timeMillis) {
        DateOnlyCalendar fromCalendar = DateOnlyCalendar.obtain();
        fromCalendar.setTimeZone(fromTimeZone);
        fromCalendar.setTimeInMillis(timeMillis);
        DateOnlyCalendar toCalendar = DateOnlyCalendar.obtain();
        toCalendar.setTimeZone(toTimeZone);
        toCalendar.set(fromCalendar.get(Calendar.YEAR),
                fromCalendar.get(Calendar.MONTH),
                fromCalendar.get(Calendar.DAY_OF_MONTH),
                fromCalendar.get(Calendar.HOUR_OF_DAY),
                fromCalendar.get(Calendar.MINUTE),
                fromCalendar.get(Calendar.SECOND));
        long localTimeMillis = toCalendar.getTimeInMillis();
        fromCalendar.recycle();
        toCalendar.recycle();
        return localTimeMillis;

    }


    private static class DateOnlyCalendar extends GregorianCalendar {

        private static Pools.SimplePool<DateOnlyCalendar> sPools = new Pools.SimplePool<>(5);

        private static DateOnlyCalendar obtain() {
            DateOnlyCalendar instance = sPools.acquire();
            return instance == null ? new DateOnlyCalendar() : instance;
        }

        public static DateOnlyCalendar today() {
            return fromTime(System.currentTimeMillis());
        }

        public static DateOnlyCalendar fromTime(long timeMillis) {
            if (timeMillis < 0) {
                return null;
            }
            DateOnlyCalendar dateOnlyCalendar = DateOnlyCalendar.obtain();
            dateOnlyCalendar.setTimeInMillis(timeMillis);
            dateOnlyCalendar.stripTime();
            //noinspection WrongConstant
            dateOnlyCalendar.setFirstDayOfWeek(sWeekStart);
            return dateOnlyCalendar;
        }

        private DateOnlyCalendar() {
            super();
        }

        //Kiểm tra xemcos fai tháng truwosc không
        public boolean monthBefore(DateOnlyCalendar other) {
            int day = other.get(DAY_OF_MONTH);
            other.set(DAY_OF_MONTH, 1);
            boolean before = getTimeInMillis() < other.getTimeInMillis();
            other.set(DAY_OF_MONTH, day);
            return before;
        }

        //Kiểm tra xem có fai tháng sau không
        public boolean monthAfter(DateOnlyCalendar other) {
            int day = other.get(DAY_OF_MONTH);
            other.set(DAY_OF_MONTH, other.getActualMaximum(DAY_OF_MONTH));
            boolean after = getTimeInMillis() > other.getTimeInMillis();
            other.set(DAY_OF_MONTH, day);
            return after;
        }

        public boolean sameMonth(DateOnlyCalendar other) {
            return get(YEAR) == other.get(YEAR) && get(MONTH) == other.get(MONTH);
        }

        void stripTime() {
            set(Calendar.HOUR_OF_DAY, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MILLISECOND, 0);
        }

        void recycle() {
            setTimeZone(TimeZone.getDefault());
            sPools.release(this);
        }
    }
}

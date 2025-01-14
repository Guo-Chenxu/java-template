package com.guochenxu.javatemplate.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具
 *
 * @author: 郭晨旭
 * @create: 2023-11-17 13:33
 * @version: 1.0
 */
@Slf4j
public class DateUtil {

    /**
     * 格式化日期
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final SimpleDateFormat DATE_HOUR_MINUTE = new SimpleDateFormat("HH:mm");

    private static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat DATE_FORMAT_MONTH = new SimpleDateFormat("yyyy/MM");

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4}/\\d{2}/\\d{2})\\s*(\\d{1,2})?(?::(\\d{1,2})(?::(\\d{1,2}))?)?");

    /**
     * 补充日期时间字符串中的小时、分钟和秒的前导零。
     *
     * @param dateString 日期时间字符串。
     * @return 补充前导零后的日期时间字符串。
     */
    public static String padZero(String dateString) {
        Matcher matcher = DATE_PATTERN.matcher(dateString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(matcher.group(1)).append(' '); // 年-月-日

        // 补充小时前导零
        String hour = matcher.group(2);
        if (hour == null) {
            sb.append("00:");
        } else if (hour.length() == 1) {
            sb.append('0').append(hour).append(':');
        } else {
            sb.append(hour).append(':');
        }

        // 补充分钟前导零
        String minute = matcher.group(3);
        if (minute != null) {
            if (minute.length() == 1) {
                sb.append('0').append(minute).append(':');
            } else {
                sb.append(minute).append(':');
            }

            // 补充秒前导零
            String second = matcher.group(4);
            if (second != null) {
                if (second.length() == 1) {
                    sb.append('0').append(second);
                } else {
                    sb.append(second);
                }
            } else {
                sb.append("00");
            }
        } else {
            sb.append("00:00");
        }

        return sb.toString();
    }

    /**
     * 合并两个日期的月份和日期部分
     * 日期格式: yyyy/MM/dd HH:mm:ss
     */
    public static String mergeDates(String dateStr1, String dateStr2) {
        dateStr1 = dateStr1.split(" ")[0].replaceAll("/", "");
        dateStr2 = dateStr2.split(" ")[0].replaceAll("/", "");
        return dateStr1 + "_" + dateStr2;
    }

    public static String getDay(String date) throws ParseException {
        Date formatDate = parseTime(date);
        return getFormatDay(formatDate);
    }

    public static String getHourAndMinute(String date) throws ParseException {
        Date formatDate = parseTime(date);
        return getFormatHourAndMinute(formatDate);
    }

    public static String getFormatHourAndMinute(Date s) {
        return DATE_HOUR_MINUTE.format(s);
    }

    public static String getNowTime() {
        return DATE_FORMAT.format(new Date());
    }

    public static String getToday() {
        return DATE_FORMAT_DAY.format(new Date());
    }

    public static String getFormatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String getFormatMonth(Date date) {
        return DATE_FORMAT_MONTH.format(date);
    }

    public static String getFormatDay(Date date) {
        return DATE_FORMAT_DAY.format(date);
    }

    public static Date setDateToTime(Date date, String timeStr) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date time = timeFormat.parse(timeStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date setDateToTime(String day, String timeStr) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date time = timeFormat.parse(day + " " + timeStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static double getHoursDifference(String startDateStr, String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diff = endTime - startTime;
        return diff * 1.0 / (1000 * 1.0 * 60 * 60);
    }

    public static double getHoursDifference(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diff = endTime - startTime;
        return diff * 1.0 / (1000 * 1.0 * 60 * 60);
    }

    public static Date parseTimeNoSplit(String timeStr) throws ParseException {
        SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
        return FORMAT.parse(timeStr);
    }

    public static Date convertIsoToUtilDate(String isoDateTimeStr) {
        if (isoDateTimeStr == null || isoDateTimeStr.isEmpty()) {
            throw new IllegalArgumentException("日期时间字符串不能为空");
        }

        try {
            // 使用预定义的格式化器解析字符串
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDateTimeStr, formatter);

            // 将ZonedDateTime转换为Date
            return Date.from(zonedDateTime.toInstant());
        } catch (Exception e) {
            throw new IllegalArgumentException("无法解析日期时间字符串: " + isoDateTimeStr, e);
        }
    }

    public static Date getStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();
        return date;
    }

    public static Date parseTime(String time) throws ParseException {
        time = padZero(time);
        return DATE_FORMAT.parse(time);
    }


    public static String getFirstDayOfMonth() {
//        Calendar calendar = getFirstDayCalendarOfMonth();
//        Date date = calendar.getTime();
        Date d = getNMonthFirstDay(0);
        return DATE_FORMAT.format(d);
    }

    public static String getLastDayOfMonth() {
//        Calendar calendar = getFirstDayCalendarOfMonth();
//        calendar.add(Calendar.MONTH, 1);
//        calendar.add(Calendar.MILLISECOND, -1);
//        Date date = calendar.getTime();
        Date d = getNMonthLastDay(0);
        return DATE_FORMAT.format(d);
    }

    public static Calendar getFirstDayCalendarOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 查找最近的一个特定星期几的日期。
     *
     * @param currentDate     当前日期
     * @param targetDayOfWeek 目标星期几，如 Calendar.WEDNESDAY
     * @return 最近的一个特定星期几的日期
     */
    public static Date findNearestTargetDay(Date currentDate, int targetDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilTargetDay = (targetDayOfWeek - currentDayOfWeek + 7) % 7;

        if (daysUntilTargetDay == 0) {
            return currentDate;
        }

        calendar.add(Calendar.DAY_OF_MONTH, daysUntilTargetDay);
        return calendar.getTime();
    }

    /**
     * 查找下一个个特定星期几的日期。
     *
     * @param currentDate     当前日期
     * @param targetDayOfWeek 目标星期几，如 Calendar.WEDNESDAY
     * @return 最近的一个特定星期几的日期
     */
    public static Date findNextTargetDay(Date currentDate, int targetDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 如果当前已经是目标星期几，则需要跳过当天，查找下一周的目标日期
        int daysUntilNextTargetDay = (targetDayOfWeek != currentDayOfWeek)
                ? (targetDayOfWeek - currentDayOfWeek + 7) % 7
                : 7;

        // 设置目标日期为当前日期加上所需的天数差
        calendar.add(Calendar.DAY_OF_MONTH, daysUntilNextTargetDay);
        return calendar.getTime();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRange {
        Integer year;
        Integer month;
        Integer day;
    }

    /**
     * 计算间隔多少年月
     */
    public static DateRange calculateYearsAndMonths(String fromDateString) {
        try {
            Date fromDate = DATE_FORMAT_DAY.parse(fromDateString);
            Calendar from = Calendar.getInstance();
            from.setTime(fromDate);
            Calendar to = Calendar.getInstance();
            to.setTime(new Date());
            int fromYear = from.get(Calendar.YEAR);
            int fromMonth = from.get(Calendar.MONTH);
            int fromDay = from.get(Calendar.DAY_OF_MONTH);

            int toYear = to.get(Calendar.YEAR);
            int toMonth = to.get(Calendar.MONTH);
            int toDay = to.get(Calendar.DAY_OF_MONTH);

            return DateRange.builder().year(toYear - fromYear)
                    .month(toMonth - fromMonth)
                    .day(toDay - fromDay).build();
        } catch (ParseException e) {
            log.error("日期格式错误", e);
            return new DateRange();
        }
    }

    public static String getYearAndMonth(String from) {
        DateRange range = calculateYearsAndMonths(from);
        return range.getYear() + "年" + range.getMonth() + "月";
    }

    public static String getYearAndMonth(Date from) {
        String fromString = getFormatDate(from);
        return getYearAndMonth(fromString);
    }

    /**
     * 获取n月的第一天
     */
    public static Date getNMonthFirstDay(Integer n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, n);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取n月的最后一天
     */
    public static Date getNMonthLastDay(Integer n) {
        Date date = getNMonthFirstDay(n + 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    /**
     * 获取第n天的0点
     */
    public static Date getNDayBegin(Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, n);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取第n天的23:59:59
     *
     * @param n
     * @return
     */
    public static Date getNDayEnd(Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, n);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 判断时间段是否重叠
     */
    public static boolean isTimeOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        return !(endTime1.compareTo(startTime2) < 0 || endTime2.compareTo(startTime1) < 0);
    }
}

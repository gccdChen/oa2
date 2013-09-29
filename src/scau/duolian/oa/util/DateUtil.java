package scau.duolian.oa.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static final String FORMATER = "yyyy-MM-dd";
	private static final String FORMATER2 = "yyyyMMdd";
	private static final String FORMATER3 = "MM月dd日 HH:mm";
	private static final String FORMATER4 = "MM月dd";
	private static final String FORMATER5 = "yyyy-MM-dd hh:mm";
	private static final String FORMATER_TIME = "yyyy-MM-dd HH:mm:ss";
	private static final String FORMATER_HOUR = "HH:mm";
	private static SimpleDateFormat sdf = null;
	private static SimpleDateFormat sdf2 = null;
	private static SimpleDateFormat sdf3 = null;
	private static SimpleDateFormat sdf4 = null;
	private static SimpleDateFormat sdf5 = null;
	private static SimpleDateFormat sdfTime = null;
	private static SimpleDateFormat sdfHour = null;
	static {
		sdf = new SimpleDateFormat(FORMATER);
		sdf2 = new SimpleDateFormat(FORMATER2);
		sdf3 = new SimpleDateFormat(FORMATER3);
		sdf4 = new SimpleDateFormat(FORMATER4);
		sdf5 = new SimpleDateFormat(FORMATER5);
		sdfTime = new SimpleDateFormat(FORMATER_TIME);
		sdfHour = new SimpleDateFormat(FORMATER_HOUR);
	}

	public static Date strToDate(String str) {
		if (null == str || "".equals(str))
			return null;
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @param str 2013-09-29
	 * @return
	 */
	public static Long dateStrToLong(String str){
		try {
			return sdf.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}

	public static String strTostr(String str) {
		if (null == str || "".equals(str))
			return null;
		return sdf.format(new Date(Long.parseLong(str)));
	}

	public static Date strToTime(String time) {
		if (null == time || "".equals(time))
			return null;
		try {
			return sdfTime.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param date
	 * @return "yyyy-MM-dd"
	 */
	public static String dateToStr(Date date) {
		return sdf.format(date);
	}

	private static final int MinuteUnit = 60;
	private static final int HourUnit = 60 * 60;
	private static final int DayUnit = 60 * 60 * 24;
	private static final DecimalFormat decimalFormat = new DecimalFormat("00");

	/**
	 * 
	 * @param time
	 *            秒级的long
	 * @return "yyyyMMdd" 如果一小时之内则输出 XX分钟前 一天之内 则输出 今天 hh:MM
	 */
	public static String longToStr(Long time) {
		Date date = new Date();
		long now = date.getTime() / 1000;
		Long nowHour = now / HourUnit * HourUnit;
		Long nowDay = now / DayUnit * DayUnit;
		if (time > nowHour) {
			return (now - time) / MinuteUnit + "分钟前";
		} else if (time > nowDay) {
			return "今天 " + decimalFormat.format((time % DayUnit / HourUnit)) + ":" + decimalFormat.format((time % HourUnit / MinuteUnit));
		} else {
			return sdf3.format(new Date(time * 1000));
		}
	}

	/**
	 * 
	 * @param time
	 *            ms级的long
	 * @return
	 */
	public static String longStrToStr(String time) {
		if (time == null || time.length() <= 3)
			return "2013-09-04 02:28";
		Long t = null;
		try {
			t = Long.parseLong(time);
			return sdf5.format(new Date(t));
		} catch (Exception e) {
			return "2013-09-04 02:28";
		}
	}

	/**
	 * 
	 * @param time
	 *            ms级的long
	 * @return
	 */
	public static Date longStrToDate(String time) {
		if (time == null || time.length() <= 3)
			return new Date();
		Long t = null;
		try {
			t = Long.parseLong(time);
			return new Date(t);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * 
	 * @param time
	 *            秒级的long
	 * @return "yyyyMMdd" 如果一小时之内则输出 XX分钟前 一天之内 则输出 今天 hh:MM
	 */
	public static String intToStr(Integer time) {
		Date date = new Date();
		long now = date.getTime() / 1000;
		Long nowHour = now / HourUnit * HourUnit;
		Long nowDay = now / DayUnit * DayUnit;
		if (time > nowHour) {
			return (now - time) / MinuteUnit + "分钟前";
		} else if (time > nowDay) {
			return "今天 " + decimalFormat.format((time % DayUnit / HourUnit)) + ":" + decimalFormat.format((time % HourUnit / MinuteUnit));
		} else {
			return sdf3.format(new Date(time * 1000));
		}
	}

	/**
	 * 
	 * @param time
	 *            秒级的long
	 * @return "yyyyMMdd" 如果一小时之内则输出 XX分钟前 一天之内 则输出 今天 hh:MM 否则是 XX月XX
	 */
	public static String intToStr2(Integer time) {
		Date date = new Date();
		long now = date.getTime() / 1000;
		Long nowHour = now / HourUnit * HourUnit;
		Long nowDay = now / DayUnit * DayUnit;
		if (time > nowHour) {
			return (now - time) / MinuteUnit + "分钟前";
		} else if (time > nowDay) {
			return "" + decimalFormat.format((time % DayUnit / HourUnit)) + ":" + decimalFormat.format((time % HourUnit / MinuteUnit));
		} else {
			return sdf4.format(new Date(time * 1000));
		}
	}

	/**
	 * 
	 * @param date
	 * @return "yyyyMMdd"
	 */
	public static String dateToStr2(Date date) {
		return sdf2.format(date);
	}

	public static String timeToStr(Date date) {
		return sdfTime.format(date);
	}

	public static Integer getDateInt() {
		Long t = new Date().getTime() / 1000;
		return Integer.parseInt("" + t);
	}

	public static String getDateStr() {
		return sdf5.format(new Date());
	}

	public static String strToHourStr(String xdsj) {
		// TODO Auto-generated method stub
		return sdfHour.format(new Date(Long.parseLong(xdsj)));
	}

}

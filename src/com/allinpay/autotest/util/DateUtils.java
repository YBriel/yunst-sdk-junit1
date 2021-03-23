package com.allinpay.autotest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

public class DateUtils {

	/** 
	 * fromat for yyyy-MM-dd
	 */
	public static final String DATE_YYYYMMDD = "yyyy-MM-dd";
	public static final String DATE_YYMMDD_NOLINK = "yyyyMMdd";
	public static final String DATETIME_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_YYYYMMDD_HHMMSS_NOLINK = "yyyyMMddHHmmss";
	public static final String DATETIME_YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";
	public static final String TIME_HHMMSS = "HH:mm:ss";
	public static final String TIME_HHMM = "HH:mm";
	
	public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * (long)1000;
	
//	public static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	
//	public static final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
//	
//	public static final SimpleDateFormat YYYYMM_FORMAT = new SimpleDateFormat("yyyy-MM");	

	public static Date getCurrentDate(String format){
		Date currentDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateStr = formatter.format(cal.getTime());
		try {
			currentDate = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}
	
	public static Date getCurrentDate() {
		Date currentDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_YYYYMMDD);
		String dateStr = formatter.format(cal.getTime());
		try {
			currentDate = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}
	
	public static Date getCurrentDateTimes() {
		Date currentDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_YYYYMMDD_HHMMSS);
		String dateStr = formatter.format(cal.getTime());
		try {
			currentDate = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}
	public static String getCurrentDateTimesNolink() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_YYYYMMDD_HHMMSS_NOLINK);
		String dateStr = formatter.format(cal.getTime());
		return dateStr;
	}

	public static String getCurrentDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				DATETIME_YYYYMMDD_HHMMSS);
		return formatter.format(cal.getTime());
	}

	public static String getNowDate(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				format);
		return formatter.format(cal.getTime());
	}

	public static String getDate(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_YYYYMMDD);
			return formatter.format(date);
		}
		return null;
	}

	
	public static String getDate(Date date,String format){
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(date);
		}
		return null;
	}
	
	public static String getTime(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(TIME_HHMMSS);
			return formatter.format(date);
		}
		return null;
	}
	
	public static String getTime(Date date,String format) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(date);
		}
		return null;
	}
	
	/**
	 * 时间区间内天数, 开始日期必须小于结束日期
	 * @param startDate 不能为null, 包含起始日期
	 * @param endDate 不能为null, 包含结束日期
	 * @return
	 */
	public static int getNumberOfDays(Date startDate, Date endDate){
		if(startDate == null){
			throw new IllegalArgumentException(" startDate can not be null");
		}
		
		if(endDate == null){
			throw new IllegalArgumentException(" endDate can not be null");
		}

		int numberOfDays = 0;
		numberOfDays = (int)((endDate.getTime() - startDate.getTime()) / ONE_DAY_MILLIS) + 1;
		
		return numberOfDays;
		
	}
	
	/**
	 * 从字符转换成日期格式
	 * @param strDate
	 * @param format 本类中有几个默认的format可以调用
	 * @return
	 * @throws ParseException 
	 * @see YYYYMMDDHHMMSS_FORMAT,YYYYMMDD_FORMAT,YYYYMM_FORMAT
	 */
	public static Date parse(String strDate, String format) {
		try {
			return new SimpleDateFormat(format).parse(strDate);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	
	
	/**
	 * 从字符转换成日期格式
	 * @param strDate
	 * @param format 本类中有几个默认的format可以调用
	 * @return
	 * @throws ParseException 
	 * @see YYYYMMDDHHMMSS_FORMAT,YYYYMMDD_FORMAT,YYYYMM_FORMAT
	 */
	public static String format(Date date, String format){
		if(date!=null){
			return new SimpleDateFormat(format).format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 把日期转换成当天的最后时间， 即取到当天参数之前的日期
	 * @param date
	 * @return
	 */
	public static java.util.Date parse2EndDate(java.util.Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * 得到下一个星期几的日期
	 * @param dayOfWeek 1到7代表星期一到星期日
	 * @return
	 */
	public static Date getNextDayOfWeekDate(int dayOfWeek){
		if(dayOfWeek<8&&dayOfWeek>0){
			dayOfWeek++;
		}else{
			return new Date();
		}
		if(dayOfWeek==8){
			dayOfWeek = 1;
		}
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(currentDate);
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return calendar.getTime();
	}
	
	/**
     * @see 得到传入日期n天后的日期,如果传入日期为null，则表示当前日期n天后的日期
     * 
     * @author SQJ(Kira.Sun)
     * @param Date dt
     * @param days 可以为任何整数，负数表示前days天，正数表示后days天
     * @return Date
     */
//    public static Date getAddDayDate(Date dt, int days,int formId) {
//        if (dt == null)
//            dt = new Date(System.currentTimeMillis());
//        Calendar cal = Calendar.getInstance();
//        /******************闰年与非闰年中二月份的特殊处理*******余路************/
//        cal.setTime(dt);
//        int year=cal.get(Calendar.YEAR);
//        int month=cal.get(Calendar.MONTH);
//        int month2;
//        //月底表单特殊处理
//        if(formId==6){
//        	if(days<=19){
//        		//开始时间加上偏移量
//        		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
//        	}else{
//        		//截至时间取得当月的最后一天
//        		cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
//        		cal.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH-1);
//        	}
//        }else{
//        	//非特殊表单处理
//        	//添加偏移量之后
//        	//
//        	cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
//        	cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+days);
//        	cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
//        	month2=cal.get(Calendar.MONTH);
//        	if(month2>month){
//        		cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
//        		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
//        			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+3);
//        		}else if(month==4||month==6||month==9||month==11){
//        			cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+2);
//        		}else if(month==2){
//        			if((year%4==0&&year%100!=0)||year%400==0){
//        				cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
//        			}else{
//        				cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH));
//        			}
//        		}
//        		cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
//        	}else{
//        		cal=Calendar.getInstance();
//        		cal.setTime(dt);
//                int year1=cal.get(Calendar.YEAR);
//                int month1=cal.get(Calendar.MONTH);
//            	if((year1%4==0&&year1%100!=0)||year1%400==0){
//                	if(month1==2){
//                		if(days>28){
//                			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days+1);
//                		}else{
//                			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
//                		}
//                	}else{
//                		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
//                	}
//                }
//            	else{       	
//                    	cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
//                }
//        	}
//        }
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTime();
//    }
    
    /**
     * 
     * @version 2010-11-18 下午05:30:23
     * @author  SQJ(Kira.Sun)
     * @see		计算2个时间的 时间间隔  
     * @param      {引入参数名}   {引入参数说明}
     * @return      boolean true 则d1时间晚，false则d2时间晚
     * @exception   {说明在某情况下,将发生什么异常}
     */
    public static boolean computeInterval(Date d1,Date d2){
    	if((d1.getTime() - d2.getTime())>0){
    		return true;
    	}else if((d1.getTime() - d2.getTime())<0){
    		return false;
    	}	
    	return true;
    }
	
    
    /**
	 * 
	 * @version 2010-11-18 下午03:44:22
	 * @author SQJ(Kira.Sun)
	 * @see 根据类型得到某个周期第一天   
	 * @param type:1 年 2 半年 3季度 4月 5旬 6周 7日   model:1当前周期 2上个周期
	 * @return Date {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public static  Date getFirstDayOfCycle(int type,int model) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		if(model==1){
			date=computeDayByType(date,calendar,type,year,month,day);
		}else{
			date=computeDayByType2(date,calendar,type,year,month,day);
		}
		return date;
	}
	
	
	/**
	 * 
	 * @version 2010-11-18 下午03:44:22
	 * @author SQJ(Kira.Sun)
	 * @see 根据类型得到某个周期第一天   
	 * @param type:1 年 2 半年 3季度 4月 5旬 6周 7日   model:1当前周期 2上个周期 3下个周期
	 * @return Date {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public static  Date getFirstDayOfCycle(Calendar calendar,int type,int model) {
		Date date = new Date();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		if(model==1){
			date=computeDayByType(date,calendar,type,year,month,day);
		}else if(model==2){
			date=computeDayByType2(date,calendar,type,year,month,day);
		}else{
			date=computeDayByType3(date,calendar,type,year,month,day);
		}
		return date;
	}

	/**
	 * 
	 * @version 2010-11-18 下午03:44:22
	 * @author SQJ(Kira.Sun)
	 * @see 根据类型得到某个周期第一天   
	 * @param type:1 年 2 半年 3季度 4月 5旬 6周 7日   model:1当前周期 2上个周期
	 * @return Date {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public static  Date getFirstDayOfCycle(int type,int model,Date date) {
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(date); 
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		if(model==1){
			date=computeDayByType(date,calendar,type,year,month,day);
		}else{
			date=computeDayByType2(date,calendar,type,year,month,day);
		}
		return date;
	}
	
	
	
	/**
	 * 
	 * @version 2010-11-19 下午01:58:12
	 * @author  SQJ(Kira.Sun)
	 * @see		{方法的功能/动作描述}
	 * @param      {引入参数名}   {引入参数说明}
	 * @return      Date {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	private static Date computeDayByType(Date date, Calendar calendar, int type, int year,
			int month, int day) {
		String strDate = "";
		//type:表单类型，年报表单，季度表单，月报表单
		switch (type) {
		case 1:
			strDate = String.valueOf(year) + "-01-01";
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 2:
			if (month <= 5) {
				strDate = String.valueOf(year) + "-01-01";
			} else {
				strDate = String.valueOf(year) + "-07-01";
			}
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 3:
			if (month <= 2) {
				strDate = String.valueOf(year) + "-01-01";
			} else if (month <= 5) {
				strDate = String.valueOf(year) + "-04-01";
			} else if (month <= 8) {
				strDate = String.valueOf(year) + "-07-01";
			} else if (month <= 11) {
				strDate = String.valueOf(year) + "-10-01";
			}
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 4:
			calendar.set(Calendar.DATE, 1);
			date = calendar.getTime();
			break;
		case 5:
			String monthNo = String.valueOf(month);
			if (month <= 8) {
				monthNo = "0" + monthNo;
			}
			if (day <= 10) {
				strDate = String.valueOf(year) + "-" + monthNo + "-01";
			} else if (day <= 20) {
				strDate = String.valueOf(year) + "-" + monthNo + "-11";
			} else if (day <= 31) {
				strDate = String.valueOf(year) + "-" + monthNo + "-21";
			}
			break;
		case 6:
//			int n = calendar.get(Calendar.DAY_OF_WEEK);
//			calendar.roll(Calendar.DATE, -n);
//			date = calendar.getTime();
			break;
		case 7:
			date = calendar.getTime();
			break;
		default:
			calendar = null;
			break;
		}
		return date;
	}
	
	
	/**
	 * 
	 * @version 2010-11-19 下午01:58:12
	 * @author  SQJ(Kira.Sun)
	 * @see		{方法的功能/动作描述}
	 * @param      {引入参数名}   {引入参数说明}
	 * @return      Date {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	private static Date computeDayByType2(Date date, Calendar calendar, int type, int year,
			int month, int day) {
		String strDate = "";
		switch (type) {
		case 1:
			strDate = String.valueOf(year-1) + "-01-01";
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 2:
			if (month <= 5) {
				strDate = String.valueOf(year-1) + "-07-01";
			} else {
				strDate = String.valueOf(year) + "-01-01";
			}
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 3:
			if (month <= 2) {
				strDate = String.valueOf(year-1) + "-10-01";
			} else if (month <= 5) {
				strDate = String.valueOf(year) + "-01-01";
			} else if (month <= 8) {
				strDate = String.valueOf(year) + "-04-01";
			} else if (month <= 11) {
				strDate = String.valueOf(year) + "-07-01";
			}
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 4:
			calendar.set(Calendar.DATE, 1);
			if(month==0){
				calendar.set(Calendar.MONTH, 11);
				calendar.set(Calendar.YEAR, year-1);
			}else{
				calendar.set(Calendar.MONTH, month-1);
			}
			date = calendar.getTime();
			break;
		case 5:
			String monthNo = String.valueOf(month);
			if (month <= 8) {
				monthNo = "0" + monthNo;
			}
			if (day <= 10) {
				if(month==0){
					year=year-1;
					month=11;
				}
				strDate = String.valueOf(year) + "-" + month + "-21";
			} else if (day <= 20) {
				strDate = String.valueOf(year) + "-" + monthNo + "-01";
			} else if (day <= 31) {
				strDate = String.valueOf(year) + "-" + monthNo + "-11";
			}
			break;
		case 6:
//			int n = calendar.get(Calendar.DAY_OF_WEEK);
//			calendar.roll(Calendar.DATE, -n);
//			date = calendar.getTime();
			break;
		case 7:
			date = calendar.getTime();
			break;
		default:
			calendar = null;
			break;
		}
		return date;
	}
	
	
	
	/**
	 * 
	 * @version 2010-11-19 下午01:58:12
	 * @author  SQJ(Kira.Sun)
	 * @see		{方法的功能/动作描述}
	 * @param      {引入参数名}   {引入参数说明}
	 * @return      Date {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	private static Date computeDayByType3(Date date, Calendar calendar, int type, int year,
			int month, int day) {
		String strDate = "";
		switch (type) {
		case 1:
			strDate = String.valueOf(year+1) + "-01-01";
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 2:
			if (month <= 5) {
				strDate = String.valueOf(year) + "-07-01";
			} else {
				strDate = String.valueOf(year+1) + "-01-01";
			}
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 3:
			if (month <= 2) {
				strDate = String.valueOf(year) + "-04-01";
			} else if (month <= 5) {
				strDate = String.valueOf(year) + "-07-01";
			} else if (month <= 8) {
				strDate = String.valueOf(year) + "-010-01";
			} else if (month <= 11) {
				strDate = String.valueOf(year+1) + "-01-01";
			}
			date = DateUtils.parse(strDate, "yyyy-MM-dd");
			break;
		case 4:
			if(month==11){
				calendar.set(Calendar.MONTH, 0);
				calendar.set(Calendar.YEAR, year+1);
			}else{
				calendar.set(Calendar.MONTH, month+1);
			}
			
			calendar.set(Calendar.DATE, 1);
			date = calendar.getTime();
			break;
		case 5:
			String monthNo = String.valueOf(month);
			if (month <= 8) {
				monthNo = "0" + monthNo;
			}
			if (day <= 10) {
				strDate = String.valueOf(year) + "-" + monthNo + "-11";
			} else if (day <= 20) {
				strDate = String.valueOf(year) + "-" + monthNo + "-21";
			} else if (day <= 31) {
				if(month==11){
					monthNo="01";
				}else{
					month=month+1;
				}
				if (month <= 8) {
					monthNo = "0" + monthNo;
				}else{
					monthNo = String.valueOf(month);
				}
				strDate = String.valueOf(year) + "-" + monthNo + "-01";
			}
			break;
		case 6:
//			int n = calendar.get(Calendar.DAY_OF_WEEK);
//			calendar.roll(Calendar.DATE, -n);
//			date = calendar.getTime();
			break;
		case 7:
			date = calendar.getTime();
			break;
		default:
			calendar = null;
			break;
		}
		return date;
	}
	
	/**  
     * 将CST的时间字符串转换成需要的日期格式字符串<br>  
     *   
     * @param cststr  
     *            The source to be dealed with. <br>  
     *            (exp:Fri Jan 02 00:00:00 CST 2009)  
     * @param fmt  
     *            The format string  
     * @return string or <code>null</code> if the cststr is unpasrseable or is  
     *         null return null,else return the string.  
     * @author Kira.Sun  
     */  
    public static String getDateFmtStrFromCST(String cststr, String fmt) {   
        if ((null == cststr) || (null == fmt)) {   
            return null;   
        }   
        String str = null;   
        SimpleDateFormat sdfy = new SimpleDateFormat(fmt.trim());   
        SimpleDateFormat sdf = new SimpleDateFormat(   
                "EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);   
        try {   
            str = sdfy.format(sdf.parse(cststr.trim()));   
        } catch (ParseException e) {   
            e.printStackTrace();   
            return null;   
        }   
        return str;   
    }
    
    
	public static Date getAddDayDate(Date dt, int days, int formId) {
		if (dt == null)
			dt = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int year = cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		if (formId == 6 ||formId == 7) {
			if (days <= 19) {
				// 开始时间加上偏移量
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)
						+ days);
			} else {
				// 截至时间取得当月的最后一天
				cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
				cal.set(Calendar.DAY_OF_MONTH,1);
				cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
			}
		}else if(formId == 45 || formId == 47) {
			if (days <= 25) {
				// 开始时间加上偏移量
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)
						+ days);
			} else {
				// 下月5号
				if(month==11){
					cal.set(Calendar.YEAR,year+1);
					cal.set(Calendar.MONTH,0);
				}else{
					cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
				}
				
				cal.set(Calendar.DAY_OF_MONTH,5);
			}
			
		}else {
			if (((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) && days>28) {
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)
						+ (days + 1));
			} else {
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)
						+ days);
			}
		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 * 判断日期格式是否正确YYYY-MM-DD
	 */
	public static boolean isFullDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_YYYYMMDD);
		formatter.setLenient(false);
		try {
			formatter.format(formatter.parse(dateString));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 比较日期大小
	 * @param smallDate
	 * @param bigDate
	 * @return
	 */
	public static boolean compareDate(String smallDate, String bigDate) {
		 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	        try {
	            Date dt1 = df.parse(smallDate);
	            Date dt2 = df.parse(bigDate);
	            if (dt1.getTime() < dt2.getTime()) {
	                return true;
	            } else if (dt1.getTime() > dt2.getTime()) {
	                return false;
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return false;
	    }
	/**
	 * 日期显示yyyy-MM-dd格式
	 */
	public static String date2showString(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(DATE_YYYYMMDD);
		return format.format(date);
	}
	/**
	 * 把参数date指定的日期格式化为参数pattern指定的日期格式字符串.
	 * 
	 * @param pattern
	 * @param date
	 * @return the formatted date-time string
	 * @see java.text.SimpleDateFormat
	 */
	public static String formatDateTime(String pattern, Date date) {
		try {
			String strDate = null;
			String strFormat = pattern;
			SimpleDateFormat dateFormat = null;

			if (date == null)
				return "";

			dateFormat = new SimpleDateFormat(strFormat);
			strDate = dateFormat.format(date);

			return strDate;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 把参数yyyyMMddHHmmss指定的日期格式化为参数pattern指定的日期格式字符串.
	 * 
	 * @param pattern
	 * @param date
	 * @return the formatted date-time string
	 * @see java.text.SimpleDateFormat
	 */
	public static String formatDateTime(String pattern, String dateStr) {
		
		SimpleDateFormat dateFormat =  new SimpleDateFormat(pattern);
		try {
			if(StringUtils.isEmpty(dateStr)){
				return "";
			}
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(dateStr);
			dateStr = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	/**
	 * 转换日期格式 20151112——>2015-11-12
	 * @param dateStr
	 * @return
	 */
	public static String formatDateStr(String dateStr){
		if (dateStr == null || "".equals(dateStr))
			return "";
		SimpleDateFormat dateFormat =  new SimpleDateFormat(DATE_YYYYMMDD);
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			dateStr = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	/**
	 * 格式化日期 20151111 192112 ——>2015-11-11 19:21:12
	 * @param dateStr
	 * @return
	 */
	public static String formatDateTimeStr(String dateStr){
		SimpleDateFormat dateFormat =  new SimpleDateFormat(DATETIME_YYYYMMDD_HHMMSS);
		try {
			Date date = new SimpleDateFormat("yyyyMMdd HHmmss").parse(dateStr);
			dateStr = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	/**
	 * 获取时间序列号 当前时间(年月日时分12位)+UUID哈希值(10位)
	 * @return
	 */
	public static String getTimestampSerialNo(){
		String uuid = UUID.randomUUID().toString();
		String hashCode = String.valueOf(uuid.hashCode()& 0x7FFFFFFF);
		if(hashCode.length()<10){
			hashCode = StringUtils.leftPad(hashCode, 10, "0");
		}
		StringBuffer timeSb = new StringBuffer();
		String dateTime = format(new Date(), "yyyyMMddHHmm");
		timeSb.append(dateTime).append(hashCode);
		return timeSb.toString();
	}
	/**
	 * 获得两时间相隔天数
	 * @return
	 */
		public static int getDifferDay(String endDate,String beginDate){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			int time = 0;
			try{
				Date b_time = sdf.parse(beginDate.trim());
				Date e_time = sdf.parse(endDate.trim());
				time = (int) ((e_time.getTime()-b_time.getTime())/(1000*60*60*24));
			}catch(ParseException e){
				time = 0;
			}
			return time;
		}
	/**
	 * 获取日期所在年的周数
	 * @param dateStr
	 * @return
	 */
	public static int getWeekOfYear(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYMMDD_NOLINK);
		Date date;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day == 1){
			//周日属于前一周
			week = week-1;
		}
		return week;
	}
	/**
	 * 获取第一天日期
	 * @param dateStr 日期 20151205
	 * @param type 类型：1-周 ，2-月，3-半年，4-年
	 * @return
	 */
	public static String getFirstDate(String dateStr,int type){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYMMDD_NOLINK);
		Date date;
		String firstDate="";
		Calendar cal = Calendar.getInstance();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			return "";
		}
		cal.setTime(date);
		if(type ==1){
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if(day == 1){
				//周日往前推6天
				day = 8;
			}
			//2-day天为所在周第一天
			cal.add(Calendar.DATE, 2-day);
			firstDate = sdf.format(cal.getTime());
		}else if(type ==2){
			cal.set(Calendar.DAY_OF_MONTH, 1);
			firstDate = sdf.format(cal.getTime());
		}else if(type ==3){
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			if(month <7){
				//上半年
				firstDate = year+"0101";
			}else{
				firstDate = year+"0701";
			}
		}else if(type == 4){
			int year = cal.get(Calendar.YEAR);
			firstDate = year+"0101";
		}
		return firstDate;
	}
	/**
	 * 获取最后一天日期
	 * @param dateStr 日期 20151205
	 * @param type 类型：1-周 ，2-月，3-半年，4-年
	 * @return
	 */
	public static String getLastDate(String dateStr,int type){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYMMDD_NOLINK);
		Date date;
		String lastDate="";
		Calendar cal = Calendar.getInstance();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			return "";
		}
		cal.setTime(date);
		if(type ==1){
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if(day == 1){
				//周日返回当天，否则往后推8-day天为所在周最后一天
				day = 8;
			}
			cal.add(Calendar.DATE, 8-day);
			lastDate = sdf.format(cal.getTime());
		}else if(type ==2){
			cal.add(Calendar.MONTH,1);//月增加1天
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE,-1);//日期倒数一日,既得到本月最后一天 
			lastDate = sdf.format(cal.getTime());
		}else if(type ==3){
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			if(month <7){
				//上半年
				lastDate = year+"0630";
			}else{
				lastDate = year+"1231";
			}
		}else if(type == 4){
			int year = cal.get(Calendar.YEAR);
			lastDate = year+"1231";
		}
		return lastDate;
	}
	/**
	 * 格式化日期 20151216——>2015年12月16日
	 * @param date
	 * @return
	 */
	public static String formatDateShow(String date){
		String showDate="";
		if(StringUtils.isBlank(date) || date.length() != 8){
			return date;
		}else{
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String day = date.substring(6, 8);
			showDate = year+"年"+month+"月"+day+"日";
			return showDate;
		}
	}
	public static String addDate(String dateStr,int n){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYMMDD_NOLINK);
		Date date;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			return "";
		}
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		dateStr = sdf.format(cal.getTime());
		return dateStr;
	}
	
	public static String addMonth(Date date,int n){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYYYMMDD);
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		String dateStr = sdf.format(cal.getTime());
		return dateStr;
	}

	/**
	 * 格式化日期 2016-06-23 18:11:37 -> 20160623181137
	 * @param dateStr
	 * @return
	 */
	public static String formatDateNolink(String dateStr){
		
		if (dateStr == null || "".equals(dateStr))
			return "";
		
		Date date = new Date();		
		try {
			date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr);
			dateStr = (new SimpleDateFormat("yyyyMMddHHmmss")).format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateStr;
	}
	
	/**
	 * 格式化日期 20160623181137  -> 2016-06-23 18:11:37
	 * @param dateStr
	 * @return
	 */
	public static String formatDateHaslink(String dateStr){
		
		if (dateStr == null || "".equals(dateStr))
			return "";
		
		Date date = new Date();		
		try {
			date = new SimpleDateFormat("yyyyMMddHHmmss").parse(dateStr);
			dateStr = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateStr;
	}
	
	/**
	 * 格式化日期 2016-06-23 -> 20160623
	 * @param dateStr
	 * @return
	 */
	public static String formatDateStrNolink(String dateStr){
		
		if (dateStr == null || "".equals(dateStr))
			return "";
		
		Date date = new Date();		
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			dateStr = (new SimpleDateFormat("yyyyMMdd")).format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateStr;
	}
	
	public static String addDate(Date date,int n){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_YYMMDD_NOLINK);
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return sdf.format(cal.getTime());
	}
	public static void main(String[] args) {
    	System.out.println(addDate(new Date(),-1));
    	//System.out.println(getFirstDate("20151108",1));
    	//System.out.println(getLastDate("20151108",1));
    	//System.out.println(formatDateNolink("2016-06-23 18:11:37"));
    	//getWeekByDate("20160101");
    	/*String date = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
    	System.out.println(getTimestampSerialNo());
    	System.out.println(date.substring(3, 17));*/
	}
} 
 


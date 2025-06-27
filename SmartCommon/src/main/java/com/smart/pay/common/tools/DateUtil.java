package com.smart.pay.common.tools;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    public static Date getDateFromStr(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try {
            result = sdf.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    public static Date getYYMMHHMMSSDateFromStr(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = null;
        try {
            result = sdf.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    public static Date getYYMMDateFromStr(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date result = null;
        try {
            result = sdf.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 判断两个日期相隔几天
     * 
     * @param fDate
     * @param oDate
     * @return
     */
    public int getIntervalDays(Date fDate, Date oDate) {

        /*
         * if (null == fDate || null == oDate) {
         * 
         * return -1;
         * 
         * }
         * 
         * long intervalMilli = oDate.getTime() - fDate.getTime();
         * 
         * return (int) (intervalMilli / (24 * 60 * 60 * 1000));
         */

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);
        // aCalendar.add(Calendar.HOUR_OF_DAY, -17);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }
    
    
    /***增加一天*/
    public static Date getTomorrow(Date today) {
    	
    	Calendar c = Calendar.getInstance();  
        c.setTime(today);  
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
    	
    	return c.getTime(); 
    }
    
    /**
     * String和Date互转,
     * @param target	目标类型 String/Date
     * @param source	源类型 Date/String
     * @param partten	转换格式
     * @return
     */
	public static <T, U> T  getDateStringConvert(T target, U source,String partten){
		SimpleDateFormat sdf = new SimpleDateFormat(partten);
		if(target instanceof Date){
			if(source instanceof String){
				try {
					return (T) sdf.parse((String)source);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}else if(source instanceof Date){
				try {
					return (T) sdf.parse(sdf.format(source));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}else{
			if(source instanceof Date){
				try {
					return (T) sdf.format((Date)source);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}else if(source instanceof String){
				try {
					return (T) sdf.parse((sdf.format(source)));
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
}

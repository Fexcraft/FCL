package net.fexcraft.mod.lib.util.math;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Time {
	
	public static final Calendar cal = Calendar.getInstance();
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	
	public static final Calendar getCalendar(){
		return cal;
	}
	
	public static int getMonth(){
		return cal.get(0x2);
	}
	
	public static int getDay(){
		return cal.get(0x5);
	}
	
	public static int getDay365(){
		return cal.get(0x6);
	}
	
	public static int getDay7(){
		return cal.get(0x7);
	}
	
	public static int getHour12(){
		return cal.get(0xa);
	}
	
	public static int getHour24(){
		return cal.get(0xb);
	}
	
	public static int getMinute(){
		return cal.get(0xc);
	}
	
	public static int getSecond(){
		return cal.get(0xd);
	}
	
	public static int getMilisecond(){
		return cal.get(0xe);
	}
	
	public static long getDate(){
		return new Date().getTime();
	}
	
	public static Calendar getGMTCalendar(){
		return Calendar.getInstance(GMT);
	}
	
	public static final String getAsString(long date){
		return format.format(new Date(date));
	}
	
	private static final SimpleDateFormat format = new SimpleDateFormat("dd|MM|yyyy hh:mm:ss");
	
}
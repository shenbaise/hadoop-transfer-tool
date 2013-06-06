package org.sbs.transfer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static void main(String[] args) {
		System.out.println(dateFormat.format(new Date()));
	}
	
}

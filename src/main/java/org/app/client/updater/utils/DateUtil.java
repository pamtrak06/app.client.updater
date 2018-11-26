package org.app.client.updater.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final  class DateUtil {

	
	public static String formatNewDate(){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
}

package com.dhirain.newsgo.utills;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by DJ on 10-09-2017.
 */

public class DateUtil {
    static final String DATEFORMAT = "E, dd MMM yyyy HH:mm:ss";


    public static String getUTCString(long date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(date);
        return utcTime;
    }
}

package com.applicationcourse.mobile.graphmaster.Util;

/**
 * Created by teresa on 07/03/16.
 */
public class Util {
    /**
     *  elapsed time in hours/minutes/seconds
     * @return String
     */
    public static String getElapsedTime(long milliseconds) {
        String format = String.format("%%0%dd", 2);
        long elapsedTime = milliseconds / 1000;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);
        String time =  hours + ":" + minutes + ":" + seconds;
        return time;
    }
}

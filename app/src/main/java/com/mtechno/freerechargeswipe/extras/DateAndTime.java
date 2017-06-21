package com.mtechno.freerechargeswipe.extras;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by DEVEN SINGH on 2/21/2015.
 */
public class DateAndTime {


    public String DateNTime() {
        Calendar calendar = Calendar.getInstance();
        String month = "" + (calendar.get(Calendar.MONTH) + 1);
        String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
        if ((calendar.get(Calendar.MONTH) + 1) < 10) {
            month = "0" + (calendar.get(Calendar.MONTH) + 1);
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        }
        String weekDays=calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        String aDateTym = weekDays+"  "+ day+ "/" + month + "/" +calendar.get(Calendar.YEAR);
        return aDateTym;
    }


}

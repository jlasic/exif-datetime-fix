package com.example.lasic.exifdatetimefix;


import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lasic on 17.06.2017..
 */

public class DateParser {

    private static final String TAG = "DateParser";

    public static String parse(String imageName){
        String result = null;

        String [] stringArray = imageName.split("-");
        int datePosition = -1;
        for (int i = 0; i<stringArray.length; i++){
            try{
                Integer.parseInt(stringArray[i]);
                datePosition = i;
            }catch (NumberFormatException e){}
        }
        if (datePosition == -1)
            return null;

        result = format(stringArray[datePosition]);
        result += " 00:00:00";

        return result;
    }
    private static String format(String unformattedDate){
        String result = unformattedDate.substring(0, 4) + ":";
        result += unformattedDate.substring(4, 6) + ":";
        result += unformattedDate.substring(6);
        return result;
    }
}

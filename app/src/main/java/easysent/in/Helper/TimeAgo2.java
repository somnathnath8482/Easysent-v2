package easysent.in.Helper;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo2 {
    public static String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "to go";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();


            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (dateDiff < 0) {
                dateDiff=Math.abs(dateDiff);
                second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
                minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
                day = TimeUnit.MILLISECONDS.toDays(dateDiff);

                if (second < 60) {
                    convTime = second + " Seconds " + prefix;
                } else if (minute < 60) {
                    convTime = minute + " Minutes " + prefix;
                } else if (hour < 24) {
                    convTime = hour + " Hours " + prefix;
                } else if (day >= 7) {
                    if (day > 360) {
                        convTime = (day / 360) + " Years " + prefix;
                    } else if (day > 30) {
                        convTime = (day / 30) + " Months " + prefix;
                    } else {
                        convTime = (day / 7) + " Week " + prefix;
                    }
                } else if (day < 7) {
                    convTime = day + " Days " + prefix;
                }

            }


            else {
                if (second < 60) {
                    convTime = second + " Seconds " + suffix;
                } else if (minute < 60) {
                    convTime = minute + " Minutes " + suffix;
                } else if (hour < 24) {
                    convTime = hour + " Hours " + suffix;
                } else if (day >= 7) {
                    if (day > 360) {
                        convTime = (day / 360) + " Years " + suffix;
                    } else if (day > 30) {
                        convTime = (day / 30) + " Months " + suffix;
                    } else {
                        convTime = (day / 7) + " Week " + suffix;
                    }
                } else if (day < 7) {
                    convTime = day + " Days " + suffix;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertAM_PM(String dataDate){
         DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         DateFormat dateoutputFormat = new SimpleDateFormat("dd-MM-yyyy");
         DateFormat timeoutputFormat = new SimpleDateFormat("KK:mm a");

        try {
         /*   Date pasTime = inputFormat.parse(dataDate);
            Date nowTime = new Date();
            long dateDiff = (nowTime.getTime() - pasTime.getTime())/ (1000 * 60 * 60 * 24);*/


            Date pasTime = inputFormat.parse(dataDate);

            if (DateUtils.isToday(pasTime.getTime())){
                return timeoutputFormat.format(inputFormat.parse(dataDate));
            }else{
                return dateoutputFormat.format(inputFormat.parse(dataDate));
            }


        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}

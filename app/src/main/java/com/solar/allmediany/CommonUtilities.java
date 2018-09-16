package com.solar.allmediany;

import android.graphics.Typeface;
import android.text.Html;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtilities {

    static final String SENDER_ID = "330878643409";
    public static boolean notificationReceived;
    public static String Description="";

    public static String funConvertTimesFulldate(String fTime, String strType){
        String newTime="";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
            final Date dateObj = sdf.parse(fTime);

            if(strType!=null && strType.equals("WORD")){
                newTime = new SimpleDateFormat("MMM dd,yyyy hh:mm aa").format(dateObj);
            }else{
                newTime = new SimpleDateFormat("yyyy-MM-dd hh:mm aa").format(dateObj);
            }

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    public static String stipHtml(String description) {
        return Html.fromHtml(description).toString();

        /*if(description!=null && !description.equalsIgnoreCase("")){
            description = description.replaceAll("&nbsp;", "");
            description = description.replaceAll("<p>", "");
            description = description.replaceAll("</p>", "\n");
            description = description.replaceAll("<br>", "\n");
            description = description.replaceAll("<br />", "\n");
            description = description.replaceAll("<br/>", "\n");

            description = description.replaceAll("&rsquo;", "’");
            description = description.replaceAll("&rdquo;", "’’");

            description = description.replaceAll("&lsquo;", "‘");

            String REGEX = "<[^>]+>";
            Pattern p = Pattern.compile(REGEX);
            Matcher m = p.matcher(description);
            StringBuffer sb = new StringBuffer(description.length());
            while(m.find()) {
                m.appendReplacement(sb, "");
            }
            m.appendTail(sb);
            return sb.toString();
        }
        else{
            return "";
        }*/
    }

}
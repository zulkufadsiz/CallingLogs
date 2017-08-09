package com.example.zulkuf.callinglogs;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.text.DecimalFormat;

/**
 * Created by zulkuf on 8.08.2017.
 */

public class LogObject implements CallObject {
    private String number;
    private long date;
    private int duration,type;
    private Context context;

    public LogObject(Context context) {
     this.context = context;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String getCoolDuration() {
        return getCoolDuration(getDuration());
    }

    @Override
    public String getContactName() {
        if (getNumber() != null){
            return findNameByNumber(getNumber());
        }else {
            return null;
        }

    }

    private String findNameByNumber(final String phoneNumber){
        ContentResolver cr = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        Cursor cursor = cr.query(uri,
                                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                        null,
                                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                        null);
        if (cursor == null){
            return null;
        }

        String contactName = null;
        if (cursor.moveToFirst()){
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if(!cursor.isClosed()){
            cursor.close();
        }

        return (contactName == null) ?phoneNumber : contactName;
    }

    private String getCoolDuration(float sum){
        String duration = "";
        String result;

        if (sum >= 0 && sum < 3600){
            result = String.valueOf(sum/60);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int minutes = Integer.parseInt(decimal);
            float seconds = Float.parseFloat(point);

            DecimalFormat formatter = new DecimalFormat("#");
            duration = minutes + "min" + formatter.format(seconds) + "secs";

        }else if (sum >= 3600){
            result = String.valueOf(sum / 3600);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int hours = Integer.parseInt(decimal);
            float minutes = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            duration = hours + "hrs" + formatter.format(minutes) + "secs";
        }

        return duration;
    }
}

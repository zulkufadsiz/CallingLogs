# Calling Logs

## Introduction
 Accessing device call logs

![Image of the library in action](https://s2.postimg.org/74lys32gp/Whats_App_Image_2017-08-09_at_14.17.36_1.jpg "screenshot")
## Usage

First create to listview and list items activity for showing call log. Create new  
 ``LogManager`` object and ``List<LogObject>`` .  Get call logs with ``LogsManager.ALL_CALLS``. Then create new adapter from ``LogAdapter``. Set adapter to listview. 



## Examples 

Retriving a list of all call logs:

```java
 ListView logList = (ListView) findViewById(R.id.LogsList);
 LogsManager logsManager = new LogsManager(this);
 List<LogObject> callLogs = logsManager.getLogs(LogsManager.ALL_CALLS);
 LogsAdapter logsAdapter = new LogsAdapter(this, R.layout.log_layout, callLogs);
 logList.setAdapter(logsAdapter);
```

Have enjoy ! 

## Library Description

``LogManager ``

```java
package com.wickerlabs.logmanager;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LogsManager {

    public static final int INCOMING = CallLog.Calls.INCOMING_TYPE;
    public static final int OUTGOING = CallLog.Calls.OUTGOING_TYPE;
    public static final int MISSED = CallLog.Calls.MISSED_TYPE;
    public static final int TOTAL = 579;

    public static final int INCOMING_CALLS = 672;
    public static final int OUTGOING_CALLS = 609;
    public static final int MISSED_CALLS = 874;
    public static final int ALL_CALLS = 814;
    private static final int READ_CALL_LOG = 47;
    private Context context;


    public LogsManager(Context context) {
        this.context = context;
    }

    public int getOutgoingDuration() {
        int sum = 0;

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                CallLog.Calls.TYPE + " = " + CallLog.Calls.OUTGOING_TYPE, null, null);

        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String callDuration = cursor.getString(duration);
            sum += Integer.parseInt(callDuration);
        }

        cursor.close();

        return sum;
    }

    public int getIncomingDuration() {
        int sum = 0;

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE, null, null);

        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String callDuration = cursor.getString(duration);
            sum += Integer.parseInt(callDuration);
        }

        cursor.close();

        return sum;
    }

    public int getTotalDuration() {
        int sum = 0;

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String callDuration = cursor.getString(duration);
            sum += Integer.parseInt(callDuration);
        }

        cursor.close();

        return sum;
    }

    public String getCoolDuration(int type) {
        float sum;

        switch (type) {
            case INCOMING:
                sum = getIncomingDuration();
                break;
            case OUTGOING:
                sum = getOutgoingDuration();
                break;
            case TOTAL:
                sum = getTotalDuration();
                break;
            default:
                sum = 0;
        }

        String duration = "";
        String result;

        if (sum >= 0 && sum < 3600) {

            result = String.valueOf(sum / 60);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int minutes = Integer.parseInt(decimal);
            float seconds = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            duration = minutes + " min " + formatter.format(seconds) + " secs";

        } else if (sum >= 3600) {

            result = String.valueOf(sum / 3600);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int hours = Integer.parseInt(decimal);
            float minutes = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            duration = hours + " hrs " + formatter.format(minutes) + " min";

        }

        return duration;
    }

    public List<LogObject> getLogs(int callType) {
        List<LogObject> logs = new ArrayList<>();

        String selection;

        switch (callType) {
            case INCOMING_CALLS:
                selection = CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE;
                break;
            case OUTGOING_CALLS:
                selection = CallLog.Calls.TYPE + " = " + CallLog.Calls.OUTGOING_TYPE;
                break;
            case MISSED_CALLS:
                selection = CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE;
                break;
            case ALL_CALLS:
                selection = null;
            default:
                selection = null;
        }

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, selection, null, null);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            LogObject log = new LogObject(context);

            log.setNumber(cursor.getString(number));
            log.setType(cursor.getInt(type));
            log.setDuration(cursor.getInt(duration));
            log.setDate(cursor.getLong(date));

            logs.add(log);
        }

        cursor.close();


        return logs;
    }

}


```
``
LogObject
``

```java
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
```




### Interface
``CallObject``

```java
package com.example.zulkuf.callinglogs;

/**
 * Created by zulkuf on 8.08.2017.
 */

interface CallObject {
    String getNumber();
    void setNumber(String number);

    int getType();
    void setType(int type);

    long getDate();
    void setDate(long date);

    int getDuration();
    void setDuration(int duration);

    String getCoolDuration();
    String getContactName();

}


```
  ## Acknowledgements
 * [wickerlabs](https://github.com/wickerlabs) for creating libraries
 

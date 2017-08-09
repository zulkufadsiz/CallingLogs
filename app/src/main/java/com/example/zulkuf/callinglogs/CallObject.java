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

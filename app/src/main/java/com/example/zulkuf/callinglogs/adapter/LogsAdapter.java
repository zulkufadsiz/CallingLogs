package com.example.zulkuf.callinglogs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zulkuf.callinglogs.LogObject;
import com.example.zulkuf.callinglogs.LogsManager;
import com.example.zulkuf.callinglogs.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zulkuf on 9.08.2017.
 */

public class LogsAdapter extends ArrayAdapter<LogObject> {

    List<LogObject> logs;
    Context context;
    int resource;

    TextView phone, duration, date;
    ImageView imageView;

    public LogsAdapter(@NonNull Context context, int resource, List<LogObject> logs) {
        super(context, resource, logs);

        this.context = context;
        this.logs = logs;
        this.resource = resource;

    }

    @Nullable
    @Override
    public LogObject getItem(int position) {
        return logs.get(position);
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = LayoutInflater.from(getContext()).inflate(resource,parent,false);

        phone    = (TextView) row.findViewById(R.id.phoneNum);
        duration = (TextView) row.findViewById(R.id.callDuration);
        date     = (TextView) row.findViewById(R.id.callDate);

        imageView = (ImageView) row.findViewById(R.id.callImage);

        LogObject log = getItem(position);
        Date date1 = new Date(log.getDate());

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.ERA_FIELD, DateFormat.SHORT);
        phone.setText(log.getContactName());
        duration.setText(log.getContactName());
        date.setText(dateFormat.format(date1));

        switch(log.getType()){
            case LogsManager.INCOMING:
                imageView.setImageResource(R.drawable.received);
            case LogsManager.OUTGOING:
                imageView.setImageResource(R.drawable.sent);
            case LogsManager.MISSED:
                imageView.setImageResource(R.drawable.missed);
            default:
                imageView.setImageResource(R.drawable.cancelled);
                break;
        }

        return row;

    }
}

package com.example.zulkuf.callinglogs;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.zulkuf.callinglogs.adapter.LogsAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  static final int READ_LOGS = 725;
    private ListView logList;
    private Runnable logsRunnable;
    private String[] requiredPermissions = {Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logList = (ListView) findViewById(R.id.LogList);

        logsRunnable = new Runnable() {
            @Override
            public void run() {
                loadLogs();
            }
        };

        //Checking permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermissionToExecute(requiredPermissions,READ_LOGS,logsRunnable);
        }else {
            logsRunnable.run();
        }
    }


    private void loadLogs(){
        LogsManager logsManager = new LogsManager(this);
        List<LogObject> callLogs = logsManager.getLogs(LogsManager.ALL_CALLS);

        LogsAdapter logsAdapter = new LogsAdapter(this, R.layout.contact_list_item,callLogs);
        logList.setAdapter(logsAdapter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionToExecute(String permissions[], int requestCode, Runnable runnable){
        boolean logs = ContextCompat.checkSelfPermission(this,permissions[1]) != PackageManager.PERMISSION_GRANTED;
        boolean contacts = ContextCompat.checkSelfPermission(this,permissions[0]) != PackageManager.PERMISSION_GRANTED;

        if (logs || contacts){
            requestPermissions(permissions, requestCode);
        }else {
            runnable.run();
        }
    }

        //Permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_LOGS && permissions[0].equals(Manifest.permission.READ_CALL_LOG) &&
                permissions[1].equals(Manifest.permission.READ_CONTACTS)){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                logsRunnable.run();
            }else {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("The app needs these permissions to work, Exit ?")
                        .setTitle("Permission Denied")
                        .setCancelable(false)
                        .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                checkPermissionToExecute(requiredPermissions,READ_LOGS,logsRunnable);
                            }
                        })
                        .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }

        }
    }
}

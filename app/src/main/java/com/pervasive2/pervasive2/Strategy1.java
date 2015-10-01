package com.pervasive2.pervasive2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Strategy1 extends Activity implements LocationListener {

    Button btnOK;
    EditText number;
    long updateInterval = 0;
    LocationManager locationManager;
    boolean TC = false;
    boolean go = false;
    private boolean b = true;
    private AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

    // Klasse der bliver kaldt igennem alarmen
    private BroadcastReceiver localReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startUpdates();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy1);

        LocalBroadcastManager.getInstance(this).registerReceiver(localReciever,
                new IntentFilter("strat1"));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        number = (EditText) findViewById(R.id.configText);
        updateInterval = Long.parseLong(number.getText().toString());

        btnOK = (Button) findViewById(R.id.okBtn);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b) {
                    btnOK.setText("Stop");
                    String dString = number.getText().toString();
                    updateInterval = Long.parseLong(dString);
                    startUpdates();
                    b = false;
                }

                else {
                    btnOK.setText("Start");
                    stopUpdates();
                    b = true;
                }

            }
        });

        final Button logButton = (Button) findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTime();
            }
        });
    }

    private void stopUpdates() {
        locationManager.removeUpdates(this);
    }

    private void startUpdates() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                this);
    }

    private void logTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String s = sdf.format(new Date());
        generateNoteOnSD("Strategy1LogTime.txt", s);
    }

    @Override
    public void onProviderEnabled(String abe) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void onStatusChanged(String x, int y, Bundle z) {

    }

    private int fix = 0;

    @Override
    public void onLocationChanged(Location x) {
        fix++;
        stopUpdates();

        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String s = sdf.format(new Date());

        String end = "Latitude: " + x.getLatitude() + " Longitude: " + x.getLongitude() + " Time: " + s  + " GPSFixes: " + fix;

        generateNoteOnSD("Strategy1.txt", end);

        Intent newIntent = new Intent("strat1");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, newIntent,0);
        am.set(AlarmManager.RTC, System.currentTimeMillis()+(updateInterval*1000), pendingIntent);

    }


    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Pervasive2");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(sBody+"\n");
            writer.flush();
            writer.close();
            if(!TC) {
                Toast.makeText(getApplicationContext(), "New Location Saved", Toast.LENGTH_SHORT).show();
                TC = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
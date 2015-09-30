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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Strategy3 extends Activity implements LocationListener {

    private LocationManager locationManager;
    private long speed = 0;
    private long distanceInterval = 0;
    private long updateInterval = 0;
    private boolean b = true;
    private boolean TC = false;
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
        setContentView(R.layout.activity_strategy3);

        LocalBroadcastManager.getInstance(this).registerReceiver(localReciever,
                new IntentFilter("finalCountdown"));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager==null) {
            Log.d("MAIN", "MAIN: LocationManager is NULL");
        }

        Log.d("MAIN","GPS enabled = "+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        final Button startButton  = (Button) findViewById(R.id.startButton);
        final Button logButton = (Button) findViewById(R.id.logButton);
        final EditText distanceView = (EditText) findViewById(R.id.distanceText);
        final EditText speedView = (EditText) findViewById(R.id.speedText);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b) {
                    startButton.setText("Stop");
                    String dString = distanceView.getText().toString();
                    distanceInterval = Long.parseLong(dString);
                    String sString = speedView.getText().toString();
                    speed = Long.parseLong(sString);
                    updateInterval = distanceInterval / speed;
                    startUpdates();
                    b = false;
                }

                else {
                    startButton.setText("Start");
                    stopUpdates();
                    b = true;
                }
            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTime();
            }
        });
    }

    private void logTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String s = sdf.format(new Date());
        generateNoteOnSD("Strategy3LogTime", s);
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

    // Gammel position og tid
    private Location loc = null;
    private int fix = 0;

    @Override
    public void onLocationChanged(Location x){
        fix++;
        if(loc == null || loc.distanceTo(x) > distanceInterval) {

            // Vi har fået vores update, så stop GPSen.
            stopUpdates();

            // Opdater loc og log vores position.
            loc = x;
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            String s = sdf.format(new Date());
            String end = "Latitude: " + x.getLatitude() + " Longitude: " + x.getLongitude() + " Time: " + s + " GPSFixes: " + fix;
            generateNoteOnSD("Strategy3", end);

            // Vent indtil der er gået "updateInterval" og slå GPS til igen.
            Intent newIntent = new Intent("finalCountdown");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, newIntent,0);
            am.set(AlarmManager.RTC, System.currentTimeMillis()+(updateInterval*1000), pendingIntent);
        }
    }

    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Pervasive2");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(sBody);
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

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReciever);
    }
}

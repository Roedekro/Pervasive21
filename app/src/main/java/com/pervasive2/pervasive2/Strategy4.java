package com.pervasive2.pervasive2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
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


public class Strategy4 extends Activity implements SensorEventListener, LocationListener {

    private SensorManager senSM;
    private Sensor senAC;
    private LocationManager locationManager;
    private EditText et;
    private Button btn;
    private boolean go = false;

    private long lastUpdate = 0;
    private float x,y,z,last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 200;

    private boolean isMoving = false;
    private boolean TC = false;
    private AlarmManager am;
    private boolean b = true;
    private long distanceInterval;
    private long updateInterval;
    private long speed;

    // Klasse der bliver kaldt igennem alarmen
    private BroadcastReceiver localReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!isMoving) {
                // Bevæger os ikke, kald igen om set sekund.
                Intent newIntent = new Intent("strat4");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent,0);
                am.set(AlarmManager.RTC, System.currentTimeMillis() + (1000), pendingIntent);
            }
            else {startUpdates();}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy4);

        am = (AlarmManager)getSystemService(ALARM_SERVICE);

        LocalBroadcastManager.getInstance(this).registerReceiver(localReciever,
                new IntentFilter("strat4"));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        senSM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAC = senSM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSM.registerListener(this, senAC, SensorManager.SENSOR_DELAY_NORMAL);

        btn = (Button) findViewById(R.id.btn);
        final EditText distanceView = (EditText) findViewById(R.id.distanceText);
        final EditText speedView = (EditText) findViewById(R.id.speedText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b) {
                    btn.setText("Stop");
                    String dString = distanceView.getText().toString();
                    distanceInterval = Long.parseLong(dString);
                    String sString = speedView.getText().toString();
                    speed = Long.parseLong(sString);
                    updateInterval = distanceInterval / speed;
                    startUpdates();
                    b = false;
                    go = true;
                }

                else {
                    btn.setText("Start");
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

    private void logTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String s = sdf.format(new Date());
        generateNoteOnSD("Strategy4LogTime.txt", s);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER && go == true) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }

        long curTime = System.currentTimeMillis();

        if((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if(speed > SHAKE_THRESHOLD) {
                isMoving = true;
            } else {
                isMoving = false;
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private Location loc = null;
    private int fix = 0;

    @Override
    public void onLocationChanged(Location x) {
        fix++;
        if(loc == null || loc.distanceTo(x) > distanceInterval) {

            // Vi har fået vores update, så stop GPSen.
            stopUpdates();

            // Opdater loc og log vores position.
            loc = x;
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            String s = sdf.format(new Date());
            String end = "Latitude: " + x.getLatitude() + " Longitude: " + x.getLongitude() + " Time: " + s + " GPSFixes: " + fix;
            generateNoteOnSD("Strategy4.txt", end);

            // Vent indtil der er gået "updateInterval" og slå GPS til igen.
            Intent newIntent = new Intent("strat4");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, newIntent,0);
            am.set(AlarmManager.RTC, System.currentTimeMillis() + (updateInterval * 1000), pendingIntent);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReciever);
        super.onDestroy();
    }
}

package com.pervasive2.pervasive2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Strategy4 extends Activity implements SensorEventListener, LocationListener {

    private SensorManager senSM;
    private Sensor senAC;
    private LocationManager lm;

    private long lastUpdate = 0;
    private float x,y,z,last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private boolean isMoving = false;
    private boolean TC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy4);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        senSM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAC = senSM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSM.registerListener(this, senAC, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location x) {
        if(isMoving = true) {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            String s = sdf.format(new Date());
            String end = "Latitude: " + x.getLatitude() + " Longitude: " + x.getLongitude() + " Time: " + s;
            generateNoteOnSD("Strategy4Positions", end);
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
            FileWriter writer = new FileWriter(gpxfile);
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
}

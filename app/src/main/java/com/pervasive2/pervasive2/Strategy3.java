package com.pervasive2.pervasive2;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Strategy3 extends Activity implements LocationListener {

    private LocationManager locationManager;
    private long speed = 0;
    private long distanceInterval = 0;
    private long updateInterval = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy3);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager==null) {
            Log.d("MAIN", "START: LocationManager is NULL");
        }

        Log.d("MAIN","GPS enabled = "+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        final Button startButton  = (Button) findViewById(R.id.startButton);
        final TextView distanceView = (TextView) findViewById(R.id.distanceText);
        final TextView speedView = (TextView) findViewById(R.id.speedText);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dString = distanceView.getText().toString();
                distanceInterval = Long.parseLong(dString);
                String sString = speedView.getText().toString();
                speed = Long.parseLong(sString);
                updateInterval = distanceInterval / speed;
                startUpdates();
            }
        });
    }

    private void startUpdates() {
        /*
        First parameter: Requesting updates from GPS_Provider.
        Second parameter: Time in milliseconds between updates.
        Third parameter: Minimum distance moved between updates in meters.
        Fourth parameter: The LocationListener called upon GPS updates.
        */
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                updateInterval, // I strategy 3 skal vi kombinere 1 og 2, så inkluder et update interval
                0, // Distance skal filtreres i onLocationChanged()
                this);
    }

    // Gammel position
    private Location loc = null;

    @Override
    public void onLocationChanged(Location x){
        if(loc == null || loc.distanceTo(x) > distanceInterval) {
            loc = x;
            String end = "Latitude: " + x.getLatitude() + " Longitude: " + x.getLongitude();
            generateNoteOnSD("Strategy3Positions", end);
        }
    }

    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Strategy3Info");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
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
}

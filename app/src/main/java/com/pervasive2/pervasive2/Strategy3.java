package com.pervasive2.pervasive2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;


public class Strategy3 extends ActionBarActivity implements LocationListener {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_strategy3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

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

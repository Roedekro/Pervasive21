package com.pervasive2.pervasive2;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ragnar on 24/09/15.
 */
public class Strategy2 extends Activity {
    private long minTime = 0;
    private float minDist = 0;

    private float distance_treshold = 0;

    private Location lastGoodLocation;

    private LocationManager lm;

    private ArrayList<Location> receivedLocationUpdates;
    private ArrayList<Location> filteredLocations;

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d("RJ", "location received, loc: " + location.getLatitude() + ", " + location.getLongitude());
            receivedLocationUpdates.add(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {  }

        @Override
        public void onProviderEnabled(String s) {  }

        @Override
        public void onProviderDisabled(String s) {  }
    }


    private SeekBar seekBar;
    private TextView textView;


    private void updateLocation(Location loc) {
        if (lastGoodLocation != null) {

            // Distance
            float distance = lastGoodLocation.distanceTo(loc);

            if (distance > distance_treshold){
                lastGoodLocation = loc;
                filteredLocations.add(loc);

                // Notify that we have new location?
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy2);
        Toast.makeText(getApplicationContext(), "VERIFY TOAST WORKS?", Toast.LENGTH_SHORT).show();


        seekBar = (SeekBar) findViewById(R.id.distanceSetting);
        textView = (TextView) findViewById(R.id.distanceSettingView);
        textView.setText("WORKS?");

        // Initialize list
        receivedLocationUpdates = new ArrayList<Location>();
        filteredLocations = new ArrayList<Location>();

        LocationListener listener = new MyLocationListener();

        // Setup location service
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDist, listener);



        textView.setText("Covered: " + seekBar.getProgress() + "/" + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_strategy2, menu);
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

}

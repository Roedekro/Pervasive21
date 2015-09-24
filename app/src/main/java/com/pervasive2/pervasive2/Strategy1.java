package com.pervasive2.pervasive2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Strategy1 extends AppCompatActivity implements LocationListener {

    Button btnOK;
    EditText number;
    LocationManager lm;
    LocationListener ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy1);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        number = (EditText) findViewById(R.id.configText);
        btnOK = (Button) findViewById(R.id.okBtn);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHelper(Integer.parseInt(number.getText().toString()));
            }
        });




    }

    private void buttonHelper(int nr){
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, nr, 0, this);
    }
    @Override
    public void onProviderEnabled(String abe){

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void onStatusChanged(String x, int y, Bundle z){

    }

    @Override
    public void onLocationChanged(Location x){
        String end = "Latitude: " + x.getLatitude() + " Longitude: " + x.getLongitude();

        generateNoteOnSD("Stragery1Positions", end);
    }

    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Strategy1Info");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_strategy1, menu);
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

package com.pervasive2.pervasive2;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class KMLBuilderActivity extends ActionBarActivity {

    public boolean TC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmlbuilder);

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Pervasive2");
        if (!root.exists()) {
            root.mkdirs();
        }

        File file = new File(root, "Strategy1.txt");
        if(file.exists()) {
            try{
                File newFile = new File(root,"Strategy1.kml");
                FileWriter writer = new FileWriter(newFile,true);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                int c = 0;
                while ((line = br.readLine()) != null) {
                    c++;
                    String[] data = line.split(" ");
                    writer.append("<Placemark>");
                    writer.append("<name>"+c+"</name>");
                    writer.append("<description>Numer of GPS fixes: "+data[7]+" Time: "+data[5]+"</description>");
                    writer.append("<Point>\n" +
                            "<coordinates>"+data[1]+","+data[3]+",0</coordinates>\n" +
                            "</Point>");
                    writer.append("</Placemark>");
                }
                br.close();

                writer.append("</kml>");
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();

            }
        }

        file = new File(root, "Strategy2.txt");
        if(file.exists()) {
            try{
                File newFile = new File(root,"Strategy2.kml");
                FileWriter writer = new FileWriter(newFile,true);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                int c = 0;
                while ((line = br.readLine()) != null) {
                    c++;
                    String[] data = line.split(" ");
                    writer.append("<Placemark>");
                    writer.append("<name>"+c+"</name>");
                    writer.append("<description>Numer of GPS fixes: "+data[7]+" Time: "+data[5]+"</description>");
                    writer.append("<Point>\n" +
                            "<coordinates>"+data[1]+","+data[3]+",0</coordinates>\n" +
                            "</Point>");
                    writer.append("</Placemark>");
                }
                br.close();

                writer.append("</kml>");
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();

            }
        }

        file = new File(root, "Strategy3.txt");
        if(file.exists()) {
            try{
                File newFile = new File(root,"Strategy3.kml");
                FileWriter writer = new FileWriter(newFile,true);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                int c = 0;
                while ((line = br.readLine()) != null) {
                    c++;
                    String[] data = line.split(" ");
                    writer.append("<Placemark>");
                    writer.append("<name>"+c+"</name>");
                    writer.append("<description>Numer of GPS fixes: "+data[7]+" Time: "+data[5]+"</description>");
                    writer.append("<Point>\n" +
                            "<coordinates>"+data[1]+","+data[3]+",0</coordinates>\n" +
                            "</Point>");
                    writer.append("</Placemark>");
                }
                br.close();

                writer.append("</kml>");
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();

            }
        }

        file = new File(root, "Strategy4.txt");
        if(file.exists()) {
            try{
                File newFile = new File(root,"Strategy4.kml");
                FileWriter writer = new FileWriter(newFile,true);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                int c = 0;
                while ((line = br.readLine()) != null) {
                    c++;
                    String[] data = line.split(" ");
                    writer.append("<Placemark>");
                    writer.append("<name>"+c+"</name>");
                    writer.append("<description>Numer of GPS fixes: "+data[7]+" Time: "+data[5]+"</description>");
                    writer.append("<Point>\n" +
                            "<coordinates>"+data[1]+","+data[3]+",0</coordinates>\n" +
                            "</Point>");
                    writer.append("</Placemark>");
                }
                br.close();

                writer.append("</kml>");
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();

            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kmlbuilder, menu);
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

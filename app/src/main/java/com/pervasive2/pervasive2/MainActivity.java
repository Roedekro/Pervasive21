package com.pervasive2.pervasive2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    Button str1, str2, str3, str4, str5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        str1 = (Button) findViewById(R.id.strategy1Button);
        str1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in1 = new Intent(MainActivity.this, Strategy1.class);
                startActivity(in1);
            }
        });

        str2 = (Button) findViewById(R.id.strategy2Button);
        str2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2 = new Intent(MainActivity.this, Strategy2.class);
                startActivity(in2);
            }
        });

        str3 = (Button) findViewById(R.id.strategy3Button);
        str3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in3 = new Intent(MainActivity.this, Strategy3.class);
                startActivity(in3);
            }
        });

        str4 = (Button) findViewById(R.id.strategy4Button);
        str4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in4 = new Intent(MainActivity.this, Strategy4.class);
                startActivity(in4);
            }
        });

        str5 = (Button) findViewById(R.id.kmlButton);
        str5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in5 = new Intent(MainActivity.this, KMLBuilderActivity.class);
                startActivity(in5);
            }
        });

    }


}

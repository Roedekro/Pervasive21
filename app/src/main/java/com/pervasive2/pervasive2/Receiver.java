package com.pervasive2.pervasive2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Martin on 01-10-2015.
 */
public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle b = intent.getExtras();
        int i =  b.getInt("value");
        Intent anotherIntent;
        if(i == 1) {
            anotherIntent = new Intent("strat1");
        }
        else if(i== 2) {
            anotherIntent = new Intent("strat2");
        }
        else if(i==3) {
            anotherIntent = new Intent("strat3");
        }
        else  {
            anotherIntent = new Intent("strat4");
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(anotherIntent);

    }
}

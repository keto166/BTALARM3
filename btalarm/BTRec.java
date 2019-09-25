package com.example.tom.btalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class BTRec extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BTDCListenerService.instance != null) {
            BTDCListenerService.instance.toasty(intent);
        } else {
            Intent i = new Intent(context, BTDCListenerService.class);
            context.startService(i);

        }
    }
}

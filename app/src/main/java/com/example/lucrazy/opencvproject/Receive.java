package com.example.lucrazy.opencvproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lucrazy on 19-1-17.
 */

public class Receive extends BroadcastReceiver {
    Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context,BackgroudService.class);
        context.startService(intent);
    }
}

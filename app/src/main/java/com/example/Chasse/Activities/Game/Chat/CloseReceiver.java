package com.example.Chasse.Activities.Game.Chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CloseReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Chat", "je reçois le broadcast");
        ((Activity) context).finish();
    }
}

package com.ican.qnote.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MusicReceiver extends BroadcastReceiver {

    private static final String PLAYSTATE_CHANGED = "com.android.music.playstatechanged";
    public MusicReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean playing = false;
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals(PLAYSTATE_CHANGED)) {
            playing  = intent.getBooleanExtra("playing", false);
        }
        SharedPreferences preferences = context.getSharedPreferences("music",Context.MODE_PRIVATE);
        preferences.edit().putBoolean("playing",playing).apply();
    }
}

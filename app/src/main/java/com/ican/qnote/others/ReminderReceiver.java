package com.ican.qnote.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ican.qnote.ui.ReminderActivity;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.ican.qnote.others.ReminderReceiver";
    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ReminderActivity.class);
        context.startActivity(intent);
    }
}

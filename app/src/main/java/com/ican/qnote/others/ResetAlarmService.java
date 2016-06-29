package com.ican.qnote.others;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;

import com.ican.qnote.data.Note;
import com.ican.qnote.data.Reminder;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ResetAlarmService extends IntentService {

    public ResetAlarmService() {
        super("ResetAlarmService");
    }

    private class MyReminder {
        private long mNoteId;
        private long mAlarmTime;

        private void setNoteId(long noteId) {
            mNoteId = noteId;
        }

        private void setAlarmTime(long alarmTime) {
            mAlarmTime = alarmTime;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<MyReminder> reminders = new ArrayList<>();
        Cursor cursor;
        cursor = getContentResolver().query(Reminder.URI,
                new String[]{Reminder.NOTE_ID, Reminder.TIME}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MyReminder reminder = new MyReminder();
                reminder.setNoteId(cursor.getLong(cursor.getColumnIndex(Reminder.NOTE_ID)));
                reminder.setAlarmTime(cursor.getLong(cursor.getColumnIndex(Reminder.TIME)));
                if (reminder.mAlarmTime > System.currentTimeMillis())
                    reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        for (MyReminder reminder : reminders) {
            Intent i = new Intent(this, ReminderReceiver.class);
            i.setAction(ReminderReceiver.ACTION);
            i.setData(ContentUris.withAppendedId(Note.URI, reminder.mNoteId));
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent p = PendingIntent.getBroadcast(this, 0, i, 0);
            am.set(AlarmManager.RTC_WAKEUP, reminder.mAlarmTime, p);
        }
    }

}

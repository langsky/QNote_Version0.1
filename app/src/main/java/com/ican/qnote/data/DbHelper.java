package com.ican.qnote.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by swd1 on 16-6-28.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + Note.TABLE_NAME
                + "("
                + Note.ID + " INTEGER_PRIMARY_KAY AUTOINCREMENT, " //_id
                + Note.BG_ID + " INTEGER DEAFAULT 0, "
                + Note.GROUP_ID + " INTEGER DEAFAULT 1, "
                + Note.TITLE + " TEXT, "//title
                + Note.CREATE_TIME + " TEXT, "//create_time
                + Note.MODIFY_TIME + " TEXT, "//modify_time
                + Note.HAS_TEXT + " INTEGER DEAFAULT 0, "//has_text
                + Note.HAS_AUDIO + " INTEGER DEAFAULT 0, "//has_audio
                + Note.HAS_IMAGE + " INTEGER DEAFAULT 0, "//has_image
                + Note.HAS_REMINDER + " INTEGER DEAFAULT 0, "//has_reminder
                + Note.LABEL_ID + " INTEGER DEAFAULT 0 "//label_id
                + ")");
        db.execSQL("CREATE TABLE "
                + Text.TABLE_NAME
                + "("
                + Text.ID + "INTEGER PRIMARY KAY, "//_id
                + Text.NOTE_ID + " INTEGER, " //note_id
                + Text.CONTENT + " TEXT, "//content
                + Text.TEXT + " TEXT "//text
                + ")");
        db.execSQL("CREATE TABLE "
                + Image.TABLE_NAME
                + "("
                + Image.ID + "INTEGER PRIMARY KAY, " //_id
                + Image.NOTE_ID + " INTEGER, "//note_id
                + Image.CONTENT + " TEXT, "//content
                + Image.MIME_TYPE + " TEXT "//mime_type
                + ")");
        db.execSQL("CREATE TABLE "
                + Audio.TABLE_NAME
                + "("
                + Audio.ID + "INTEGER PRIMARY KAY, " //_id
                + Audio.NOTE_ID + " INTEGER, "//note_id
                + Audio.URI_CONTENT + " TEXT, "//uri
                + Audio.DURATION + " INTEGER "//duration
                + ")");
        db.execSQL("CREATE TABLE "
                + Reminder.TABLE_NAME
                + "("
                + Reminder.ID + "INTEGER PRIMARY KAY, " //_id
                + Reminder.NOTE_ID + " INTEGER, "//note_id
                + Reminder.URI_CONTENT + " TEXT,"//ringtone_uri
                + Reminder.TIME + " INTEGER "//reminder_time
                + ")");
        db.execSQL("CREATE TABLE "
                + Label.TABLE_NAME
                + "("
                + Label.ID + "INTEGER PRIMARY KAY, " //_id
                + Label.CONTENT + " TEXT "//note_id
                + ")");
        db.execSQL("CREATE TABLE "
                + Group.TABLE_NAME
                + "("
                + Group.ID + "INTEGER PRIMARY KAY, " //_id
                + Group.TITLE +" TEXT, "//title
                + Group.COUNT + " INTEGER"//count
                + ")");
        // TODO: 16-6-28 look at pr 1033004
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

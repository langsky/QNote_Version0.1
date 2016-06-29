package com.ican.qnote.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by swd1 on 16-6-28.
 */
public class Provider extends ContentProvider {

    public static final String DB_NAME = "qnote.db";

    public static final String URI_AUTHORITY = "com.ican.qnote.provider";

    private static final int URI_TEXT_TABLE = 1;
    private static final int URI_TEXT_ID = 2;
    private static final int URI_IMAGE_TABLE = 3;
    private static final int URI_IMAGE_ID = 4;
    private static final int URI_REMINDER_TABLE = 5;
    private static final int URI_REMINDER_ID = 6;
    private static final int URI_AUDIO_TABLE = 7;
    private static final int URI_AUDIO_ID = 8;
    private static final int URI_NOTE_TABLE = 9;
    private static final int URI_NOTE_ID = 10;
    private static final int URI_GROUP_TABLE = 11;
    private static final int URI_GROUP_ID = 12;
    private static final int URI_LABEL_TABLE = 13;
    private static final int URI_LABEL_ID = 14;

    public static final String URI_MIME_TEXT = "vnd.android.cursor.dir/vnd.note.text";
    public static final String URI_ITEM_MIME_TEXT = "vnd.android.cursor.item/vnd.note.text";
    public static final String URI_MIME_IMAGE = "vnd.android.cursor.dir/vnd.note.image";
    public static final String URI_ITEM_MIME_IMAGE = "vnd.android.cursor.item/vnd.note.image";
    public static final String URI_MIME_REMINDER = "vnd.android.cursor.dir/vnd.note.reminder";
    public static final String URI_ITEM_MIME_REMINDER = "vnd.android.cursor.item/vnd.note.reminder";
    public static final String URI_MIME_AUDIO = "vnd.android.cursor.dir/vnd.note.audio";
    public static final String URI_ITEM_MIME_AUDIO = "vnd.android.cursor.item/vnd.note.audio";
    public static final String URI_MIME_NOTE = "vnd.android.cursor.dir/vnd.note.note";
    public static final String URI_ITEM_MIME_NOTE = "vnd.android.cursor.item/vnd.note.note";
    public static final String URI_MIME_GROUP = "vnd.android.cursor.dir/vnd.note.group";
    public static final String URI_ITEM_MIME_GROUP = "vnd.android.cursor.item/vnd.note.group";
    public static final String URI_MIME_LABEL = "vnd.android.cursor.dir/vnd.note.label";
    public static final String URI_ITEM_MIME_LABEL = "vnd.android.cursor.item/vnd.note.label";

    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private DbHelper mDbHelper;
    private Context mContext;

    static {
        mUriMatcher.addURI(URI_AUTHORITY, Text.TABLE_NAME, URI_TEXT_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Text.TABLE_NAME + "/#", URI_TEXT_ID);
        mUriMatcher.addURI(URI_AUTHORITY, Image.TABLE_NAME, URI_IMAGE_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Image.TABLE_NAME + "/#", URI_IMAGE_ID);
        mUriMatcher.addURI(URI_AUTHORITY, Reminder.TABLE_NAME, URI_REMINDER_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Reminder.TABLE_NAME + "/#", URI_REMINDER_ID);
        mUriMatcher.addURI(URI_AUTHORITY, Audio.TABLE_NAME, URI_AUDIO_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Audio.TABLE_NAME + "/#", URI_AUDIO_ID);
        mUriMatcher.addURI(URI_AUTHORITY, Group.TABLE_NAME, URI_GROUP_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Group.TABLE_NAME + "/#", URI_GROUP_ID);
        mUriMatcher.addURI(URI_AUTHORITY, Note.TABLE_NAME, URI_NOTE_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Note.TABLE_NAME + "/#", URI_NOTE_ID);
        mUriMatcher.addURI(URI_AUTHORITY, Label.TABLE_NAME, URI_LABEL_TABLE);
        mUriMatcher.addURI(URI_AUTHORITY, Label.TABLE_NAME + "/#", URI_LABEL_ID);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new DbHelper(getContext(), DB_NAME, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        switch (mUriMatcher.match(uri)) {
            case URI_TEXT_TABLE:
                sqlBuilder.setTables(Text.TABLE_NAME);
                break;
            case URI_TEXT_ID:
                sqlBuilder.setTables(Text.TABLE_NAME);
                sqlBuilder.appendWhere(Text.ID + "=" + uri.getLastPathSegment());
                break;

            case URI_IMAGE_TABLE:
                sqlBuilder.setTables(Image.TABLE_NAME);
                break;
            case URI_IMAGE_ID:
                sqlBuilder.setTables(Image.TABLE_NAME);
                sqlBuilder.appendWhere(Image.ID + "=" + uri.getLastPathSegment());
                break;

            case URI_REMINDER_TABLE:
                sqlBuilder.setTables(Reminder.TABLE_NAME);
                break;
            case URI_REMINDER_ID:
                sqlBuilder.setTables(Reminder.TABLE_NAME);
                sqlBuilder.appendWhere(Reminder.ID + "=" + uri.getLastPathSegment());
                break;

            case URI_AUDIO_TABLE:
                sqlBuilder.setTables(Audio.TABLE_NAME);
                break;
            case URI_AUDIO_ID:
                sqlBuilder.setTables(Audio.TABLE_NAME);
                sqlBuilder.appendWhere(Audio.ID + "=" + uri.getLastPathSegment());
                break;

            case URI_NOTE_TABLE:
                sqlBuilder.setTables(Note.TABLE_NAME);
                break;
            case URI_NOTE_ID:
                sqlBuilder.setTables(Note.TABLE_NAME);
                sqlBuilder.appendWhere(Note.ID + "=" + uri.getLastPathSegment());
                break;

            case URI_GROUP_TABLE:
                sqlBuilder.setTables(Group.TABLE_NAME);
                break;
            case URI_GROUP_ID:
                sqlBuilder.setTables(Group.TABLE_NAME);
                sqlBuilder.appendWhere(Group.ID + "=" + uri.getLastPathSegment());
                break;

            case URI_LABEL_TABLE:
                sqlBuilder.setTables(Label.TABLE_NAME);
                break;
            case URI_LABEL_ID:
                sqlBuilder.setTables(Label.TABLE_NAME);
                sqlBuilder.appendWhere(Label.ID + "=" + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        cursor = sqlBuilder.query(mDbHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_TEXT_TABLE:
                return URI_MIME_TEXT;
            case URI_TEXT_ID:
                return URI_ITEM_MIME_TEXT;
            case URI_IMAGE_TABLE:
                return URI_MIME_IMAGE;
            case URI_IMAGE_ID:
                return URI_ITEM_MIME_IMAGE;
            case URI_REMINDER_TABLE:
                return URI_MIME_REMINDER;
            case URI_REMINDER_ID:
                return URI_ITEM_MIME_REMINDER;
            case URI_AUDIO_TABLE:
                return URI_MIME_AUDIO;
            case URI_AUDIO_ID:
                return URI_ITEM_MIME_AUDIO;
            case URI_NOTE_TABLE:
                return URI_MIME_NOTE;
            case URI_NOTE_ID:
                return URI_ITEM_MIME_NOTE;
            case URI_GROUP_TABLE:
                return URI_MIME_GROUP;
            case URI_GROUP_ID:
                return URI_ITEM_MIME_GROUP;
            case URI_LABEL_TABLE:
                return URI_MIME_LABEL;
            case URI_LABEL_ID:
                return URI_ITEM_MIME_LABEL;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowId;
        Uri rowUri = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //为了在添加新note时立即产生Image文件夹
        new File(mContext.getFilesDir().getAbsolutePath()+"/Image");
        switch (mUriMatcher.match(uri)) {
            case URI_TEXT_TABLE:
                rowId = db.insert(Text.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Text.URI, rowId);
                }
                break;
            case URI_IMAGE_TABLE:
                rowId = db.insert(Image.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Image.URI, rowId);
                }
                break;
            case URI_REMINDER_TABLE:
                rowId = db.insert(Reminder.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Reminder.URI, rowId);
                }
                break;
            case URI_AUDIO_TABLE:
                rowId = db.insert(Audio.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Audio.URI, rowId);
                }
                break;
            case URI_NOTE_TABLE:
                rowId = db.insert(Note.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Note.URI, rowId);
                }
                break;

            case URI_LABEL_TABLE:
                rowId = db.insert(Label.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Label.URI, rowId);
                }
                break;

            case URI_GROUP_TABLE:
                rowId = db.insert(Group.TABLE_NAME, null, values);
                if (rowId != -1) {
                    rowUri = ContentUris.withAppendedId(Group.URI, rowId);
                }
                break;

        }
        ContentResolver resolver = mContext.getContentResolver();
        if (resolver!=null)
            resolver.notifyChange(uri,null);
        db.close();
        return rowUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case URI_TEXT_TABLE:
                count = db.delete(Text.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_TEXT_ID:
                count = db.delete(Text.TABLE_NAME, Text.ID + "=" + uri.getLastPathSegment()
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;

            case URI_IMAGE_TABLE:
                count = db.delete(Image.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_IMAGE_ID:
                count = db
                        .delete(Image.TABLE_NAME,
                                Image.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_REMINDER_TABLE:
                count = db.delete(Reminder.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_REMINDER_ID:
                count = db
                        .delete(Reminder.TABLE_NAME,
                                Reminder.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_AUDIO_TABLE:
                count = db.delete(Audio.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_AUDIO_ID:
                count = db
                        .delete(Audio.TABLE_NAME,
                                Audio.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_NOTE_TABLE:
                count = db.delete(Note.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_NOTE_ID:
                count = db.delete(Note.TABLE_NAME, Note.ID + "=" + uri.getLastPathSegment()
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;

            case URI_GROUP_TABLE:
                count = db.delete(Group.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_GROUP_ID:
                count = db
                        .delete(Group.TABLE_NAME,
                                Group.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_LABEL_TABLE:
                count = db.delete(Label.TABLE_NAME, selection, selectionArgs);
                break;
            case URI_LABEL_ID:
                count = db
                        .delete(Label.TABLE_NAME,
                                Label.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // TODO: 16-6-28 how to add close() in the right location?
        db.close();
        ContentResolver resolver = mContext.getContentResolver();
        if (resolver!=null)
        resolver.notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int count;
        switch (mUriMatcher.match(uri)) {
            case URI_TEXT_TABLE:
                count = db.update(Text.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_TEXT_ID:
                count = db
                        .update(Text.TABLE_NAME,
                                values,
                                Text.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_IMAGE_TABLE:
                count = db.update(Image.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_IMAGE_ID:
                count = db
                        .update(Image.TABLE_NAME,
                                values,
                                Image.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_REMINDER_TABLE:
                count = db.update(Reminder.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_REMINDER_ID:
                count = db
                        .update(Reminder.TABLE_NAME,
                                values,
                                Reminder.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_AUDIO_TABLE:
                count = db.update(Audio.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_AUDIO_ID:
                count = db
                        .update(Audio.TABLE_NAME,
                                values,
                                Audio.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_NOTE_TABLE:
                count = db.update(Note.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_NOTE_ID:
                count = db
                        .update(Note.TABLE_NAME,
                                values,
                                Note.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_GROUP_TABLE:
                count = db.update(Group.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_GROUP_ID:
                count = db
                        .update(Group.TABLE_NAME,
                                values,
                                Group.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;

            case URI_LABEL_TABLE:
                count = db.update(Label.TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_LABEL_ID:
                count = db
                        .update(Label.TABLE_NAME,
                                values,
                                Label.ID
                                        + "="
                                        + uri.getLastPathSegment()
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                        + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }
}

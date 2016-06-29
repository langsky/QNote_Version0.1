package com.ican.qnote.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.ican.qnote.others.ReminderReceiver;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by swd1 on 16-6-28.
 * This class is to make cursor to object of Note.
 */
public class NoteHandler {


    public static Note cursorToNote(Cursor cursor) {
        Note note = new Note();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(Note.ID);
        if (columnIndex != -1) {
            note.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Note.GROUP_ID);
        if (columnIndex != -1) {
            note.mGroupId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Note.TITLE);
        if (columnIndex != -1) {
            note.mTitle = cursor.getString(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Note.LABEL_ID);
        if (columnIndex != -1) {
            note.mLabelId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Note.HAS_TEXT);
        if (columnIndex != -1) {
            note.mHasText = cursor.getInt(columnIndex) == 1;
        }
        columnIndex = cursor.getColumnIndex(Note.HAS_IMAGE);
        if (columnIndex != -1) {
            note.mHasImage = cursor.getInt(columnIndex) == 1;
        }
        columnIndex = cursor.getColumnIndex(Note.HAS_REMINDER);
        if (columnIndex != -1) {
            note.mHasReminder = cursor.getInt(columnIndex) == 1;
        }
        columnIndex = cursor.getColumnIndex(Note.HAS_AUDIO);
        if (columnIndex != -1) {
            note.mHasAudio = cursor.getInt(columnIndex) == 1;
        }
        columnIndex = cursor.getColumnIndex(Note.BG_ID);
        if (columnIndex != -1) {
            note.mBGId = cursor.getInt(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Note.CREATE_TIME);
        if (columnIndex != -1) {
            note.mCreateTime = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Note.MODIFY_TIME);
        if (columnIndex != -1) {
            note.mModifyTime = cursor.getLong(columnIndex);
        }
        return note;
    }

    public static Text cursorToText(Cursor cursor) {
        Text text = new Text();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(Text.ID);
        if (columnIndex != -1) {
            text.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Text.NOTE_ID);
        if (columnIndex != -1) {
            text.mNoteId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Text.CONTENT);
        if (columnIndex != -1) {
            text.mContent = cursor.getString(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Text.TEXT);
        if (columnIndex != -1) {
            text.mText = cursor.getString(columnIndex);
        }
        return text;
    }

    public static Image cursorToImage(Cursor cursor) {
        Image image = new Image();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(Image.ID);
        if (columnIndex != -1) {
            image.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Image.NOTE_ID);
        if (columnIndex != -1) {
            image.mNoteId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Image.MIME_TYPE);
        if (columnIndex != -1) {
            image.mMimeType = cursor.getString(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Image.CONTENT);
        if (columnIndex != -1) {
            image.mContent = cursor.getString(columnIndex);
        }
        return image;
    }

    public static Reminder cursorToReminder(Cursor cursor) {
        Reminder Reminder = new Reminder();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(com.ican.qnote.data.Reminder.ID);
        if (columnIndex != -1) {
            Reminder.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(com.ican.qnote.data.Reminder.NOTE_ID);
        if (columnIndex != -1) {
            Reminder.mNoteId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(com.ican.qnote.data.Reminder.URI_CONTENT);
        if (columnIndex != -1) {
            Reminder.mUriContent = cursor.getString(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(com.ican.qnote.data.Reminder.TIME);
        if (columnIndex != -1) {
            Reminder.mTime = cursor.getLong(columnIndex);
        }

        return Reminder;
    }

    public static Audio cursorToAudio(Cursor cursor) {
        Audio audio = new Audio();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(Audio.ID);
        if (columnIndex != -1) {
            audio.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Audio.NOTE_ID);
        if (columnIndex != -1) {
            audio.mNoteId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Audio.URI_CONTENT);
        if (columnIndex != -1) {
            audio.mUriContent = cursor.getString(columnIndex);
        }

        columnIndex = cursor.getColumnIndex(Audio.DURATION);
        if (columnIndex != -1) {
            audio.mDuration = cursor.getLong(columnIndex);
        }

        return audio;
    }

    public static Group cursorToGroup(Cursor cursor) {
        Group group = new Group();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(Group.ID);
        if (columnIndex != -1) {
            group.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Group.TITLE);
        if (columnIndex != -1) {
            group.mTitle = cursor.getString(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Group.COUNT);
        if (columnIndex != -1) {
            group.mCount = cursor.getLong(columnIndex);
        }
        return group;
    }

    public static Label cursorToLabel(Cursor cursor) {
        Label label = new Label();

        int columnIndex;
        columnIndex = cursor.getColumnIndex(Label.ID);
        if (columnIndex != -1) {
            label.mId = cursor.getLong(columnIndex);
        }
        columnIndex = cursor.getColumnIndex(Label.CONTENT);
        if (columnIndex != -1) {
            label.mContent = cursor.getString(columnIndex);
        }
        return label;
    }

    public static void deleteNote(long noteId, Context context) {
        Note note = null;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Note.URI, null, Note.ID + "=" + noteId, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            note = cursorToNote(cursor);
            cursor.close();
        }
        if (note == null)
            return;
        File file;
        String path;
        if (note.mHasText) {
            String whereClause = Text.NOTE_ID + "=" + noteId;
            resolver.delete(Text.URI,whereClause,null);
        }
        if (note.mHasImage) {
            cursor = resolver.query(Image.URI, new String[]{
                    Image.CONTENT
            }, Image.NOTE_ID + "=" + noteId, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    path = cursor.getString(cursor.getColumnIndex(Image.CONTENT));
                    if (path != null) {
                        AttachmentHandler.deleteImage(context, path);
                    }
                }
                cursor.close();
            }
        }
        if (note.mHasAudio) {
            cursor = resolver.query(Audio.URI, new String[]{
                    Audio.URI_CONTENT
            }, Audio.NOTE_ID + "=" + noteId, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    path = cursor.getString(cursor.getColumnIndex(Audio.URI_CONTENT));
                    if (path != null) {
                        file = new File(path);
                        if (file.exists()) file.delete();
                    }
                }
                cursor.close();
            }
        }

        if (note.mHasReminder) {
            cancelAlarm(context, noteId);
        }


        int value1 = 0;
        Cursor groupCursor = resolver.query(Group.URI, null, Group.ID
                + " = " + note.mGroupId, null, null);
        if (groupCursor != null && groupCursor.getCount() > 0) {
            groupCursor.moveToFirst();
            value1 = groupCursor.getInt(groupCursor.getColumnIndex(Group.TITLE)) - 1;
            if (value1 < 0) value1 = 0;
            groupCursor.close();
        }
        int value2 = 0;
        Cursor labelCursor = resolver.query(Label.URI, null, Label.ID
                + " = " + note.mLabelId, null, null);
        if (labelCursor != null && labelCursor.getCount() > 0) {
            labelCursor.moveToFirst();
            value2 = labelCursor.getInt(labelCursor.getColumnIndex(Group.TITLE)) - 1;
            if (value2 < 0) value2 = 0;
            labelCursor.close();
        }

        ContentProviderOperation.Builder builder;
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        builder = ContentProviderOperation.newDelete(Text.URI);
        builder.withSelection(Text.NOTE_ID + "=" + noteId, null);
        operations.add(builder.build());


        builder = ContentProviderOperation.newDelete(Reminder.URI);
        builder.withSelection(Reminder.NOTE_ID + "=" + noteId, null);
        operations.add(builder.build());

        builder = ContentProviderOperation.newDelete(Audio.URI);
        builder.withSelection(Audio.NOTE_ID + "=" + noteId, null);
        operations.add(builder.build());

        builder = ContentProviderOperation.newDelete(Note.URI);
        builder.withSelection(Note.ID + "=" + noteId, null);
        operations.add(builder.build());

        builder = ContentProviderOperation.newUpdate(Group.URI);
        builder.withSelection(Group.ID + "=" + note.mGroupId, null);
        builder.withValue(Group.TITLE, value1);
        operations.add(builder.build());

        builder = ContentProviderOperation.newUpdate(Label.URI);
        builder.withSelection(Label.ID + "=" + note.mLabelId, null);
        builder.withValue(Label.CONTENT, value2);
        operations.add(builder.build());

        try {
            resolver.applyBatch(Provider.URI_AUTHORITY, operations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void cancelAlarm(Context context, long NoteId) {
        Intent i = new Intent(context, ReminderReceiver.class);
        i.setData(ContentUris.withAppendedId(Note.URI, NoteId));
        PendingIntent p = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(p);
    }
}

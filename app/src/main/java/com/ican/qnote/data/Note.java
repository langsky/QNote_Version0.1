package com.ican.qnote.data;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by hgl on 16-6-28.
 * This class is used to describe the note object and its database.
 * The note should have text, image, audio, reminder and label, background color, title, related time.
 * sometimes you want to create a widget of a note.
 * sometimes you want to search some information from note.
 */
public class Note {
    public static final String TABLE_NAME = "note";
    public static final String ID = "_id";
    public static final String BG_ID = "bg_id";
    public static final String GROUP_ID = "group_id";
    public static final String HAS_TEXT = "has_text";
    public static final String HAS_IMAGE = "has_image";
    public static final String HAS_AUDIO = "has_audio";
    public static final String LABEL_ID = "label_id";
    public static final String HAS_REMINDER = "has_reminder";
    public static final String TITLE = "title";
    public static final String CREATE_TIME = "create_time";
    public static final String MODIFY_TIME = "modify_time";
    public static final Uri URI = Uri.parse("content://" +
            Provider.URI_AUTHORITY + "/" + TABLE_NAME);

    public long mId;
    public long mGroupId;
    public long mBGId;
    public String mTitle;
    public boolean mHasText;
    public boolean mHasImage;
    public boolean mHasAudio;
    public long mLabelId;
    public boolean mHasReminder;
    public long mCreateTime;
    public long mModifyTime;

    public ContentValues toConentValues() {
        ContentValues values = new ContentValues();
        if (mId != 0)
            values.put(ID, mId);
        values.put(BG_ID, mBGId);
        values.put(GROUP_ID, mGroupId);
        values.put(TITLE, mTitle);
        values.put(HAS_TEXT, mHasText);
        values.put(HAS_IMAGE, mHasImage);
        values.put(HAS_AUDIO, mHasAudio);
        values.put(HAS_REMINDER, mHasReminder);
        values.put(LABEL_ID, mLabelId);
        values.put(CREATE_TIME, mCreateTime);
        values.put(MODIFY_TIME, mModifyTime);
        return values;
    }

}


package com.ican.qnote.data;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by swd1 on 16-6-28.
 */
public class Audio {
    public static final String TABLE_NAME = "audio";
    public static final String ID = "_id";
    public static final String NOTE_ID = "note_id";
    public static final String URI_CONTENT = "content";
    public static final String DURATION = "duration";
    public static final Uri URI = Uri.parse(
            "content://"
                    + Provider.URI_AUTHORITY
                    + "/"
                    + TABLE_NAME);

    public long mId;
    public long mNoteId;
    public String mUriContent;
    public long mDuration;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (mId != 0)
            values.put(ID, mId);
        values.put(NOTE_ID, mNoteId);
        values.put(URI_CONTENT, mUriContent);
        values.put(DURATION, mDuration);
        return values;
    }
}

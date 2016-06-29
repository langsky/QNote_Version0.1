package com.ican.qnote.data;

import android.content.ContentValues;
import android.net.Uri;

/**
 * This class is used to describe Text, but pay attention  to the difference of
 * CONTENT and TEXT, they are not all same.
 */
public class Text {
    public static final String TABLE_NAME = "text";
    public static final String ID = "_id";
    public static final String NOTE_ID = "note_id";
    public static final String CONTENT = "content";
    public static final String TEXT = "text";
    public static final Uri URI = Uri.parse(
            "content://"
                    + Provider.URI_AUTHORITY
                    + "/"
                    + TABLE_NAME);

    public long mId;
    public long mNoteId;
    public String mContent;
    public String mText;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (mId != 0)
            values.put(ID, mId);
        values.put(NOTE_ID, mNoteId);
        values.put(CONTENT, mContent);
        values.put(TEXT, mText);
        return values;
    }

}

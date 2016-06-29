package com.ican.qnote.data;

import android.content.ContentValues;
import android.net.Uri;

/**
 * how to insert a image to a note? it is a problem.
 */
public class Image {
    public static final String TABLE_NAME = "image";
    public static final String ID = "_id";
    public static final String NOTE_ID = "note_id";
    public static final String CONTENT = "content";
    public static final String MIME_TYPE = "mime_type";
    public static final Uri URI = Uri.parse(
            "content://"
                    + Provider.URI_AUTHORITY
                    + "/"
                    + TABLE_NAME);

    public long mId;
    public long mNoteId;
    public String mContent;
    public String mMimeType;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (mId != 0)
            values.put(ID, mId);
        values.put(NOTE_ID, mNoteId);
        values.put(CONTENT, mContent);
        values.put(MIME_TYPE, mMimeType);
        return values;
    }
}

package com.ican.qnote.data;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by swd1 on 16-6-28.
 */
public class Label {
    public static final String TABLE_NAME = "label";
    public static final String ID = "_id";
    public static final String CONTENT = "content";
    public static final Uri URI = Uri.parse(
            "content://"
                    + Provider.URI_AUTHORITY
                    + "/"
                    + TABLE_NAME);

    public long mId;
    public String mContent;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (mId != 0)
            values.put(ID, mId);
        values.put(CONTENT, mContent);
        return values;
    }
}

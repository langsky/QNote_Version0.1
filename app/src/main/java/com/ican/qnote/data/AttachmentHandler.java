package com.ican.qnote.data;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by swd1 on 16-6-28.
 */
public class AttachmentHandler {
    /**
     * 最后确定image的存储位置在/data/data/package name/files/Image/*
     */

    public static void deleteImage(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            deleteImageFile(context, path);
            String whereClause = Image.CONTENT + " = ? ";
            String[] selectionArgs = new String[]{path};
            context.getContentResolver().delete(Image.URI, whereClause, selectionArgs);
        }
    }

    private static void deleteImageFile(Context context, String path) {
        File localFile = new File(getImagePath(context, path));
        if ((localFile.exists()) && (localFile.isFile()))
            localFile.delete();
    }

    public static String getImagePath(Context context, String path) {
        return getImageDir(context) + "/" + path;
    }

    private static String getImageDir(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/Image"; //getFilesDir() 获得的是内部存储，不需要权限访问。
    }

    public static String saveImage(Context context, Uri uri,
                                   long noteId) {
        String mimeType = getImageMimeType(context, uri);
        long time = System.currentTimeMillis();
        String content = "AT_IMAGE" + time + "_END";
        ContentValues values = new ContentValues();
        values.put(Image.MIME_TYPE, mimeType);
        values.put(Image.NOTE_ID, noteId);
        values.put(Image.CONTENT, content);
        context.getContentResolver().insert(Image.URI, values);
        saveImageFile(context, uri, content);
        return content;
    }

    private static boolean copyImageToDir(InputStream is, File destFile) {
        try {
            if (destFile.exists()) destFile.delete();
            FileOutputStream os = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, bytesRead);
                }
            } finally {
                os.flush();
                os.getFD().sync();
                os.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean saveImageFile(Context context, Uri uri, String content) {
        File destFile = new File(getImagePath(context,content));
        if (!destFile.exists()){
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream is;
        try {
            is = context.getContentResolver().openInputStream(uri);
            copyImageToDir(is, destFile);
            if (is !=null) is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getImageMimeType(Context context, Uri uri) {
        if (uri==null) return null;
        InputStream is;
        try {
            is = context.getContentResolver().openInputStream(uri);
            if (is!=null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds =true;
                BitmapFactory.decodeStream(is,null,options);
                is.close();
                return options.outMimeType;
            }
            else
                return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO: 16-6-29 实时更新数据库的操作，需要后续完成



}

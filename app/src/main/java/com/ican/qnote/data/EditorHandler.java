package com.ican.qnote.data;

import android.util.Pair;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by swd1 on 16-6-29.
 */
public class EditorHandler {

    private static final Pattern PATTERN_CONTACT = Pattern.compile("##([^\r\n]+?)@([^\r\n]+?)##");
    private static final Pattern PATTERN_IMAGE =Pattern.compile("^☺ (AT_IMAGE\\d+)$", Pattern.MULTILINE);
    static final SparseArray<String> TAG_NAME_MAP = new SparseArray<>(3);
    static final HashMap<String, Integer> TAG_TYPE_MAP= new HashMap<>(3);
    static {
        TAG_TYPE_MAP.put("√ ", 2);
        TAG_TYPE_MAP.put("☺ ", 3);
        TAG_NAME_MAP.put(2, "√ ");
        TAG_NAME_MAP.put(3, "☺ ");
    }

    public interface ParserHandler{
        void handleComplete();
        boolean handleContent(int a, String s);
    }

    public static class RichTextBuilder{
        private ArrayList<Pair<Integer, String>> mContents = new ArrayList<>();

        public RichTextBuilder append(int paramInt, String paramString) {
            mContents.add(new Pair<>(paramInt, paramString));
            return this;
        }

        public String build() {
            StringBuilder localStringBuilder = new StringBuilder();
            for (Pair<Integer, String> localPair : mContents) {
                if (localStringBuilder.length() > 0)
                    localStringBuilder.append("\n");
                String str = EditorHandler.TAG_NAME_MAP.get(localPair.first);
                if (str != null)
                    localStringBuilder.append(str);
                localStringBuilder.append(localPair.second);
            }
            return localStringBuilder.toString();
        }
    }

    public static class RichTextParser {

        private EditorHandler.ParserHandler mHandler;
        private String mText;

        public RichTextParser(String paramString, EditorHandler.ParserHandler paramParserHandler) {
            if (paramString == null)
                paramString = "";
            mText = paramString;
            mHandler = paramParserHandler;
        }

        public void parse() {
            String[] arrayOfString = this.mText.split("\n");
            int i = arrayOfString.length;
            for (String str : arrayOfString) {
                if (str.startsWith(EditorHandler.TAG_NAME_MAP.valueAt(0))) {
                    mHandler.handleContent(EditorHandler.TAG_NAME_MAP.keyAt(0),
                            str.substring((EditorHandler.TAG_NAME_MAP.valueAt(0)).length()));
                } else if (str.startsWith(EditorHandler.TAG_NAME_MAP.valueAt(1))) {
                    mHandler.handleContent(EditorHandler.TAG_NAME_MAP.keyAt(1),
                            str.substring((EditorHandler.TAG_NAME_MAP.valueAt(1)).length()));
                } else {
                    if (i == 1) {
                        mHandler.handleContent(4, str);
                    } else {
                        mHandler.handleContent(1, str);
                    }
                }
            }
            mHandler.handleComplete();
        }
    }

    public static boolean isInImageSpan(String str, int index){
        if(index==-1){
            return false;
        }
        String temp;
        if(str.length()>19&&str.length()>index+19) {
            temp= str.substring(index,index+19);
            if (temp.startsWith("AT_IMAGE") && temp.endsWith("END")) {
                return true;
            }
        }
        return false;
    }
}

package com.ican.qnote.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ican.qnote.R;
import com.ican.qnote.data.AttachmentHandler;
import com.ican.qnote.data.EditorHandler;

import java.util.ArrayList;

/**
 * Created by swd1 on 16-6-28.
 */
public class NoteEditView extends EditText {

    private Context mContext;
    private ImageTextWatcher mImageTextWatcher;
    Paint mPaint;
    private boolean isSelectAll;
    private boolean isView;
    private boolean isCut;
    private int imageCount;
    private boolean isShowing;
    ArrayList<String> tempCut;
    private boolean isSelectIndexBeforeImagespan;


    public NoteEditView(Context context) {
        super(context);
        init(context);
    }

    public NoteEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha(50);
        mPaint.setColor(getResources().getColor(R.color.colorPaint));
        mImageTextWatcher = new ImageTextWatcher();
        tempCut = new ArrayList<>();
        this.addTextChangedListener(mImageTextWatcher);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (selStart == selEnd) {
            int end = selEnd;
            if (getImageSpanEndAt(end) != null) {
                do {
                    end = getText().toString().indexOf('\n', end + 1);
                } while (getImageSpanEndAt(end) != null);
                if (end < 0) end = getText().length();
                setSelection(1 + getText().toString().lastIndexOf('\n', end - 1));
            }
        }
        if (isSelectAll) isSelectAll = false;
        if (isImageSpanBeginAt(selStart))
            setDelDialog();
        super.onSelectionChanged(selStart, selEnd);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(mImageTextWatcher);
        Editable editable = getText();
        SmartImageSpan[] arrayOfSmartImageSpan = editable.getSpans(0,
                editable.length(), SmartImageSpan.class);
        for (SmartImageSpan imageSpan : arrayOfSmartImageSpan) {
            editable.removeSpan(imageSpan);
            Drawable drawable = imageSpan.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                ((BitmapDrawable) drawable).getBitmap().recycle();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        setCursorVisible(true);
        try {
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isView)
            setMovementMethod(LinkMovementMethod.getInstance());
        else {
            setAutoLinkMask(0);
            setMovementMethod(getDefaultMovementMethod());
        }
        super.onDraw(canvas);
    }

    private SmartImageSpan getImageSpanEndAt(int end) {
        Editable editable = getText();
        if ((end > 0) && (end < editable.length())
                && (editable.charAt(end) == '\n')) {
            int i = end - 1;
            if ((i > 0) && (editable.charAt(i) == '\n')) {
                if (i > 0)
                    i++;
            }
            SmartImageSpan[] arrayOfSmartImageSpan = editable.getSpans(i,
                    end, SmartImageSpan.class);
            if ((arrayOfSmartImageSpan != null) && (arrayOfSmartImageSpan.length > 0)) {
                SmartImageSpan smartImageSpan = arrayOfSmartImageSpan[(-1 + arrayOfSmartImageSpan.length)];
                if (editable.getSpanEnd(smartImageSpan) == end)
                    return smartImageSpan;
            }
        }
        return null;
    }

    private boolean isImageSpanBeginAt(int selStart) {
        Editable localEditable = getText();
        if (selStart >= localEditable.length())
            return false;
        int i = localEditable.toString().indexOf('\n', selStart);
        if (i < 0)
            return false;
        SmartImageSpan[] arrayOfSmartImageSpan = localEditable.getSpans(selStart, i, SmartImageSpan.class);
        for (SmartImageSpan anArrayOfSmartImageSpan : arrayOfSmartImageSpan) {
            if (localEditable.getSpanStart(anArrayOfSmartImageSpan) == selStart)
                return true;
        }
        return false;
    }

    private boolean isImageSpanEndAt(int selEnd) {
        Editable editable = getText();
        if (selEnd >= editable.length())
            return false;
        int i = editable.toString().indexOf('\n', selEnd);
        if (i < 0)
            return false;
        SmartImageSpan[] arrayOfSmartImageSpan = editable.getSpans(selEnd, i, SmartImageSpan.class);
        for (SmartImageSpan anArrayOfSmartImageSpan : arrayOfSmartImageSpan)
            if (editable.getSpanEnd(anArrayOfSmartImageSpan) == selEnd)
                return true;
        return false;
    }

    private void setDelDialog() {

        final int selectedIndex = getSelectionStart();
        if (isImageSpanBeginAt(getSelectionStart())) {
            String title = mContext.getResources().getString(R.string.delete_item);
            String ok = mContext.getResources().getString(R.string.btn_ok);
            final Editable editable = NoteEditView.this.getText();
            AlertDialog d = new AlertDialog.Builder(mContext)
                    .setMessage(title)
                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SmartImageSpan selectedImageSpan = getSelectedImageSpan(selectedIndex);
                            if (selectedImageSpan != null) {
                                int start = editable.getSpanStart(selectedImageSpan);
                                int end = editable.getSpanEnd(selectedImageSpan);
                                if (editable.charAt(end) == '\n' && editable.length() > end + 1
                                        && !isImageSpanBeginAt(end + 1)) {
                                    end += 1;
                                }
                                editable.delete(start, end);
                                setSelection(start);
                                Drawable drawable = selectedImageSpan.getDrawable();
                                if (drawable instanceof BitmapDrawable) {
                                    ((BitmapDrawable) drawable).getBitmap().recycle();
                                }
                                saveTextForDb();
                                String text = NoteEditView.this.getText().toString().trim();
                                if (null != text && !"".equals(text)) {
                                    NoteEditView.this.append(" ");
                                }
                                AttachmentHandler.deleteImage(mContext,
                                        selectedImageSpan.mContent);
                            }
                            isShowing = false;
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            NoteEditView.this.requestFocus();

                            int selectionEnd = getSelectionEnd();

                            if (selectionEnd < 0 || !isImageSpanEndAt(selectionEnd)) {
                                selectionEnd = getText().length();
                                setSelection(selectionEnd);
                            } else {
                                setSelection(selectionEnd);
                            }
                            isShowing = false;
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // TODO Auto-generated method stub
                            isShowing = false;
                            NoteEditView.this.requestFocus();
                            int selectionEnd = getSelectionEnd();
                            if (selectionEnd < 0 || !isImageSpanEndAt(selectionEnd)) {
                                selectionEnd = getText().length();
                                setSelection(selectionEnd);
                            } else setSelection(selectionEnd);
                        }
                    }).create();
            if (!isShowing) d.show();
            isShowing = true;
        }

    }

    private class ImageTextWatcher implements TextWatcher {
        private int mReplaceType;
        private boolean isSkip;
        private NoteEditView.SmartImageSpan mToBeDeleteImageSpan;
        private boolean isLeaveBeforeImage;
        private boolean isToBeMarkStrike;

        private ImageTextWatcher() {
        }

        private <T> void deleteSpans(int paramInt1, int paramInt2, Class<T> paramClass) {
            Editable editable = NoteEditView.this.getText();
            for (Object localObject : editable.getSpans(paramInt1, paramInt1 + paramInt2,
                    paramClass)) {
                editable.removeSpan(localObject);
                if ((localObject instanceof NoteEditView.SmartImageSpan)) {

                    if (isCut)
                        tempCut.add(((NoteEditView.SmartImageSpan) localObject).mContent);
                    else
                        AttachmentHandler.deleteImage(mContext,
                                ((SmartImageSpan) localObject).mContent);
                    imageCount--;
                    Drawable drawable = ((NoteEditView.SmartImageSpan) localObject).getDrawable();
                    if (drawable instanceof BitmapDrawable) {
                        ((BitmapDrawable) drawable).getBitmap().recycle();
                    }
                }
            }
            isCut = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            if(NoteUtils.isMTKPlatform()&&isSelectIndexBeforeImagespan) {
                if(getSelectionStart()==1&&s.toString().substring(getSelectionStart() - 1, getSelectionStart()).equals("\n")&&count==1&&after==0) {
                    isLeaveBeforeImage=true;
                    return;
                }else if(getSelectionStart()>=2&& s.toString().substring(getSelectionStart() - 2, getSelectionStart()).equals("\n\n")&&count==1&&after==0){
                    isLeaveBeforeImage=true;
                    return;
                }
                isLeaveBeforeImage=false;
                changetheSelection();
                return;
            }
            mToBeDeleteImageSpan = null;
            isToBeMarkStrike = false;
            int i = s.length();
            boolean bool = false;
            if (i == 0)
                bool = true;
            isSkip = bool;
            if (isSkip)
                return;
            String str = s.toString();
            int j = 1 + str.lastIndexOf('\n', start - 1);
            int k = str.indexOf('\n', j);
            int m = 0;
            if (k < 0) {
                m = str.length();
                if (start != j) {
                    mReplaceType = 2;
                } else if (start + count < m) {
                    mReplaceType = 1;
                } else {
                    mReplaceType = 3;
                }
            } else {
                m = k - 1;
                if (m >= j) {
                    mReplaceType = 1;
                } else {
                    m = j;
                    mReplaceType = 3;
                }
            }
            if (((NoteEditView.this.getText().getSpans(j, m,
                    StrikethroughSpan.class)).length > 0)
                    && (this.mReplaceType != 3))
                this.isToBeMarkStrike = true;
            if (count > 0) {
                deleteSpans(start, count, SmartImageSpan.class);
                deleteSpans(start, count, StrikethroughSpan.class);
                mToBeDeleteImageSpan = NoteEditView.this.getImageSpanEndAt(start);
            }
            changetheSelection();

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class SmartImageSpan extends ImageSpan {
        int mBottom;
        int mLeft;
        int mWidth;
        int mHeight;
        int mDescent;
        String mContent;

        public SmartImageSpan(Drawable d, String source) {
            super(d, DynamicDrawableSpan.ALIGN_BASELINE);
            mWidth = d.getIntrinsicWidth();
            mHeight = d.getIntrinsicHeight();
            mContent = source;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            this.mLeft = (int) x;
            this.mBottom = bottom;
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        }

        public int getImageBottom() {
            return mBottom - mDescent;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            if (fm != null)
                mDescent = fm.descent;
            return super.getSize(paint, text, start, end, fm);
        }

    }

    private class RichParserHandler implements EditorHandler.ParserHandler {
        private SpannableStringBuilder mBuilder = new SpannableStringBuilder();

        private RichParserHandler() {
        }

        @Override
        public void handleComplete() {
            NoteEditView.this.setText(mBuilder);
        }

        @Override
        public boolean handleContent(int a, String s) {
            int i = mBuilder.length();
            switch (a) {
                case 2:
                    NoteEditView.this.appendAndMarkQueryResult(mBuilder, s);
                    mBuilder.append('\n');
                    int k = mBuilder.length();
                    NoteEditView.this.setStrikeSpan(mBuilder, i, k - 1);
                    return false;
                case 3:
                    mBuilder.append(s).append("\n");
                    int j = mBuilder.length();
                    NoteEditView.this.setImageSpan(mBuilder, i, j - 1, s);
                    return false;
                case 4:
                    NoteEditView.this.appendAndMarkQueryResult(mBuilder, s);
                    return false;
                default:
                    NoteEditView.this.appendAndMarkQueryResult(mBuilder, s);
                    mBuilder.append('\n');
                    break;
            }
            return true;
        }

    }


    private void setImageSpan(Editable editable, int start, int end, String path) {
        Bitmap bitmap = adjustBitmapSize(AttachmentHandler.getImagePath(mContext, path));
        if (bitmap != null) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(),
                    bitmap);
            bitmapDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            if (start <= editable.length() && end <= editable.length()) {
                editable.setSpan(new SmartImageSpan(bitmapDrawable, path), start,
                        end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                imageCount++;
            }
        }
    }

    private Bitmap adjustBitmapSize(String imagePath) {
        DisplayMetrics localDisplayMetrics = mContext.getResources().getDisplayMetrics();
        float ff = 0.9f;
        return resizeImageAttachment((int) (ff * localDisplayMetrics.widthPixels),
                (int) (0.9F * localDisplayMetrics.heightPixels), imagePath,
                (NinePatchDrawable) mContext.getResources().getDrawable(R.drawable.actionbar));
    }

    public Bitmap resizeImageAttachment(int paramInt1, int paramInt2, String path,
                                        NinePatchDrawable paramNinePatchDrawable) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if ((width <= 0) || (height <= 0)) return null;
        float rate;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        if (width > height) {
            if (metrics.widthPixels > metrics.heightPixels) {
                rate = getRate(width, paramInt2);
                width = paramInt2;
                height = (int) (height / rate);
            } else {
                rate = getRate(width, paramInt1);
                width = paramInt1;
                height = (int) (height / rate);
            }
        } else {
            if (metrics.widthPixels > metrics.heightPixels) {
                rate = getRate(width, paramInt2);
                width = paramInt2;
                height = (int) (height / rate);
            } else {
                rate = getRate(width, paramInt1);
                width = paramInt1;
                height = (int) (height / rate);
            }
        }
        if (options.outWidth < paramInt1) {
            width = options.outWidth;
            height = options.outHeight;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) rate;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null) return null;
        Rect rect = new Rect();
        paramNinePatchDrawable.getPadding(rect);
        Bitmap bitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        paramNinePatchDrawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        paramNinePatchDrawable.draw(canvas);
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
        bitmap.recycle();
        return bitmap2;
    }

    private float getRate(int p1, int p2) {
        String tempS = p1 / p2 + "." + (p1 % p2) * 10 / p2 + "" + (p1 % p2) * 100 / p2;
        return Float.parseFloat(tempS);
    }

    private void setStrikeSpan(Editable editable, int start, int end) {
        if (start > editable.length() || end > editable.length()) return;
        editable.setSpan(new StrikethroughSpan(), start, end, 17);
    }

    private void appendAndMarkQueryResult(Editable editable, String s) {
        if (!TextUtils.isEmpty(s))
            editable.append(s);
    }

    public void setAsView(boolean isView) {
        this.isView = isView;
    }

    public boolean isView() {
        return isView;
    }

    private int isImageString(int paramInt) {
        Editable editable = getText();
        if (paramInt >= editable.length())
            return paramInt;
        int i = editable.toString().indexOf('\n', paramInt);
        if (i < 0)
            return paramInt;
        SmartImageSpan[] arrayOfSmartImageSpan = editable.getSpans(paramInt, i, SmartImageSpan.class);
        for (SmartImageSpan anArrayOfSmartImageSpan : arrayOfSmartImageSpan) {
            int start = editable.getSpanStart(anArrayOfSmartImageSpan);
            int end = editable.getSpanEnd(anArrayOfSmartImageSpan);
            if (start < paramInt && end > paramInt) {
                return end;
            }
        }
        return paramInt;
    }

}

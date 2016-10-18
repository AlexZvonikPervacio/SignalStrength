package com.example.circularprogress;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtil {

    public static final long FRAME_DURATION = 1000 / 60;

    public static void setBackground(View v, Drawable drawable){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            v.setBackground(drawable);
        else
            v.setBackgroundDrawable(drawable);
    }

    /**
     * Apply any View style attributes to a view.
     * @param v The view is applied.
     * @param resId The style resourceId.
     */
    public static void applyStyle(View v, int resId){
        applyStyle(v, null, 0, resId);
    }

    /**
     * Apply any View style attributes to a view.
     * @param v The view is applied.
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public static void applyStyle(View v, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray a = v.getContext().obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, defStyleRes);

        int leftPadding = -1;
        int topPadding = -1;
        int rightPadding = -1;
        int bottomPadding = -1;
        int startPadding = Integer.MIN_VALUE;
        int endPadding = Integer.MIN_VALUE;
        int padding = -1;

        boolean startPaddingDefined = false;
        boolean endPaddingDefined = false;
        boolean leftPaddingDefined = false;
        boolean rightPaddingDefined = false;

        for(int i = 0, count = a.getIndexCount(); i < count; i++){
            int attr = a.getIndex(i);
            if(attr == R.styleable.View_android_background) {
                Drawable bg = a.getDrawable(attr);
                ViewUtil.setBackground(v, bg);
            }
            else if(attr == R.styleable.View_android_backgroundTint){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    v.setBackgroundTintList(a.getColorStateList(attr));
            }
            else if(attr == R.styleable.View_android_backgroundTintMode){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    int value = a.getInt(attr, 3);
                    switch (value){
                        case 3:
                            v.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                            break;
                        case 5:
                            v.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                            break;
                        case 9:
                            v.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 14:
                            v.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            break;
                        case 15:
                            v.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
                            break;
                        case 16:
                            v.setBackgroundTintMode(PorterDuff.Mode.ADD);
                            break;
                    }
                }
            }
            else if(attr == R.styleable.View_android_elevation){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    v.setElevation(a.getDimensionPixelOffset(attr, 0));
            }
            else if(attr == R.styleable.View_android_padding) {
                padding = a.getDimensionPixelSize(attr, -1);
                leftPaddingDefined = true;
                rightPaddingDefined = true;
            }
            else if(attr == R.styleable.View_android_paddingLeft) {
                leftPadding = a.getDimensionPixelSize(attr, -1);
                leftPaddingDefined = true;
            }
            else if(attr == R.styleable.View_android_paddingTop)
                topPadding = a.getDimensionPixelSize(attr, -1);
            else if(attr == R.styleable.View_android_paddingRight) {
                rightPadding = a.getDimensionPixelSize(attr, -1);
                rightPaddingDefined = true;
            }
            else if(attr == R.styleable.View_android_paddingBottom)
                bottomPadding = a.getDimensionPixelSize(attr, -1);
            else if(attr == R.styleable.View_android_paddingStart) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    startPadding = a.getDimensionPixelSize(attr, Integer.MIN_VALUE);
                    startPaddingDefined = (startPadding != Integer.MIN_VALUE);
                }
            }
            else if(attr == R.styleable.View_android_paddingEnd) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    endPadding = a.getDimensionPixelSize(attr, Integer.MIN_VALUE);
                    endPaddingDefined = (endPadding != Integer.MIN_VALUE);
                }
            }
            else if(attr == R.styleable.View_android_fadeScrollbars)
                v.setScrollbarFadingEnabled(a.getBoolean(attr, true));
            else if(attr == R.styleable.View_android_fadingEdgeLength)
                v.setFadingEdgeLength(a.getDimensionPixelOffset(attr, 0));
            else if(attr == R.styleable.View_android_minHeight)
                v.setMinimumHeight(a.getDimensionPixelSize(attr, 0));
            else if(attr == R.styleable.View_android_minWidth)
                v.setMinimumWidth(a.getDimensionPixelSize(attr, 0));
            else if(attr == R.styleable.View_android_requiresFadingEdge)
                v.setVerticalFadingEdgeEnabled(a.getBoolean(attr, true));
            else if(attr == R.styleable.View_android_scrollbarDefaultDelayBeforeFade) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    v.setScrollBarDefaultDelayBeforeFade(a.getInteger(attr, 0));
            }
            else if(attr == R.styleable.View_android_scrollbarFadeDuration) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    v.setScrollBarFadeDuration(a.getInteger(attr, 0));
            }
            else if(attr == R.styleable.View_android_scrollbarSize) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    v.setScrollBarSize(a.getDimensionPixelSize(attr, 0));
            }
            else if(attr == R.styleable.View_android_scrollbarStyle) {
                int value = a.getInteger(attr, 0);
                switch (value){
                    case 0x0:
                        v.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        break;
                    case 0x01000000:
                        v.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                        break;
                    case 0x02000000:
                        v.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                        break;
                    case 0x03000000:
                        v.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
                        break;
                }
            }
            else if(attr == R.styleable.View_android_soundEffectsEnabled)
                v.setSoundEffectsEnabled(a.getBoolean(attr, true));
            else if(attr == R.styleable.View_android_textAlignment){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    int value = a.getInteger(attr, 0);
                    switch (value){
                        case 0:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
                            break;
                        case 1:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                            break;
                        case 2:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            break;
                        case 3:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                            break;
                        case 4:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            break;
                        case 5:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            break;
                        case 6:
                            v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                            break;
                    }
                }
            }
            else if(attr == R.styleable.View_android_textDirection){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    int value = a.getInteger(attr, 0);
                    switch (value){
                        case 0:
                            v.setTextDirection(View.TEXT_DIRECTION_INHERIT);
                            break;
                        case 1:
                            v.setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);
                            break;
                        case 2:
                            v.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                            break;
                        case 3:
                            v.setTextDirection(View.TEXT_DIRECTION_LTR);
                            break;
                        case 4:
                            v.setTextDirection(View.TEXT_DIRECTION_RTL);
                            break;
                        case 5:
                            v.setTextDirection(View.TEXT_DIRECTION_LOCALE);
                            break;
                    }
                }
            }
            else if(attr == R.styleable.View_android_visibility){
                int value = a.getInteger(attr, 0);
                switch (value){
                    case 0:
                        v.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        v.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        v.setVisibility(View.GONE);
                        break;
                }
            }
            else if(attr == R.styleable.View_android_layoutDirection){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    int value = a.getInteger(attr, 0);
                    switch (value){
                        case 0:
                            v.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            break;
                        case 1:
                            v.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                            break;
                        case 2:
                            v.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
                            break;
                        case 3:
                            v.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
                            break;
                    }
                }
            }
            else if(attr == R.styleable.View_android_src){
                if(v instanceof ImageView){
                    int resId = a.getResourceId(attr, 0);
                    ((ImageView)v).setImageResource(resId);
                }
            }
        }

        if (padding >= 0)
            v.setPadding(padding, padding, padding, padding);
        else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
            if(startPaddingDefined)
                leftPadding = startPadding;
            if(endPaddingDefined)
                rightPadding = endPadding;

            v.setPadding(leftPadding >= 0 ? leftPadding : v.getPaddingLeft(),
                    topPadding >= 0 ? topPadding : v.getPaddingTop(),
                    rightPadding >= 0 ? rightPadding : v.getPaddingRight(),
                    bottomPadding >= 0 ? bottomPadding : v.getPaddingBottom());
        }
        else{
            if(leftPaddingDefined || rightPaddingDefined)
                v.setPadding(leftPaddingDefined ? leftPadding : v.getPaddingLeft(),
                        topPadding >= 0 ? topPadding : v.getPaddingTop(),
                        rightPaddingDefined ? rightPadding : v.getPaddingRight(),
                        bottomPadding >= 0 ? bottomPadding : v.getPaddingBottom());

            if(startPaddingDefined || endPaddingDefined)
                v.setPaddingRelative(startPaddingDefined ? startPadding : v.getPaddingStart(),
                        topPadding >= 0 ? topPadding : v.getPaddingTop(),
                        endPaddingDefined ? endPadding : v.getPaddingEnd(),
                        bottomPadding >= 0 ? bottomPadding : v.getPaddingBottom());
        }

        a.recycle();

        if(v instanceof TextView)
            applyStyle((TextView)v, attrs, defStyleAttr, defStyleRes);
    }

}

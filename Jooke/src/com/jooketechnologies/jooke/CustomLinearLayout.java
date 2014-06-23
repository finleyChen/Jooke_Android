package com.jooketechnologies.jooke;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout
{
    private Context myContext;

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec+((int)myContext.getResources().getDimension(R.dimen.quickplay_offset)));
    }
}
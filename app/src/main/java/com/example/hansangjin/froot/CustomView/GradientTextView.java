package com.example.hansangjin.froot.CustomView;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.hansangjin.froot.R;

/**
 * Created by hansangjin on 2018. 2. 21..
 */

public class GradientTextView extends TextView {
    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            getPaint().setShader(new LinearGradient(0, 0, getWidth(), 0,
                    ContextCompat.getColor(getContext(),
                            R.color.logoColor),
                    ContextCompat.getColor(getContext(),
                            R.color.logoColor),
                    Shader.TileMode.CLAMP));
        }
    }
}

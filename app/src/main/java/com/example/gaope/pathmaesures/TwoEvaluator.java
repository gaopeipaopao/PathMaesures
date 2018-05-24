package com.example.gaope.pathmaesures;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * Created by gaope on 2018/5/24.
 */

public class TwoEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        float end = (float) endValue;
        float start = (float) startValue;
        Log.d("eeee",":"+(start + fraction * end));
        return (start + fraction * end);
    }
}

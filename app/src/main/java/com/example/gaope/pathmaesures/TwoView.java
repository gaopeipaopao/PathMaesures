package com.example.gaope.pathmaesures;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 初始状态
 * 准备状态
 * 开始搜索状态
 * 结束状态
 * 三个动画状态
 * 1.从内部放大镜图标变为一个点
 * 2.绕圆环运动
 * 3.从一个点变为一个放大镜
 *在操作动画时，记得开启的逻辑
 * Created by gaope on 2018/5/24.
 */

public class TwoView extends View {

    private static final String TAG = "TwoView";

    private Paint paint;

    /**
     * 外部圆环的路径
     */
    private Path pathCrile;

    /**
     * 内部放大镜的路径
     */
    private Path pathMagnifier;

    private PathMeasure pm;

    /**
     * 外部圆环的起点位置
     */
    private float[] crilePos;

    /**
     * 外部圆环位置的正切值
     */
    private float[] crileTan;

    /**
     * 初始状态
     */
    private boolean begain = true;

    /**
     * 变为一个点,becomePoint为true时，执行变为一个点的动画
     */
    private boolean becomePoint = false;

    /**
     * 绕圆环运动，runningCrile为true时，执行绕圆环运动动画
     */
    private boolean runningCrile = false;

    /**
     * 变为放大镜，becomeCrile为true时，执行变为放大镜动画
     */
    private boolean becomeCrile = false;

    /**
     * 映射整个path
     */
    private float curr;

    /**
     *
     */
    private Path path;
    private Path path1;
    private Path path2;


    public TwoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        pathCrile = new Path();
        pathMagnifier = new Path();

        pm = new PathMeasure();

        crilePos = new float[2];
        crileTan = new float[2];


        //建立内部与外部的路径
        init();
    }

    private void init() {

        RectF crile = new RectF(-100,-100,100,100);
        //path的addArc的第三个参数为移动的度数 ，所以终点度数是第二个参数加上第三个参数
        pathCrile.addArc(crile,45, (float) 359.9);
        pm.setPath(pathCrile,false);
        pm.getPosTan(0,crilePos,crileTan);
     //   Log.d(TAG,"crilePos[0]:"+crilePos[0]+"    crilePos[1]"+crilePos[1]);

        RectF magnifier = new RectF(-50,-50,50,50);
        //path的addArc的第三个参数为移动的度数 ，所以终点度数是第二个参数加上第三个参数
        pathMagnifier.addArc(magnifier,45, (float) 359.9);
        pathMagnifier.lineTo(crilePos[0],crilePos[1]);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#cdcdcd"));
        canvas.translate(getWidth()/2,getHeight()/2);



        if (begain){
            Log.d(TAG,"abababb");
            //初始状态
            canvas.drawPath(pathMagnifier,paint);
            begain = false;
            becomePoint = true;
            pm.setPath(pathMagnifier,false);
            invalidate();
        }

        if (becomePoint){
            //Log.d("aaa","aaaa");
            if (path == null){
                path = new Path();
                //从放大镜变为一个点
                becomePointAnimation();
            }else {
                path.reset();
                pm.getSegment(curr * pm.getLength(),pm.getLength(),path,true);
              //  Log.d(TAG,"curr:"+curr);
              //  Log.d(TAG,"curr * pm.getLength():"+curr * pm.getLength());
                canvas.drawPath(path,paint);
            }
//            becomePoint = false;
//            runningCrile = true;
        }
//        canvas.drawPath(path,paint);
//        path.reset();

        if (runningCrile){

            if (path1 == null){
                path1 = new Path();
                pm.setPath(pathCrile,false);
                //Log.d(TAG,"ecec");
                //绕圆环运动
                runningCrileAnimation();
            } else {
                path.reset();
                //逆着来画外部的圆
              //  Log.d(TAG,"curr:"+curr);
               // Log.d(TAG,"curr * pm.getLength():"+curr * pm.getLength());
                if (curr >= 0.8){
                    pm.getSegment(curr * pm.getLength() - 20,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0.7){
                    pm.getSegment(curr * pm.getLength() - 30,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0.6){
                    pm.getSegment(curr * pm.getLength() - 40,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0.5){
                    pm.getSegment(curr * pm.getLength() - 50,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0.4){
                    pm.getSegment(curr * pm.getLength() - 60,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0.3){
                    pm.getSegment(curr * pm.getLength() - 40,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0.2){
                    pm.getSegment(curr * pm.getLength() - 30,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }else if (curr >= 0){
                    pm.getSegment(curr * pm.getLength() - 20,curr * pm.getLength(),path,true);
                    canvas.drawPath(path,paint);
                }
//                pm.getSegment(curr * pm.getLength(),pm.getLength(),path,true);
//                canvas.drawPath(path,paint);
            }
//            runningCrile = false;
//            becomeCrile = true;
        }
        if (becomeCrile){

            if (path2 == null){
                Log.d(TAG,TAG);
                pm.setPath(pathMagnifier,false);
                path2 = new Path();
                curr = 1;
                //变为放大镜
                becomeCrileAnimation();
            }else {
                path.reset();
                pm.getSegment(curr * pm.getLength(),pm.getLength(),path,true);
                Log.d(TAG,"curr:"+curr);
                Log.d(TAG,"curr * pm.getLength():"+curr * pm.getLength());
                canvas.drawPath(path,paint);
            }

        }









    }

    private void becomeCrileAnimation() {

        ValueAnimator value = ValueAnimator.ofFloat(1,0);
        value.setDuration(2000);
        value.setInterpolator(new AccelerateDecelerateInterpolator());
        value.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Log.d(TAG,"bbbbbbb");
                curr = (float) animation.getAnimatedValue();
                //  Log.d(TAG,"currb:"+curr);
                if (curr == 0){
                   // runningCrile = false;
                    becomeCrile = false;
                    begain = true;
                    //Log.d(TAG,"cccccc");
                }
                invalidate();
            }
        });
        value.start();
    }

    private void runningCrileAnimation() {

        ValueAnimator value = ValueAnimator.ofFloat(1,0);
        value.setDuration(2000);
        value.setInterpolator(new AccelerateDecelerateInterpolator());
        value.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curr = (float) animation.getAnimatedValue();
               // Log.d(TAG,"currb:"+curr);
                if (curr == 0){
                    runningCrile = false;
                    becomeCrile = true;
                }
                invalidate();
            }
        });
        value.start();
    }

    private void becomePointAnimation() {

        ValueAnimator value = ValueAnimator.ofFloat(0,1);
        value.setDuration(2000);
        value.setInterpolator(new AccelerateDecelerateInterpolator());
        value.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               // Log.d(TAG,"bbbbbbb");
                curr = (float) animation.getAnimatedValue();
              //  Log.d(TAG,"currb:"+curr);
                if (curr == 1){
                    becomePoint = false;
                    runningCrile = true;
                    //Log.d(TAG,"cccccc");
                }
                invalidate();
            }
        });
        value.start();
    }
}

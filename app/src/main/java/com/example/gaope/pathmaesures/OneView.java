package com.example.gaope.pathmaesures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * getPosTan()方法
 * getMatrix()方法
 * Created by gaope on 2018/5/24.
 */

public class OneView extends View {

    private static final String TAG = "OneView";

    private Paint paint;
    private PathMeasure pm;

    /**
     *当前点的位置
     */
    private float[] pos;

    /**
     * 当前的的tan值
     */
    private float[] tan;

    /**
     * 记录当前的位置,映射整个path
     */
    private float cuur = 0;

    private Bitmap bitmap;
    private Matrix matrix;

    /**
     *
     * @param context
     * @param attrs
     */


    public OneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8);

        pm = new PathMeasure();

        pos = new float[2];
        tan = new float[2];

        BitmapFactory.Options bp = new BitmapFactory.Options();
        //缩放图片
        bp.inSampleSize = 2;
        bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.arrow1,bp);
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.addCircle(getWidth()/2,getHeight()/2,200, Path.Direction.CW);
        pm.setPath(path,false);

        cuur = (float) (cuur + 0.005);
        if (cuur >= 1){
            cuur = 0;
        }


       // pmGetPostTan(path,canvas);
        pmGetMatrix(path,canvas);

    }

    private void pmGetMatrix(Path path, Canvas canvas) {
        pm.getMatrix(pm.getLength() * cuur,matrix,PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        matrix.preTranslate(- bitmap.getWidth() / 2, - bitmap.getHeight() / 2);
        canvas.drawPath(path,paint);
        canvas.drawBitmap(bitmap,matrix,paint);
        invalidate();
    }

    private void pmGetPostTan(Path path,Canvas canvas) {
        pm.getPosTan(pm.getLength() * cuur,pos,tan);
        Log.d(TAG,"pos[0]:" +  pos[0] + "    pos[1]:" + pos[1]);
        Log.d(TAG,"tan[0]:" + tan[0] + "    tan[1]:" + tan[1]);


        matrix.reset();
//        float degress = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        //Math.atan2(tan[1], tan[0])为弧度值
        float degress = (float) Math.toDegrees(Math.atan2(tan[1], tan[0]));
        Log.d(TAG,"atan2:"+Math.atan2(tan[1], tan[0]));
        Log.d(TAG,"toDegress:"+Math.toDegrees(Math.atan2(tan[1], tan[0])));
        //绕照片的中点旋转degress角度
        matrix.postRotate(degress,bitmap.getWidth() / 2,bitmap.getHeight() / 2);
        matrix.postTranslate(pos[0] - bitmap.getWidth() / 2,pos[1] - bitmap.getHeight() / 2);
        Log.d(TAG,"degress:"+degress);
        canvas.drawPath(path,paint);
        canvas.drawBitmap(bitmap,matrix,paint);
        invalidate();
    }

}

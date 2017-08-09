package com.cdy.shoppingcart.shoppingcartdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蓝兵 on 2017/8/8.
 */

public class ShopCarUtil {

    private static ShopCarUtil sInstance;
    //动画的图片
    private Drawable mDrawable;
    // 动画时间
    private int mAnimationDuration = 5000;
    private Bitmap mBitmap;
    private ImageView mImageView;

    private ShopCarUtil() {
    }

    public static synchronized ShopCarUtil getInstance() {
        if (sInstance == null) {
            sInstance = new ShopCarUtil();
        }
        return sInstance;
    }

    private Context mContext;
    private FrameLayout animation_viewGroup;

    public void setAnimDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    boolean isClean;

    public boolean isClean() {
        return isClean;
    }

    public void clearAll() {
        if (animation_viewGroup != null) {
            //animation_viewGroup.removeAllViews();
            //number = 0;
            isClean = true;
        }
    }

    private Handler mHandler = new Handler();

    public ShopCarUtil createAnimLayout(Activity activity) {
        mContext = activity;

        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        //animLayout.setBackgroundResource(R.color.yellow12);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);

        animation_viewGroup = animLayout;

        mImageView = new ImageView(mContext);

        mImageView.setImageDrawable(mDrawable);
        mImageView.setAlpha(0.8f);
        animation_viewGroup.addView(mImageView);
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //mImageView.setPadding(5, 5, 5, 5);
        mImageView.setLayoutParams(lp1);

        mImageView.setBackgroundResource(R.color.yellow12);

        init();

        return this;
    }

    private Paint textPaint;

    private void init() {
        //初始化绘制贝塞尔曲线画笔
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8f);
        mPaint.setColor(Color.RED);
        //初始化绘制辅助线和文字的画笔
        textPaint = new Paint();
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(4f);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(40);
    }

    /**
     * 动画效果设置
     *
     * @param start_location 起始位置
     */
    @SuppressLint("NewApi")
    public void doAnim(int[] start_location, int[] end_location, final ShopCarAnimationListener listener) {

        final ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mDrawable);
        imageView.setAlpha(0.6f);
        animation_viewGroup.addView(imageView);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = start_location[0];
        lp.topMargin = start_location[1];
        imageView.setPadding(5, 5, 5, 5);
        imageView.setLayoutParams(lp);

        Animation mScaleAnimation = new ScaleAnimation(1.2f, 0.6f, 1.2f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mScaleAnimation.setFillAfter(true);

        // 计算位移
        int endX = end_location[0] - start_location[0];// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
                0.3f);
        mRotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);

        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);

        set.setDuration(mAnimationDuration);// 动画的执行时间
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                listener.onAnimationStart(animation, animation_viewGroup.getChildCount());
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                //在android.view.animation.Animation.AnimationListener的onAnimationStart, onAnimationEnd, onAnimationRepeat 中不要addView或removeView
                //
                //这样可能会引起java.lang.NullPointerException: Attempt to read from field ‘int android.view.View.mViewFlags’ on a null object reference，因为这个三个动画的回调方法都是在View.draw()中执行的，而在draw函数中不要addView或removeView。在addView的方法说明中有强调do
                //not invoke this method from {@link #draw(android.graphics.Canvas)},{@link #onDraw(android.graphics.Canvas)},{@link #dispatchDraw(android.graphics.Canvas)} or any related method.

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e("run", "----" + number);
                        animation_viewGroup.removeView(imageView);
                        listener.onAnimationEnd(animation, animation_viewGroup.getChildCount());
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        imageView.startAnimation(set);
    }

    /**
     * 动画效果设置
     *
     * @param start_location 起始位置
     */
    @SuppressLint("NewApi")
    public void doAnimator(int[] start_location, int[] end_location, final ShopCarAnimationListener listener) {

        final ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mDrawable);
        imageView.setAlpha(0.6f);
        animation_viewGroup.addView(imageView);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = start_location[0];
        lp.topMargin = start_location[1];
        imageView.setPadding(5, 5, 5, 5);
        imageView.setLayoutParams(lp);

        //int x = end_location[0] - start_location[0];
        //int y = end_location[1] - start_location[1];

        int endX = end_location[0] - start_location[0];// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标

        List<Animator> animators = new ArrayList<>();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.2f, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.2f, 0.6f);

        //http://blog.csdn.net/gzejia/article/details/51063564
        //加速器
        ObjectAnimator translationX = ObjectAnimator.ofFloat(imageView, "translationX", 0, endX);
        translationX.setInterpolator(new LinearInterpolator());

        ObjectAnimator translationY = ObjectAnimator.ofFloat(imageView, "translationY", 0, -20, endY);
        translationY.setInterpolator(new ShopCarInterpolator());

        //translationX.setInterpolator(new DecelerateInterpolator());
        //translationX.setRepeatCount(ValueAnimator.INFINITE);
        //translationX.setRepeatMode(ValueAnimator.REVERSE);
        //translationX.setDuration(mAnimationDuration);

        animators.add(scaleX);
        animators.add(scaleY);
        animators.add(translationY);
        animators.add(translationX);

        //translationY.start();
        //translationX.start();

        AnimatorSet mAnimatorSet = new AnimatorSet();

        mAnimatorSet.setDuration(mAnimationDuration);
        mAnimatorSet.playTogether(animators);

        mAnimatorSet.start();
    }

    Paint mPaint;
    private float radius = 8f;

    public void doAnimator1(int[] start_location, int[] end_location, final ShopCarAnimationListener listener) {

        final ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mDrawable);
        imageView.setAlpha(0.6f);
        animation_viewGroup.addView(imageView);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //lp.leftMargin = start_location[0];
        //lp.topMargin = start_location[1];
        //imageView.setPadding(5, 5, 5, 5);
        imageView.setLayoutParams(lp);

        //imageView.setTranslationX(start_location[0]);
        //imageView.setTranslationY(start_location[1]);

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8f);
        mPaint.setColor(Color.RED);

        Log.e("doAnimator()", "----start_location[0]----" + start_location[0] + "----");
        Log.e("doAnimator()", "----start_location[1]----" + start_location[1] + "----");
        Log.e("doAnimator()", "----end_location[0]----" + end_location[0] + "----");
        Log.e("doAnimator()", "----end_location[1]----" + end_location[1] + "----");

        PointF dataPoint1, dataPoint2, controlPoint;

        //初始化数据点和控制点
        dataPoint1 = new PointF();
        dataPoint2 = new PointF();
        controlPoint = new PointF();

        dataPoint1.set(start_location[0], start_location[1]);
        dataPoint2.set(end_location[0], end_location[1]);

        //controlPoint.set(start_location[0], start_location[1]);
        //controlPoint.set(end_location[0], start_location[1]);
        controlPoint.set(end_location[0] - 50, start_location[1] - 50);

        //初始化路径
        Path mPath = new Path();
        mPath.reset();

        //绘制二阶贝塞尔曲线
        mPath.moveTo(dataPoint1.x, dataPoint1.y);
        mPath.quadTo(controlPoint.x, controlPoint.y, dataPoint2.x, dataPoint2.y);

        //mPath.lineTo(dataPoint2.x, dataPoint2.y);

        //RectF mRectF = new RectF();
        //
        //mRectF.set(start_location[0], start_location[1], end_location[0], end_location[1]);
        //mPath.addRect(mRectF, Path.Direction.CW);

        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ascii).copy(Bitmap.Config.ARGB_8888, true);

        final Canvas canvas = new Canvas(mBitmap);
        canvas.drawPath(mPath, mPaint);

        //绘制点
        canvas.drawCircle(dataPoint1.x, dataPoint1.y, radius, textPaint);
        canvas.drawCircle(dataPoint2.x, dataPoint2.y, radius, textPaint);
        canvas.drawCircle(controlPoint.x, controlPoint.y, radius, textPaint);
        //绘制文字
        canvas.drawText("数据点1", dataPoint1.x + 10, dataPoint1.y + 10, textPaint);
        canvas.drawText("数据点2", dataPoint2.x + 10, dataPoint2.y + 10, textPaint);
        canvas.drawText("控制点", controlPoint.x + 10, controlPoint.y + 10, textPaint);
        //绘制辅助线
        canvas.drawLine(dataPoint1.x, dataPoint1.y, controlPoint.x, controlPoint.y, textPaint);
        canvas.drawLine(controlPoint.x, controlPoint.y, dataPoint2.x, dataPoint2.y, textPaint);

        final PathMeasure pathMeasure = new PathMeasure();

        pathMeasure.setPath(mPath, false);

        float length = pathMeasure.getLength();
        Log.e("doAnimator", "----length---" + length + "-------");


        ValueAnimator animator = ValueAnimator.ofFloat(0, length);
        animator.setDuration(8 * 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                //tan[0]是邻边 tan[1]是对边
                Log.e("doAnimator", "----distance---" + distance + "-------");

                float[] pos = new float[2];
                float[] tan = new float[2];

                pathMeasure.getPosTan(distance, pos, tan);

                float translationX = pos[0];
                float translationY = pos[1];

                //float translationX = pos[0] - 450;
                //float translationY = pos[1] - 200;

                imageView.setTranslationX(translationX);
                imageView.setTranslationY(translationY);


                int[] end_location = new int[2];
                imageView.getLocationInWindow(end_location);
                Log.e("onAnimationUpdate", "----**- " + end_location[0] + " ------- " + end_location[1]);
                Log.e("onAnimationUpdate", "------- " + translationX + " ------- " + translationY);

                //绘制圆
                mPaint.setColor(Color.RED);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(pos[0], pos[1], 12, mPaint);
                mImageView.setImageBitmap(mBitmap);
            }
        });
        animator.start();
    }

    /**
     * 销毁当前单例，防止内存泄露
     */
    public void onDestroy() {

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (sInstance != null) {
            sInstance = null;
            mContext = null;
        }
    }
}

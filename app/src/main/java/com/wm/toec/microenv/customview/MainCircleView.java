package com.wm.toec.microenv.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wm on 2018/1/16.
 * 详情界面的圆形view 修改了不能显示float问题
 */

public class MainCircleView extends View {

    private int mRadius; // 画布边缘半径（去除padding后的半径）
    private int mStartAngle = 150; // 起始角度
    private int mSweepAngle = 240; // 绘制角度
    private int mMin = 350; // 最小值
    private int mMax = 950; // 最大值
    private int mSection = 10; // 值域（mMax-mMin）等分份数
    private int mPortion = 3; // 一个mSection等分份数
    private String mHeaderText = "当前成员："; // 表头
    private int mCreditValue = 650; // 信用分
    private float mSolidCreditValue = mCreditValue; // 信用分(设定后不变)
    private String unit = "暂无";
    private String jumpWord = "点击查看详细健康报告";
    private float mPreNum = 0; // 变化初始数值
    private int mSparkleWidth; // 亮点宽度
    private int mProgressWidth; // 进度圆弧宽度
    private float mLength1; // 刻度顶部相对边缘的长度
    private float mLength2; // 刻度读数顶部相对边缘的长度
    private int delay = 600;
    private boolean isMinus = false;//所显示数据是否为负数

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint;
    private RectF mRectFProgressArc;
    private Path mPath;
    private Rect mRectText;
    private String[] mTexts;

    private boolean isAnimFinish = true;
    private float mAngleWhenAnim;

    public MainCircleView(Context context) {
        this(context, null);
    }

    public MainCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化方法
     */

    private void init() {
        mSparkleWidth = dp2px(16);
        mProgressWidth = dp2px(3);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setColor(Color.WHITE);

        mRectFProgressArc = new RectF();
        mPath = new Path();
        mRectText = new Rect();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        mLength1 = mPadding + mSparkleWidth / 2f + dp2px(8);

        int width = resolveSize(dp2px(220), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width - dp2px(30));

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mRectFProgressArc.set(
                mPadding + mSparkleWidth / 2f,
                mPadding + mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f
        );

        mPaint.setTextSize(sp2px(10));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画进度圆弧背景
         */
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setAlpha(80);
        canvas.drawArc(mRectFProgressArc, mStartAngle + 1, mSweepAngle - 2, false, mPaint);

        mPaint.setAlpha(255);
        if (isAnimFinish) {
            /**
             * 画进度圆弧(起始到信用值)
             */
            mPaint.setShader(generateSweepGradient());
            canvas.drawArc(mRectFProgressArc, mStartAngle + 1,
                    calculateRelativeAngleWithValue(950) - 2, false, mPaint);
            /**
             * 画信用值指示亮点
             */
            float[] point = getCoordinatePoint(
                    mRadius - mSparkleWidth / 2f,
                    mStartAngle + calculateRelativeAngleWithValue(950)
            );
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setShader(generateRadialGradient(point[0], point[1]));
            canvas.drawCircle(point[0], point[1], mSparkleWidth / 2f, mPaint);
        } else {
            /**
             * 画进度圆弧(起始到信用值)
             */
            mPaint.setShader(generateSweepGradient());
            canvas.drawArc(mRectFProgressArc, mStartAngle + 1,
                    mAngleWhenAnim - mStartAngle - 2, false, mPaint);
            /**
             * 画信用值指示亮点
             */
            float[] point = getCoordinatePoint(
                    mRadius - mSparkleWidth / 2f,
                    mAngleWhenAnim
            );
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setShader(generateRadialGradient(point[0], point[1]));
            canvas.drawCircle(point[0], point[1], mSparkleWidth / 2f, mPaint);
        }
        /**
         * 画点状的内圈
         */
        int cnt = (mMax - mMin) / 2 / 10;
        float degree = mSweepAngle / ((mMax - mMin) / 10);
        float a = calculateRelativeAngleWithValue(1000);
        float b = mSweepAngle / 2f;
        mPaint.setShader(null);
        mPaint.setAlpha(200);
        canvas.save();
        canvas.drawLine(mCenterX, mPadding + mLength1, mCenterX, mPadding + mLength1 - 1, mPaint);
        // 逆时针旋转
        for (int i = 0; i < cnt; i++) {
            canvas.rotate(-degree, mCenterX, mCenterY);
            b -= degree;
            mPaint.setAlpha(200);
            canvas.drawLine(mCenterX, mPadding + mLength1, mCenterX, mPadding + mLength1 - 1, mPaint);
        }
        canvas.restore();
        canvas.save();
        // 顺时针旋转
        b = mSweepAngle / 2f;
        for (int i = 0; i < cnt; i++) {
            canvas.rotate(degree, mCenterX, mCenterY);
            b += degree;
            mPaint.setAlpha(200);
            canvas.drawLine(mCenterX, mPadding + mLength1, mCenterX, mPadding + mLength1 - 1, mPaint);
        }
        canvas.restore();


        /**
         * 画中心数值
         */
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(sp2px(35));
        mPaint.setTextAlign(Paint.Align.CENTER);
        String value = String.valueOf(mSolidCreditValue);
        if (isMinus) {
            value = "-" + value;
        }
        canvas.drawText(value, mCenterX, mCenterY, mPaint);

        /**
         * 画单位描述
         */
        mPaint.setAlpha(160);
        mPaint.setTextSize(sp2px(10));
        canvas.drawText(mHeaderText, mCenterX, mCenterY - dp2px(65), mPaint);

        /**
         * 画成员姓名姓名
         */
        mPaint.setAlpha(255);
        mPaint.setTextSize(sp2px(14));
        canvas.drawText(unit, mCenterX, mCenterY - dp2px(40), mPaint);

        /**
         * 画 点击跳转文字
         */
        mPaint.setAlpha(160);
        mPaint.setTextSize(sp2px(9));
        canvas.drawText(jumpWord, mCenterX, mCenterY + dp2px(25), mPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    private SweepGradient generateSweepGradient() {
        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY,
                new int[]{Color.argb(0, 255, 255, 255), Color.argb(200, 255, 255, 255)},
                new float[]{0, calculateRelativeAngleWithValue(mSolidCreditValue) / 360}
        );
        Matrix matrix = new Matrix();
        matrix.setRotate(mStartAngle - 1, mCenterX, mCenterY);
        sweepGradient.setLocalMatrix(matrix);

        return sweepGradient;
    }

    private RadialGradient generateRadialGradient(float x, float y) {
        return new RadialGradient(x, y, mSparkleWidth / 2f,
                new int[]{Color.argb(255, 255, 255, 255), Color.argb(80, 255, 255, 255)},
                new float[]{0.4f, 1},
                Shader.TileMode.CLAMP
        );
    }

    private float[] getCoordinatePoint(float radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    /**
     * 相对起始角度计算信用分所对应的角度大小
     */
    private float calculateRelativeAngleWithValue(float value) {
        float degreePerSection = 1f * mSweepAngle / mSection;
        if (value > 700) {
            return 8 * degreePerSection + 2 * degreePerSection / 250 * (value - 700);
        } else if (value > 650) {
            return 6 * degreePerSection + 2 * degreePerSection / 50 * (value - 650);
        } else if (value > 600) {
            return 4 * degreePerSection + 2 * degreePerSection / 50 * (value - 600);
        } else if (value > 550) {
            return 2 * degreePerSection + 2 * degreePerSection / 50 * (value - 550);
        } else {
            return 2 * degreePerSection / 200 * (value - 350);
        }
    }

    private SimpleDateFormat mDateFormat;

    private String getFormatTimeStr() {
        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        }
        return String.format("查询时间:%s", mDateFormat.format(new Date()));
    }

    public int getCreditValue() {
        return mCreditValue;
    }

    /**
     * 设置 成员姓名
     */
    public void setUnit(String unit) {
        this.unit = unit == null || unit == "" ? "暂无" : unit;
    }


    /**
     * 设置数值并播放动画
     *
     * @param creditValue 数值
     */
    public void setValueWithAnim(float creditValue) {
      /*  if (creditValue < mMin || creditValue > mMax || !isAnimFinish) {
            return;
        }*/
        if (!isAnimFinish) {
            return;
        }
        //当数值小于10时动画速度变快
        delay = creditValue <= 12 ? 300 : 600;

        if (creditValue < 0) {
            creditValue = Math.abs(creditValue);
            isMinus = true;
        } else {
            isMinus = false;
        }
        mSolidCreditValue = creditValue;
        //设置初始值动画
        ValueAnimator creditValueAnimator = ValueAnimator.ofFloat(mPreNum, mSolidCreditValue);
        creditValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = (float) animation.getAnimatedValue();
                mSolidCreditValue = (float) Math.round(temp * 100) / 100;
                postInvalidate();
            }
        });

        // 计算最终值对应的角度，以扫过的角度的线性变化来播放动画
        float degree = calculateRelativeAngleWithValue(950);
        ValueAnimator degreeValueAnimator = ValueAnimator.ofFloat(mStartAngle, mStartAngle + degree);
        degreeValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngleWhenAnim = (float) animation.getAnimatedValue();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .setDuration(delay)
                .playTogether(creditValueAnimator, degreeValueAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimFinish = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimFinish = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isAnimFinish = true;
            }
        });
        animatorSet.start();
    }


}

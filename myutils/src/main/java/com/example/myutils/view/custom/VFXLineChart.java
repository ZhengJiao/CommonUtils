package com.example.myutils.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.myutils.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zheng on 2018/3/19 0019.
 * 折线图（财经日历详情）
 */

public class VFXLineChart extends View {

    private String startDateStr = "";
    private String endDateStr = "";
    //数据线左边的标识数据
    private List<Float> lineDataList = new ArrayList<>();
    //折线图数据
    private List<Float> lineBrokenDataList = new ArrayList<>();

    private Paint mPaint;
    private int lineStraightColor, textDataColor, textDateColor, lineBrokenColor;
    private float textDataSize, textDateSize;
    private float lineBrokenWidth;
    private Float max;
    private Float min;

    private Rect endDateRect;
    private int lineAllHeight;
    private int allDataWidth;
    private Rect iRect;

    private int weekPosition = -1;

    //定时器
    private CountDownTimer timer;

    //发布时间
    private List<String> dateList = new ArrayList<>();
    private int allDataHeight;
    private String unit = "";

    public VFXLineChart(Context context) {
        this(context, null);
    }

    public VFXLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VFXLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlAttrs(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
    }

    private void initXmlAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VFXLineChart);
        if (typedArray == null) return;
        lineStraightColor = typedArray.getColor(R.styleable.VFXLineChart_line_straight_color, getResources().getColor(R.color.gray_b6b6b6));
        textDataColor = typedArray.getColor(R.styleable.VFXLineChart_text_data_color, getResources().getColor(R.color.gray_b6b6b6));
        textDataSize = typedArray.getDimension(R.styleable.VFXLineChart_text_data_size, 32);
        textDateColor = typedArray.getColor(R.styleable.VFXLineChart_text_date_color, getResources().getColor(R.color.gray_b6b6b6));
        textDateSize = typedArray.getDimension(R.styleable.VFXLineChart_text_date_size, 38);
        lineBrokenColor = typedArray.getColor(R.styleable.VFXLineChart_line_broken_color, getResources().getColor(R.color.yellow_c9a13b));
        lineBrokenWidth = typedArray.getDimension(R.styleable.VFXLineChart_line_broken_width, 4);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制日期
        drawDate(canvas);
        // 绘制数据线
        drawDataLine(canvas);
        // 绘制折现
        drawBrokenLine(canvas);
        //绘制点击事件的显示数据
        drawClickData(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                int weekScale = allDataWidth / 10;
                for (int i = 0; i < lineBrokenDataList.size(); i++) {
                    if (Math.abs(event.getX() - (iRect.width() + allDataWidth / 10 * i)) < weekScale / 2) {
                        weekPosition = i;
                        //倒计时4秒隐藏
                        if (timer != null)
                            timer.cancel();
                        startCountDownTime(4);
                        invalidate();
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private void startCountDownTime(long time) {

        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
            }

            @Override
            public void onFinish() {
                //倒计时结束
                weekPosition = -1;
                invalidate();
            }
        };
        timer.start();// 开始计时
        //timer.cancel(); // 取消
    }

    private void drawClickData(Canvas canvas) {

        if (weekPosition == -1) return;

        Paint weekPaint = new Paint();
        weekPaint.setAntiAlias(true);
        weekPaint.setStrokeWidth(2);
        weekPaint.setTextSize(30);
        weekPaint.setColor(getResources().getColor(R.color.black_a7262601));

        //绘制矩形数据
        String topDataStr = lineBrokenDataList.get(weekPosition) + unit;
        String bottomDataStr = dateList.get(weekPosition);

        //百分比
        Rect topDataRect = new Rect();
        weekPaint.getTextBounds(topDataStr, 0, topDataStr.length(), topDataRect);

        //日期
        Rect bottomDataRect = new Rect();
        weekPaint.getTextBounds(bottomDataStr, 0, bottomDataStr.length(), bottomDataRect);

        weekPaint.setColor(Color.parseColor("#99c69d5e"));

        int weekPointX = iRect.width() + allDataWidth / 10 * weekPosition;
        int weekPointY = (int) (lineAllHeight / 6 + (getHeight() - lineAllHeight / 6 - endDateRect.height() * 2) / (max - min) * (max - lineBrokenDataList.get(weekPosition)));

        int tempWidth = topDataRect.width() > bottomDataRect.width() ? topDataRect.width() : bottomDataRect.width();
        RectF weekBgRect = new RectF(
                weekPosition < 6 ?
                        (weekPointX + 10) :
                        (weekPointX - tempWidth - 40),
                weekPointY - topDataRect.height() - 10 - bottomDataRect.height() - 10 - 10,
                weekPosition < 6 ?
                        (weekPointX + tempWidth + 40) :
                        (weekPointX - 10),
                weekPointY - 10
        );

        canvas.drawRoundRect(weekBgRect, 3, 3, weekPaint);
        weekPaint.setColor(Color.WHITE);

        Paint.FontMetricsInt fontMetrics = weekPaint.getFontMetricsInt();
        int baseline = (int) ((weekBgRect.bottom + weekBgRect.top - fontMetrics.bottom - fontMetrics.top) / 2);

        weekPaint.setTextAlign(Paint.Align.LEFT);
        //绘制上下文本
        canvas.drawText(
                topDataStr,
                weekBgRect.left + 10,
                baseline - topDataRect.height() / 2 - 5,
                weekPaint
        );
        weekPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(
                bottomDataStr,
                weekBgRect.centerX(),
                baseline + bottomDataRect.height() / 2 + 3,
                weekPaint
        );

        weekPaint.setColor(lineBrokenColor);
        //空心效果
        weekPaint.setStyle(Paint.Style.STROKE);
        //绘制圆点
        canvas.drawCircle(
                iRect.width() + allDataWidth / 10 * weekPosition,
                (int) (lineAllHeight / 6 + allDataHeight / (max - min) * (max - lineBrokenDataList.get(weekPosition))),
                4,
                weekPaint
        );
        weekPaint.setColor(Color.WHITE);
        //空心效果
        weekPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(
                iRect.width() + allDataWidth / 10 * weekPosition,
                (int) (lineAllHeight / 6 + allDataHeight / (max - min) * (max - lineBrokenDataList.get(weekPosition))),
                3,
                weekPaint
        );
        mPaint.setColor(Color.BLACK);
    }

    private void drawBrokenLine(Canvas canvas) {
        iRect = new Rect();
        String iStr = "999999.9";
        mPaint.getTextBounds(iStr, 0, iStr.length(), iRect);
        allDataWidth = getWidth() - iRect.width();
        allDataHeight = getHeight() - lineAllHeight / 6 - endDateRect.height() * 2;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(lineBrokenColor);
        mPaint.setStrokeWidth(lineBrokenWidth);
        Path path = new Path();
        //折线图真实数据
        if (lineBrokenDataList.size() > 0)
            path.moveTo(
                    iRect.width(),
                    (int) (lineAllHeight / 6 + allDataHeight / (max - min) * (max - lineBrokenDataList.get(0)))
            );

        for (int i = 1; i < lineBrokenDataList.size(); i++) {
            path.lineTo(
                    iRect.width() + allDataWidth / 10 * i,
                    (int) (lineAllHeight / 6 + allDataHeight / (max - min) * (max - lineBrokenDataList.get(i)))
            );
        }

        canvas.drawPath(path, mPaint);

        mPaint.setColor(Color.BLACK);
    }

    private void drawDataLine(Canvas canvas) {

        mPaint.setColor(lineStraightColor);
        mPaint.setStrokeWidth((float) 0.5);
        //画六条横线
        lineAllHeight = getHeight() - endDateRect.height() * 2;
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(
                    0, lineAllHeight / 6 * (i + 1),
                    getWidth(), lineAllHeight / 6 * (i + 1),
                    mPaint
            );
        }

        mPaint.setTextSize(textDataSize);
        mPaint.setColor(textDataColor);
        mPaint.setTextAlign(Paint.Align.LEFT);
        //画左边的六个数
        for (int i = 0; i < lineDataList.size(); i++) {
            Rect dataRect = new Rect();
            String dataStr = lineDataList.get(i) + "";
            mPaint.getTextBounds(dataStr, 0, dataStr.length(), dataRect);
            canvas.drawText(
                    dataStr,
                    0,
                    lineAllHeight / 6 * (i + 1) - 5,
                    mPaint
            );
        }

    }

    private void drawDate(Canvas canvas) {
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(textDateSize);
        mPaint.setColor(textDateColor);
        mPaint.setStyle(Paint.Style.FILL);

        //画开始时间和结束时间
        Rect startDateRect = new Rect();
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.getTextBounds(startDateStr, 0, startDateStr.length(), startDateRect);
        canvas.drawText(
                startDateStr,
                0,
                getHeight() - 10,
                mPaint
        );
        endDateRect = new Rect();
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mPaint.getTextBounds(endDateStr, 0, endDateStr.length(), endDateRect);
        canvas.drawText(
                endDateStr,
                getWidth(),
                getHeight() - 10,
                mPaint
        );
    }

    public void setDataView(List<String> dateList, List<Float> actuaList, String unit) {
        startDateStr = dateList.get(0);
        endDateStr = dateList.get(dateList.size() - 1);

        this.dateList.clear();
        this.dateList.addAll(dateList);
        this.unit = unit;

        max = Collections.max(actuaList);
        min = Collections.min(actuaList);

        if (max == min) max += max;

        lineDataList.clear();
        lineDataList.add(max);
        DecimalFormat fnum = new DecimalFormat("##0.0");
        for (int i = 1; i <= 5; i++) {
            lineDataList.add(Float.valueOf(fnum.format(max - (max - min) / 5 * i)));
        }

        lineBrokenDataList.clear();
        for (Float data : actuaList)
            lineBrokenDataList.add(data);

        invalidate();
    }

}


package com.example.myutils.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.myutils.R;
import com.example.myutils.bean.MineChartsBean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zheng on 2018/3/28 0027.
 * 我的模块 贝塞尔区线数据图
 */
public class VFXBesselChart extends View {

    // 主画笔
    private Paint mPaint;
    // 标题矩形
    private Rect titleRect;
    // 数据区域矩形
    private Rect dataRect;

    private String titleName = "";//VFX
    // 根据index标识是什么数据  每日净值/浮动收益/每日交易量
    private int sortIndex = 1;
    private Float max;
    private Float min;
    // x数据线下标的标识日期
    private List<String> dateList = new ArrayList<>();
    // y轴左边刻度值
    private List<String> yScaleStrList = new ArrayList<>();
    // 真正的数据
    private List<Float> pointDataList = new ArrayList<>();
    // 沉默真实用户时使用
    private List<Float> silentPointList = new ArrayList<>();

    // 定时器
    private CountDownTimer timer;
    // 手势点击记录周期
    private int weekPosition = -1;
    /**
     * 曲线上数据点
     */
    private List<Point> pointList;
    private List<Point> pointSilentList;

    // 左上角标题文本颜色 数据区域颜色
    private int textTitleColor,dataRangeColor;
    // x轴底部文本颜色
    private int textXColor;
    // x轴刻度颜色
    private int scaleXColor;
    // y轴左边文本颜色
    private int textYColor;
    // 贝塞尔数据线颜色
    private int lineBesselColor;
    // 手势点击后数据标注线颜色
    private int lineClickColor;
    // 虚线颜色
    private int lineImaginaryColor;
    // x轴刻度线宽度
    private int scaleXWidth;
    // 虚线宽度
    private float lineImaginaryWidth;
    // 贝塞尔数据线宽度
    private int lineBesselWidth;
    // 手势点击后数据标注线宽度
    private float lineClickWidth;
    // 数据区域距离顶部距离
    private float dataMarginTop;
    // y轴左边文本距离(以前根据文本宽度自动变化，现在根据需求改成固定宽度)
    private int yScaleDataWidth;
    // x轴下边文本距离底部的高度
    private int xScaleDataHeight;
    // 数据区域距离右边的距离
    private int dataMarginRightWidth;
    // 数据区域内部padding距离
    private int dataPaddingLeft;
    private int dataPaddingRight;
    private int dataPaddingTop;
    private int dataPaddingBottom;

    private OnChartClickListener onChartClickListener;

    public interface OnChartClickListener {
        void onItemClick(String str, String date);
    }

    public void setOnItemClickListener(OnChartClickListener onChartClickListener) {
        this.onChartClickListener = onChartClickListener;
    }

    public VFXBesselChart(Context context) {
        this(context, null);
    }

    public VFXBesselChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VFXBesselChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlAttrs(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
    }

    private void initXmlAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VFXBesselChart);
        if (typedArray == null) return;
        dataRangeColor = typedArray.getColor(R.styleable.VFXBesselChart_data_range_color, getResources().getColor(R.color.gray_f8f8f8));
        textTitleColor = typedArray.getColor(R.styleable.VFXBesselChart_text_title_color, getResources().getColor(R.color.black));
        textXColor = typedArray.getColor(R.styleable.VFXBesselChart_text_x_color, getResources().getColor(R.color.black));
        scaleXColor = typedArray.getColor(R.styleable.VFXBesselChart_scale_x_color, getResources().getColor(R.color.gray_d5d5d5));
        textYColor = typedArray.getColor(R.styleable.VFXBesselChart_text_y_color, getResources().getColor(R.color.black));
        lineBesselColor = typedArray.getColor(R.styleable.VFXBesselChart_line_bessel_color, getResources().getColor(R.color.gold_c69d5e));
        lineClickColor = typedArray.getColor(R.styleable.VFXBesselChart_line_click_color, getResources().getColor(R.color.black_a7262601));
        lineImaginaryColor = typedArray.getColor(R.styleable.VFXBesselChart_line_imaginary_color, getResources().getColor(R.color.gray_d5d5d5));
        scaleXWidth = typedArray.getInteger(R.styleable.VFXBesselChart_scale_x_width, 2);
        lineImaginaryWidth = typedArray.getDimension(R.styleable.VFXBesselChart_line_imaginary_width, 2);
        lineBesselWidth = typedArray.getInteger(R.styleable.VFXBesselChart_line_bessel_widht, 3);
        lineClickWidth = typedArray.getDimension(R.styleable.VFXBesselChart_line_click_widht, 2);
        dataMarginTop = typedArray.getDimension(R.styleable.VFXBesselChart_data_margin_top, 120);
        yScaleDataWidth = typedArray.getInteger(R.styleable.VFXBesselChart_width_y_scale_data, 120);
        xScaleDataHeight = typedArray.getInteger(R.styleable.VFXBesselChart_height_x_scale_data, 60);
        dataMarginRightWidth = typedArray.getInteger(R.styleable.VFXBesselChart_width_data_margin_right, 50);
        dataPaddingLeft = typedArray.getInteger(R.styleable.VFXBesselChart_padding_data_left, 10);
        dataPaddingRight = typedArray.getInteger(R.styleable.VFXBesselChart_padding_data_right, 10);
        dataPaddingTop = typedArray.getInteger(R.styleable.VFXBesselChart_padding_data_top, 10);
        dataPaddingBottom = typedArray.getInteger(R.styleable.VFXBesselChart_padding_data_bottom, 20);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制左上角标题  浮动收益
        drawTitle(canvas);
        // 数据区域  (灰色矩形)
        drawDataRect(canvas);
        // y轴左边数据 ($10000.00)
        drawYStrs(canvas);
        // x轴下面的数据  (3.20)
        drawXStrs(canvas);
        // 绘制五条虚线
        drawImaginary(canvas);
        // 绘制x轴的刻度
        drawXScales(canvas);
        // 绘制主要数据
        drawMainDatas(canvas);
        // 绘制点击事件的显示数据
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
                int weekScale = pointList.get(1).x - pointList.get(0).x;
                for (int i = 0; i < pointList.size(); i++) {
                    if (Math.abs(event.getX() - pointList.get(i).x) < weekScale / 2) {
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

    // 绘制点击事件的显示数据
    private void drawClickData(Canvas canvas) {

        // 默认就是-1，当点击的时候赋值
        if (weekPosition == -1) return;

        Paint weekPaint = new Paint();
        weekPaint.setAntiAlias(true);
        weekPaint.setStrokeWidth(lineClickWidth);
        weekPaint.setTextSize(30);
        weekPaint.setColor(lineClickColor);
        // 绘制竖线
        canvas.drawLine(
                yScaleDataWidth + dataPaddingLeft + (dataRect.width() - dataPaddingLeft - dataPaddingRight) / 6 * weekPosition,
                dataPaddingTop + dataMarginTop,
                yScaleDataWidth + dataPaddingLeft + (dataRect.width() - dataPaddingLeft - dataPaddingRight) / 6 * weekPosition,
                getHeight() - dataPaddingBottom - xScaleDataHeight,
                weekPaint
        );

        String fnumStr = "##0.00";
        // 成交量 用户量 不能有小数
        if (sortIndex == 3 || sortIndex == 4) fnumStr = "##0";
        DecimalFormat fnum = new DecimalFormat(fnumStr);

        // 绘制矩形数据
        Point weekPoint = pointList.get(weekPosition);
        String weekDataStr = "$" + fnum.format(pointDataList.get(weekPosition));
        if (sortIndex == 3) {
            weekDataStr = fnum.format(pointDataList.get(weekPosition)) + "笔";
        } else if (sortIndex == 4) {
            weekDataStr = "活跃:" + fnum.format(pointDataList.get(weekPosition)) + "沉默:" + fnum.format(silentPointList.get(weekPosition));
        }

        if (onChartClickListener != null)
            onChartClickListener.onItemClick(weekDataStr, dateList.get(weekPosition));

        Rect weekRect = new Rect();
        weekPaint.getTextBounds(weekDataStr, 0, weekDataStr.length(), weekRect);

        weekPaint.setColor(Color.parseColor("#aac69d5e"));
        weekPaint.setTextAlign(Paint.Align.CENTER);

        RectF weekBgRect = new RectF(
                weekPosition < 5 ? (weekPoint.x + 5) : (weekPoint.x - weekRect.width() - 30),
                weekPoint.y - weekRect.height() - 15,
                weekPosition < 5 ? (weekPoint.x + weekRect.width() + 30) : (weekPoint.x - 5),
                weekPoint.y - 5
        );

        canvas.drawRoundRect(weekBgRect, 5, 5, weekPaint);
        weekPaint.setColor(Color.WHITE);

        Paint.FontMetricsInt fontMetrics = weekPaint.getFontMetricsInt();
        int baseline = (int) ((weekBgRect.bottom + weekBgRect.top - fontMetrics.bottom - fontMetrics.top) / 2);

        canvas.drawText(
                weekDataStr,
                weekBgRect.centerX(),
                baseline,
                weekPaint
        );

    }

    // 绘制主要数据
    private void drawMainDatas(Canvas canvas) {

        Paint dataPaint = new Paint();
        dataPaint.setAntiAlias(true);
        dataPaint.setStyle(Paint.Style.STROKE);
        dataPaint.setStrokeWidth(lineBesselWidth);
//        Path path = new Path();
//        path.reset();
        int allDataWidth = dataRect.width() - dataPaddingLeft - dataPaddingRight;
        int allDataHeight = dataRect.height() - dataPaddingTop - dataPaddingBottom;
        pointList = new ArrayList<>();
        pointSilentList = new ArrayList<>();

        // 每日沉默客户和活跃客户会有两条线
        if (sortIndex == 4) {
            for (int i = 0; i < silentPointList.size(); i++) {
                Point point = new Point();
                point.set(
                        yScaleDataWidth + dataPaddingLeft + allDataWidth / 6 * i,
                        (int) (getHeight() - xScaleDataHeight - dataPaddingBottom - (allDataHeight / (max - min) * silentPointList.get(i)))
                );
                pointSilentList.add(point);
            }
            dataPaint.setColor(getResources().getColor(R.color.gray_7a7a7a));
            drawScrollLine(canvas, dataPaint, pointSilentList);
        }

        for (int i = 0; i < pointDataList.size(); i++) {
            Point point = new Point();
            // y坐标pointDataList.get(i)必须-min  因为有可能min是负数，如果不减出现负数不正确
            point.set(
                    yScaleDataWidth + dataPaddingLeft + allDataWidth / 6 * i,
                    (int) (
                            getHeight() - xScaleDataHeight - dataPaddingBottom - (allDataHeight / (max - min) * (pointDataList.get(i) - min))
                    )
            );
            pointList.add(point);
        }
        dataPaint.setColor(lineBesselColor);
        drawScrollLine(canvas, dataPaint, pointList);

    }

    // 绘制x轴的刻度
    private void drawXScales(Canvas canvas) {
        Paint xScalesPaint = new Paint();
        xScalesPaint.setAntiAlias(true);
        xScalesPaint.setStyle(Paint.Style.STROKE);
        xScalesPaint.setColor(scaleXColor);
        xScalesPaint.setStrokeWidth(scaleXWidth);

        int allXScalesWidth = getWidth() - yScaleDataWidth - dataPaddingLeft - dataPaddingRight - dataMarginRightWidth;

        for (int i = 0; i < 7; i++) {
            canvas.drawLine(
                    yScaleDataWidth + dataPaddingLeft + allXScalesWidth / 6 * i, getHeight() - xScaleDataHeight,
                    yScaleDataWidth + dataPaddingLeft + allXScalesWidth / 6 * i, getHeight() - xScaleDataHeight - dataPaddingBottom + 10,
                    xScalesPaint
            );
        }

    }

    // 绘制五条虚线
    private void drawImaginary(Canvas canvas) {

        Paint imaginaryPaint = new Paint();
        imaginaryPaint.setStyle(Paint.Style.FILL);
        imaginaryPaint.setColor(lineImaginaryColor);
        imaginaryPaint.setStrokeWidth(lineImaginaryWidth);
        // 绘制长度为4的实线后再绘制长度为4的空白区域，0位间隔
        DashPathEffect effect = new DashPathEffect(new float[]{4, 4}, 0);
        imaginaryPaint.setPathEffect(effect);
        imaginaryPaint.setAntiAlias(true);

        int allImaginaryHeight = dataRect.height() - dataPaddingTop - dataPaddingBottom;

        for (int i = 0; i < 5; i++) {
            // 两点一线
            canvas.drawLine(
                    yScaleDataWidth + dataPaddingLeft, dataMarginTop + dataPaddingTop + allImaginaryHeight / 4 * i,
                    getWidth() - dataMarginRightWidth - dataPaddingRight, dataMarginTop + dataPaddingTop + allImaginaryHeight / 4 * i,
                    imaginaryPaint
            );
        }

    }

    // x轴下面的数据  (04-20)
    private void drawXStrs(Canvas canvas) {
        mPaint.setTextSize(25);
        mPaint.setColor(textXColor);
        for (int i = 0; i < dateList.size(); i++) {
            if (i == 0) {
                mPaint.setTextAlign(Paint.Align.LEFT);
            } else if (i == 6) {
                mPaint.setTextAlign(Paint.Align.RIGHT);
            } else {
                mPaint.setTextAlign(Paint.Align.CENTER);
            }
            Rect xDataRect = new Rect();
            mPaint.getTextBounds(dateList.get(i), 0, dateList.get(i).length(), xDataRect);
            canvas.drawText(
                    dateList.get(i),
                    yScaleDataWidth + 10 + ((dataRect.width() - 20) / 6) * i,
                    dataMarginTop + dataRect.height() + (int) (xDataRect.height() * 1.5),
                    mPaint
            );
        }
    }

    // y轴左边数据 ($10000.00)
    private void drawYStrs(Canvas canvas) {
        mPaint.setTextSize(23);
        mPaint.setColor(textYColor);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i < yScaleStrList.size(); i++) {
            canvas.drawText(
                    yScaleStrList.get(i) + "",
                    yScaleDataWidth - 12,
                    (dataRect.height() - 30) / 4 * i + 20 + dataMarginTop,
                    mPaint
            );
        }
    }

    // 数据区域  (灰色矩形)
    private void drawDataRect(Canvas canvas) {
        mPaint.setColor(dataRangeColor);
        dataRect = new Rect(
                yScaleDataWidth, (int) dataMarginTop,
                getWidth() - dataMarginRightWidth, getHeight() - xScaleDataHeight
        );
        canvas.drawRect(
                dataRect,
                mPaint
        );
    }

    // 绘制左上角标题  浮动收益
    private void drawTitle(Canvas canvas) {
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(textTitleColor);
        mPaint.setTextSize(38);
        titleRect = new Rect();
        mPaint.getTextBounds(titleName, 0, titleName.length(), titleRect);
        canvas.drawText(
                titleName,
                50,
                titleRect.height() + 40,
                mPaint
        );
    }

    // 绘制曲线
    private void drawScrollLine(Canvas canvas, Paint dataPaint, List<Point> dataPointList) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < dataPointList.size() - 1; i++) {
            startp = dataPointList.get(i);
            endp = dataPointList.get(i + 1);
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, dataPaint);
        }
    }

    private void startCountDownTime(long time) {

        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每隔countDownInterval秒会回调一次onTick()方法
            }

            @Override
            public void onFinish() {
                // 倒计时结束
                weekPosition = -1;
                invalidate();
            }
        };
        timer.start();// 开始计时
        //timer.cancel(); // 取消
    }

    public void setData(MineChartsBean chartBean) {

        titleName = chartBean.getTitle();
        sortIndex = chartBean.getSortIndex();

        List<MineChartsBean.DataBean> chartDataList = chartBean.getDatas();
        dateList.clear();
        pointDataList.clear();
        silentPointList.clear();

        for (int x = 0; x < chartDataList.size(); x++) {
            dateList.add(chartDataList.get(x).getDateStr());
            pointDataList.add((float) chartDataList.get(x).getPoint());
            // 沉默活跃用户需要两组数据
            if (sortIndex == 4) silentPointList.add((float) chartDataList.get(x).getPoint1());
        }

        // 初始化最大值最小值
        if (chartDataList.size() > 0) {
            if (sortIndex != 4)
                max = Collections.max(pointDataList);
            else
                max = Collections.max(pointDataList) > Collections.max(silentPointList) ? Collections.max(pointDataList) : Collections.max(silentPointList);

            min = Collections.min(pointDataList);
        }


        if (max == null) max = 0f;
        if (min == null) min = 0f;

        // 如果最大值最小值都等于0 需要设置最大值+10000或者+100   如果不等于0但是最大值=最小值 将最大值+10
        if (Float.toString(max).equals(Float.toString(min))) {
            if (max == 0) {
                switch (sortIndex) {
                    case 1://每日净值
                        max = 10000.00f;
                        break;
                    case 2://浮动收益
                        max = 10000.00f;
                        break;
                    case 3://每日交易量
                        max = 100f;
                        break;
                    case 4://沉默-活跃用户
                        max = 20f;
                        break;
                    case 5://每日佣金
                        max = 10000.00f;
                        break;
                }
            } else {
                max += 10;
            }
        } else {
            //每日交易量/沉默活跃用户 必须为4的倍数
            if (sortIndex == 3 || sortIndex == 4) {
                min = 0f;
                max = (float) Math.ceil(max / 4) * 4;
            }
        }

        yScaleStrList.clear();

        String fnumStr = "##0.00";
        //成交量 用户量 不能有小数
        if (sortIndex == 3 || sortIndex == 4) fnumStr = "##0";

        yScaleStrList.add(
                sortIndex == 1 || sortIndex == 2 || sortIndex == 5 ?
                        "$" + initYScaleValue(fnumStr, max) :
                        initYScaleValue(fnumStr, max)
        );

        NumberFormat nbf = NumberFormat.getInstance();
        nbf.setMinimumFractionDigits(2);
        for (int i = 1; i <= 4; i++) {
            yScaleStrList.add(
                    sortIndex == 1 || sortIndex == 2 || sortIndex == 5 ?
                            "$" + initYScaleValue(fnumStr, max - (max - min) / 4 * i) :
                            initYScaleValue(fnumStr, max - (max - min) / 4 * i));
        }

//        yScaleDataWidth = String.valueOf((max + "").length() > (min + "").length() ? max : min).length() * 20;

        invalidate();
    }

    // 初始化Y轴刻度值，大于小于1000以 *K 表示
    private String initYScaleValue(String fnumStr, Float scaleValue) {
        DecimalFormat fnum = new DecimalFormat(fnumStr);
        if (scaleValue > 1000 || scaleValue < -1000) {
            return fnum.format(scaleValue / 1000) + "k";
        }
        return fnum.format(scaleValue);
    }

}

package com.example.myutils.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.myutils.R;
import com.example.myutils.utils.DataUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zheng on 2018/8/14.
 * 订单买卖价格 折线图
 */

public class VFXOrderChart extends View {

    // 买卖颜色，虚线颜色
    private int bidColor, askColor, virtualLineColor;
    private float dataPaddingTop, dataPaddingLeft, dataPaddingRight, dataPaddingBottom;

    private float yScaleDataWidth;
    private int textSize;

    // 数据区域显示30个数据点
    private int showPointNums;

    // y轴右边刻度值
    private List<String> yScaleStrList = new ArrayList<>();
    // 买卖价格集合
    private List<Float> bidList = new ArrayList<>();
    private List<Float> askList = new ArrayList<>();

    // 记录上一次买卖价格，默认为0
    private float lastAsk = 0f;
    private float lastBid = 0f;

    private float maxPrice = 100;
    private float minPrice = 0;
    private int curPriceTxtColor;

    private Paint mPaint;
    private Paint imaginaryPaint;
    private Paint baPaint;
    private Paint txtPaint;
    // 小数位，默认为2
    private int digits = 2;
    private int scaleTextColor;

    public VFXOrderChart(Context context) {
        this(context, null);
    }

    public VFXOrderChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VFXOrderChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlAttrs(context, attrs);
        initPaint();
    }

    private void initXmlAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VFXOrderChart);
        if (typedArray == null) return;
        bidColor = typedArray.getColor(R.styleable.VFXOrderChart_bid_color, getResources().getColor(R.color.red_a8171a));
        askColor = typedArray.getColor(R.styleable.VFXOrderChart_ask_color, getResources().getColor(R.color.blue_4a90e2));
        virtualLineColor = typedArray.getColor(R.styleable.VFXOrderChart_line_virtual_color, getResources().getColor(R.color.black));
        curPriceTxtColor = typedArray.getColor(R.styleable.VFXOrderChart_text_current_price_color, getResources().getColor(R.color.white));
        scaleTextColor = typedArray.getColor(R.styleable.VFXOrderChart_text_scale_color, getResources().getColor(R.color.black));
        showPointNums = typedArray.getInt(R.styleable.VFXOrderChart_price_point_nums, 35);
        yScaleDataWidth = typedArray.getDimension(R.styleable.VFXOrderChart_scale_width_y, 150);
        dataPaddingTop = typedArray.getDimension(R.styleable.VFXOrderChart_oc_padding_top, 50);
        dataPaddingLeft = typedArray.getDimension(R.styleable.VFXOrderChart_oc_padding_left, 30);
        dataPaddingRight = typedArray.getDimension(R.styleable.VFXOrderChart_oc_padding_right, 30);
        dataPaddingBottom = typedArray.getDimension(R.styleable.VFXOrderChart_oc_padding_bottom, 50);
        textSize = typedArray.getInteger(R.styleable.VFXOrderChart_oc_text_size, 35);
        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);

        // 虚线画笔
        imaginaryPaint = new Paint();
        imaginaryPaint.setStyle(Paint.Style.FILL);
        imaginaryPaint.setColor(virtualLineColor);
        imaginaryPaint.setStrokeWidth(1);
        // 绘制长度为4的实线后再绘制长度为4的空白区域，0位间隔
        DashPathEffect effect = new DashPathEffect(new float[]{6, 6}, 0);
        imaginaryPaint.setPathEffect(effect);
        imaginaryPaint.setAntiAlias(true);

        // 买卖画笔
        baPaint = new Paint();
        baPaint.setAntiAlias(true);
        baPaint.setStrokeWidth(3);
        baPaint.setTextSize(textSize);
        baPaint.setStyle(Paint.Style.STROKE);
        baPaint.setTextAlign(Paint.Align.CENTER);

        // 现价文本画笔
        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(textSize);
        txtPaint.setColor(curPriceTxtColor);
        txtPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 虚线
        initVirtualLine(canvas);
        // Y刻度
        drawYStrs(canvas);
        // 买卖
        drawBidAdk(canvas);
    }

    private void drawBidAdk(Canvas canvas) {

        if (bidList.size() == 0 || askList.size() == 0) return;

        float dataHeight = getHeight() - dataPaddingTop - dataPaddingBottom;
        float dataWidth = getWidth() - dataPaddingLeft - dataPaddingRight - yScaleDataWidth;

        // bid
        baPaint.setColor(bidColor);
        baPaint.setStyle(Paint.Style.STROKE);
        Path bidPath = new Path();
        bidPath.moveTo(
                dataPaddingLeft,
                getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (bidList.get(0) - minPrice))
        );
        for (int x = 1; x < bidList.size() & x < showPointNums; x++) {
            bidPath.lineTo(
                    dataPaddingLeft + dataWidth / showPointNums * x,
                    getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (bidList.get(x) - minPrice))
            );
        }
        if (bidList.size() <= showPointNums)
            bidPath.lineTo(
                    dataPaddingLeft + dataWidth,
                    getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (bidList.get(bidList.size() - 1) - minPrice))

            );
        canvas.drawPath(bidPath, baPaint);
        // 绘制圆角矩形
        String endBidStr = String.valueOf(bidList.get(bidList.size() - 1));
        Rect endBidRect = new Rect();
        baPaint.getTextBounds(endBidStr, 0, endBidStr.length(), endBidRect);
        baPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(
                new RectF(
                        getWidth() - dataPaddingRight - yScaleDataWidth,
                        getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (bidList.get(bidList.size() - 1) - minPrice)) - endBidRect.height(),
                        getWidth() - dataPaddingRight,
                        getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (bidList.get(bidList.size() - 1) - minPrice)) + endBidRect.height()
                ),
                10,
                10,
                baPaint
        );
        // 绘制圆角矩形内文本
        canvas.drawText(
                endBidStr,
                getWidth() - dataPaddingRight - yScaleDataWidth / 2,
                getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (bidList.get(bidList.size() - 1) - minPrice)) + endBidRect.height() / 2,
                txtPaint
        );

        // ask
        baPaint.setColor(askColor);
        baPaint.setStyle(Paint.Style.STROKE);

        Path askPath = new Path();
        askPath.moveTo(
                dataPaddingLeft,
                getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (askList.get(0) - minPrice))
        );
        for (int x = 1; x < askList.size() & x < showPointNums; x++) {
            askPath.lineTo(
                    dataPaddingLeft + dataWidth / showPointNums * x,
                    getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (askList.get(x) - minPrice))
            );
        }
        if (askList.size() <= showPointNums)
            askPath.lineTo(
                    dataPaddingLeft + dataWidth,
                    getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (askList.get(askList.size() - 1) - minPrice))

            );
        canvas.drawPath(askPath, baPaint);
        // 绘制圆角矩形
        String endAskStr = String.valueOf(askList.get(askList.size() - 1));
        Rect endAskRect = new Rect();
        baPaint.getTextBounds(endAskStr, 0, endAskStr.length(), endAskRect);
        baPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(
                new RectF(
                        getWidth() - dataPaddingRight - yScaleDataWidth,
                        getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (askList.get(askList.size() - 1) - minPrice)) - endAskRect.height(),
                        getWidth() - dataPaddingRight,
                        getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (askList.get(askList.size() - 1) - minPrice)) + endAskRect.height()
                ),
                10,
                10,
                baPaint
        );
        // 绘制圆角矩形内文本
        canvas.drawText(
                endAskStr,
                getWidth() - dataPaddingRight - yScaleDataWidth / 2,
                getHeight() - dataPaddingBottom - (dataHeight / (maxPrice - minPrice) * (askList.get(askList.size() - 1) - minPrice)) + endAskRect.height() / 2,
                txtPaint
        );

    }

    // 刻度值
    private void drawYStrs(Canvas canvas) {
        mPaint.setTextSize(textSize);
        mPaint.setColor(scaleTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        //(dataRect.height() - 30) / 4 * i + 20 + dataMarginTop,
        for (int i = 0; i < yScaleStrList.size(); i++) {
            String str = yScaleStrList.get(i);
            Rect rect = new Rect();
            mPaint.getTextBounds(str, 0, str.length(), rect);
            canvas.drawText(
                    str,
                    getWidth() - dataPaddingRight - yScaleDataWidth / 2,
                    (getHeight() - dataPaddingTop - dataPaddingBottom) / 5 * i + dataPaddingTop + rect.height() / 2,
                    mPaint
            );
        }
    }

    // 虚线
    private void initVirtualLine(Canvas canvas) {

        int allImaginaryHeight = (int) (getHeight() - dataPaddingTop - dataPaddingBottom);

        for (int i = 0; i < 6; i++) {
            // 两点一线
            canvas.drawLine(
                    dataPaddingLeft, dataPaddingTop + allImaginaryHeight / 5 * i,
                    getWidth() - yScaleDataWidth - dataPaddingRight,
                    dataPaddingTop + allImaginaryHeight / 5 * i,
                    imaginaryPaint
            );
        }
    }

    public void clearData() {
        lastAsk = 0f;
        lastBid = 0f;
        bidList.clear();
        askList.clear();
        digits = 2;
    }

    public void setDigits(int outDigits) {
        digits = outDigits;
    }

    public void setData(float bidPrice, float askPrice) {

        // if (bidPrice == lastBid && askPrice == lastAsk) return;

        bidList.add(bidPrice);
        askList.add(askPrice);
        lastBid = bidPrice;
        lastAsk = askPrice;
        if (bidList.size() > showPointNums)
            bidList.remove(0);
        if (askList.size() > showPointNums)
            askList.remove(0);
        Float bidMax = Collections.max(bidList);
        Float askMax = Collections.max(askList);
        Float bidMin = Collections.min(bidList);
        Float askMin = Collections.min(askList);
        maxPrice = bidMax > askMax ? bidMax : askMax;
        minPrice = bidMin < askMin ? bidMin : askMin;

        yScaleStrList.clear();
        yScaleStrList.add(String.valueOf(minPrice));
        float scalePrice = (maxPrice - minPrice) / 5;
        for (int x = 1; x < 5; x++) {
            yScaleStrList.add(DataUtil.format(minPrice + scalePrice * x, digits, false));
        }
        yScaleStrList.add(String.valueOf(maxPrice));
        Collections.reverse(yScaleStrList);

        invalidate();
    }

}

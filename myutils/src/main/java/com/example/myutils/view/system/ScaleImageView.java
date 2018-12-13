package com.example.myutils.view.system;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.myutils.R;
import com.example.myutils.utils.DataUtil;

/**
 * Created by zheng on 2018/12/6 0006.
 * <p>
 * 可设置宽高比例的ImageView
 * app:siv_scale_mode="fixed_width"  设置比例模式：以宽为主/以高为主 （默认以宽为主）
 * app:siv_scale="1:1"  设置宽高比例
 */
public class ScaleImageView extends android.support.v7.widget.AppCompatImageView {

    // 0:以宽为主  其他以高为主
    private int scaleMode;
    // 宽高比例 以：隔开
    private String whScale = "1:1";

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlAttrs(context, attrs);
    }

    private void initXmlAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);

        if (typedArray == null) return;

        scaleMode = typedArray.getInt(R.styleable.ScaleImageView_siv_scale_mode, 0);
        whScale = typedArray.getString(R.styleable.ScaleImageView_siv_scale);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int scaleWidht = 1;
        int scaleHeight = 1;

        if (!TextUtils.isEmpty(whScale) && whScale.contains(":")) {
            String[] tempArray = whScale.split(":");
            scaleWidht = DataUtil.parseString2Int(tempArray[0]);
            scaleHeight = DataUtil.parseString2Int(tempArray[1]);
        }

        if (scaleWidht == 0) scaleWidht = 1;
        if (scaleHeight == 0) scaleHeight = 1;

        int widthSize, heightSize;

        if (scaleMode == 0) {
            widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightSize = widthSize / scaleWidht * scaleHeight;
        } else {
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
            widthSize = heightSize / scaleHeight * scaleWidht;
        }

        setMeasuredDimension(widthSize, heightSize);

    }

    public void setScale(String scaleStr) {
        whScale = scaleStr;
        requestLayout();
    }

}

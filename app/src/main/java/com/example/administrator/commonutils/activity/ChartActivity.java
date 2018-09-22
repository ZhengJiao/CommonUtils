package com.example.administrator.commonutils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.commonutils.R;
import com.example.administrator.commonutils.model.ChartModel;

/**
 * Created by zheng on 2018/9/22 0022.
 * 图表
 */

public class ChartActivity extends Activity implements View.OnClickListener {

    private TextView tv_vfx_bessel,tv_vfx_line,tv_vfx_order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initParam();
        initView();
        initListener();

    }

    private void initParam() {
        new ChartModel(this);
    }

    private void initView() {
        tv_vfx_bessel=findViewById(R.id.tv_vfx_bessel);
        tv_vfx_line=findViewById(R.id.tv_vfx_line);
        tv_vfx_order=findViewById(R.id.tv_vfx_order);
    }

    private void initListener() {
        tv_vfx_bessel.setOnClickListener(this);
        tv_vfx_line.setOnClickListener(this);
        tv_vfx_order.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_vfx_bessel: // 贝塞尔区线图
                Intent besselIntent = new Intent(ChartActivity.this, ShowActivity.class);
                besselIntent.putExtra("type",1);
                startActivity(besselIntent);
                break;
            case R.id.tv_vfx_line: // 财经日历折线图
                Intent lineIntent = new Intent(ChartActivity.this, ShowActivity.class);
                lineIntent.putExtra("type",2);
                startActivity(lineIntent);
                break;
            case R.id.tv_vfx_order: // 订单图表
                Intent orderIntent = new Intent(ChartActivity.this, ShowActivity.class);
                orderIntent.putExtra("type",3);
                startActivity(orderIntent);
                break;
        }
    }
}

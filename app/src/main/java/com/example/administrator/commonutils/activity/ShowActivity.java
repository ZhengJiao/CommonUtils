package com.example.administrator.commonutils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.administrator.commonutils.R;
import com.example.administrator.commonutils.model.ShowModel;
import com.example.myutils.bean.MineChartsBean;
import com.example.myutils.view.custom.VFXBesselChart;
import com.example.myutils.view.custom.VFXLineChart;
import com.example.myutils.view.custom.VFXOrderChart;

/**
 * Created by zheng on 2018/9/22 0022.
 * 展示
 */
public class ShowActivity extends Activity {

    private int type;

    private VFXBesselChart vfxBesselChart;
    private VFXLineChart vfxLineChart;
    private VFXOrderChart vfxOrderChart;
    private ShowModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        initParam();
        initView();
        initData();

    }

    private void initView() {
        vfxBesselChart = findViewById(R.id.bessel_chart);
        vfxLineChart = findViewById(R.id.line_chart);
        vfxOrderChart = findViewById(R.id.order_chart);

    }

    private void initParam() {
        model = new ShowModel(this);
        type = getIntent().getIntExtra("type", 1);
    }

    private void initData() {
        switch (type) {
            case 1:
                vfxBesselChart.setVisibility(View.VISIBLE);
                vfxBesselChart.setData(model.getChartsBean());
                break;
            case 2:
                vfxLineChart.setVisibility(View.VISIBLE);
                vfxLineChart.setDataView(model.getDateList(), model.getActuaList(), "%");
                break;
            case 3:
                vfxOrderChart.setVisibility(View.VISIBLE);
                vfxOrderChart.setData(10f,50f);
                break;
        }
    }

}

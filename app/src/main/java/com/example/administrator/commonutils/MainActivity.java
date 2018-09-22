package com.example.administrator.commonutils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.commonutils.activity.ChartActivity;
import com.example.administrator.commonutils.activity.DialogActivity;
import com.example.administrator.commonutils.activity.UtilsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDialog, tvChart, tvUtils;
    private MainModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParam();
        initView();
        initListener();

    }

    private void initParam() {
        model = new MainModel(this);
    }

    private void initView() {
        tvDialog = findViewById(R.id.tv_dialog);
        tvChart = findViewById(R.id.tv_chart);
        tvUtils = findViewById(R.id.tv_utils);
    }

    private void initListener() {
        tvDialog.setOnClickListener(this);
        tvChart.setOnClickListener(this);
        tvUtils.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog: // 弹窗
                startActivity(new Intent(MainActivity.this, DialogActivity.class));
                break;
            case R.id.tv_chart: // 图表
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
                break;
            case R.id.tv_utils: // 工具类
                startActivity(new Intent(MainActivity.this, UtilsActivity.class));
                break;
        }
    }
}

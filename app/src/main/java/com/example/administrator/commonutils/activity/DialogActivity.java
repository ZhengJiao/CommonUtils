package com.example.administrator.commonutils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.commonutils.R;
import com.example.administrator.commonutils.model.DialogModel;

/**
 * Created by zheng on 2018/9/22 0022.
 * 弹窗
 */

public class DialogActivity extends Activity implements View.OnClickListener {

    private TextView tvSwitchAccountDialog;
    private DialogModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        initParam();
        initView();
        initListener();

    }

    private void initParam() {
        model = new DialogModel(this);
    }

    private void initView() {
        tvSwitchAccountDialog=findViewById(R.id.tv_switch_account_dialog);
    }

    private void initListener() {
        tvSwitchAccountDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_switch_account_dialog: // 切换账户的dialog
                model.showSwitchAccountDialog();
                break;
        }
    }

}

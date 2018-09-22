package com.example.myutils.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myutils.R;

/**
 * Created by zheng on 2018/9/22 0022.
 * 切换账户dialog
 */

public class SwitchAccountDialog extends Dialog {

    private TextView tvCancel, tvConfirm;

    private ButtonListener buttonListener;

    public SwitchAccountDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vfx_switch_account);

        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8);
        getWindow().setAttributes(lp);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initView();
        initListener();

    }

    private void initView() {
        tvConfirm = findViewById(R.id.tv_confirm);
        tvCancel = findViewById(R.id.tv_cancel);
    }

    private void initListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener!=null){
                    buttonListener.onCancelButtonListener();
                }
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public SwitchAccountDialog setButtonListener(ButtonListener listener) {
        this.buttonListener = listener;
        return this;
    }

    public interface ButtonListener {
        void onCancelButtonListener();
    }

}

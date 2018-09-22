package com.example.administrator.commonutils.model;

import com.example.administrator.commonutils.activity.DialogActivity;
import com.example.myutils.view.SwitchAccountDialog;

/**
 * Created by zheng on 2018/9/22 0022.
 */

public class DialogModel {

    private final DialogActivity activity;

    public DialogModel(DialogActivity activity) {
        this.activity=activity;
    }

    public void showSwitchAccountDialog() {
        SwitchAccountDialog dialog = new SwitchAccountDialog(activity);
        dialog.show();
    }

}

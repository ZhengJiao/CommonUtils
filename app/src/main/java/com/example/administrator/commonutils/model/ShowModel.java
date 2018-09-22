package com.example.administrator.commonutils.model;

import com.example.administrator.commonutils.activity.ShowActivity;
import com.example.myutils.bean.MineChartsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zheng on 2018/9/22 0022.
 */

public class ShowModel {

    private final ShowActivity activity;

    public ShowModel(ShowActivity activity) {
        this.activity=activity;
    }

    public MineChartsBean getChartsBean() {
        MineChartsBean chartsBean = new MineChartsBean();
        chartsBean.setTitle("每日净值");
        chartsBean.setSortIndex(1);
        chartsBean.setMaxIndex(1);
        chartsBean.setQueryFrom("09-15");
        chartsBean.setQueryTo("09-21");
        ArrayList<MineChartsBean.DataBean> dataBeanList = new ArrayList<>();
        MineChartsBean.DataBean db1 = new MineChartsBean.DataBean();
        db1.setDateStr("09-15");
        db1.setPoint(174417.78);
        dataBeanList.add(db1);
        MineChartsBean.DataBean db2 = new MineChartsBean.DataBean();
        db2.setDateStr("09-16");
        db2.setPoint(174417.78);
        dataBeanList.add(db2);
        MineChartsBean.DataBean db3 = new MineChartsBean.DataBean();
        db3.setDateStr("09-17");
        db3.setPoint(174394.95);
        dataBeanList.add(db3);
        MineChartsBean.DataBean db4 = new MineChartsBean.DataBean();
        db4.setDateStr("09-18");
        db4.setPoint(174011.23);
        dataBeanList.add(db4);
        MineChartsBean.DataBean db5 = new MineChartsBean.DataBean();
        db5.setDateStr("09-19");
        db5.setPoint(174011.23);
        dataBeanList.add(db5);
        MineChartsBean.DataBean db6 = new MineChartsBean.DataBean();
        db6.setDateStr("09-20");
        db6.setPoint(174027.45);
        dataBeanList.add(db6);
        MineChartsBean.DataBean db7 = new MineChartsBean.DataBean();
        db7.setDateStr("09-20");
        db7.setPoint(174027.45);
        dataBeanList.add(db7);
        chartsBean.setDatas(dataBeanList);
        return chartsBean;
    }

    public List<String> getDateList() {
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add("2017年11月");
        dateList.add("2017年12月");
        dateList.add("2018年1月");
        dateList.add("2018年2月");
        dateList.add("2018年3月");
        dateList.add("2018年4月");
        dateList.add("2018年5月");
        dateList.add("2018年6月");
        dateList.add("2018年7月");
        dateList.add("2018年8月");
        return dateList;
    }

    public List<Float> getActuaList() {
        ArrayList<Float> actuaList = new ArrayList<>();
        actuaList.add(117.5f);
        actuaList.add(117.2f);
        actuaList.add(117.6f);
        actuaList.add(115.4f);
        actuaList.add(114.7f);
        actuaList.add(102.1f);
        actuaList.add(102.2f);
        actuaList.add(117.5f);
        actuaList.add(101.7f);
        actuaList.add(103.8f);
        return actuaList;
    }
}

package com.example.myutils.bean;

import java.util.List;

/**
 * Created by zheng on 2018/4/23 0023.
 * 1
 */

public class MineChartsBean {

    private String title;
    //记录哪一周数据
    private int weekIndex = 0;
    //1每日净值  2浮动收益  3每日交易量  4活跃沉默用户  5佣金
    private int sortIndex;
    //当前的位置
    private int currentIndex=0;
    //最大有多少周的数据
    private int maxIndex=0;
    //开始时间和结束时间
    private String queryFrom;
    private String queryTo;
    private List<DataBean> datas;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public String getQueryFrom() {
        return queryFrom;
    }

    public void setQueryFrom(String queryFrom) {
        this.queryFrom = queryFrom;
    }

    public String getQueryTo() {
        return queryTo;
    }

    public void setQueryTo(String queryTo) {
        this.queryTo = queryTo;
    }

    public List<DataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DataBean> datas) {
        this.datas = datas;
    }

    public static class DataBean {

        private String dateStr="dataStr";
        private double point = 0;
        //沉默活跃用户需要两个point，其他需要一个
        private double point1 = 0;

        public String getDateStr() {
            return dateStr;
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }

        public double getPoint() {
            return point;
        }

        public void setPoint(double point) {
            this.point = point;
        }

        public double getPoint1() {
            return point1;
        }

        public void setPoint1(double point1) {
            this.point1 = point1;
        }
    }

}

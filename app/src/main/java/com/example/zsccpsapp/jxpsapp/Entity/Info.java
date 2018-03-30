package com.example.zsccpsapp.jxpsapp.Entity;

import com.example.zsccpsapp.jxpsapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 99zan on 2018/2/9.
 */

public class Info implements Serializable {
    private static final long serialVersionUID = -758459502806858414L;
    /**
     * 精度
     */
    private double latitude;
    /**
     * 纬度
     */
    private double longitude;
    /**
     * 图片ID，真实项目中可能是图片路径
     */
    private int imgId;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 距离
     */
    private String distance;
    /**
     * 赞数量
     */
    private int zan;

    public static List<Info> infos = new ArrayList<Info>();

    static {
        infos.add(new Info(45.7612130000, 126.6614550000, R.drawable.qu, "英伦贵族小旅馆",
                "距离209米", 1456));
        infos.add(new Info(45.7610130000, 126.6715550000, R.drawable.qu, "沙井国际洗浴会所",
                "距离897米", 456));
        infos.add(new Info(45.7640130000, 126.6615590000, R.drawable.qu, "五环服装城",
                "距离249米", 1456));
    }

    public Info() {
    }

    public Info(double latitude, double longitude, int imgId, String name,
                String distance, int zan) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

}

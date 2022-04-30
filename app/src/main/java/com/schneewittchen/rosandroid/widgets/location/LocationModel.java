package com.schneewittchen.rosandroid.widgets.location;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationModel {


    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private static LocationModel instance;
    private static AMapLocationListener aMapLocationListener;

    public static LocationModel getInstance(final Context context) {
        if (instance == null) {
            instance = new LocationModel(context);
        }
        return instance;
    }

    public LocationModel(Context context) {
        locationInit(context);
    }

    public void locationInit(Context context) {
        try {
            locationClient = new AMapLocationClient(context);
            locationOption = getDefaultOption();
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClient.setLocationOption(locationOption);
            // 启动定位
//            locationClient.setLocationListener(locationListener);
//            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setListener(AMapLocationListener locationListener) {
        if (instance != null) {
            instance.locationClient.unRegisterLocationListener(aMapLocationListener);
            aMapLocationListener = locationListener;
            instance.locationClient.setLocationListener(aMapLocationListener);
        }
//            instance.locationClient.setLocationListener(locationListener);
    }

    public static void startLocation() {
        if (instance != null)
            instance.locationClient.startLocation();
    }

    public static void stopLocation() {
        if (instance != null)
            instance.locationClient.stopLocation();
    }


    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }


}

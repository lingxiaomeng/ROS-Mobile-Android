package com.schneewittchen.rosandroid.widgets.imu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ImuModel {


    private static ImuModel instance;
    private final SensorManager sensorManager;
    private final Sensor accelSensor;
    private final Sensor gyroSensor;
    private final Sensor quatSensor;
    private static SensorEventListener sensorEventListener;


    public static ImuModel getInstance(SensorManager sensorManager) {
        if (instance == null) {
            instance = new ImuModel(sensorManager);
        }
        return instance;
    }

    public ImuModel(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.accelSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.gyroSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.quatSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void imuInit(Context context) {

    }

    public static void setListener(SensorEventListener sensorEventListener) {
        if (instance != null) {
            instance.sensorManager.unregisterListener(ImuModel.sensorEventListener);
            ImuModel.sensorEventListener = sensorEventListener;
            instance.sensorManager.registerListener(ImuModel.sensorEventListener, instance.accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
            instance.sensorManager.registerListener(ImuModel.sensorEventListener, instance.gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            instance.sensorManager.registerListener(ImuModel.sensorEventListener, instance.quatSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }




}

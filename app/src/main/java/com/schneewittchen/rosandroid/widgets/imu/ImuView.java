package com.schneewittchen.rosandroid.widgets.imu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;

import org.ros.message.Time;


/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 * @updated on 10.03.2021
 * @modified by Nico Studt
 */

public class ImuView extends PublisherWidgetView {

    public static final String TAG = ImuView.class.getSimpleName();

    Paint buttonPaint;
    TextPaint textPaint;
    StaticLayout staticLayout;
    String data;
    SensorListener sensorListener;

    private class SensorListener implements SensorEventListener {

        private boolean hasAccel;
        private boolean hasGyro;
        private boolean hasQuat;

        private long accelTime;
        private long gyroTime;
        private long quatTime;

        private ImuData imu;

        private SensorListener(boolean hasAccel, boolean hasGyro, boolean hasQuat) {
            this.hasAccel = hasAccel;
            this.hasGyro = hasGyro;
            this.hasQuat = hasQuat;
            this.accelTime = 0;
            this.gyroTime = 0;
            this.quatTime = 0;
            this.imu = new ImuData();
        }

        //	@Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        //	@Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                this.imu.LinearAcceleration_X = (event.values[0]);
                this.imu.LinearAcceleration_Y = (event.values[1]);
                this.imu.LinearAcceleration_Z = (event.values[2]);
                this.imu.AccelerationCovariance = (new double[]{0.01, 0, 0, 0, 0.01, 0, 0, 0, 0.01});
                this.accelTime = event.timestamp;
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                this.imu.AngularVelocity_X = (event.values[0]);
                this.imu.AngularVelocity_Y = (event.values[1]);
                this.imu.AngularVelocity_Z = (event.values[2]);
                this.imu.AngularVelocityCovariance = (new double[]{0.0025, 0, 0, 0, 0.0025, 0, 0, 0, 0.0025});
                this.gyroTime = event.timestamp;
            } else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                float[] quaternion = new float[4];
                SensorManager.getQuaternionFromVector(quaternion, event.values);
                this.imu.Orientation_W = (quaternion[0]);
                this.imu.Orientation_X = (quaternion[1]);
                this.imu.Orientation_Y = (quaternion[2]);
                this.imu.Orientation_Z = (quaternion[3]);
                this.imu.OrientationCovariance = (new double[]{0.001, 0, 0, 0, 0.001, 0, 0, 0, 0.001});
                this.quatTime = event.timestamp;
            }

            // Currently storing event times in case I filter them in the future.  Otherwise they are used to determine if all sensors have reported.
            if ((this.accelTime != 0 || !this.hasAccel) && (this.gyroTime != 0 || !this.hasGyro) && (this.quatTime != 0 || !this.hasQuat)) {
                // Convert event.timestamp (nanoseconds uptime) into system time, use that as the header stamp
                long time_delta_millis = System.currentTimeMillis() - SystemClock.uptimeMillis();
                this.imu.Stamp = Time.fromMillis(time_delta_millis + event.timestamp / 1000000);
                this.imu.FrameId = "/imu";// TODO Make parameter

                publishViewData(imu);
                Log.i(TAG,imu.toString()+"\n"+hashCode());
                // Create a new message
                this.imu = new ImuData();

                // Reset times
                this.accelTime = 0;
                this.gyroTime = 0;
                this.quatTime = 0;
            }
        }
    }


    public ImuView(Context context) {
        super(context);
        init();
    }

    public ImuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        data = "Location";

        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().density);

        sensorListener = new SensorListener(true, true, true);
        ImuModel.setListener(sensorListener);

    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float textLayoutWidth = width;

        ImuEntity entity = (ImuEntity) widgetEntity;

        if (entity.rotation == 90 || entity.rotation == 270) {
            textLayoutWidth = height;
        }

        canvas.drawRect(new Rect(0, 0, (int) width, (int) height), buttonPaint);

        staticLayout = new StaticLayout(data,
                textPaint,
                (int) textLayoutWidth,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0,
                false);
        canvas.save();
        canvas.rotate(entity.rotation, width / 2, height / 2);
        canvas.translate(((width / 2) - staticLayout.getWidth() / 2), height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}

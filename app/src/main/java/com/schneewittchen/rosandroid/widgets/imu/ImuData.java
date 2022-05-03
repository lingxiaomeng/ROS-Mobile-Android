package com.schneewittchen.rosandroid.widgets.imu;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickData;

import org.ros.internal.message.Message;
import org.ros.message.Time;
import org.ros.node.topic.Publisher;

import java.util.Arrays;

import sensor_msgs.Imu;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class ImuData extends BaseData {

    public static final String TAG = JoystickData.class.getSimpleName();

    double LinearAcceleration_X = 0;
    double LinearAcceleration_Y = 0;
    double LinearAcceleration_Z = 0;

    double[] AccelerationCovariance;

    double AngularVelocity_X = 0;
    double AngularVelocity_Y = 0;
    double AngularVelocity_Z = 0;
    double[] AngularVelocityCovariance;

    double Orientation_W = 0;
    double Orientation_X = 0;
    double Orientation_Y = 0;
    double Orientation_Z = 0;
    double[] OrientationCovariance;

    Time Stamp;
    String FrameId = "";

    public ImuData() {

    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        Imu message = (Imu) publisher.newMessage();
        message.getLinearAcceleration().setX(LinearAcceleration_X);
        message.getLinearAcceleration().setY(LinearAcceleration_Y);
        message.getLinearAcceleration().setZ(LinearAcceleration_Z);
        message.setLinearAccelerationCovariance(AccelerationCovariance);

        message.getAngularVelocity().setX(AngularVelocity_X);
        message.getAngularVelocity().setY(AngularVelocity_Y);
        message.getAngularVelocity().setZ(AngularVelocity_Z);
        message.setAngularVelocityCovariance(AngularVelocityCovariance);

        message.getOrientation().setW(Orientation_W);
        message.getOrientation().setX(Orientation_X);
        message.getOrientation().setY(Orientation_Y);
        message.getOrientation().setZ(Orientation_Z);
        message.setOrientationCovariance(OrientationCovariance);

        message.getHeader().setFrameId(FrameId);
        message.getHeader().setStamp(Stamp);
        return message;
    }


    @Override
    public String toString() {
        return "ImuData{" +
                "LinearAcceleration_X=" + LinearAcceleration_X +
                ", LinearAcceleration_Y=" + LinearAcceleration_Y +
                ", LinearAcceleration_Z=" + LinearAcceleration_Z +
                ", AccelerationCovariance=" + Arrays.toString(AccelerationCovariance) +
                ", AngularVelocity_X=" + AngularVelocity_X +
                ", AngularVelocity_Y=" + AngularVelocity_Y +
                ", AngularVelocity_Z=" + AngularVelocity_Z +
                ", AngularVelocityCovariance=" + Arrays.toString(AngularVelocityCovariance) +
                ", Orientation_W=" + Orientation_W +
                ", Orientation_X=" + Orientation_X +
                ", Orientation_Y=" + Orientation_Y +
                ", Orientation_Z=" + Orientation_Z +
                ", OrientationCovariance=" + Arrays.toString(OrientationCovariance) +
                ", Stamp=" + Stamp +
                ", FrameId='" + FrameId + '\'' +
                '}';
    }
}
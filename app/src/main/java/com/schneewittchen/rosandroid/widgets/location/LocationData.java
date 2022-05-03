package com.schneewittchen.rosandroid.widgets.location;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickData;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickEntity;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;


import sensor_msgs.NavSatFix;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LocationData extends BaseData {

    public static final String TAG = JoystickData.class.getSimpleName();
    private final double latitude;
    private final double longitude;

    public LocationData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        NavSatFix message = (NavSatFix) publisher.newMessage();
        message.setLatitude(latitude);
        message.setLongitude(longitude);
        return message;
    }
}
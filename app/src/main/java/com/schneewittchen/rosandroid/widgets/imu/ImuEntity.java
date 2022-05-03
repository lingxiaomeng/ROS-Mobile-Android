package com.schneewittchen.rosandroid.widgets.imu;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

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

public class ImuEntity extends PublisherWidgetEntity {

    public String text;
    public int rotation;


    public ImuEntity() {
        this.width = 2;
        this.height = 1;
        this.topic = new Topic("imu", Imu._TYPE);
        this.immediatePublish = true;
        this.text = "A button";
        this.rotation = 0;
    }

}

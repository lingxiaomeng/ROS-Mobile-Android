package com.schneewittchen.rosandroid.widgets.marker;

import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;


/**
 * Path entity represents a widget which subscribes
 * to a topic with message type "nav_msgs.Path".
 * Usable in 2D widgets.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class MarkerEntity extends SubscriberLayerEntity {


    public MarkerEntity() {
        this.topic = new Topic("/start_marker", visualization_msgs.Marker._TYPE);
    }
}

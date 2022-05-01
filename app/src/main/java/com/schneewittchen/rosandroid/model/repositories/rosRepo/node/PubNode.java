package com.schneewittchen.rosandroid.model.repositories.rosRepo.node;

import android.util.Log;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.PublisherLayerEntity;
import com.schneewittchen.rosandroid.ui.fragments.custom.GPSWayPointsData;

import org.ros.internal.message.Message;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ROS Node for publishing Messages on a specific topic.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 16.09.20
 * @updated on
 * @modified by
 */
public class PubNode extends AbstractNode {

    private Publisher<Message> publisher;
    private BaseData lastData;
    private Timer pubTimer;
    private long pubPeriod = 100L;
    private boolean immediatePublish = true;
    private ConnectedNode node;


    @Override
    public void onStart(ConnectedNode parentNode) {
        publisher = parentNode.newPublisher(topic.name, topic.type);
        node = parentNode;
        this.createAndStartSchedule();
    }

    public ConnectedNode getNode() {
        return node;
    }

    /**
     * Call this method to publish a ROS message.
     *
     * @param data Data to publish
     */
    public void setData(BaseData data) {
        this.lastData = data;

        if (immediatePublish) {
            publish();
//            Log.i("MLX", TAG + "," + data.getTopic().type);
        }
    }

    /**
     * Set publishing frequency.
     * E.g. With a value of 10 the node will publish 10 times per second.
     *
     * @param hz Frequency in hertz
     */
    public void setFrequency(float hz) {
        this.pubPeriod = (long) (1000 / hz);
    }

    /**
     * Enable or disable immediate publishing.
     * In the enabled state the node will create und send a ros message as soon as
     *
     * @param flag Enable immediate publishing
     * @link #setData(Object) is called.
     */
    public void setImmediatePublish(boolean flag) {
        this.immediatePublish = flag;
    }

    private void createAndStartSchedule() {
        if (pubTimer != null) {
            pubTimer.cancel();
        }

        if (immediatePublish) {
            return;
        }

        pubTimer = new Timer();
        pubTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                publish();
            }
        }, pubPeriod, pubPeriod);
    }

    private void publish() {
        if (publisher == null) {
            return;
        }
        if (lastData == null) {
            return;
        }
        if (lastData instanceof GPSWayPointsData) {
            Message message = ((GPSWayPointsData) lastData).toRosMessage(publisher, widget, node);
            publisher.publish(message);

        } else {
            Message message = lastData.toRosMessage(publisher, widget);
            publisher.publish(message);
        }
    }

    @Override
    public void setWidget(BaseEntity widget) {
        super.setWidget(widget);

        if (!(widget instanceof PublisherLayerEntity)) {
            return;
        }

        PublisherLayerEntity pubEntity = (PublisherLayerEntity) widget;

        this.setImmediatePublish(pubEntity.immediatePublish);
        this.setFrequency(pubEntity.publishRate);
    }
}

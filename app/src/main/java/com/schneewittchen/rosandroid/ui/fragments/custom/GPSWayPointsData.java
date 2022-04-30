package com.schneewittchen.rosandroid.ui.fragments.custom;

import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickData;

import org.ros.internal.message.Message;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import java.util.ArrayList;
import java.util.List;

import geometry_msgs.Point;
import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;
import nav_msgs.Path;
import sensor_msgs.JointState;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class GPSWayPointsData extends BaseData {

    public static final String TAG = JoystickData.class.getSimpleName();
    ArrayList<LatLng> latLngs;

    public GPSWayPointsData(ArrayList<LatLng> latLngs) {
        this.latLngs = latLngs;
    }


    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget, ConnectedNode node) {
        Path message = (Path) publisher.newMessage();

//        List<PoseStamped> poseStampeds = message.getPoses();

        Log.i("MLX", message.getPoses().toString());
        for(LatLng latLng:latLngs){
            PoseStamped poseStamped = node.getTopicMessageFactory().newFromType(PoseStamped._TYPE);
            poseStamped.getPose().getPosition().setX(latLng.latitude);
            poseStamped.getPose().getPosition().setY(latLng.longitude);
            message.getPoses().add(poseStamped);
        }
        Log.i("MLX", message.getPoses().toString());
        return message;
    }

}
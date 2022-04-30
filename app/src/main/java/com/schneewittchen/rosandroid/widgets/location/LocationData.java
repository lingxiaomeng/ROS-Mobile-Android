package com.schneewittchen.rosandroid.widgets.location;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

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

    private NavSatFix navSatFix;

    public LocationData(NavSatFix navSatFix) {
        this.navSatFix = navSatFix;
    }

    public double getLat() {
        return navSatFix.getLatitude();
    }

    public double getLon() {
        return navSatFix.getLongitude();
    }
}
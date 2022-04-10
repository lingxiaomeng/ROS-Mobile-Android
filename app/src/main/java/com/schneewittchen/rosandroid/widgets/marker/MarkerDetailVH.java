package com.schneewittchen.rosandroid.widgets.marker;

import android.util.Log;
import android.view.View;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberLayerViewHolder;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.Collections;
import java.util.List;

import visualization_msgs.Marker;
//import nav_msgs.Path;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class MarkerDetailVH extends SubscriberLayerViewHolder {

    @Override
    protected void initView(View parentView) {
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
    }

    @Override
    protected void updateEntity(BaseEntity entity) {

    }


    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(Marker._TYPE);
    }
}

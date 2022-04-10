package com.schneewittchen.rosandroid.widgets.path;

import android.util.Log;
import android.view.View;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberLayerViewHolder;
import com.schneewittchen.rosandroid.widgets.laserscan.LaserScanDetailVH;
import com.schneewittchen.rosandroid.widgets.laserscan.LaserScanEntity;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.Collections;
import java.util.List;

import nav_msgs.Path;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class PathDetailVH extends SubscriberLayerViewHolder {

    private AlphaTileView pathTileView;
    private static final String TAG = PathDetailVH.class.getSimpleName();
    private int pathColor;


    @Override
    protected void initView(View parentView) {
        this.pathTileView = parentView.findViewById(R.id.pathTileView);
        this.pathTileView.setOnClickListener(v -> openColorChooser(pathTileView));

    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        PathEntity pathEntity = (PathEntity) entity;
        this.chooseColor(pathTileView, pathEntity.lineColor);

    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        PathEntity pathEntity = (PathEntity) entity;

        pathEntity.lineColor = pathColor;

    }

    private void chooseColor(AlphaTileView tileView, int color) {
         if (tileView == pathTileView) {
            this.pathColor = color;
            this.pathTileView.setBackgroundColor(color);
        }
    }

    private void openColorChooser(AlphaTileView tileView) {
        Log.i(TAG, "OPen stuff");

        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this.itemView.getContext())
                .setTitle("Choose a color")
                .setPositiveButton(R.string.ok, (ColorEnvelopeListener) (envelope, fromUser) -> {
                    chooseColor(tileView, envelope.getColor());
                    this.forceWidgetUpdate();
                })
                .setNegativeButton(R.string.cancel,
                        (dialogInterface, i) -> dialogInterface.dismiss());

        int initialColor = pathColor;

        builder.getColorPickerView().setInitialColor(initialColor);
        builder.show();
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(Path._TYPE);
    }
}

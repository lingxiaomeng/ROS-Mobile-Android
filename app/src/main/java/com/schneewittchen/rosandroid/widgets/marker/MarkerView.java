package com.schneewittchen.rosandroid.widgets.marker;

import android.content.Context;
import android.util.Log;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.TransformProvider;
import com.schneewittchen.rosandroid.ui.opengl.shape.GoalShape;
import com.schneewittchen.rosandroid.ui.opengl.shape.Shape;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.Vertices;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberLayerView;

import org.ros.internal.message.Message;
import org.ros.namespace.GraphName;
import org.ros.rosjava_geometry.FrameTransform;
import org.ros.rosjava_geometry.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.PoseStamped;
import visualization_msgs.Marker;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class MarkerView extends SubscriberLayerView {

    public static final String TAG = MarkerView.class.getSimpleName();

    private Shape shape;
    private Marker marker;

    public MarkerView(Context context) {
        super(context);
        shape = new GoalShape();
    }


    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (marker == null) return;
        double x = marker.getPose().getPosition().getX();
        double y = marker.getPose().getPosition().getY();
        double z = marker.getPose().getPosition().getZ();
//        Log.i(TAG, x + " " + y + " " + z);
        ROSColor pointcolor = new ROSColor(marker.getColor());

        FloatBuffer pointVertices = Vertices.allocateBuffer(3 );

        pointVertices.put((float) x);
        pointVertices.put((float) y);
        pointVertices.put((float) z);

        pointVertices.position(0);

//        pointVertices.position(0);
        Vertices.drawPoints(gl, pointVertices, pointcolor, 20);
    }

    @Override
    public void onNewMessage(Message message) {
        marker = (Marker) message;
        GraphName source = GraphName.of(marker.getHeader().getFrameId());
        frame = source;
        FrameTransform frameTransform = TransformProvider.getInstance().getTree().transform(source, frame);

        if (frameTransform == null) return;

        Transform poseTransform = Transform.fromPoseMessage(marker.getPose());
        shape.setTransform(frameTransform.getTransform().multiply(poseTransform));
    }

}
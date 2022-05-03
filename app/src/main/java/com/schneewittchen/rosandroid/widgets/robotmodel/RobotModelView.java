package com.schneewittchen.rosandroid.widgets.robotmodel;

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
import org.ros.rosjava_geometry.FrameTransformTree;
import org.ros.rosjava_geometry.Transform;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.TransformStamped;
import tf2_msgs.TFMessage;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class RobotModelView extends SubscriberLayerView {

    public static final String TAG = RobotModelView.class.getSimpleName();


    public RobotModelView(Context context) {
        super(context);
//        shape = new GoalShape();
    }


    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
//        if (marker == null) return;
//        double x = marker.getPose().getPosition().getX();
//        double y = marker.getPose().getPosition().getY();
//        double z = marker.getPose().getPosition().getZ();
//        Log.i(TAG, x + " " + y + " " + z);
        ROSColor pointcolor = new ROSColor(0.5f, 0.5f, 0.5f, 1f);

        FloatBuffer pointVertices = Vertices.allocateBuffer(3);

        pointVertices.put(0.0f);
        pointVertices.put(0.0f);
        pointVertices.put(0.0f);

        pointVertices.position(0);
//        gl.glScalef((float)view.getCamera().getZoom()/100, (float)view.getCamera().getZoom()/100, 1.0f);
        Vertices.drawPoints(gl, pointVertices, pointcolor, 25 * view.getCamera().getScale());
//        shape.draw(view, gl);
    }

    @Override
    public void onNewMessage(Message message) {
        GraphName base_link = GraphName.of("base_link");
        frame = base_link;
    }
}
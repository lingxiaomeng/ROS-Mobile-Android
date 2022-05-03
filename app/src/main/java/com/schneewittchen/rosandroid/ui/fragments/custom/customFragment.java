package com.schneewittchen.rosandroid.ui.fragments.custom;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.databinding.FragmentCustomBinding;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.viewmodel.CustomViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import nav_msgs.Path;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.3.0
 * @created on 10.01.2020
 * @updated on 05.10.2020
 * @modified by Nico Studt
 * @updated on 16.11.2020
 * @modified by Nils Rottmann
 * @updated on 13.05.2021
 * @modified by Nico Studt
 */
public class customFragment extends naviFragment implements AMap.OnMapClickListener, View.OnClickListener {

    private static final String TAG = customFragment.class.getSimpleName();

    private CustomViewModel mViewModel;
    private FragmentCustomBinding binding;
    private TextureMapView mAMapNaviView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private AMapNavi mAMapNavi;
    private Polyline polyline;
    private int evenType = -1;
    private Toast toast;


    public static customFragment newInstance() {
        Log.i(TAG, "New Master Fragment");
        return new customFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAMapNaviView.onDestroy();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        mAMapNaviView = (TextureMapView) getView().findViewById(R.id.map);

        if (mAMapNaviView != null) {
            mAMapNaviView.onCreate(savedInstanceState);
            aMap = mAMapNaviView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            aMap.setOnMapClickListener(this);
            try {
                mAMapNavi = AMapNavi.getInstance(getContext());
                mAMapNavi.addAMapNaviListener(this);
                mAMapNavi.addParallelRoadListener(this);
//                mAMapNaviView.add
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }

        binding.buttonSelectEnd.setOnClickListener(this);
        binding.buttonStart.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        super.onCalculateRouteFailure(aMapCalcRouteResult);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getContext(), "规划失败:"+aMapCalcRouteResult.getErrorDescription(), Toast.LENGTH_SHORT);
        toast.show();
        if (polyline != null)
            polyline.remove();
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        super.onCalculateRouteSuccess(aMapCalcRouteResult);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getContext(), "规划成功", Toast.LENGTH_SHORT);
        toast.show();

        if (polyline != null)
            polyline.remove();

        HashMap<Integer, AMapNaviPath> navipaths = mAMapNavi.getNaviPaths();
        ArrayList<LatLng> latLngs = new ArrayList<>();

        ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();
        for (AMapNaviPath path : navipaths.values()) {
            for (NaviLatLng p : path.getCoordList()) {
                LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
                latLngs.add(latLng);
                markerOptionsArrayList.add(new MarkerOptions().position(latLng));
            }
        }
        mViewModel.publishGPSWayPointsData(new GPSWayPointsData(latLngs));
        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 0, 0, 255)));
    }

    @Override
    public void onInitNaviSuccess() {
        Log.i(TAG, "init navi");
        super.onInitNaviSuccess();
    }

    @Override
    public void onInitNaviFailure() {
        Log.e(TAG, "init navi");
        super.onInitNaviFailure();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    Marker endMarker = null;

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("MLX", "终点");
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (evenType == 1) {
            if (endMarker != null) {
                endMarker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .position(latLng)
                    .draggable(true).title("终点");
            endMarker = aMap.addMarker(markerOptions);
            endMarker.setObject(this);
        }
        evenType = -1;
    }

    public void pick(int evenType) {
        this.evenType = evenType;
        toast = Toast.makeText(getContext(), "请在地图上选取坐标", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        switch (view.getId()) {
            case R.id.button_select_end:
                pick(1);
                break;
            case R.id.button_start:
                if (endMarker != null) {
                    toast = Toast.makeText(getContext(), "开始导航", Toast.LENGTH_SHORT);
                    toast.show();
                    mAMapNavi.calculateRideRoute(new NaviLatLng(endMarker.getPosition().latitude, endMarker.getPosition().longitude), new NaviLatLng(aMap.getMyLocation().getLatitude(),aMap.getMyLocation().getLongitude()));
                }else{
                    toast = Toast.makeText(getContext(), "请选择终点", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }




}

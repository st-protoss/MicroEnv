package com.wm.toec.microenv.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.app.App;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.databinding.ActivityDeviceMapBinding;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.plugin.lbs.LocationService;
import com.wm.toec.microenv.ui.portal.ActivityPortal;
import com.wm.toec.microenv.util.ToastUtil;
import com.wm.toec.microenv.viewmodel.device.LocationCommand;
import com.wm.toec.microenv.viewmodel.device.LocationViewModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wm on 2018/7/4.
 * 地图选址界面
 */

public class ActivityLocation extends BaseActivity<ActivityDeviceMapBinding> implements LocationCommand {
    private LocationViewModel locationViewModel;
    private LocationService locationService;

    private double startLati;//初始定位精度
    private double startLongti;//初始定位维度
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private GeoCoder mSearch = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_map);
        setTitle("重新定位设备");
        locationViewModel = new LocationViewModel(this);
        locationViewModel.setLocationCommand(this);
        initMap();
        lbs();
        setToolbar();
    }

    /**
     * 设置标题栏的刷新按钮
     */
    private void setToolbar() {
        //这里用和设备列表一个刷新按钮 节约空间
        mBaseView.toolbar.inflateMenu(R.menu.menu_device_manager);
        mBaseView.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.action_refresh){
                //重新定位位置
                locationService.start();
            }
            return false;
        });
    }

    /**
     * 初始化百度地图
     */
    private void initMap() {
    mMapView = (MapView)findViewById(R.id.map_view);
    mBaiduMap = mMapView.getMap();
    }

    /**
     * 获取当前定位
     */
    private void lbs() {
        locationService = new LocationService(App.getAppContext());
        locationService.registerListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                {
                    if (location.getLocType() == BDLocation.TypeNetWorkException) {
                        locationViewModel.firstLocateFail();
                        return;
                    }

                    startLati = location.getLatitude();
                    startLongti = location.getLongitude();
                    StringBuffer sb = new StringBuffer(256);
                    //sb.append("\ncity : ");// 城市
                    /*sb.append(location.getCity());
                    sb.append(" ");*/
                    //sb.append("\nDistrict : ");// 区
                    sb.append("当前位置：");
                    sb.append(location.getDistrict());
                    //sb.append("\nStreet : ");// 街道
                    sb.append(location.getStreet());
                    sb.append(" ");
                    //sb.append("\naddr : ");// 地址信息
                    sb.append(location.getAddrStr());
                    locationViewModel.firstLocateSuccess(sb.toString());
                    addMarker();
                    locationService.stop();
                }
            }
        });
        locationService.start();
    }

    /**
     * 添加选取位置图标
     */
    private void addMarker() {
        mSearch =GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    locationViewModel.reverseGeoFail();
                    return;
                }
                locationViewModel.reverseGeoSuccess(result.getAddress());
            }
        });
        mBaiduMap.clear();
        LatLng startPoint = new LatLng(startLati,startLongti);
        MarkerOptions pointer = new MarkerOptions().position(startPoint)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add))
                .zIndex(3).draggable(true);
        mBaiduMap.addOverlay(pointer);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(startPoint).zoom(15.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //逆向Geo搜索
                locationViewModel.locating();
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(marker.getPosition()).newVersion(1));
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });

    }

    @Override
    public void confirmLocation(String location) {
        locationViewModel.confirmLocation(location);
    }

    @Override
    public void bindSuccess() {
        //更改成功
        ToastUtil.showToast("更改成功！");
        ActivityLocation.this.finish();
    }

    @Override
    public void bindFail() {
        //绑定时报
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.clear();
        mMapView.onDestroy();
        mSearch.destroy();
    }
}

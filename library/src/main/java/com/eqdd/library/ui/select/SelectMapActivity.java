package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.view.SearchInputView;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectMapActivityCustom;
import com.eqdd.library.base.CommonActivity;

import com.eqdd.library.base.Config;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

@Route(path = Config.LIBRARY_SELECT_MAP)
public class SelectMapActivity extends CommonActivity implements LocationSource,
        GeocodeSearch.OnGeocodeSearchListener, AMapLocationListener,
        PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener {
    private SelectMapActivityCustom dataBinding;
    private BitmapDescriptor mBitmapDescriptor;
    private AMap mAMap;
    private Marker mMarker;
    private GeocodeSearch mGeocodeSearch;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;
    private double mMyLat;
    private double mMyLng;
    private String mMyPoi;
    private AMapLocationClient mlocationClient;
    private ArrayList<MapSuggestion> searchSuggestions;
    private Subscription subSearch;
    private String mCity;
    private int selectType;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_map);
        initTopTitleBar(View.VISIBLE, "位置");
        initTopRightText("选定", v -> {

            if (!TextUtils.isEmpty(dataBinding.tvAddress.getText())) {
                Intent intent = new Intent();
                intent.putExtra(Config.MAP_RESULT, dataBinding.tvAddress.getText().toString().trim());
                setResult(Config.SUCCESS, intent);
                finish();
            } else {
                ToastUtil.showShort("你还没选定位置呢");
            }
        });
    }

    @Override
    public void initData() {
        //获取权限
        selectType = getIntent().getIntExtra(Config.SELECT_MAP_TYPE, -1);
        RxPermissions.getInstance(this)
                .request(ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        initMap();
                    } else {
                        ToastUtil.showShort("请开启定位权限");
                        finish();
                    }
                });
    }

    private void setMaker(LatLng latLng, String address) {
        if (mMarker == null) {
            mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.library_map_location);
            MarkerOptions markerOptions = (new MarkerOptions()).position(latLng).icon(this.mBitmapDescriptor);
            mMarker = mAMap.addMarker(markerOptions);
        }
        mMarker.setPosition(latLng);
        if (!TextUtils.isEmpty(address)) {
            dataBinding.tvAddress.setText(address);
        }

    }

    private void initMap() {
        mAMap = dataBinding.mapView.getMap();
        dataBinding.mapView.onCreate(getSavedInstanceState());
        if (selectType != Config.MAP_TYPE_MY_LOCATION) {

            dataBinding.mapView.getMap().setOnMapClickListener(latLng -> {
                setMaker(latLng, null);
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 50, GeocodeSearch.AMAP);
                mGeocodeSearch.getFromLocationAsyn(query);
            });
        }
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);
        mAMap.setMapType(1);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.library_map_my_location));
        myLocationStyle.strokeWidth(0.0F);
        myLocationStyle.strokeColor(R.color.library_main_color);
        myLocationStyle.radiusFillColor(0);
        mAMap.setMyLocationStyle(myLocationStyle);
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
    }

    private void initLocation() {
        if (mlocationClient==null) {
            AMapLocationClientOption mLocationOption;
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            mLocationOption.setMockEnable(false);//是否允许模拟
            mLocationOption.setOnceLocationLatest(true);
            mlocationClient.setLocationOption(mLocationOption);
        }
        mlocationClient.startLocation();
        dataBinding.tvAddress.setText(null);
    }


    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000 && regeocodeResult != null) {
            dataBinding.tvAddress.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
        }

    }

    private String getMapUrl(double latitude, double longitude) {
        return "http://restapi.amap.com/v3/staticmap?location=" + longitude + "," + latitude + "&zoom=16&scale=2&size=408*240&markers=mid,,A:" + longitude + "," + latitude + "&key=" + "e09af6a2b26c02086e9216bd07c960ae";
    }

    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        this.mLocationChangedListener = onLocationChangedListener;
        System.out.println("当前类=SelectMapActivity.activate()");
        initLocation();
    }

    public void deactivate() {
    }

    protected void onDestroy() {
        dataBinding.mapView.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.onDestroy();
        }
        if (subSearch != null && !subSearch.isUnsubscribed()) {
            subSearch.unsubscribe();
        }
        super.onDestroy();
    }


    @Override
    public void setView() {
        SearchInputView searchInputView = (SearchInputView) dataBinding.floatingSearchView.findViewById(R.id.search_bar_text);


        dataBinding.floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //第二个参数传入null或者代表在全国进行检索，否则按照传入的city进行检索
                //限制在当前城市
                subSearch = RxTextView.textChangeEvents(searchInputView)
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(t -> {
                            //第二个参数传入null或者代表在全国进行检索，否则按照传入的city进行检索
                            InputtipsQuery inputquery = new InputtipsQuery(t.text().toString(), mCity);
                            inputquery.setCityLimit(true);//限制在当前城市
                            Inputtips inputTips = new Inputtips(SelectMapActivity.this, inputquery);
                            inputTips.setInputtipsListener(SelectMapActivity.this);
                            inputTips.requestInputtipsAsyn();
                        }, e -> {

                        }, () -> {
                            System.out.println("完成");
                        });
            }

            @Override
            public void onFocusCleared() {
                if (subSearch != null && !subSearch.isUnsubscribed()) {
                    subSearch.unsubscribe();
                }

            }
        });

        dataBinding.floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {

                textView.setText(item.getBody());
            }

        });
        dataBinding.floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                MapSuggestion search = (MapSuggestion) searchSuggestion;
                updataLocation(new LatLng(search.getLat(), search.getLon()), search.getAddress());

            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

    }

    private void updataLocation(LatLng latLng, String address) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                latLng, 16);
        mAMap.animateCamera(update, new AMap.CancelableCallback() {
            public void onFinish() {
                setMaker(latLng, address);
            }

            public void onCancel() {
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        }
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        System.out.println("当前类=SelectMapActivity.onLocationChanged()");
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mMyLat = amapLocation.getLatitude();
                mMyLng = amapLocation.getLongitude();
                mMyPoi = amapLocation.getAddress();
                mCity = amapLocation.getCity();
                mLocationChangedListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                updataLocation(new LatLng(mMyLat, mMyLng), mMyPoi);
            } else {
                ToastUtil.showShort("定位失败");
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        System.out.println(list);
        searchSuggestions = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            MapSuggestion searchSuggestion = new MapSuggestion();
            Tip tip = list.get(j);
            if (tip.getPoint() == null)
                continue;
            searchSuggestion.setBody(tip.getName());
            searchSuggestion.setAddress(tip.getDistrict() + tip.getAddress());
            searchSuggestion.setLat(tip.getPoint().getLatitude());
            searchSuggestion.setLon(tip.getPoint().getLongitude());
            searchSuggestions.add(searchSuggestion);
        }
        dataBinding.floatingSearchView.swapSuggestions(searchSuggestions);
    }
}

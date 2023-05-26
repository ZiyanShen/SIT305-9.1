package com.example.myproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.myproject.R;
import com.example.myproject.entity.ReleaseEntity;
import com.example.myproject.helper.MyDataHelper;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView;
    private List<ReleaseEntity> releaseEntities = new ArrayList<>();
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        AMapLocationClient.updatePrivacyShow(this,true,true);
        AMapLocationClient.updatePrivacyAgree(this,true);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setMinZoomLevel(10.0f);
            aMap.setMapLanguage(AMap.ENGLISH);
        }
        init();
    }

    public void init(){
        releaseEntities = new ArrayList<>();
        MyDataHelper myDataHelper = new MyDataHelper(MapActivity.this);
        SQLiteDatabase db = myDataHelper.getWritableDatabase();
        Cursor cursor = db.query("lost_found", null, null, null, null, null, null);
        if (cursor.getCount() != 0){
            if(cursor.moveToFirst()){
                do{
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex("type"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                    @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                    @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                    @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
                    Log.d("TAG", "id:" + id + "; type:" + type + "; name:" + name + "; phone:" + phone + "; description:" + description
                            + "; date:" + date
                            + "; location:" + location+":::::"+longitude+":::"+latitude);
                    ReleaseEntity releaseEntity = new ReleaseEntity();
                    releaseEntity.setId(id);
                    releaseEntity.setDate(date);
                    releaseEntity.setDescription(description);
                    releaseEntity.setLocation(location);
                    releaseEntity.setPhone(phone);
                    releaseEntity.setName(name);
                    releaseEntity.setType(type);
                    releaseEntity.setLatitude(latitude);
                    releaseEntity.setLongitude(longitude);
                    releaseEntities.add(releaseEntity);
                }while (cursor.moveToNext());

                mHandler.sendEmptyMessageDelayed(1, 3000);
            }
        }
        cursor.close();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    for (ReleaseEntity releaseEntity: releaseEntities) {
                        Double lat = Double.valueOf(releaseEntity.getLatitude());
                        Double lon = Double.valueOf(releaseEntity.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition(new LatLng(lat, lon), 15, 0, 30);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        aMap.moveCamera(cameraUpdate);
                        addMarkers(releaseEntity);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 添加标注
     */
    private void addMarkers(ReleaseEntity releaseEntity) {
        Double lat=Double.valueOf(releaseEntity.getLatitude());
        Double lon=Double.valueOf(releaseEntity.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed));

        markerOptions.position(new LatLng(lat,lon));
        switch (releaseEntity.getType()){
            case 1:
                markerOptions.title("exist"+releaseEntity.getLocation()+"loss"+releaseEntity.getDescription());
                break;
            case 2:
                markerOptions.title("exist"+releaseEntity.getLocation()+"find"+releaseEntity.getDescription());
                break;
        }

        markerOptions.period(100);

        Marker growMarker = aMap.addMarker(markerOptions);
        growMarker.setClickable(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
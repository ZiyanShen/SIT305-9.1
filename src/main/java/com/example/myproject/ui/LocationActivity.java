package com.example.myproject.ui;

import android.Manifest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.myproject.R;

import com.example.myproject.entity.LocationEntity;
import com.example.myproject.ui.adapter.LocationResultAdapter;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;

import com.hjq.permissions.XXPermissions;
import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends AppCompatActivity implements AMapLocationListener,TextWatcher, Inputtips.InputtipsListener {
    private EditText search_et;
    private RecyclerView recyclerView;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private String city;
    private LocationResultAdapter locationResultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        search_et = (EditText) findViewById(R.id.search_et);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        locationResultAdapter = new LocationResultAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(locationResultAdapter);
        AMapLocationClient.updatePrivacyShow(this,true,true);
        AMapLocationClient.updatePrivacyAgree(this,true);
        verifyStoragePermissions();

        search_et.addTextChangedListener(this);

        locationResultAdapter.addChildClickViewIds(R.id.ll_item);
        locationResultAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                LocationEntity locationEntity = (LocationEntity) adapter.getData().get(position);
                switch (view.getId()){
                    case R.id.ll_item:
                        Gson gson = new Gson();
                        String data = gson.toJson(locationEntity);
                        Intent intent = new Intent();
                        intent.putExtra("data", data);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });
    }
    // TODO:
    private void showLocation() {
        try {
            mlocationClient = new AMapLocationClient(getApplicationContext());
            mlocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(3500);
            mLocationOption.setNeedAddress(true);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        try {
            if (amapLocation != null) {
                StringBuffer dz = new StringBuffer();
                if (amapLocation.getErrorCode() == 0) {
                    city = amapLocation.getCity();
                    mlocationClient.stopLocation();
                } else {
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
            else{
                Toast.makeText(getBaseContext(),"seek failed",Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mlocationClient) {
            mlocationClient.stopLocation();
        }
    }

    private void destroyLocation() {
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyLocation();
        super.onDestroy();
    }

    public void verifyStoragePermissions() {
        XXPermissions.with(LocationActivity.this)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .permission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            showLocation();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        Toast.makeText(LocationActivity.this,"Failed to obtain permissions",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        locationResultAdapter.getData().clear();
        String trim = s.toString().trim();
        if (!TextUtils.isEmpty(s)){
            InputtipsQuery inputQuery = new InputtipsQuery(trim,city);
            inputQuery.setCityLimit(true);
            Inputtips inputTips = new Inputtips(LocationActivity.this, inputQuery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {


    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<LocationEntity> listString = new ArrayList<>();
            if (list.size() > 0){
                for (Tip tip : list){
                    LocationEntity locationEntity = new LocationEntity();
                    locationEntity.setAddress(tip.getDistrict());
                    locationEntity.setName(tip.getName());
                    locationEntity.setLatitude(tip.getPoint().getLatitude());
                    locationEntity.setLongitude(tip.getPoint().getLongitude());
                    listString.add(locationEntity);
                }
                locationResultAdapter.addData(listString);
            }
        } else {
            Toast.makeText(LocationActivity.this,"error",Toast.LENGTH_SHORT).show();
        }
    }
}

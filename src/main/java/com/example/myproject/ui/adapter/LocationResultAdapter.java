package com.example.myproject.ui.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myproject.R;
import com.example.myproject.entity.LocationEntity;
import com.example.myproject.entity.ReleaseEntity;

import java.util.List;


public class LocationResultAdapter extends BaseQuickAdapter<LocationEntity, BaseViewHolder> {

    public LocationResultAdapter(@Nullable List<LocationEntity> data) {
        super(R.layout.item_location_result,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocationEntity item) {
        helper.setText(R.id.lost_key,item.getName());
        helper.setText(R.id.lost_fount,item.getAddress());
    }
}

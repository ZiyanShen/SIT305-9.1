package com.example.myproject.ui.adapter;


import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myproject.R;
import com.example.myproject.entity.ReleaseEntity;

import java.util.List;


public class ReleaseResultAdapter extends BaseQuickAdapter<ReleaseEntity, BaseViewHolder> {

    public ReleaseResultAdapter(@Nullable List<ReleaseEntity> data) {
        super(R.layout.item_release_result,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReleaseEntity item) {
        helper.setText(R.id.lost_key,item.getName());
        switch (item.getType()){
            case 1:
                helper.setText(R.id.lost_fount,"Loss");
                break;
            case 2:
                helper.setText(R.id.lost_fount,"Find");
                break;
        }
        helper.setText(R.id.lost_description,item.getDescription());
    }
}

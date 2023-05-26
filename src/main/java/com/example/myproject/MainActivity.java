package com.example.myproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.myproject.entity.ReleaseEntity;
import com.example.myproject.helper.MyDataHelper;

import com.example.myproject.ui.ReleaseDetailsActivity;
import com.example.myproject.ui.adapter.ReleaseResultAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<ReleaseEntity> releaseEntities = new ArrayList<>();
    private ReleaseResultAdapter releaseResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        releaseResultAdapter = new ReleaseResultAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(releaseResultAdapter);
        init();

        releaseResultAdapter.addChildClickViewIds(R.id.ll_item);
        releaseResultAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ReleaseEntity releaseEntity = (ReleaseEntity) adapter.getData().get(position);
                switch (view.getId()){
                    case R.id.ll_item:
                        Gson gson = new Gson();
                        String data = gson.toJson(releaseEntity);
                        Intent intent = new Intent(MainActivity.this, ReleaseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("data",data);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,1,bundle);
                        break;
                }
            }
        });
    }

    public void init(){
        releaseEntities = new ArrayList<>();
        MyDataHelper myDataHelper = new MyDataHelper(MainActivity.this);
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
                releaseResultAdapter.getData().clear();
                releaseResultAdapter.addData(releaseEntities);
                releaseResultAdapter.notifyDataSetChanged();
            }
        }else {
            releaseResultAdapter.getData().clear();
            releaseResultAdapter.addData(releaseEntities);
            releaseResultAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            init();
        }
    }
}
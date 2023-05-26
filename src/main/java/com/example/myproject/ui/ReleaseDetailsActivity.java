package com.example.myproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.R;
import com.example.myproject.entity.ReleaseEntity;
import com.example.myproject.helper.MyDataHelper;


public class ReleaseDetailsActivity extends AppCompatActivity {

    private ReleaseEntity info;
    TextView lost_key,lost_fount,lost_description;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_details);
        lost_key = findViewById(R.id.lost_key);
        lost_fount = findViewById(R.id.lost_fount);
        lost_description = findViewById(R.id.lost_description);
        button = findViewById(R.id.button);
        if (getIntent().getExtras() != null){
            try {
                String data = getIntent().getExtras().getString("data");
                info = JSONObject.parseObject(data, ReleaseEntity.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        switch (info.getType()){
            case 1:
                lost_key.setText("Loss");
                break;
            case 2:
                lost_key.setText("Find");
                break;
        }
        lost_fount.setText(info.getDate());
        lost_description.setText("exist"+info.getLocation()+lost_key.getText().toString()+info.getName());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MyDataHelper myDataHelper = new MyDataHelper(ReleaseDetailsActivity.this);
                    SQLiteDatabase db = myDataHelper.getWritableDatabase();
                    db.execSQL("delete from lost_found where id=?", new String[] {String.valueOf(info.getId())});
                    Toast.makeText(ReleaseDetailsActivity.this,"Successfully deleted",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }catch (Exception e){
                    Toast.makeText(ReleaseDetailsActivity.this,"Delete failed",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
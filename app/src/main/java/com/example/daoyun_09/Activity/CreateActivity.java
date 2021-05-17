package com.example.daoyun_09.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daoyun_09.R;

import java.security.cert.Certificate;

public class CreateActivity extends AppCompatActivity {

    EditText cName;
    EditText tName,time,tcn;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_create);
        cName = findViewById(R.id.tv_cName);
        tName = findViewById(R.id.tv_tName);
        time = findViewById(R.id.tv_cTime);
        tcn = findViewById(R.id.tv_class);
        btn = findViewById(R.id.bt_createClass);
        Intent intent=getIntent();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                //将求和的结果放进intent中
                intent.putExtra("courseName",cName.getText().toString());
                //intent.putExtra("courseName","cName.getText()");
                intent.putExtra("teacherName",tName.getText().toString());
                intent.putExtra("time",time.getText().toString());
                intent.putExtra("tcn",tcn.getText().toString());
                Toast.makeText(CreateActivity.this,cName.getText(),Toast.LENGTH_SHORT).show();
                //返回结果
                setResult(0x002,intent);
                //关闭当前界面
                finish();
            }
        });

    }


}

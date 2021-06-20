package com.example.daoyun09.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.daoyun09.R;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.daoyun09.zxing.encode.CodeCreator.createQRCode;


public class QRCode extends AppCompatActivity {

    @BindView(R.id.qrNumber)
    EditText qrnum;
    @BindView(R.id.qrImage)
    ImageView qrImg;
    @BindView(R.id.qrConfimr)
    Button confirm;

    private String qrNum;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        qrNum = intent.getStringExtra("qrNum");

        Bitmap qrCode = null;
        try {
            qrCode = createQRCode(qrNum);
            qrnum.setText(qrNum);
            qrImg.setImageBitmap(qrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.qrConfimr)
    public void confirm()
    {
        Intent intent = new Intent(QRCode.this,MainActivity.class);
        this.startActivity(intent);
    }

}

package com.uniquestudio.technorange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SellerActivity extends AppCompatActivity {

    ImageButton monitorImgBtn, cpuboxImgBtn, keyboardImgBtn;
    ImageButton mboardImgBtn, mouseImgBtn, cpuImgBtn;
    ImageButton webcamImgBtn, ramImgBtn, gpuImgBtn, routerImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        monitorImgBtn = findViewById(R.id.monitor_imgbtn);
        cpuboxImgBtn = findViewById(R.id.cpubox_imgbtn);
        keyboardImgBtn = findViewById(R.id.keyboard_imgbtn);

        mboardImgBtn = findViewById(R.id.motherboard_imgbtn);
        mouseImgBtn = findViewById(R.id.mouse_imgbtn);
        cpuImgBtn = findViewById(R.id.cpu_imgbtn);

        webcamImgBtn = findViewById(R.id.webcam_imgbtn);
        ramImgBtn = findViewById(R.id.ram_imgbtn);
        gpuImgBtn = findViewById(R.id.gpu_imgbtn);
        routerImgBtn = findViewById(R.id.router_imgbtn);


        monitorImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(SellerActivity.this, AddProductActivity.class);
                i1.putExtra("category", "monitors");
                startActivity(i1);
            }
        });

        cpuboxImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(SellerActivity.this, AddProductActivity.class);
                i2.putExtra("category", "cpu_boxes");
                startActivity(i2);
            }
        });

        keyboardImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(SellerActivity.this, AddProductActivity.class);
                i3.putExtra("category", "keyboards");
                startActivity(i3);
            }
        });

        mboardImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i4 = new Intent(SellerActivity.this, AddProductActivity.class);
                i4.putExtra("category", "motherboards");
                startActivity(i4);
            }
        });

        mouseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(SellerActivity.this, AddProductActivity.class);
                i5.putExtra("category", "mouses");
                startActivity(i5);
            }
        });

        cpuImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i6 = new Intent(SellerActivity.this, AddProductActivity.class);
                i6.putExtra("category", "cpus");
                startActivity(i6);
            }
        });

        webcamImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i7 = new Intent(SellerActivity.this, AddProductActivity.class);
                i7.putExtra("category", "webcams");
                startActivity(i7);
            }
        });

        ramImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i8 = new Intent(SellerActivity.this, AddProductActivity.class);
                i8.putExtra("category", "rams");
                startActivity(i8);
            }
        });

        gpuImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i9 = new Intent(SellerActivity.this, AddProductActivity.class);
                i9.putExtra("category", "gpus");
                startActivity(i9);
            }
        });

        routerImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i10 = new Intent(SellerActivity.this, AddProductActivity.class);
                i10.putExtra("category", "routers");
                startActivity(i10);
            }
        });


    }
}
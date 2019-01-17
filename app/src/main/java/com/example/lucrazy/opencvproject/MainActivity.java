package com.example.lucrazy.opencvproject;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    int requestCode = 1002;
    Intent service;
    Receive receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new Intent(this,BackgroudService.class);
        setPermisson();
        registReceiver();
        findViewById(R.id.begin_tess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(service);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initDebug(true);
    }

    void registReceiver(){
        receive = new Receive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Constant.filter);
        registerReceiver(receive,filter);
    }

    /**
     *  动态申请 需要的权限
     */
    void setPermisson(){
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,permissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,permissions[1]) != PackageManager.PERMISSION_GRANTED
                    ){
                ActivityCompat.requestPermissions(this,permissions,requestCode);
            }else {

            }
        }
    }

    /**
     *  权限回调  获取权限后 再启动服务
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("TAG","授权成功");
            }else {
                Toast.makeText(this, "没有授权，应用无法使用", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.d("TAG","");
                    break;
                default:
                    break;
            }
        }
    };
}

package com.example.lucrazy.opencvproject;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lucrazy.opencvproject.utils.FileUtil;
import com.example.lucrazy.opencvproject.utils.Tesscv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lucrazy on 19-1-16.
 */

public class BackgroudService extends Service {

    Tesscv m_tesscv;
    int count = 0;
    File file;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTesscv();
        new Thread(new ParentRunnable()).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 初始化 图片碳化 和识别操作
     */
    void initTesscv(){
        AssetManager manager = getAssets();
        try {
            InputStream inputStream = manager.open("tessdata/eng.traineddata");
            m_tesscv = new Tesscv(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 识别的主题任务
     * 策略：文件夹下没有文件 阻塞线程 ，文件夹下有文件，但是数量少，直接全部识别，比较多则每次识别20个
     */
    void doWork(){
        file = FileUtil.findFile();  //获取图片保存的目录

            count = 0;
            while (true) {
                if (file.listFiles().length == 0) {  // 如果文件夹下没有图片文件
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    File image = file.listFiles()[0];
                    String imageName = image.getName();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + Constant.path + File.separator + imageName, options);
                    m_tesscv.setM_phone(bitmap);
                    String res = m_tesscv.getOcrOfBitmap();
                    image.delete();
                }
            }


    }
    class  ParentRunnable implements Runnable{
        @Override
        public void run() {
            doWork();
        }
    }


    /**
     * 服务被销毁 发送广播重启服务
     */
    @Override
    public void onDestroy() {
        Intent intent = new Intent(Constant.filter);
        sendBroadcast(intent);
        super.onDestroy();
    }
}

package com.example.lucrazy.opencvproject.utils;

import android.os.Environment;

import com.example.lucrazy.opencvproject.Constant;

import java.io.File;

/**
 * Created by lucrazy on 19-1-16.
 */

public class FileUtil {

    /**
     *查找文件夹，返回文件夹对象
     */
    public static File findFile(){
        File sdDir = null;
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //  判断sdcard是否挂载
            sdDir = Environment.getExternalStorageDirectory();
            String fileDir = sdDir.getAbsolutePath()+Constant.path;
             file = new File(fileDir);
            if (!file.exists()){  // 判断文件是否存在
                file.mkdirs();
            }

        }else {
            return null;
        }
        return file;
    }


}

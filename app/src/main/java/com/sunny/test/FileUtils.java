package com.sunny.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class FileUtils {


    /**
     * 获得应用的根目录
     *
     * @return
     */
    public static String getAppPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + App.AppName + "/");//获取根目录
        } else {
            sdDir = new File(Environment.getDataDirectory() + "/" + App.AppName + "/");
        }
        if (!sdDir.exists()) {
            sdDir.mkdirs();
        }
        return sdDir.toString();
    }

    public static String getWifiDirectPath() {
        String path = getAppPath() + "/WifiDirectFile/";
        File file = new File(path);
        if (!file.exists() && file.mkdir()) {
            return path;
        }
        return path;
    }


    /**
     * 获得SDCard的根目录
     *
     * @return
     */
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return null;
        }
    }


    public static String getPath() {
        String path = getAppPath() + "/header/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            System.out.println("54555");
        }
        return path;
    }


    public static void openFile(final Context context, final String filePath){
        Intent intent = FileUtils.getOpenFileIntent(filePath);
        if (intent != null)
            context.startActivity(intent);
        else
            Toast.makeText(context, "该文件无法打开", Toast.LENGTH_SHORT).show();
    }
    /**
     * 打开一个文件
     *
     * @param filePath
     *            文件的绝对路径
     */
    public static Intent getOpenFileIntent(final String filePath)
    {
        String ext = filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);
        try
        {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String temp = ext.substring(1);
            String mime = mimeTypeMap.getMimeTypeFromExtension(temp);

            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            File file = new File(filePath);
            intent.setDataAndType(Uri.fromFile(file), mime);
            return intent;
        }
        catch (Exception e) {
            Log.e("err", "文件无法打开");
            return null;
        }
    }

}

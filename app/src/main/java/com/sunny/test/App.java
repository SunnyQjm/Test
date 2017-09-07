package com.sunny.test;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public class App extends Application{
    public static final String AppName = "a_Test";
    @Override
    public void onCreate() {
        super.onCreate();
        //file_downloader init
        FileDownloader.setupOnApplicationOnCreate(this);
        com.sunny.test.internet.FileDownloader.bind(this);
    }
}

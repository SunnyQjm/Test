package com.sunny.test.internet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public enum FileDownloader {
    @SuppressLint("StaticFieldLeak")INSTANCE;

    public static FileDownloader getInstance() {
        return INSTANCE;
    }

    private Context context = null;

    public static void bind(Context context) {
        getInstance().context = context;
    }

    public static void unBind(Context context) {
        getInstance().context = null;
    }

    public void download(String url, String fileName) {
        if (context == null)
            return;
        action2Service(url, fileName, FileDownloadService.ACTION_DOWNLOAD);
    }

    public void pause(String url) {
        if (context == null)
            return;
        action2Service(url, "pause", FileDownloadService.ACTION_PAUSE);
    }

    public void continueDownload(String url) {
        if(context == null)
            return;
        action2Service(url, "continue", FileDownloadService.ACTION_CONTINUE);
    }

    private void action2Service(String url, String fileName, String action) {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.setAction(action);
        intent.putExtra(FileDownloadService.PARAM_SAVE_NAME, fileName);
        intent.putExtra(FileDownloadService.PARAM_URL, url);
        context.startService(intent);
    }
}

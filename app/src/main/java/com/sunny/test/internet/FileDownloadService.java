package com.sunny.test.internet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sunny.test.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public class FileDownloadService extends Service {

    public static final String PARAM_URL = "url";
    public static final String PARAM_SAVE_NAME = "save name";

    public static final String ACTION_DOWNLOAD = "download";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_CONTINUE = "continue";

    private String desDir = FileUtils.getAppPath();

    private final Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();
    @Override
    public void onCreate() {
        System.out.println("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        if (intent != null) {
            String url = intent.getStringExtra(PARAM_URL);
            String action = intent.getAction();
            String fileName = intent.getStringExtra(PARAM_SAVE_NAME);
            System.out.println("action: " + intent.getAction());
            download(url, fileName, action);
        } else {
            System.out.println("intent is null");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(String url, String fileName, String action) {
        if (url == null || url.equals("") ||
                fileName == null || fileName.equals(""))
            return;
        switch (action) {
            case ACTION_DOWNLOAD:
                RxDownload.getInstance(this)
                        .download(url, fileName, desDir)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DownloadStatus>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableMap.put(url, d);
                            }

                            @Override
                            public void onNext(DownloadStatus downloadStatus) {
                                EventBus.getDefault()
                                        .post(downloadStatus);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                System.out.println("onCompleted");
                            }
                        });
                break;
            case ACTION_PAUSE:
                System.out.println("action_pause");
                Disposable disposable = disposableMap.get(url);
                //取消订阅, 即可暂停下载
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                } else {
                    System.out.println("disposable is null");
                }
                break;
            case ACTION_CONTINUE:
                RxDownload.getInstance(this)
                        .download(url)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DownloadStatus>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableMap.put(url, d);
                            }

                            @Override
                            public void onNext(DownloadStatus downloadStatus) {
                                EventBus.getDefault()
                                        .post(downloadStatus);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                System.out.println("onCompleted");
                            }
                        });
                break;

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    private void saveFile(ResponseBody responseBody, String destFileDir, String destFileName) {
        InputStream is = null;
        byte[] buf = new byte[24 * 1024];
        int len;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(destFileDir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

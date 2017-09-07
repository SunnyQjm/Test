package com.sunny.test.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sunny.test.R;
import com.sunny.test.internet.FileDownloader;
import com.sunny.test.utils.MyNotifyUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class DownloadManagerTest extends AppCompatActivity {

    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.btn_pause)
    Button btnPause;

    private long downloadId = -1;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager_test);
        ButterKnife.bind(this);
        etUrl.setText("http://110.81.153.179/download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk?mkey=599ccafe9f45749b&f=f24&c=0&p=.apk");
    }

    private void download2() {
        String downloadUrl = etUrl.getText().toString();
        if (downloadUrl.equals(""))
            return;

        FileDownloader.getInstance()
                .download(downloadUrl, "xueban.apk");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgress(DownloadStatus downloadStatus) {
        System.out.println(gson.toJson(downloadStatus));
        if (downloadStatus.getPercentNumber() == 100) {
            System.out.println("显示通知");
            Intent intent = new Intent(this, MainActivity.class);
            MyNotifyUtil.newInstance(this, 1)
                    .setCompatBuilder(new MyNotifyUtil.NormalNotificationBuilder(this)
                            .contentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                            .smallIcon(R.mipmap.ic_launcher)
                            .largerIcon(R.mipmap.ic_launcher)
                            .ticker("文件下载完成")
                            .title("学伴.apk")
                            .autoCancel(true)
                            .content("文件下载完成")
                            .build())
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_download, R.id.btn_pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    new RxPermissions(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean)
                                    download2();
                            });
                    return;
                }
                download2();
                break;
            case R.id.btn_pause:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    new RxPermissions(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean)
                                    pause();
                            });
                    return;
                }
                pause();
                break;
        }
    }

    private void pause() {
        String downloadUrl = etUrl.getText().toString();
        if(downloadUrl.equals(""))
            return;
        FileDownloader.getInstance()
                .pause(downloadUrl);
    }
}

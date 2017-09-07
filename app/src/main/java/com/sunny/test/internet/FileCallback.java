package com.sunny.test.internet;

import com.sunny.test.utils.UUIDUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

public abstract class FileCallback {

    private String destFileDir;
    private String destFileName;
    private final String uuid = UUIDUtil.getUUID();

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeProgress();
    }

    public abstract void progress(long progress, long total, boolean done);

    public abstract void onError(Throwable e);

    public void saveFile(ResponseBody responseBody){
        InputStream is = null;
        byte[] buf = new byte[2 * 1024];
        int len;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            File dir = new File(destFileDir);
            if(!dir.exists())
                dir.mkdirs();
            File file = new File(destFileDir, destFileName);
            fos = new FileOutputStream(file);
            while((len = is.read(buf)) != -1){
                fos.write(buf, 0, len);
            }
            fos.flush();
            unSubscribe();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    if(is != null)
                        is.close();
                    if(fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void subscribeProgress(){
//        MyRxBus.getInstance().subscribe(uuid, fileDownloadEvent -> {
//            System.out.println(fileDownloadEvent);
//            //TODO
//            progress(fileDownloadEvent.getAlready(), fileDownloadEvent.getAlready(), fileDownloadEvent.isDone());
//        }, this::onError);
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
//        MyRxBus.getInstance().unSubscribe(uuid);
    }

}

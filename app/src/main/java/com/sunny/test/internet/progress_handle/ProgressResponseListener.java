package com.sunny.test.internet.progress_handle;

/**
 * Created by Sunny on 2017/6/23 0023.
 */

public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}

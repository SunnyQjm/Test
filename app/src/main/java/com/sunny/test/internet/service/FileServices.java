package com.sunny.test.internet.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public interface FileServices {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}

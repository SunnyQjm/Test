package com.sunny.test.internet.service;


import android.content.Context;

import com.sunny.test.internet.cookie_persisten.CookieJarImpl;
import com.sunny.test.internet.cookie_persisten.PersistentCookieStore;
import com.sunny.test.internet.progress_handle.ProgressResponseBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class APIManager {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static APIManager getInstance() {
        return SingleTon.mInstance;
    }

    private static class SingleTon {
        static APIManager mInstance = new APIManager();
    }

    private static int cacheSize = 20 * 1024 * 1024;
    //    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static final int DEFAULT_TIMEOUT = 15;
    private static OkHttpClient client;
    private FileServices fileServices;


    /**
     * 初始化，在Application类里面的onCreate方法当中初始化
     *
     * @param applicationContext
     */
    public static void init(Context applicationContext) {
        client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new youyunRequestInterceptor())
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(applicationContext)))
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }


    public FileServices getFileServices(Converter.Factory... factory) {
        if (fileServices == null)
            fileServices = createService(FileServices.class, factory);
        return fileServices;
    }

    private <T> T createService(Class<T> serviceClass, Converter.Factory... factory) {
        Retrofit.Builder build = new Retrofit.Builder()
                .baseUrl(ApiInfo.BaseUrl)
                .client(client);
        for (Converter.Factory aFactory : factory) {
            build.addConverterFactory(aFactory);
        }
        Retrofit retrofit = build
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    private static class youyunRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //begin deal request before request

            Response response = chain.proceed(request);
//            //deal response after receive response
//            ResponseBody responseBody = response.body();
//            BufferedSource source = responseBody.source();
//            source.request(Long.MAX_VALUE); // Buffer the entire body.
//            Buffer buffer = source.buffer();
//            Charset charset = UTF8;
//            MediaType contentType = responseBody.contentType();
//            if (contentType != null) {
//                charset = contentType.charset(UTF8);
//            }
//            BaseResponseBody<Object> baseResponseBody = GsonUtil.getInstance().fromJson(buffer.clone().readString(charset),
//                    new TypeToken<BaseResponseBody<Object>>() {
//                    }.getType());

            //对下载文件的Response拦截处理
            if (request.url().toString().startsWith(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD)) {
                System.out.println("返回带下载进度监听的responseBody");
                return response.newBuilder()
                        .body(new ProgressResponseBody(response.body()))
                        .build();
            }
            return response.newBuilder()
                    .body(new ProgressResponseBody(response.body()))
                    .build();
        }
    }
}

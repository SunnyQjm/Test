package com.sunny.test.internet.progress_handle;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Sunny on 2017/6/23 0023.
 */

public class ProgressRequestBody extends RequestBody{
    //实际待包装的请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final ProgressRequestListener progressRequestListener;
    //包装完成的BufferSink
    private BufferedSink bufferedSink;

    /**
     *
     * @param requestBody   待包装的请求体
     * @param progressRequestListener   回调接口
     */
    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressRequestListener) {
        this.requestBody = requestBody;
        this.progressRequestListener = progressRequestListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if(bufferedSink == null)
            bufferedSink = Okio.buffer(sink(sink));
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，防止最后一部分数据不写入
        bufferedSink.flush();

    }

    private Sink sink(BufferedSink sink) {

        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度
            long contentLength = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if(contentLength == 0){
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if(progressRequestListener != null)
                    progressRequestListener.onRequestProgress(bytesWritten, contentLength(), bytesWritten == contentLength);
            }
        };
    }


}

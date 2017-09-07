package com.sunny.test.internet.progress_handle;

import com.sunny.test.internet.FileDownloadEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Sunny on 2017/6/23 0023.
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null)
            bufferedSource = Okio.buffer(source(responseBody.source()));
        return bufferedSource;
    }

    private Source source(BufferedSource source) {
        return new ForwardingSource(source) {
            long currentBytesRead = 0L;
            long bytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                currentBytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                bytesRead += currentBytesRead != -1 ? currentBytesRead : 0;

                EventBus.getDefault().post(new FileDownloadEvent.Builder()
                        .total(contentLength())
                        .already(bytesRead)
                        .done(currentBytesRead == -1)
                        .build());
                return currentBytesRead;
            }
        };
    }
}

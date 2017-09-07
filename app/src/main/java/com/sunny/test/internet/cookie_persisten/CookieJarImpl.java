package com.sunny.test.internet.cookie_persisten;

/**
 * Created by Administrator on 2017/3/10/010.
 */


import android.util.Log;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zhy on 16/3/10.
 */
public class CookieJarImpl implements CookieJar
{
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore)
    {
        if (cookieStore == null) Exceptions.illegalArgument("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        String info = "";
        for (Cookie c : cookies) {
            info += "/n{" + c.name() + " : " + c.value() + "; persistent : " + (c.expiresAt() - System.currentTimeMillis());
        }
        Log.e("saveFromResponse : ", info);
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url)
    {
        List<Cookie> cookies = cookieStore.get(url);
        String info = "";
        for (Cookie c : cookies) {
            info += "/n{" + c.name() + " : " + c.value() + "; persistent : " + (c.expiresAt() - System.currentTimeMillis());
        }
        Log.e("loadForRequest : ", info);
        return cookies;
    }

    public CookieStore getCookieStore()
    {
        return cookieStore;
    }
}

package com.sunny.test.internet.cookie_persisten;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
/**
 * Created by zhy on 15/12/14.
 */
public class Exceptions
{
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}

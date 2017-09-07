package com.sunny.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Sunny on 2017/5/13 0013.
 */

public class BitmapUtils {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 指定大小的缩放
     * @param bitmap
     * @param destWidth
     * @param destHeight
     * @return
     */
    public static Bitmap thumbImageWithInSampleSize(Bitmap bitmap, float destWidth, float destHeight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        byte[] buffer = readBitmap(bitmap);
        BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opt);

        double scaleW = Math.max(destWidth, opt.outWidth) / (Math.min(destWidth, opt.outWidth) * 1.0);
        double scaleH = Math.max(destHeight, opt.outHeight) / (Math.min(destHeight, opt.outHeight) * 1.0);
        opt.inSampleSize = (int) Math.max(scaleW, scaleH);

        opt.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opt);
        return bitmap;
    }

    /**
     * Bitmap --> byte[]
     *
     * @param bmp
     * @return
     */
    private static byte[] readBitmap(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        try
        {
            baos.flush();
            baos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}

package com.jwong.education.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ScreenShotUtils {

    private static final String IMAGE_FILE_NAME_TEMPLATE = "%s.jpg";
    private static final String IMAGE_FILE_PATH_TEMPLATE = "%s/%s";

    /**
     * view转bitmap
     */
    public static String viewConversionBitmap(View v, String fileName) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        //如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h);
        v.draw(c);
        return saveToSD(bmp, 1024 * 1024, fileName);
    }


    /**
     * 存储到sdcard
     */
    public static String saveToSD(Bitmap bmp, int maxSize, String fileName) {
        if (bmp == null) {
            Log.e("ScreenShotUtils", "saveToSD--->bmp is null");
            return "";
        }
        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件名
            String imageDate = FormatUtils.convert2DateTime(Calendar.getInstance().getTime());
            String mFileName = String.format(IMAGE_FILE_NAME_TEMPLATE, fileName + imageDate);
            String mstrRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                    + "/MoreThan";
            File dir = new File(mstrRootPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //文件全名
            String filePath = String.format(IMAGE_FILE_PATH_TEMPLATE, mstrRootPath, mFileName);
            Log.d("ScreenShotUtils", "saveToSD--->file path:" + filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("ScreenShotUtils", "saveToSD--->file AbsolutePath:" + filePath);
            try {
                compressAndGenImage(bmp, filePath, maxSize);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                bmp.recycle();
            }

            return filePath;
        }
        return "";
    }

    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        if (maxSize != 0) {
            while (os.toByteArray().length / 1024 > maxSize) {
                // Clean up os
                os.reset();
                // interval 10
                options -= 10;
                image.compress(Bitmap.CompressFormat.JPEG, options, os);
            }
        }
        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        Log.d("ScreenShotUtils", "compressAndGenImage--->文件大小：" + os.size() + "，压缩比例：" + options);
        fos.flush();
        fos.close();
    }
}

package com.vigorx.effort.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by songlei on 16/7/15.
 */
public class ScreenCatcher {

    public static void saveScreenToPNG(Activity activity, String filePath) {
        try {
            Bitmap bitmap = getScreenImage(activity);
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            if (null != outputStream) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getScreenImage(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        // 标题蓝高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 去掉标题栏
        Rect rect = new Rect();
        activity.getWindowManager().getDefaultDisplay().getRectSize(rect);
        int width = rect.width();
        int height = rect.height();
        Bitmap realBitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();

        return realBitmap;
    }
}

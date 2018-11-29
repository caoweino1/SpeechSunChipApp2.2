package com.turing.turingsdksample.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiongcen on 17/4/28.
 */

public class ReadAssetsUtils {

    /**
     * 获取assets目录下指定文件内�?
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getData(Context context, String fileName) {
        String result = null;
        if (TextUtils.isEmpty(fileName)) {
            return result;
        }
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] b = new byte[size];
            is.read(b);
            result = new String(b, "UTF-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String getdefaultData(Context context, String fileName) {
        String result = null;
        if (TextUtils.isEmpty(fileName)) {
            return result;
        }
        try {
            InputStream is =new FileInputStream(new File(fileName));
            int size = is.available();
            byte[] b = new byte[size];
            is.read(b);
            result = new String(b, "UTF-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从assets目录下复制文件去指定目录
     *
     * @param ctx
     * @param filePath 路径
     * @param filename 文件�?
     * @return
     */
    public static boolean CopyFileByAssets(Context ctx, String filePath, String filename) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(filename)) {
            return false;
        }

        File file = new File(filePath + filename);
        if (file.exists()) {
            return true;
        }

        File applibs = new File(filePath);
        if (!applibs.exists()) {
            applibs.mkdir();
        }
        boolean copyIsFinish = false;
        try {
            InputStream is = ctx.getAssets().open(filename);
            //File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
            file = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }
}

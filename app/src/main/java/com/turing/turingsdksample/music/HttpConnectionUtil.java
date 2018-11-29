package com.turing.turingsdksample.music;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author：licheng@uzoo.com
 */

public class HttpConnectionUtil {

    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "turingsdkmusic" + File.separator;

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr   url路径
     * @param callback 回调
     * @throws IOException
     */
    public static final String TAG = HttpConnectionUtil.class.getSimpleName();
    public static void downLoadFromUrl(String urlStr, MusicHttpDownLoadCallback callback) {
        Log.e(TAG,"downLoadFromUrl url ="+urlStr);
        File file = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 6000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //创建父目录
            File parentfile = new File(BASE_PATH);
            if (!parentfile.exists()) {
                if (parentfile.mkdirs()) {

                } else {
                    System.out.println("创建目录失败");
                }

            }
            //得到文件路径
            String filePathUrl = conn.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
            String path = BASE_PATH + fileFullName;
            file = new File(path);
            if (file.exists()) {
                if (callback != null) {
                    callback.onSuccess(path);
                    return;
                }
            }
            //创建文件
            file.createNewFile();
            //得到网络的，输入流
            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
            //将网络的输入流，开始写入
            OutputStream out = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bin.read(buf)) != -1) {
                if (callback != null) {
                    Log.e(TAG,"onLoading");
                    callback.onLoading();
                }
                out.write(buf, 0, len);
            }
            bin.close();
            out.close();
            if (callback != null) {
                Log.e(TAG,"onSuccess");
                callback.onSuccess(path);
            }
        } catch (Exception e) {
            if (file != null && file.exists()) {
                file.delete();
            }
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}

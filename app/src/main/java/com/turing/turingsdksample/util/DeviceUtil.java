package com.turing.turingsdksample.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Kelvin on 16/6/6.
 */
public class DeviceUtil {

    public static final String OS_VERSION = Build.VERSION.RELEASE;


    /**
     * BASEBAND-VER
     * 鍩哄甫鐗堟湰
     * return String
     */
    public static String getBasebandVer() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CORE-VER
     * 鍐呮牳鐗堟湰
     * return String
     */
    public static String getLinuxCoreVer() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

        String result = "";
        String line;
        // get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * INNER-VER
     * 鍐呴儴鐗堟湰
     * return String
     */
    public static String getInnerVer() {
        String ver;
        if (Build.DISPLAY.contains(Build.VERSION.INCREMENTAL)) {
            ver = Build.DISPLAY;
        } else {
            ver = Build.VERSION.INCREMENTAL;
        }
        return ver;
    }

    /**
     * 杩樺師鍑哄巶璁剧疆
     *
     * @param context
     * @param mEraseSdCard
     */
    public static void doMasterClear(Context context, boolean mEraseSdCard) {
        if (mEraseSdCard) {
            Intent intent = new Intent("com.android.internal.os.storage.FORMAT_AND_FACTORY_RESET");
            intent.putExtra("android.intent.extra.REASON", "MasterClearConfirm");
            ComponentName COMPONENT_NAME = new ComponentName("android", "ExternalStorageFormatter");
            intent.setComponent(COMPONENT_NAME);
            context.startService(intent);
        } else {
            Intent intent = new Intent("android.intent.action.MASTER_CLEAR");
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.putExtra("android.intent.extra.REASON", "MasterClearConfirm");
            context.sendBroadcast(intent);
            // Intent handling is asynchronous -- assume it will happen soon.
        }
    }

    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 鑾峰彇ip鍦板潃
     *
     * @return
     */
    public static String getHostIP() {
        String hostIp = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }

    public static String getRomVersion() {

        return Build.ID;
    }
    public static String getGujianVersion() {

        return Build.VERSION.RELEASE;
    }

    public static String getSdCardMemory(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.toString());
        //姝ゅ杩涜鐗堟湰鐨勫垽鏂洜涓哄湪2.3鐗堟湰涓� getBlockSize()绛夋柟娉曡繕閫傜敤
        //涔嬪悗鐨勬湁浜涚増鏈湁浜嗘柊鐨勬柟娉曡繘琛屾浛浠ｃ��
        long blocksize;
        long totalblock;
        long availbleblocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blocksize = stat.getBlockSizeLong();
            totalblock = stat.getBlockCountLong();
            availbleblocks = stat.getAvailableBlocksLong();
        } else {
            blocksize = stat.getBlockSize();
            totalblock = stat.getBlockCount();
            availbleblocks = stat.getAvailableBlocks();
        }
        return Formatter.formatFileSize(context, availbleblocks * blocksize);
    }

    public static void shutDown(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
            //鍏朵腑false鎹㈡垚true,浼氬脊鍑烘槸鍚﹀叧鏈虹殑纭绐楀彛
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

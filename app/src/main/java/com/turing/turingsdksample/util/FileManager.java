package com.turing.turingsdksample.util;

import android.text.TextUtils;
import android.util.Log;

import com.turing.turingsdksample.bean.MediaListData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kelvin on 2017/5/4.
 */

public class FileManager {

	public static final String FILE_TYPE_SWF = ".mp4";
	public static final String FILE_TYPE_SWF_MKTV = ".mktv";
	public static final String FILE_TYPE_SWF_SWF = ".SWF";
	public static final String FILE_TYPE_jpg = ".jpg";
	public static final String FILE_TYPE_PNG = ".png";
	private static final String TAG = FileManager.class.getSimpleName();


	public static FileManager getInstance() {
		return new FileManager();

		// TODO Auto-generated method stub

	}
	//    private static final String DEFAULT__LAUNCHERPATH = Environment.getExternalStorageDirectory().getPath() + "/";
//	private static final String DEFAULT_PATH = "/mnt/uhost/";
	private static final String DEFAULT_PATH = "/mnt/sd-ext"+ "/" +"launcher" + "/res/";
	public static final String DEFAULT__LAUNCHERPATH= "/mnt/media"+ "/";
	public static final String DEFAULT__LAUNCHERXMLPATH2= "/mnt/media/"+getRomVersion()+"_xml/";
	public static  String DEFAULT__LAUNCHERXMLPATH=getMANUFACTURER().equals("rk312x")?"/system/media/"+getRomVersion()+"_xml/":"/,mnt/media/xml/";;
	public static final String DEFAULT__LAUNCHER_YIMIXMLPATH= "/system/media/xml/";
//	public static final String DEFAULT__LAUNCHERXMLPATH= "/mnt/uhost/xml/";
	private FileManager() {
		Log.d(TAG, "DEFAULT_PATH = " + DEFAULT_PATH);
		File baseFolder = new File(DEFAULT_PATH);
		if (!baseFolder.exists()) {
			baseFolder.mkdirs();
		}
	}
	public static String getMANUFACTURER() {
		return android.os.Build.PRODUCT;
	}
	public String getSDCardResPath() {
		return DEFAULT_PATH;
	}
	public String getLauncherSDCardResPath() {
		return DEFAULT__LAUNCHERPATH;
	}
	/**
	 * 杞寲鏂囦欢瀵硅薄
	 *
	 * @param path     鏂囦欢澶硅矾寰�
	 * @param fileType 鏂囦欢绫诲瀷 swf甯﹀浘鐗�,闈瀞wf
	 * @return
	 */
	public List<MediaListData.ItemsBean> getAudioItems(final String path, final String fileType) {
		List<MediaListData.ItemsBean> list = new ArrayList<MediaListData.ItemsBean>();
		if (!TextUtils.isEmpty(DEFAULT_PATH+path)) {
			File folder = new File(DEFAULT_PATH+path);
			if (folder.exists() && folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					if (file.getName().endsWith(fileType) || file.getName().endsWith(".mkv") || file.getName().endsWith(".avi")) {
						MediaListData.ItemsBean bean = new MediaListData.ItemsBean();
						 String name = null;
							if(file.getName().endsWith(".mkv")){
								name=file.getName().replace(".mkv", "");
							}else{
								name=file.getName().replace(fileType, "");
							}
						bean.setT(name);
						bean.setUrl(file.getAbsolutePath());
						if (TextUtils.equals(FILE_TYPE_SWF, fileType) || TextUtils.equals(FILE_TYPE_SWF_MKTV,".mkv") || TextUtils.equals(FILE_TYPE_SWF_MKTV,".avi")) {
							for (File picFile : files) {
								if (TextUtils.equals(picFile.getName(), name + ".jpg") || TextUtils.equals(picFile.getName(), name + ".JPG") || TextUtils.equals(picFile.getName(), name + ".png") ) {
									bean.setI(picFile.getAbsolutePath());
									break;
								}
							}
						}
						list.add(bean);
					}
				}
			}
		}
		if (!TextUtils.isEmpty(DEFAULT__LAUNCHERPATH+path)) {
			File folder = new File(DEFAULT__LAUNCHERPATH+path);
			if (folder.exists() && folder.isDirectory()) {
				File[] files = folder.listFiles();
				if (folder.exists() && folder.isDirectory()) {
					File[] files1 = folder.listFiles();
					for (File file : files1) {
						
						if (file.getName().endsWith(fileType) || file.getName().endsWith(".mkv") || file.getName().endsWith(".avi")) {
							MediaListData.ItemsBean bean = new MediaListData.ItemsBean();
							 String name = null;
							if(file.getName().endsWith(".mkv")){
								name=file.getName().replace(".mkv", "");
							}else{
								name=file.getName().replace(fileType, "");
							}
							bean.setT(name);
							bean.setUrl(file.getAbsolutePath());
							if (TextUtils.equals(FILE_TYPE_SWF, fileType) || TextUtils.equals(FILE_TYPE_SWF_MKTV,".mkv") || TextUtils.equals(FILE_TYPE_SWF_MKTV,".avi")) {
								for (File picFile : files1) {
									Log.d(TAG, "DEFAULT_PATHFile ="+picFile.getName()+"---"+name);
									if (TextUtils.equals(picFile.getName(), name + ".jpg") || TextUtils.equals(picFile.getName(), name + ".JPG") || TextUtils.equals(picFile.getName(), name + ".png")) {
										Log.d(TAG, "DEFAULT_PATHFile 2 ="+picFile.getAbsolutePath());
										bean.setI(picFile.getAbsolutePath());
										break;
									}
								}
							}
							list.add(bean);
						}
					}
				}
			}
		}
		return list;
	}

	public List<MediaListData.ItemsBean> getAudioItems(final String path) {
		List<MediaListData.ItemsBean> list = new ArrayList<MediaListData.ItemsBean>();
		if (!TextUtils.isEmpty(DEFAULT_PATH+path)) {
			File folder = new File(DEFAULT_PATH+path);
			if (folder.exists() && folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					if (!file.getName().endsWith(FILE_TYPE_PNG) && !file.getName().endsWith(FILE_TYPE_jpg) ) {
						MediaListData.ItemsBean bean = new MediaListData.ItemsBean();
						final String name = file.getName();
						bean.setT(name);
						bean.setUrl(file.getAbsolutePath());
						//						if (TextUtils.equals(file.getName(), name + ".jpg")) {
						File file2=new File(file.getAbsolutePath());
						for (File picFile : files) {
							if (TextUtils.equals(picFile.getName(), name + ".jpg") || TextUtils.equals(picFile.getName(), name + ".JPG") || TextUtils.equals(picFile.getName(), name + ".png") ) {
								bean.setI(picFile.getAbsolutePath());
								break;
							}
						}
						list.add(bean);
					}
				}
			}
		}
		if (!TextUtils.isEmpty(DEFAULT__LAUNCHERPATH+path)) {
			File folder = new File(DEFAULT__LAUNCHERPATH+path);
			if (folder.exists() && folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					if (!file.getName().endsWith(FILE_TYPE_PNG) && !file.getName().endsWith(FILE_TYPE_jpg) ) {
						MediaListData.ItemsBean bean = new MediaListData.ItemsBean();
						final String name = file.getName();
						bean.setT(name);
						bean.setUrl(file.getAbsolutePath());
						
						for (File picFile : files) {
							if (TextUtils.equals(picFile.getName(), name + ".jpg") || TextUtils.equals(picFile.getName(), name + ".JPG") || TextUtils.equals(picFile.getName(), name + ".png")) {
								bean.setI(picFile.getAbsolutePath());
								break;
							}
						}
						list.add(bean);
					}
				}
			}
		}

		return list;
	}
	 public static String getRomVersion() {

	        return android.os.Build.ID;
	    }

}

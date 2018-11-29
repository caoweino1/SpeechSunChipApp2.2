package com.turing.turingsdksample.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.turing.turingsdksample.R;
import com.turing.turingsdksample.fragment.ASRFragment;
import com.turing.turingsdksample.fragment.AllFragment;
import com.turing.turingsdksample.fragment.IOTFragment;
import com.turing.turingsdksample.fragment.MusicFragment;
import com.turing.turingsdksample.fragment.TTSFragment;
import com.turing.turingsdksample.music.TuringMusic;
import com.turing.turingsdksample.util.ActionManager;
import com.turing.turingsdksample.util.AppManager;
import com.turing.turingsdksample.util.CommonUtil;
import com.turing.turingsdksample.util.PermissionHelper;
import com.turing.turingsdksample.util.PermissionInterface;


/**
 * @author：licheng@uzoo.com
 */
public class MainFragmentActivity extends BaseFragmentActivity implements PermissionInterface {
    private AllFragment allFragment;
    private ASRFragment asrFragment;
    private TTSFragment ttsFragment;
    private MusicFragment musicFragment;
    private IOTFragment iotFragment;
    public Handler mHandler;
    public String stop_text="AA0055010505";
    int permissinStatus = 0;
    //speech打断代码加入
    private PermissionHelper mPermissionHelper;



    Boolean resFile = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();

        initFragments();
        showFragments(getString(R.string.personvsrobot), false);

        if (!AppManager.getInstance().isNetworkConnected(MainApplication.context)) {
            CommonUtil.getInstance().startTTS(-1);
            return;
        }
    }
    private void initFragments() {
        Log.i("zhang", "initFragments =111=");
        allFragment = new AllFragment();
    }
    private void showFragments(String tag, boolean needback) {
        if(null !=mFragMgr){
            FragmentTransaction trans = mFragMgr.beginTransaction();
            trans.replace(R.id.base_main, getFragmentByTag(tag), tag);
            trans.commit();
        }
    }
    private Fragment getFragmentByTag(String tag) {
        if (getString(R.string.personvsrobot).equals(tag)) {
            return allFragment;
        }


        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)){
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    protected void getDataAndNotify() {
        Log.i("zhang", "initFragments =5555=");
        allFragment.onStartTTs();
    }
    public int getPermissionsRequestCode() {
        return 1000;
    }

    public String[] getPermissions() {
        //设置该界面所需的全部权限
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO};
    }
    @Override
    public void requestPermissionsSuccess() {
        permissinStatus = 1;
    }
    public void requestPermissionsFail() {
        permissinStatus = -1;
        mPermissionHelper.openAppDetails();
    }
    /**
     * 用于响应3个点击功能
     **/
    @Override
    public void onSelectFragment(String str) {
        showFragments(str, true);
    }

    private long clickTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("cao","onKDown");
//            ConsoleActivity consoleActivity=new ConsoleActivity(MainApplication.context);
//            ConsoleActivity.robotshift(stop_text);
            TuringMusic.getInstance().stopMusic();
            if(MainApplication.mediaPlayer!=null) {
                MainApplication.mediaPlayer.stop();
            }
//            ConsoleActivity.robotshift(stop_text);
            if (System.currentTimeMillis() - clickTime < 2 * 1000) {
                //AsrManager.getInstance().stop();
                MainApplication.isViewshow = false;
                ActionManager.getInstance().sendCmdToMachine(0);
               // MainApplication.wakeUpService.resumeRecord();
                finish();
            } else {
                clickTime = System.currentTimeMillis();
                toast(getString(R.string.resume_exit));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }


}

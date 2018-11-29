package com.turing.turingsdksample.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qdreamer.qvoice.AsrHelper;
import com.qdreamer.qvoice.BfioHelper;
import com.qdreamer.qvoice.NluHelper;
import com.qdreamer.qvoice.QSession;
import com.qdreamer.qvoice.TtsHelper;
import com.qdreamer.utils.StringUtil;
import com.sunchip.AlarmTool.AlarmUtil;
import com.turing.asr.callback.AsrListener;
import com.turing.asr.engine.AsrManager;
import com.turing.asr.function.bean.ASRErrorMessage;
import com.turing.music.MusicManager;
import com.turing.music.OnPlayerStateListener;
import com.turing.music.OnSearchListener;
import com.turing.music.bean.MusicEntity;
import com.turing.semantic.SemanticManager;
import com.turing.semantic.entity.AppAndContactsBean;
import com.turing.semantic.entity.Behavior;
import com.turing.semantic.listener.OnHttpRequestListener;
import com.turing.tts.TTSListener;
import com.turing.tts.TTSManager;
import com.turing.turingsdksample.R;
import com.turing.turingsdksample.ability.Base;
import com.turing.turingsdksample.ability.Msg;
import com.turing.turingsdksample.activity.Blackboard;
import com.turing.turingsdksample.activity.MainApplication;
import com.turing.turingsdksample.adapter.MsgAdapter;
import com.turing.turingsdksample.bean.MainItemBean;
import com.turing.turingsdksample.bean.NLUBean;
import com.turing.turingsdksample.bean.RstDataBean;
import com.turing.turingsdksample.callback.CategoryOperateCallback;
import com.turing.turingsdksample.callback.MusicCallback;
import com.turing.turingsdksample.music.TuringMusic;
import com.turing.turingsdksample.ui.ContentLayout;
import com.turing.turingsdksample.ui.ParseLayout;
import com.turing.turingsdksample.util.ActionManager;
import com.turing.turingsdksample.util.AiricheForceHandle;
import com.turing.turingsdksample.util.DateManager;
import com.turing.turingsdksample.util.DeviceUtil;
import com.turing.turingsdksample.util.FileManager;
import com.turing.turingsdksample.util.JsonUtils;
import com.turing.turingsdksample.util.OSDataTransformUtil;
import com.turing.turingsdksample.util.ReadAssetsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageButton;

import static android.widget.Toast.LENGTH_LONG;

/**
 * @author：licheng@uzoo.com
 */
public class AllFragment extends BaseFragment implements CategoryOperateCallback, MusicCallback {
    String image_path;
    private final String TAG = AllFragment.class.getSimpleName();
    private ContentLayout userLayout, robotLayout;
    private ParseLayout parselayout;
    private ImageView rec_btn;
    private String searchContent;
    private GifImageButton mVolumeView, mImageButton;
    private boolean isStart = false;
    private static boolean isDefault = true;
    private static boolean istts = true;
    /**
     * ASR的结果是否出现异常
     */
    public boolean isASR = false;
    private boolean isTotts = false;
    private List<MainItemBean> mListData;
    private ScreenStatusReceiver screenStatusReceiver;
    private boolean isScreen_Dormancy = false;
    private long clickTime;
    private MediaPlayer mMediaPlayer = null;
    private MediaPlayer m_ambiplayer;
    private Context mContext;
    private List<Msg> msgList = new ArrayList<Msg>();
    private MsgAdapter adapter;
    private RecyclerView recyclerView;
    public MediaPlayer mPlayer2;
    public String front_text = "AA0055040300821098";
    public String back_text = "AA0055040400821099";
    public String left_text = "AA0055040100501064";
    public String right_text = "AA0055040200501065";
    public String stop_text = "AA0055010505";
    public String dance_one_text = "AA0055010606";
    public String dance_two_text = "AA0055010707";
    private LinearLayout back_btn;
    public boolean isDance = false;
    //奇梦者唤醒打断加入
    private final String QM_TAG = "qtk";
    private String mResBfio;// bfio参数
    private String mPath;
    private BfioHelper bfio;// 算法
    private AsrHelper asr;// 识别
    private TtsHelper tts;// 合成
    private NluHelper nlu;// 语义

    private String mResVad;
    private String mResCldAsr;
    private String mCfgPath;
    private FileOutputStream mWavFile;
    private TextView mTvShow;
    private BufferedWriter bufferedWriter;
    private long initSession;
    private QSession ss;
    private int wakeTimes = 0;
    boolean isHandle = false;
    private long date_alarm;
    private GifImageButton gifImageButton;
    public String string;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tl, null);
        mContext = view.getContext();
        //mVolumeView = (GifImageButton) view.findViewById(R.id.id_volume_gif);
        //用户的userId
        gifImageButton = (GifImageButton) view.findViewById(R.id.gif_iamgeview);
//        rec_btn = (ImageView) view.findViewById(R.id.rec_btn);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        //创建默认的线性LayoutManager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new MsgAdapter (msgList);
//        recyclerView.setAdapter(adapter);
//        back_btn= (LinearLayout) view.findViewById(R.id.back_btn);
//        back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.exit(0);
//            }
//        });
//        rec_btn.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("cao","触摸");
//                turing_start();
//                if (MotionEvent.ACTION_DOWN == event.getAction()) {
////                    if (null != mVolumeView) {
////                        mVolumeView.setVisibility(View.VISIBLE);
////                    }
//                    Log.i("zhang", "onStart"+(System.currentTimeMillis() - clickTime)+"--"+2 * 1000);
//                    if (System.currentTimeMillis() - clickTime > 3 * 1000) {
////                            finish();
//                        onPress();
////                            toast(getString(R.string.resume_exit));
//                    }
//                    clickTime = System.currentTimeMillis();
////					TuringClientASRManager.getInstance().startRecord(asrListener);
//                } else if (MotionEvent.ACTION_UP == event.getAction()) {
////                    if (null != mVolumeView) {
////                        mVolumeView.setVisibility(View.GONE);
////                    }
//                    onRelease();
//                }
//                return true;
//            }
//        });
        screenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        // 注册
        getActivity().registerReceiver(screenStatusReceiver, screenStatusIF);
        Blackboard.getInstance().allFragment = AllFragment.this;
        Blackboard.getInstance().asrListener = asrListener;
        return view;
    }


    public void onStartTTs() {
        hander.sendEmptyMessage(2);
    }

    private void initData() {
        ss = new QSession(getActivity().getApplicationContext());
        mPath = getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/";
        mResBfio = "role=bfio;cfg=" + mPath + "qvoice/bfio1.cx.xcnh.0519.bin;use_oneMic=0;";
        mCfgPath = mPath + "qvoice/audio.cfg";
        mResVad = "role=vad;cfg=" + mPath + "qvoice/vad.2.0.android.bin;use_bin=1;left_margin=15;right_margin=15;";
        mResCldAsr = "role=asr;cfg=" + mPath + "qvoice/xasr/cfg;use_bin=0;use_json=1;min_conf=1.9;";
        Log.e("cao", " mCfgPath" + mCfgPath);
        init();
        boolean start_bfio = bfio.start(true);
        //Log.d(TAG, "start_bfio:" + start_bfio);
        createWavFile();
    }

    //奇梦者唤醒代码加入
     /* 创建保存音频文件 */
    public void createWavFile() {
        File file = new File("/sdcard/qvoice/");
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        try {
            mWavFile = new FileOutputStream("/sdcard/qvoice/aec.pcm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void init() {
        /*初始化全局回话*/
        initSession = ss.initSession("8b1aedb2-51bf-11e8-b526-00163e13c8a2", "ba0ba62d-4b70-332e-849b-4420f7dd4c1c");
        Log.d(TAG, "session" + initSession);

		/*设置全局回话错误码回调*/
        ss.setQSessionCallback(new QSession.QSessionCallBack() {

            @Override
            public void errorCode(String errorCode) {

                Log.d(TAG, "error coed:" + errorCode);

            }
        });
        /* 获取阵列算法引擎实例 */

        bfio = BfioHelper.getInstance(getActivity().getApplicationContext());
        /* 获取识别引擎实例 */
        asr = AsrHelper.getInstance(getActivity().getApplicationContext());
        //获取合成引擎实例ss
        tts = TtsHelper.getInstance(getActivity().getApplicationContext());
        //获取语义引擎实例
        nlu = NluHelper.getInstance(getActivity().getApplicationContext());
        //复制资
        bfio.copyRes(R.raw.qvoice);
        tts.init(initSession, tts.TYPE_TTS_CLOUND);
        asr.init(initSession, mCfgPath, mResVad, mResCldAsr);
        nlu.init(initSession, nlu.TYPE_NLU_CLOUND);
        bfio.init(initSession, mResBfio);
        /* 设置回调函数 */
        bfio.setListener(listener);
        //nlu.setListener(nluListener);
    }

    public void wake() {
        hander.sendEmptyMessage(5);
    }

    void processNlu(String result) {
        Gson gson = new Gson();
        NLUBean bean = gson.fromJson(result, NLUBean.class);
        String input = bean.getInput();
        String output = bean.getOutput();
        String fld = bean.getFld();
        Log.d("cao", "领域：" + fld + "\n" + "input：" + input + "\n" + "output：" + output + "\n");
        // mTvShow.setText("领域：" + fld + "\n" + "input：" + input + "\n" + "output：" + output + "\n");
        tts.stop();
        tts.setVoice(2.0, 0.9, 1.0);
        tts.start(output);
        // tv.setText(output);
    }

    // 处理识别结果
    private void processAsr(String json) {
        Gson gson = new Gson();
        RstDataBean bean = gson.fromJson(json, RstDataBean.class);
        String rec = StringUtil.removeSpace(bean.getRec());
        nlu.stop();
        nlu.start(rec);
        Log.d(TAG, "识别结果==：" + rec);
    }

    BfioHelper.QdreamerBfioListener listener = new BfioHelper.QdreamerBfioListener() {

        @Override
        public void wakeUP() {
            ++wakeTimes;
            Log.d("cao", "===wakeUP:" + wakeTimes);
            tts.stop();
            tts.setVoice(2.0, 0.9, 1.0);
            tts.start("我在,有何指导");
            turing_start();
            //hander.sendEmptyMessageDelayed(4,500);
            //hander.sendEmptyMessage(1,1000);
            hander.sendEmptyMessageDelayed(1, 2000);
            //mTvShow.setText("成功唤醒次数："+wakeTimes);
        }

        @Override
        public void onSpeechStart() {
            // Log.d(TAG, "onSpeechStart=======");
        }

        @Override
        public void onSpeechEnd() {
            // Log.d(TAG, "onSpeechEnd==========");
        }

        @Override
        public void getVolume(int volume) {
            // Log.d(TAG, "volume:" + volume);
        }

        @Override
        public void getData(byte[] data) {
            // Log.d(TAG, " bfio data:" + data.length);
            // saveWavData(data);
        }

        @Override
        public void cancelAsr() {
            // 取消识别唤醒词
            Log.d(TAG, "cancelEngine:");

        }

        @Override
        public void direction(String direction) {
            // TODO Auto-generated method stub
            //声源定位角度，单麦没有
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.i("zhang", "onStart");
//        hander.sendEmptyMessageAtTime(1,50000);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("zhang", "onResume");
    }

    @SuppressLint("WrongConstant")
    private void onPress() {
        try {
            if (!isConnectIsNomarl()) {
                MainApplication.wakeUpService.pauseRecord();
                Toast.makeText(getActivity(), "没有可用网络", LENGTH_LONG).show();
                return;
            }
            if (MusicManager.getInstance().isPlaying()) {
                MusicManager.getInstance().stop();
                TuringMusic.getInstance().stopMusic();
            }
            if (TTSManager.getInstance().isSpeaking()) {
                TTSManager.getInstance().stopTTS();
                TuringMusic.getInstance().stopMusic();
            }
            if (null != mMediaPlayer)
                mMediaPlayer.stop();
            AsrManager.getInstance().setOptionVAD(2000, 300);
            rec_btn.setScaleX(1.1f);
            rec_btn.setScaleY(1.1f);
            Log.i("cao", "onPress()");
            Base.getInstance().getAll(asrListener, httpClientListener, itsCallback);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "网络不好请重试", LENGTH_LONG).show();
        }
    }

    private void onStartPress() {
        AsrManager.getInstance().setOptionVAD(2000, 300);
        Log.i("cao", "onStartPress()");
        //聊天的时候打开录音机
        Base.getInstance().getAll(asrListener, httpClientListener, itsCallback);
    }

    @Override
    public void onDestroy() {
        if (null != screenStatusReceiver) {
            getActivity().unregisterReceiver(screenStatusReceiver);
        }
        super.onDestroy();
    }

    private void onRelease() {
        rec_btn.setScaleX(1f);
        rec_btn.setScaleY(1f);
        Log.i("zhang", "onRelease");
    }

    /**
     * 这个handler就是UI线程里面工作的线程
     */
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("zhang", msg + " Thread id ==" + Thread.currentThread().getId() + "    Thread naem =" + Thread.currentThread().getName());
            super.handleMessage(msg);
            if (!MainApplication.isViewshow) {
                Log.d("airiche", "hander not handle, msg.what =  " + msg.what);
                return;
            }
            switch (msg.what) {
                case 1:
                    //AsrManager.getInstance().startAsr(asrListener);
                    Log.e("cao", "handleMessage1");
                    if (!isConnectIsNomarl()) {
//                        TTSManager.getInstance().startTTS("网络断开请链接网络", itswlCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                        hander.sendEmptyMessageDelayed(1, 10000);

                        return;
                    }
                    if (!isScreen_Dormancy) {
                        setStartASRLayout();
                        Log.e("cao", "isScreen_Dormancy");
                        onStartPress();
                    }
                    break;
                case 2:
                    Log.e("cao", " TTSManager");
                    try {
                        TTSManager.getInstance().startTTS("小朋友你好啊！", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addLauncherIamger();
                    /**不需要重复上传词典*/
                    if (!MainApplication.updataDictionarySuccess) {
                        appADD();
                    }
                    break;
                case 3:
                    setplayer(MediaPlayer.create(mContext, R.raw.sent));
                    break;
                case 4:
                    turing_start();
                    break;
                case 5:
                    turing_start();
                    ActionManager.getInstance().sendCmdToMachine(0);
                    hander.sendEmptyMessageDelayed(1, 1000);
                    break;
                case 6:
                    Log.e("cao", "hander 6");
                    Intent intent = new Intent();
                    intent.setAction("com.sunchip.alarm");
                    AlarmUtil.setAlarmTime(getActivity(), date_alarm, intent);
                    break;
                case 7:
                    DeviceUtil.shutDown(getActivity());
                    break;
                case 100:
                    Log.i("zhang", "当前网速：" + msg.obj.toString());
                    break;
                case 21: //发送数据
//                    String out= (String) msg.obj;
//                    Log.i("cao","out=="+out);
//                    Msg outmsg=new Msg(out,Msg.TYPE_SENT);
//                    msgList.add(outmsg);
//                    adapter.notifyItemInserted(msgList.size()-1);
//                    //将RecyclerView定位到最后一行
//                    recyclerView.scrollToPosition(msgList.size() - 1);
                    break;
                case 22: //接受数据
//                    String inpt= (String) msg.obj;
//                    Log.i("cao","inptmsg"+inpt);
//                    Msg inptmsg=new Msg(inpt,Msg.TYPE_RECEIVED);
//                    msgList.add(inptmsg);
//                    adapter.notifyItemInserted(msgList.size()-1);
//                    //将RecyclerView定位到最后一行
//                    recyclerView.scrollToPosition(msgList.size() - 1);
                    break;
                default:
                    break;
            }
        }
    };

    private void addLauncherIamger() {
        File file = new File(FileManager.DEFAULT__LAUNCHERXMLPATH);
        if (file.exists()) {
            String data = ReadAssetsUtils.getdefaultData(getActivity(), FileManager.DEFAULT__LAUNCHERXMLPATH + "/defaults.json");
            mListData = JsonUtils.fromJson(data, new TypeToken<List<MainItemBean>>() {
            });
            isDefault = true;
        } else {
//                String data = ReadAssetsUtils.getData(MainActivity.this, "defaults.json");
//                mListData= JsonUtils.fromJson(data, new TypeToken<List<MainItemBean>>() {});
            isDefault = false;
        }
    }

    /**
     * 用户自己的
     **/

    private void operateUserCallback(final String str) {
        MainApplication.workHandler.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    /**
     * http返回的，请求语义和信源结果
     **/
    private synchronized void operateHttpCallback(final String str) {
        /**
         *是否其他处理
         */
        if (AiricheForceHandle.getInstance().isSematicDispose(str)) {
            return;
        }

        MainApplication.workHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //功能
                    if (!MainApplication.isViewshow) {
                        Log.d("airiche", "operateHttpCallback not handle");
                        return;
                    }
                    Log.e("airiche ", "ars semantics + data = " + str);
                    Behavior.ResponseResult result = OSDataTransformUtil.getResultBean(str);
                    string = result.getValues().getText();
                    Behavior.IntentInfo osIntentBean = OSDataTransformUtil.getIntent(str);
                    Log.i("osIntentBean", "IntentInfo==" + osIntentBean.toString());

                    Behavior behavior = OSDataTransformUtil.getBehavior(str);
                    if (osIntentBean != null) {
                        int robot_code = osIntentBean.getCode();
                        checkMusic(osIntentBean, behavior);
                    }
                } catch (Exception e) {
                    hander.sendEmptyMessageDelayed(1, 500);
                }
                //隐藏状态
                setStatusLayout();
                //清空输入
            }
        });
    }

    public static String getRomVersion() {

        return android.os.Build.ID;
    }

    /**
     * 语义结果通知
     */
    private OnHttpRequestListener httpClientListener = new OnHttpRequestListener() {

        @Override
        public void onSuccess(String s) {
            qi_start();
//            Logger.d(Logger.TAG, "httpClientListener>>" + s);
            Log.i("cao", "OnHttpRequestListener =22=" + isASR + "--" + s);
            if (!isASR) {
                operateHttpCallback(s);
            } else {
                Log.e("cao", "httpClientListener");
                hander.sendEmptyMessageDelayed(1, 500);
            }
        }

        @Override
        public void onError(int i, String s) {
            onErrorCallback(s);
            Log.i("zhang", "onCancel onError11==" + s);
            hander.sendEmptyMessageDelayed(1, 500);
        }

        @Override
        public void onCancel() {
            Log.i("zhang", "onCancel 11==");

        }
    };

    public void qi_start() {
        Log.e("cao", "qi_start()");
        //isHandle=false;
        Blackboard.getInstance().isAsrWork = false;
        // MainApplication.wakeUpService.resumeRecord();

    }

    public void turing_start() {
        Log.e("cao", " turing_start()");
        if (!MainApplication.isViewshow) {
            Log.d("airiche", "turing_start fail,startAsr not ");
            return;
        }
        //停止唤醒
        //MainApplication.wakeUpService.pauseRecord();
        //AsrManager.getInstance().startAsr(asrListener);
        Blackboard.getInstance().isAsrWork = true;
    }

    public void sendText(String text) {
        Message message = new Message();
        message.obj = text;
        message.what = 21;
        hander.sendMessage(message);
    }

    public void answerText(String text) {
        Message message = new Message();
        message.obj = text;
        message.what = 22;
        hander.sendMessage(message);
    }

    private AsrListener asrListener = new AsrListener() {
        @Override
        public void onResults(List<String> list) {
            if (!MainApplication.isViewshow) {
                Log.d("airiche", "AsrListener not handle onResults");
                return;
            }
            if (list != null && list.size() > 0) {
                // 这里获取文字结果
                operateUserCallback(list.get(0));
                Log.i("cao", "OnHttpRequestListener 11==" + list.get(0) + "---" + list.get(0).length() + "--" + list.get(0).equals(""));
                if (list == null || list.get(0).length() == 1 || list.get(0).equals("") || list.get(0) == null) {
                    hander.sendEmptyMessageDelayed(1, 500);
                    isASR = true;
                    Blackboard.getInstance().isAsrWork = true;
                } else {
                    /*检查网络*/
                    if (Blackboard.getInstance().isIgnoreResult) {
                        Log.e("airiche", "result is Ignore");
                        return;
                    }
                    Log.e("cao", "发送数据");
                    hander.sendEmptyMessage(3);
                    Message message = new Message();
                    message.obj = list.get(0);
                    message.what = 21;
                    hander.sendMessage(message);
                    isASR = false;
                    Blackboard.getInstance().isAsrWork = false;
                    Base.getInstance().doPost(list.get(0));
                }
            }
        }

        @Override
        public void onStartRecord() {
            if (!MainApplication.isViewshow) {
                Log.d("airiche", "AsrListener not handle onStartRecord");
                return;
            }
            MainApplication.workHandler.post(new Runnable() {
                @Override
                public void run() {
                    //显示
                    Log.i("cao", "turing_onStartRecord()");
                    setStartASRLayout();
                }
            });
        }


        @Override
        public void onEndOfRecord() {
            Log.i("cao", "onEndOfRecord onError 1");
        }

        @Override
        public void onError(ASRErrorMessage errorMessage) {
            Log.i("cao", "asr onError ==" + errorMessage.getMessage());
//            new NetWorkSpeedUtils(mContext,hander).startShowNetSpeed();
            //Toast.makeText(getActivity(), "网速不好请检查网络", Toast.LENGTH_LONG).show();
            hander.sendEmptyMessageDelayed(1, 500);
        }

        @Override
        public void onVolumeChange(int i) {
        }
    };
    private TTSListener itsCallback = new TTSListener() {

        @Override
        public void onSpeakBegin(String s) {
            qi_start();
            Log.e("cao", "onSpeakBegin()");
            Log.i("zhang", "onSpeakBegin 111" + s);
            if (isASR || null == s) {
                hander.sendEmptyMessageDelayed(1, 500);
            } else if (s.equals("哎呀，没听清你在说什么，请再说一遍") || s.equals("null这个单词的意思是：“空”。 ")) {
                isTotts = true;
            } else {
                Log.e("cao", "接收数据");
                Log.d("airiche", "tts onSpeakBegin");
                Message message = new Message();
                message.obj = s;
                message.what = 22;
                hander.sendMessage(message);
                isTotts = false;
            }
        }

        @Override
        public void onSpeakPaused() {
            Log.i("cao", "onSpeakBegin 22");
        }

        @Override
        public void onSpeakResumed() {


            Log.i("cao", "onSpeakBegin 33");
        }

        @Override
        public void onSpeakCompleted() {
            Log.e("cao", "onSpeakCompleted()");
            Log.i("cao", "onSpeakCompleted() 44" + isTotts);
            turing_start();
            if (!isTotts) {
                Log.i("cao", "hander.send==1");
                hander.sendEmptyMessageDelayed(1, 500);
            }
        }

        @Override
        public void onSpeakFailed() {
            Log.i("cao", "onSpeakBegin 55");
        }

    };
    private TTSListener itswlCallback = new TTSListener() {

        @Override
        public void onSpeakBegin(String s) {
            Log.e("cao", "itswlCallback_onSpeakBegin");

        }

        @Override
        public void onSpeakPaused() {
            Log.i("cao", "onSpeakBegin 22");
        }

        @Override
        public void onSpeakResumed() {
            Log.i("cao", "onSpeakBegin 33");
        }

        @Override
        public void onSpeakCompleted() {
            Log.e("cao", "itswlCallback_onSpeakCompleted");

        }

        @Override
        public void onSpeakFailed() {
            Log.i("zhang", "onSpeakBegin 55");
        }

    };

    @Override
    public String getFragmentTag() {
        return getString(R.string.personvsrobot);
    }

    @Override
    public void operateAll() {
//        if (!isASRing()) {
//            if (rel_status.getVisibility() == View.GONE) {
//                Base.getInstance().getAll(asrListener, httpClientListener, itsCallback);
//            }
//        } else {
//            AsrManager.getInstance().stop();
//        }
    }

    @Override
    public void operateTextForTTS(String str) {
        userLayout.setAdapter();
        SemanticManager.getInstance().requestSemantic(str, httpClientListener);
    }

    @Override
    public void operateOnlyASR() {
        Log.i("zhang", "operateOnlyASR");
    }

    @Override
    public void operateOnlyTTS(String str) {
        Log.i("zhang", "operateOnlyTTS");
    }

    /**
     * 状态
     **/
    private void setStatusView(int flag) {
        Log.i("zhang", "setEndASRLayout");
//        rel_status.setVisibility(flag);
    }

    /**
     * 当开始ASR时
     **/
    private void setStartASRLayout() {
        TuringMusic.getInstance().stopMusic();
        // TTSManager.getInstance().stopTTS();
        if (null != mMediaPlayer)
            mMediaPlayer.stop();
    }


    /**
     * 当解析完成后，改变状态
     **/
    private void setStatusLayout() {
        setStatusView(View.GONE);
    }

    /**
     * 判断当前是不是asring
     **/
    private boolean isASRing() {
        return AsrManager.getInstance().isRecording();
    }

    /**
     * 当解析出错时
     */
    private void onErrorCallback(final String str) {
        MainApplication.workHandler.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 开始播放音乐
     **/
    @Override
    public void startMusic(String path) {
        Log.e("cao", "media url=" + mediaUrl);
//        setCircleDialogMessage(getActivity(), R.string.http_ing_resource);
        try {
            if (null == mMediaPlayer) {
                mMediaPlayer = new MediaPlayer();
                MainApplication.mediaPlayer1 = mMediaPlayer;
            }
            mMediaPlayer.reset();// 重置
            mMediaPlayer.setDataSource(path);// 设置数据源
            mMediaPlayer.prepare();
            // 准备
            mMediaPlayer.start();// 开始播放
            mMediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            TTSManager.getInstance().startTTS("播放完成", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                        }

                    });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    TTSManager.getInstance().startTTS("播放出错", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                    return true;
                }

            });
        } catch (Exception ex) {
            TTSManager.getInstance().startTTS("播放音乐异常", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);

//            this.ShowDialog("播放音乐异常:" + ex.getMessage());
        }


    }

    private void checkMusic(final Behavior.IntentInfo bean, final Behavior behavior) {
        Log.e("cao", "check Music==" + bean.getCode());
        if (bean.getCode() == 200101) {
            JsonObject parameters = bean.getParameters();
            Log.e("cao", " judge parameters before");
            if (parameters == null) {
                Log.e("cao", "parameters is null");
                return;
            }
            try {
                Log.e("cao", " json object1 =" + parameters.toString());
                JSONObject jsonObject = new JSONObject(parameters.toString());
                String url = "";
                String song = "";
                String singer = "";
                String exception = "";
                Log.e("cao", "bean.getOperateState()==" + bean.getOperateState());
                Log.e("cao", " json object2" + jsonObject.getString("song"));
                try {
                    if (jsonObject.getString("song") != null) {
                        song = jsonObject.getString("song");
                        singer = jsonObject.getString("singer");
                        url = jsonObject.getString("url");
                    }
                } catch (JSONException e) {
                    Log.e("cao", "JSONException parse exception");
                }
                Log.e("cao", "url==" + url);
                final String finalMediaUrl = url;
                //如果暂时没有对应的资源,播报暂无此资源
                if (url.isEmpty()) {
                    Log.e("cao", "No Source");
                    TTSManager.getInstance().startTTS("暂时没有此资源", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                }
                Log.e("cao", "refresh ui before=" + bean.getOperateState() + " and  " + url + "---" + singer + song);
                if (bean.getOperateState() != 1003 && bean.getOperateState() != 1001) {//不是暂停与停止操作

                    if (!TextUtils.isEmpty(url)) {
                        MainApplication.workHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("cao", "bean.getOperateState()==" + bean.getOperateState());
                                startMusic(finalMediaUrl);
                            }
                        });
                    } else {
                        try {
                            Log.i("zhang", "MusicManager==  正在请求");
                            if (song.equals("小苹果")) {
                                singer = "筷子兄弟";
                            }
                            MusicManager.getInstance().search(singer + song, 1, 20, onSearchListener);
                        } catch (Exception e) {
                            TTSManager.getInstance().startTTS("播放出错", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                        }
                    }
                } else {
                    TTSManager.getInstance().startTTS("暂无此歌曲", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                }
            } catch (JSONException e) {
                Log.e(TAG, " parse exception");
                e.printStackTrace();
            }

        } else if (bean.getCode() == 200201) {
            JsonObject parameters = bean.getParameters();
            Log.e(TAG, " judge parameters before");
            if (parameters == null) {
                Log.e(TAG, "parameters is null");
                return;
            }
            JSONObject jsonObject = null;
            try {
                Log.e(TAG, " json object1 =" + parameters.toString());
                jsonObject = new JSONObject(parameters.toString());
                String mediaUrl = "";
                String text = "";
                Log.e(TAG, " json object2");
                if (jsonObject.getString("url") != null) {
                    mediaUrl = jsonObject.getString("url");
                }
                Log.e(TAG, " judge mediaUrl before");
                if (TextUtils.isEmpty(mediaUrl)) {
                    Log.e(TAG, "url为空");
                    return;
                }
                if (bean.getOperateState() == 1003 || bean.getOperateState() == 1001) {
                    Log.e(TAG, "暂停或者停止操作");
                    TuringMusic.getInstance().stopMusic();
                    return;
                }
                Log.e(TAG, "refresh ui before");
                final String finalMediaUrl = mediaUrl;
                MainApplication.workHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        robotLayout.setMusicSelectable(true);
                        startMusic(finalMediaUrl);
                    }
                });
            } catch (JSONException e) {
                try {
                    if (jsonObject.getString("text_spare") != null) {
                        String text = jsonObject.getString("text_spare");
                        TTSManager.getInstance().startTTS("您说的话实在太高深了，我还在回味呢。", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
                    }

                } catch (JSONException e1) {

                }
                e.printStackTrace();
            }
        } else if (bean.getCode() == 200301) {
            JsonObject parameters = bean.getParameters();
            Log.e(TAG, " judge parameters before");
            if (parameters == null) {
                Log.e(TAG, "parameters is null");
                return;
            }
            try {
                Log.e(TAG, " json object1 =" + parameters.toString());
                JSONObject jsonObject = new JSONObject(parameters.toString());
                String mediaUrl = "";
                Log.e(TAG, " json object2");
                if (jsonObject.getString("resources") != null) {
                    JSONObject jsonObject2 = new JSONObject(jsonObject.getString("resources"));
                    mediaUrl = jsonObject2.getString("url");
                }
                Log.e(TAG, " judge mediaUrl before");
                if (TextUtils.isEmpty(mediaUrl)) {
                    Log.e(TAG, "url为空");
                    return;
                }
                Log.e(TAG, "refresh ui before");
                final String finalMediaUrl = mediaUrl;
                MainApplication.workHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        robotLayout.setMusicSelectable(true);
                        startMusic(finalMediaUrl);
                    }
                });

            } catch (JSONException e) {
                Log.e(TAG, " parse exception");
                e.printStackTrace();
            }
        } else if (bean.getCode() == 201601) {
            //在线打开应用,暂时不用
//            JsonObject parameters = bean.getParameters();
//            Log.e(TAG, " judge parameters before");
//            if (parameters == null) {
//                Log.e(TAG, "parameters is null");
//                return;
//            }
//            JSONObject jsonObject = null;
//            String app_name = "";
//            String app_package = "";
//            try {+
//                jsonObject = new JSONObject(parameters.toString());
//                if (jsonObject.getString("app_package") != null) {
//                    app_package = (String) jsonObject.getString("app_package");
//                    app_name = (String) jsonObject.getString("app_name");
//                }
//            } catch (JSONException e) {
//                Log.e("zhang", "onMusicPrepared: ==233=");
//                return;
////                        TTSManager.getInstance().stopTTS();
////                      TTSManager.getInstance().startTTS("打开失败 你可能没有安装APK", itsCallback);
//            }
//            if (app_name.equals("语言启蒙") || app_name.equals("数学逻辑") || app_name.equals("故事王国") || app_name.equals("传统国学")
//                    || app_name.equals("自然科学") || app_name.equals("安全知识") || app_name.equals("多元智能") || app_name.equals("儿歌天地") || app_name.equals("视觉空间")
//                    || app_name.equals("习惯养成")) {
//                try {
//                    Intent intent = new Intent();
//                    ComponentName cn = new ComponentName("com.wyt.xq.flash", "com.wyt.xq.flash.MainActivity");
//                    intent.setComponent(cn);
//                    intent.putExtra("type", app_package);
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    Log.e("zhang", "onMusicPrepared: ==11=");
//                }
////                            TuringClientTtsManager.getInstance().start("打开失败 你可能没有安装APK", itsCallback);
////                        TuringMusic.getInstance().stopMusic();
////                        TTSManager.getInstance().stopTTS();
////                             istts=false;
////                            TTSManager.getInstance().startTTS("打开失败 你可能没有安装APK", itsCallback);
//                hander.hasMessages(1);
//                istts = false;
//                Toast.makeText(getActivity(), "打开失败 你可能没有安装APK", Toast.LENGTH_SHORT).show();
//            } else if (app_name.equals("快乐英语") || app_name.equals("动漫乐园") || app_name.equals("十万个为什么") || app_name.equals("艺术培养")
//                    || app_name.equals("本地K歌") || app_package.equals("com.kg")) {
//                if (app_package.equals("com.kg")) {
//                    app_name = "本地K歌";
//                }
//                try {
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.CommonGridViewFragment"));
//                    intent.putExtra("audio_type", 1);
//                    intent.putExtra("audio_path", app_name);
//                    if (isDefault) {
//                        intent.putExtra("bj", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(1).getIcon());
//                        intent.putExtra("isDefault", isDefault);
//                        intent.putExtra("gridViewleft", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewleft());
//                        intent.putExtra("gridViewright", FileManager.DEFAULT__LAUNCHERXMLPATH + mListData.get(0).getGridViewright());
//                    } else {
//                        intent.putExtra("bj", "launcher_bg");
//                        intent.putExtra("isDefault", isDefault);
//                        intent.putExtra("gridViewleft", "ic_dialog_left_arrow");
//                        intent.putExtra("gridViewright", "ic_dialog_right_arrow");
//                    }
//                    startActivity(intent);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//                hander.hasMessages(1);
//                istts = false;
//                Toast.makeText(getActivity(), "打开失败 你可能没有安装APK", Toast.LENGTH_SHORT).show();
//            } else if (app_name.equals("爱奇艺少儿") || app_name.equals("在线卡拉ok") || app_name.equals("在线卡拉OK") || app_name.equals("在线乐园")
//                    || app_name.equals("微信对讲") || app_name.equals("智能问答") || app_name.equals("小学") || app_name.equals("设置") || app_name.equals("wifi设置") || app_name.equals("清理内存")) {
//                try {
//                    Intent intent = new Intent();
//                    if (app_name.equals("清理内存")) {
//                        ComponentName cn = new ComponentName("com.sunchip.luqiyalauncher", "com.example.android_launcher_lqy.Ui.CleanMemoryActivity");
//                        intent.setComponent(cn);
//                    } else {
//                        intent = getActivity().getPackageManager().getLaunchIntentForPackage(
//                                app_package);
//                    }
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                hander.hasMessages(1);
//                istts = false;
//                Toast.makeText(getActivity(), "打开失败 你可能没有安装APK", Toast.LENGTH_SHORT).show();
//            }
        } else if (bean.getCode() == 900110) {
            AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            int thisVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.e("zhang", "onMusicPrepared: ===" + thisVolume);
            int maxVolume = 40;
            if (bean.getOperateState() == 1010) {
                if (maxVolume > thisVolume) {
                    thisVolume += 5;
                    //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, thisVolume, 0);
//                                TuringVolumeManager.getInstance().setVolume(MainActivity.this,TuringVolumeManager.STREAMTYPE[4],thisVolume);
                }

            } else if (bean.getOperateState() == 1011) {
                if (thisVolume != 0) {
                    thisVolume -= 5;
                    //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, thisVolume, 0);
//                                TuringVolumeManager.getInstance().setVolume(MainActivity.this,TuringVolumeManager.STREAMTYPE[4],thisVolume);
                }
            } else {
                TTSManager.getInstance().startTTS("你说的,我还不会呢", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
            }
            Log.e("zhang", "onMusicPrepared: ===" + thisVolume + "---");
//                        float level = 0.5f ;
//                        TuringVolumeManager.getInstance().setVolume(MainActivity.this,TuringVolumeManager.STREAMTYPE[4],level);
        } else if (bean.getCode() == 200301) {
            TTSManager.getInstance().startTTS("没有识别到", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);

        } else if (bean.getCode() == 900101) {
            List<Behavior.Sequence> list = behavior.getSequences();
            if (bean.getOperateState() == 1003) {
                JsonObject parameters = bean.getParameters();
                //String string=parameters.get

                if (string.contains("再见")) {
                    Log.e("cao", "string==" + string);
                    hander.sendEmptyMessage(7);
                } else {
                    Log.e("cao", "home==");
                    getActivity().finish();
                }
            }
            if (null != list.get(0).getText()) {
                // TTSManager.getInstance().startTTS(list.get(0).getText(), itsCallback);
                TTSManager.getInstance().startTTS(list.get(0).getText(), itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
            }
        }
//        动作控制
        else if (bean.getCode() == 300101) {
            JsonObject parameters = bean.getParameters();
            if (parameters == null) {
                Log.e(TAG, "parameters is null 100302");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(parameters.toString());
                String force = "";    //  前进或者后退步数
                if (jsonObject.getString("force") != null) {
                    force = jsonObject.getString("force");
                }
                if (bean.getOperateState() == 2001) {  //  前进

                    ActionManager.getInstance().sendCmdToMachine(1);

                } else if (bean.getOperateState() == 2002) {  //  后退

                    ActionManager.getInstance().sendCmdToMachine(2);

                } else if (bean.getOperateState() == 2003) {  //向左
                    ActionManager.getInstance().sendCmdToMachine(3);

                } else if (bean.getOperateState() == 2004) {  //向右
                    ActionManager.getInstance().sendCmdToMachine(4);
                } else if (bean.getOperateState() == 0) {
                    ActionManager.getInstance().sendCmdToMachine(0);

                }
                Log.e(TAG, " json object1 = 100302    " + force + "  --" + bean.getOperateState());
            } catch (JSONException e) {
                Log.e(TAG, "JSONException parse exception");
            }
        }
        //跳舞
        else if (bean.getCode() == 200701) {

            if (bean.getOperateState() == 1000) {
                ActionManager.getInstance().sendCmdToMachine(5);
            }

        }
        //闹钟功能
        else if (bean.getCode() == 200710) {
            JsonObject parameters = bean.getParameters();
            try {
                //isAction = true;
                JSONObject jsonObject = new JSONObject(parameters.toString());
                String time = jsonObject.getString("time");
                String date = jsonObject.getString("startDate");
                String alarmDate = date + " " + time;
                Date date1 = DateManager.stringToDate(alarmDate, "yyyy-MM-dd HH:mm:ss");
                date_alarm = DateManager.dateToLong(date1);
                hander.sendEmptyMessage(6);
            } catch (JSONException e) {
//                Log.e("cao", "JSONException:" + e);
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {

        }
    }

    private OnSearchListener onSearchListener = new OnSearchListener() {

        @Override
        public void onSuccess(List<MusicEntity> list) {
            Log.i("zhang", "MusicManager==  请求完成" + (list == null));
            if (list == null) {
                return;
            }
            Log.i("zhang", "MusicManager==  请求完成");
            MusicManager.getInstance().play(list.get(0));
            MusicManager.getInstance().setOnPlayStateListener(onPlayerStateListener);
//            resultTv.setText(result);

        }

        @Override
        public void onFailed(int errorCode, String message) {
            Log.e(TAG, "onFail: ");
        }
    };
    private OnPlayerStateListener onPlayerStateListener = new OnPlayerStateListener() {
        @Override
        public void onStart() {
            Log.e(TAG, "onStart: ");
//            hiddenCircleDialog();
            Log.i("zhang", "MusicManager==  开始播放");
        }

        @Override
        public void onComplete() {
            Log.e(TAG, "onComplete: ");
            istts = false;
            TTSManager.getInstance().startTTS("音乐播放完成", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
        }

        @Override
        public void onError(int i, String errorMsg) {
            Log.e(TAG, "onError: errorCode=" + i + "   errorMsg=" + errorMsg);

        }

        @Override
        public void onBufferingUpdate(int i) {

        }
    };
    private String mediaUrl;
    private int code;

    /**
     * @param isClick 播放资源是否可以按压
     **/
    private void setMusicView(boolean isClick) {
        robotLayout.setMusicSelectable(isClick);
    }


    private void appADD() {
        AppAndContactsBean appAndContactsBean = new AppAndContactsBean();
        Map<String, String> appMap = new HashMap<String, String>();
//        appMap.put("语言启蒙", "wyt[@]6");
//        appMap.put("快乐英语", "com");
//        appMap.put("数学逻辑", "wyt[@]9");
//        appMap.put("故事王国", "wyt[@]3");
//        appMap.put("传统国学 ", "wyt[@]2");
//        appMap.put("爱奇艺少儿", "com.qiyi.video.child");
//        appMap.put("动漫乐园", "com");
//        appMap.put("自然科学", "com");
//        appMap.put("安全知识", "com");
//        appMap.put("十万个为什么", "com");
//        appMap.put("多元智能", "wyt[@]0");
//        appMap.put("儿歌天地", "wyt[@]1");
//        appMap.put("视觉空间", "wyt[@]7");
//        appMap.put("习惯养成", "wyt[@]10");
//        appMap.put("艺术培养", "com");
//        appMap.put("在线卡拉OK", "com.kgeking.client");
//        appMap.put("本地", "com.kg");
//        appMap.put("地K歌", "com.kg");
//        appMap.put("本地K歌", "com.kgeking.client");
//        appMap.put("在线乐园", "net.myvst.v2");
//        appMap.put("微信对讲", "com.cx");
//        appMap.put("智能问答", "com.turing.sample");
//        appMap.put("小学", "com.wyt.zxp.xx");
//        appMap.put("设置", "com.example.settingdemo");
//        appMap.put("WIFI设置", "com.ikan.nscreen.box.settings");
//        appMap.put("清理内存", "com.sunchip.luqiyalauncher");
        Map<String, String> contactMap = new HashMap<String, String>();
        contactMap.put("爸爸", "123456789");
        contactMap.put("妈妈", "123456789");
        contactMap.put("爷爷", "123456789");
        contactMap.put("奶奶", "123456789");
        contactMap.put("外公", "123456789");
        contactMap.put("外婆", "123456789");
        contactMap.put("舅舅", "123456789");
        contactMap.put("姨妈", "123456789");
        contactMap.put("姑妈", "123456789");
        contactMap.put("伯伯", "123456789");
        appAndContactsBean.setAppsMap(appMap);
        appAndContactsBean.setContactMap(contactMap);
        SemanticManager.getInstance().uploadAppsAndContacts(appAndContactsBean, new OnHttpRequestListener() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "uploadAppsAndContacts.onSuccess.result:" + result);
                MainApplication.updataDictionarySuccess = true;
            }

            @Override
            public void onError(int code, String msg) {
                Log.d(TAG, "uploadAppsAndContacts.onError.code:" + code + ";msg:" + msg);
                appADD();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
//			Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
//            TTSManager.getInstance().startTTS("没有可用网络", itsCallback, 5, TTSManager.LIANGJIANHE_TONE, TTSManager.ROBOT_EFFECT);
            return false;
        }
    }

    public void setplayer(MediaPlayer mediaplayer) {
        try {
            if (m_ambiplayer != null) {
                m_ambiplayer.stop();
            }
            m_ambiplayer = mediaplayer;
            if (mediaplayer != null) {
                mediaplayer.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
            // 屏幕唤醒
            if (SCREEN_ON.equals(intent.getAction())) {
                Log.e(TAG, "ScreenStatusReceiver==" + SCREEN_ON);
                isScreen_Dormancy = false;
                hander.sendEmptyMessageDelayed(1, 500);
            }
            // 屏幕休眠
            else if (SCREEN_OFF.equals(intent.getAction())) {
                isScreen_Dormancy = true;
                Log.e(TAG, "ScreenStatusReceiver==" + SCREEN_OFF);
            }
        }
    }

    //打开相机功能
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String temName = new DateFormat().format("yyMMdd_hhmmss", System.currentTimeMillis()) + "_" + (Math.random() * 100) + ".jpg"; //文件名
        image_path = Environment.getExternalStorageState() + File.separator + temName;
        File file = new File(image_path);
        if (file.exists()) {
            file.delete();
        }
        Uri u = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(albumIntent, 1);
        }
    }


}
package cloud.artik.example.hellocloud;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cloud.artik.api.MessagesApi;
import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.model.NormalizedMessagesEnvelope;

import static cloud.artik.example.hellocloud.R.drawable.sos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

//import static cloud.artik.example.hellocloud.Config.DEVICE_ID;

public class Alarmservice extends Service {
    public Alarmservice() {
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////MQTT 관련 (어플을 디바이스로 사용)/////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static ArrayList<String> mArrayList2;
    private String Appcode="0";
    private static final String TAG = "Alarmservice";
    public static String alarm_data="";
    public static int alarm_state=0;
    static public String deviceId_app = "16c2754d5c464a789a34c2442c56e1fd";
    static public String deviceToken_app = "d24c84f43929474bbaae3a0350797711";
    static public String device_id;
    private String DEVICE_ID = "없음";
    static public  String From = "없음";
    private int mode=1;
    private int earthquake_level = 0;
    private  String user_name;
    private  String user_device_id;
    private  String user_bracelet_id;
    private  String user_webcam_id;
    static public String message_id;
    static public String message_data;
    static public String sdid = "";
    static public String mid = "";
    static public String data = "";

    static public String bsdid = "";
    static public String bmid = "";
    static public String bdata = "";

    static public String csdid = "";
    static public String cmid = "";
    static public String cdata = "";

    static public String sdid_before = "";
    static public String mid_before = "";
    static public String data_before = "";

    static public String bsdid_before = "";
    static public String bmid_before = "";
    static public String bdata_before = "";

    static public String csdid_before = "";
    static public String cmid_before = "";
    static public String cdata_before = "";

    static public String msgData="";
    static public String[] sdid_buf = new String[100];
    static public String[] mid_buf = new String[100];
    static public String[] data_buf = new String[100];

    static public String[] brace_sdid_buf = new String[100];
    static public String[] brace_mid_buf = new String[100];
    static public String[] brace_data_buf = new String[100];

    static public String[] webcam_sdid_buf = new String[100];
    static public String[] webcam_mid_buf = new String[100];
    static public String[] webcam_data_buf = new String[100];

    static public int device_id_flag=0;
    static public int brace_id_flag=0;
    static public int webcam_id_flag=0;
    static public int power_alarm=0;
    static public int door_alarm=0;
    static public int messageCount = 20;  //가져올 디바이스메세지 갯수
    static public int messageCount2 = 10;  //가져올 팔찌메세지 갯수
    static public int messageCount3 = 5;  //가져올 웹캠메세지 갯수

    private int interphone_alarm=0;
    private int earthquake_alarm=0;
    private int temp_alarm=0;
    private int humid_alarm=0;
    private int emergency_alarm =0;
    private int fire_alarm =0;
    private int bpm_alarm=0;
    private int gas_alarm=0;
    private int answer_alarm=0;
    private int battery_alarm=0;
    private int toiletcnt_alarm=0;
    private int motiontime_alarm=0;
    private int toilet_alarm=0;
    private int motionplace_alarm=0;

    private String humid_text;
    private String temp_text;

    private String motiontime_alarm_data;
    private String toilet_alarm_data;
    private String motionplace_alarm_data;
    private String toiletcnt_alarm_data;
    private float restbpm;
    public String arrivedmsg;
    public static String temp_data1;
    public static String temp_data2;
    public static String answer;
    public static String power_id="0";
    public static String temp_data3;
    static public String temp_data ="1";
    public static String humid_data1;
    public static String humid_data2;
    public static String humid_data3;
    static public String humid_data ="1";
    private UsersApi mUsersApi = null;
    private MessagesApi mMessagesApi = null;
    private String mAccessToken;
    private long messageArrivedTime = 0;
    static public long downloadTime = 0;
    static public boolean MainRunning= false;
    static public boolean MessageRunning = false;
    private String position;
    private int position_int=0;
    private static final int REBOOT_DELAY_TIMER = 10 * 1000;
    private Timer timer;

    // The maximum time to wait for each mqtttest operation to finish
    final int maxWaitingTimeInMs = 5000;
    private CountDownLatch lock  = null;

    final int qos                = 2;

    MqttSession mqttSession = null;
    ArtikCloudMqttCallback callback = new ArtikCloudMqttCallback() {
        @Override
        public void onFailure(OperationMode opMode, IMqttToken mqttToken, Throwable throwable) {
            Log.d("ArtikCloudMqttCallback", "onFailure");
            System.out.println("ArtikCloudMqttCallback::onFailure is called with Mode " + opMode + "; throwable (" + throwable.toString() + ")");
            lock.countDown();
            fail();
        }

        @Override
        public void onSuccess(OperationMode opMode, IMqttToken mqttToken) {
            Log.d("ArtikCloudMqttCallback", "onSuccess");
            System.out.println("ArtikCloudMqttCallback::onSuccess() with Mode "
                    + opMode);
            lock.countDown();
        }

        @Override
        public void connectionLost(Throwable cause) {
            Log.d("ArtikCloudMqttCallback", "connectionLost");

            System.out.println("Connection is lost due to " + cause);
            setup();

            subscribeToActionsTopicTest();
            subscribeToErrorsTopicTest();
            //fail();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.d("ArtikCloudMqttCallback", "messageArrived");
            String text2 = "";
            System.out.println("받은 메세지. Payload: " + new String(message.getPayload()) + ". Qos:" + message.getQos() + "; Topic:" + topic);
            arrivedmsg = new String(message.getPayload());


            ActivityManager activityManager0 = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> info0;
            info0 = activityManager0.getRunningServices(7);
            for (Iterator iterator = info0.iterator(); iterator.hasNext();)  {
                ActivityManager.RunningServiceInfo runningServiceInfo = (ActivityManager.RunningServiceInfo) iterator.next();
                Log.i("RunningServicesInfo",runningServiceInfo.getClass().toString());

            }
            MessageRunning =false;
            MainRunning = false;

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> info;
            info = activityManager.getRunningTasks(7);
            for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
                ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
                Log.i("RunningTaskInfo",runningTaskInfo.topActivity.getClassName());

                if(runningTaskInfo.topActivity.getClassName().equals("cloud.artik.example.hellocloud.MessageActivity")) {
                    Log.d("MessageActivity","MessageActivity is running");
                    MessageRunning =true;
                }

                    if(runningTaskInfo.topActivity.getClassName().equals("cloud.artik.example.hellocloud.MainActivity")) {
                    Log.d("MainActivity","MainActivity is running");
                    MainRunning=true;}

            }

            DEVICE_ID = arrivedmsg.substring(54,arrivedmsg.length()-5);

            Log.d("받은 메세지 ID",DEVICE_ID);

            //DEVICE_ID="bfff16ca56be4c5896a0cfd8c3ce7e66";

            long intertime = System.currentTimeMillis() - messageArrivedTime;
            Log.d("팝업데이터 시간",System.currentTimeMillis()+","+messageArrivedTime+","+intertime);
            //if (intertime>1000) {    // 팝업창 하나만 띄우기 열기
                messageArrivedTime = System.currentTimeMillis();
                Log.d("알람팝업데이터 보내기","작동1");
                getLatestMsg();
                SystemClock.sleep(500);      // sleep , delay 함수!!!!
                Log.d("알람팝업데이터 보내기","작동2");
                open_userlist_data();


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d("ArtikCloudMqttCallback", "deliveryComplete");

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "서비스의 onCreate");
        unregisterRestartAlarm();
        //mAccessToken = "98264d32b8f24887b05172cad3fac927";  //내 토큰
        mAccessToken = Config.mAccessToken;
        mArrayList2 = new ArrayList<String>();

        //앱 코드별로 다른 appid 로그인
        open_appcode_data();

        if(Appcode.equals("1234"))
        {deviceId_app = "9880c39fd3da42d585c40da4150cddb3";
        deviceToken_app = "3aa4eeba36f34cc19133e2422b25f95b";}
        if(Appcode.equals("5555"))
        {deviceId_app = "44d9d64dc736401f928afe6a4b7b54af";
            deviceToken_app = "59b08a098adf4098a2bc193f5d5655e9";}
        if(Appcode.equals("9876"))
        {deviceId_app = "d5693eeb4e6b44baa0f1e43ec5f829c6";
            deviceToken_app = "336614c338644ab685c7aa39b97e4737";}
        Log.d("MQTT 로그인 앱코드", Appcode);


        Log.v(TAG, "::onCreate get access token = " + mAccessToken);
        sdid_buf[0]="0";
        sdid_buf[messageCount]="0";
        mid_buf[0]="0";
        mid_buf[messageCount]="0";
        data_buf[0]="0";
        data_buf[messageCount]="0";
        brace_sdid_buf[0]="0";
        brace_sdid_buf[messageCount]="0";
        brace_mid_buf[0]="0";
        brace_mid_buf[messageCount]="0";
        brace_data_buf[0]="0";
        brace_data_buf[messageCount]="0";
        webcam_sdid_buf[0]="0";
        webcam_sdid_buf[messageCount]="0";
        webcam_mid_buf[0]="0";
        webcam_mid_buf[messageCount]="0";
        webcam_data_buf[0]="0";
        webcam_data_buf[messageCount]="0";
        ///////////////////////////////상태 알림참///////////////////////////////////
        Intent mMainIntent =new Intent(this,MainActivity.class);

        PendingIntent mPendingIntent = PendingIntent.getActivity(this,1,mMainIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder=
                new Notification.Builder(this)
                        .setSmallIcon(sos)
                        .setContentTitle("푸시 알람 대기중")
                        .setContentIntent(mPendingIntent)
                        .setContentText("클릭 시 어플로 이동합니다.");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //mNotifyMgr.notify(001,mBuilder.build());          //Task killer로 죽는 서비스 실행
        startForeground(121, mBuilder.build());         //죽지않는 서비스 실행




        setupArtikCloudApi();
        getLatestMsg();

        ///////////////////////클라우드에서 액션 받기/////////////////////
        setup();
        //publishTest();
        subscribeToActionsTopicTest();
        subscribeToErrorsTopicTest();
        ///////////////////////////////////////////////////////////////


        // 10분마다 로그남김
        TimeZone timezone = TimeZone.getTimeZone("Etc/GMT-9");
        TimeZone.setDefault(timezone);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
        Date current = new Date();
        final String mtime = formater.format(current);
        TimerTask tt =new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, ": 서비스의 10분 알림. Appcode:"+Appcode+" ("+mtime+")");

            }
        };
        timer = new Timer();
        timer.schedule(tt,0,600000);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "서비스의 onStartCommand");
        Log.v(TAG, ": get latest message button is clicked.");
        downloadTime =0;

        sdid_buf = new String[100];
        mid_buf = new String[100];
        data_buf = new String[100];
        brace_sdid_buf = new String[100];
        brace_mid_buf = new String[100];
        brace_data_buf = new String[100];
        webcam_sdid_buf = new String[100];
        webcam_mid_buf = new String[100];
        webcam_data_buf = new String[100];

        data_buf[0]="0";
        brace_data_buf[0]="0";
        webcam_data_buf[0]="0";
        Log.d("DNTime", "갱신 : "+ 0);
        mode = intent.getIntExtra("mode",1);
        DEVICE_ID = " " + intent.getStringExtra("DEVICE_ID");
        if (DEVICE_ID.equals(" null"))
        {DEVICE_ID="없음";}
        else{DEVICE_ID =DEVICE_ID.substring(1);}
        Log.d("test", "서비스의 onStartCommand "+DEVICE_ID);
        if(mode==1)
        {// Now get the message
        getLatestMsg();}
        if(mode==2)
        {// Now get the message
            getLatestMsg2();}
        if(mode==3)
        {// Now get the message
            getLatestMsg3();}
        //MessageActivitystart();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        Log.d("test", "서비스의 onDestroy");

        registerRestartAlarm();
        ///////////////////////////////상태 알림참///////////////////////////////////
        Intent mMainIntent =new Intent(this,MessageActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,1,mMainIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(this)
                        .setSmallIcon(sos)
                        .setContentTitle("푸시 알람 대기중")
                        .setContentText("클릭 시 어플로 이동합니다.");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(001);
        super.onDestroy();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////아틱 클라우드관련 함수////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void setupArtikCloudApi() {
        ApiClient mApiClient = new ApiClient();
        mApiClient.setAccessToken(mAccessToken);

        mUsersApi = new UsersApi(mApiClient);
        mMessagesApi = new MessagesApi(mApiClient);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// 가장 최근 메세지 가져오기 //////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void getLatestMsg() {
        final String tag = TAG + " getLastNormalizedMessagesAsync";
        try {
            Log.d("getLatestMsg", " request = " +messageCount+","+DEVICE_ID);

            mMessagesApi.getLastNormalizedMessagesAsync(messageCount, DEVICE_ID, null,
                    new ApiCallback<NormalizedMessagesEnvelope>() {
                        @Override
                        public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                            processFailure(tag, exc);
                        }

                        @Override
                        public void onSuccess(NormalizedMessagesEnvelope result, int i, Map<String, List<String>> stringListMap) {
                            Log.v("getLatestMsg", " onSuccess latestMessage = " + result.getData().toString());
                           downloadTime = System.currentTimeMillis();
                            Log.d("DNTime", "갱신 : "+downloadTime);
                                for (int data_i = messageCount-1; data_i >=0; data_i--) {
                                    if (!result.getData().isEmpty()) {
                                        sdid = result.getData().get(data_i).getSdid();
                                        mid = result.getData().get(data_i).getMid();
                                        if(!result.getData().get(data_i).getData().toString().equals("{}"))
                                        {data = result.getData().get(data_i).getData().toString();}
                                        else{data = "test=1.0";}
                                        //Log.d(data_i + 1 + "번째", "디바이스 = " + result.getData().get(data_i).getSdid().toString());
                                        //Log.d(data_i + 1 + "번째", "데이터 = " + result.getData().get(data_i).getData().toString());


                                        data_buf[data_i] = data;
                                            sdid_buf[data_i] = sdid;
                                            mid_buf[data_i] = mid;
                                        if (sdid.equals(sdid_before) && data.equals(data_before)) {
                                            //Log.d(data_i + 1 + "번째", "데이터 스킵");
                                        } else {
                                            //Log.d(data_i + 1 + "번째", "데이터 받음");
                                            sdid_before = sdid;
                                            data_before = data;
                                            updateGetResponse(sdid, mid, data);
                                                //Log.d(data_i + 1 + "번째", "데이터 받음(공백아님)");
                                          device_id_flag=1;

                                        }
                                    }
                                }
                                device_id = sdid;
                                message_id = mid;
                                message_data = data;
                            }

                        @Override
                        public void onUploadProgress(long bytes, long contentLen, boolean done) {
                        }

                        @Override
                        public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                        }
                    });

        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }

    private void getLatestMsg2() {
        final String tag = TAG + " getLastNormalizedMessagesAsync";
        try {
            Log.d("getLatestMsg", " request = " +messageCount2+","+DEVICE_ID);

            mMessagesApi.getLastNormalizedMessagesAsync(messageCount2, DEVICE_ID, null,
                    new ApiCallback<NormalizedMessagesEnvelope>() {
                        @Override
                        public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                            processFailure(tag, exc);
                        }

                        @Override
                        public void onSuccess(NormalizedMessagesEnvelope result, int i, Map<String, List<String>> stringListMap) {
                            Log.v("getLatestMsg", " onSuccess latestMessage = " + result.getData().toString());
                            downloadTime = System.currentTimeMillis();
                            Log.d("DNTime", "갱신 : "+downloadTime);
                            for (int data_i = messageCount2-1; data_i >=0; data_i--) {
                                if (!result.getData().isEmpty()) {
                                    bsdid = result.getData().get(data_i).getSdid();
                                    bmid = result.getData().get(data_i).getMid();
                                    if(!result.getData().get(data_i).getData().toString().equals("{}"))
                                    {bdata = result.getData().get(data_i).getData().toString();}
                                    else{bdata = "test=1.0";}
                                    Log.d(data_i + 1 + "번째팔찌", "디바이스 = " + result.getData().get(data_i).getSdid().toString());
                                    Log.d(data_i + 1 + "번째팔찌", "데이터 = " + result.getData().get(data_i).getData().toString());
                                        brace_sdid_buf[data_i] = bsdid;
                                        brace_mid_buf[data_i] = bmid;
                                        brace_data_buf[data_i] = bdata;
                                    if (bsdid.equals(bsdid_before) && bdata.equals(bdata_before)) {
                                        //Log.d(data_i + 1 + "번째", "데이터 스킵");
                                    } else {
                                        //Log.d(data_i + 1 + "번째", "데이터 받음");
                                        bsdid_before = bsdid;
                                        bdata_before = bdata;
                                        //Log.d(data_i + 1 + "번째", "데이터 받음(공백아님)");
                                      brace_id_flag=1;

                                    }
                                }
                            }
                        }

                        @Override
                        public void onUploadProgress(long bytes, long contentLen, boolean done) {
                        }

                        @Override
                        public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                        }
                    });

        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }
    private void getLatestMsg3() {
        final String tag = TAG + " getLastNormalizedMessagesAsync";
        try {
            Log.d("getLatestMsg", " request = " +messageCount3+","+DEVICE_ID);

            mMessagesApi.getLastNormalizedMessagesAsync(messageCount3, DEVICE_ID, null,
                    new ApiCallback<NormalizedMessagesEnvelope>() {
                        @Override
                        public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                            processFailure(tag, exc);
                        }

                        @Override
                        public void onSuccess(NormalizedMessagesEnvelope result, int i, Map<String, List<String>> stringListMap) {
                            Log.v("getLatestMsg", " onSuccess latestMessage = " + result.getData().toString());
                            downloadTime = System.currentTimeMillis();
                            Log.d("DNTime", "갱신 : "+downloadTime);
                            for (int data_i = messageCount3-1; data_i >=0; data_i--) {
                                if (!result.getData().isEmpty()) {
                                    csdid = result.getData().get(data_i).getSdid();
                                    cmid = result.getData().get(data_i).getMid();
                                    if(!result.getData().get(data_i).getData().toString().equals("{}"))
                                    {cdata = result.getData().get(data_i).getData().toString();}
                                    else{cdata = "test=1.0";}
                                    //Log.d(data_i + 1 + "번째웹캠", "디바이스 = " + result.getData().get(data_i).getSdid().toString());
                                    //Log.d(data_i + 1 + "번째웹캠", "데이터 = " + result.getData().get(data_i).getData().toString());

                                        webcam_sdid_buf[data_i] = csdid;
                                        webcam_mid_buf[data_i] = cmid;
                                        webcam_data_buf[data_i] = cdata;
                                    if (csdid.equals(csdid_before) && cdata.equals(cdata_before)) {
                                        //Log.d(data_i + 1 + "번째", "데이터 스킵");
                                    } else {
                                        //Log.d(data_i + 1 + "번째", "데이터 받음");
                                        csdid_before = csdid;
                                        cdata_before = cdata;
                                        //Log.d(data_i + 1 + "번째", "데이터 받음(공백아님)");

                                       webcam_id_flag=1;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onUploadProgress(long bytes, long contentLen, boolean done) {
                        }

                        @Override
                        public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                        }
                    });

        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }
    private void processFailure(final String context, ApiException exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// 특정 문자열 개수 세기//////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    int getCharNumber(String str, char c)
    {
        int count = 0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i) == c)
                count++;
        }
        return count;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////클라우드에서 가져온 데이터를 UI에 뿌리기/////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////




    private void updateGetResponse(final String sdid, final String mid, final String msgData) {

                int count = getCharNumber(msgData,',')+1;  // count(아이템개수) = 쉼표의 개수 +1
                Log.d(" updateGetResponse :", sdid+","+msgData);
                //Log.d(" msgData count(아이템개수) =", " "+ count);

                String subject0 = null;
                String subject1 = msgData.substring(1);         //아이템이 1개면 그대로 입력받음
                String subject2 = null;
                String subject3 = null;
                String subject4 = null;
                String subject5 = null;
                String subject6 = null;
                String subject7 = null;
                String subject8 = null;
                String subject9 = null;
                String subject10 = null;
                String subject11 = null;
                String subject12 = null;
                String subject13 = null;
                String subject14 = null;
                String subject15 = null;
                String subject16 = null;
                String subject17 = null;
                String subject18 = null;
                String subject19 = null;
                String subject20 = null;
                String subject21 = null;
                String subject22 = null;
                String subject23 = null;
                String subject24 = null;
                String subject25 = null;
                String subject26 = null;
                String subject27 = null;
                String subject28 = null;
                String subject29 = null;
                String subject30 = null;
                String subject31 = null;
                String subject32 = null;
                String subject33 = null;
                String subject34 = null;
                String subject35 = null;
                String subject36 = null;
                String subject37 = null;
                String subject38 = null;
                String subject39 = null;
                String subject40 = null;




        if(count > 1) {    // 아이템이 2개 이상이면 나누기
                    StringTokenizer stok = new StringTokenizer(msgData, ",", false);
                    subject1 = stok.nextToken().substring(1);
                    subject2 = stok.nextToken().substring(1);
                    if (count > 2) {                        subject3 = stok.nextToken().substring(1);                    }
                    if (count > 3) {                        subject4 = stok.nextToken().substring(1);                    }
                    if (count > 4) {                        subject5 = stok.nextToken().substring(1);                    }
                    if (count > 5) {                        subject6 = stok.nextToken().substring(1);                    }
                    if (count > 6) {                        subject7 = stok.nextToken().substring(1);                    }
                    if (count > 7) {                        subject8 = stok.nextToken().substring(1);                    }
                    if (count > 8) {                        subject9 = stok.nextToken().substring(1);                    }
                    if (count > 9) {                        subject10 = stok.nextToken().substring(1);                    }
                    if (count > 10) {                        subject11 = stok.nextToken().substring(1);                    }
                    if (count > 11) {                        subject12 = stok.nextToken().substring(1);                    }
                    if (count > 12) {                        subject13 = stok.nextToken().substring(1);                    }
                    if (count > 13) {                        subject14 = stok.nextToken().substring(1);                    }
                    if (count > 14) {                        subject15 = stok.nextToken().substring(1);                    }
                    if (count > 15) {                        subject16 = stok.nextToken().substring(1);                    }
                    if (count > 16) {                        subject17 = stok.nextToken().substring(1);                    }
                    if (count > 17) {                        subject18 = stok.nextToken().substring(1);                    }
                    if (count > 18) {                        subject19 = stok.nextToken().substring(1);                    }
                    if (count > 19) {                        subject20 = stok.nextToken().substring(1);                    }
                    if (count > 20) {                        subject21 = stok.nextToken().substring(1);                    }
                    if (count > 21) {                        subject22 = stok.nextToken().substring(1);                    }
                    if (count > 22) {                        subject23 = stok.nextToken().substring(1);                    }
                    if (count > 23) {                        subject24 = stok.nextToken().substring(1);                    }
                    if (count > 24) {                        subject25 = stok.nextToken().substring(1);                    }
                    if (count > 25) {                        subject26 = stok.nextToken().substring(1);                    }
                    if (count > 26) {                        subject27 = stok.nextToken().substring(1);                    }
                    if (count > 27) {                        subject28 = stok.nextToken().substring(1);                    }
                    if (count > 28) {                        subject29 = stok.nextToken().substring(1);                    }
                    if (count > 29) {                        subject30 = stok.nextToken().substring(1);                    }
                    if (count > 30) {                        subject31 = stok.nextToken().substring(1);                    }
                    if (count > 31) {                        subject32 = stok.nextToken().substring(1);                    }
                    if (count > 32) {                        subject33 = stok.nextToken().substring(1);                    }
                    if (count > 33) {                        subject34 = stok.nextToken().substring(1);                    }
                    if (count > 34) {                        subject35 = stok.nextToken().substring(1);                    }
                    if (count > 35) {                        subject36 = stok.nextToken().substring(1);                    }
                    if (count > 36) {                        subject37 = stok.nextToken().substring(1);                    }
                    if (count > 37) {                        subject38 = stok.nextToken().substring(1);                    }
                    if (count > 38) {                        subject39 = stok.nextToken().substring(1);                    }
                    if (count > 39) {                        subject40 = stok.nextToken().substring(1);                    }

                }
                String content0 = null;
                String content1 = null;
                String content2 = null;
                String content3 = null;
                String content4 = null;
                String content5 = null;
                String content6 = null;
                String content7 = null;
                String content8 = null;
                String content9 = null;
                String content10 = null;
                String content11 = null;
                String content12 = null;
                String content13 = null;
                String content14 = null;
                String content15 = null;
                String content16 = null;
                String content17 = null;
                String content18 = null;
                String content19 = null;
                String content20 = null;
                String content21 = null;
                String content22 = null;
                String content23 = null;
                String content24 = null;
                String content25= null;
                String content26= null;
                String content27 = null;
                String content28 = null;
                String content29 = null;
                String content30 = null;
                String content31 = null;
                String content32 = null;
                String content33= null;
                String content34= null;
                String content35= null;
                String content36= null;
                String content37= null;
                String content38= null;
                String content39= null;
                String content40= null;
                String[] subject = {subject0, subject1, subject2, subject3, subject4, subject5, subject6,subject7,subject8,subject9,subject10,
                        subject11,subject12,subject13,subject14,subject15, subject16,subject17,subject18,subject19,subject20,
                        subject21,subject22,subject23,subject24,subject25, subject26,subject27,subject28,subject29,subject30,
                        subject31,subject32,subject33,subject34,subject35, subject36,subject37,subject38,subject39,subject40};
                String[] content = {subject0, content1, content2, content3, content4, content5, content6,content7,content8,content9,content10,
                        content11,content12,content13,content14,content15, content16,content17,content18,content19,content20,
                        content21,content22,content23,content24,content25, content26,content27,content28,content29,content30,
                        content31,content32,content33,content34,content35, content36,content37,content38,content39,content40,};
                //Log.d(" data divide1", "success");
                Log.d(" subject1", subject1);
                for (int data_i = 1; data_i <= count; data_i++) {
                    StringTokenizer stok2 = new StringTokenizer(subject[data_i], "=", false);
                    subject[data_i] = stok2.nextToken();
                    content[data_i] = stok2.nextToken();
                    if(subject[data_i].equals("activitiesHeart"))
                    {   if(content[data_i].equals("{restingHeartRate"))
                    {content[data_i] = stok2.nextToken();
                        Log.d(" activitiesHeart 일때", subject[data_i]+","+content[data_i]);}
                    else{content[data_i] = "없음";}
                    }
                    //Log.d(" data divide2", "success" + data_i);

                    if (data_i == count) {
                        StringTokenizer stok3 = new StringTokenizer(content[data_i], "}", false);
                        content[data_i] = stok3.nextToken();
                        //Log.d(" data divide3", "success");
                    }

                    if ("1.0".equals(content[data_i])) {
                        content[data_i] = "O";
                        //Log.d(" data trans True", "success");
                    }


                    if ("0.0".equals(content[data_i])) {
                        content[data_i] = "X";
                        //Log.d(" data trans False", "success");
                    }

                    if ("humid".equals(subject[data_i])) {
                        if((!content[data_i].equals("X"))&&(!content[data_i].equals("O")&&(!content[data_i].equals("200")))){
                            double humid_double = Double.parseDouble(content[data_i]);
                            if ((humid_double >= 80) || (humid_double <= 20)) {
                                humid_alarm = 1;
                                humid_text=content[data_i];
                            }
                            else {humid_alarm = 0;}
                        }
                    }
                    if ("temp".equals(subject[data_i])) {
                        if((!content[data_i].equals("X"))&&(!content[data_i].equals("O")&&(!content[data_i].equals("200")))) {
                            double temp_double = Double.parseDouble(content[data_i]);
                            if ((temp_double >= 30) || (temp_double <= 17)) {
                                temp_alarm = 1;
                                temp_text=content[data_i];
                            }
                            else {temp_alarm = 0;}
                        }
                    }

                    if ("power".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {power_alarm=1;}
                        else {power_alarm=0;}
                    }
                    if ("door".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {door_alarm=1;}
                        else {door_alarm=0;}

                    }
                    if ("earthquake".equals(subject[data_i])) {
                        if("2.0".equals(content[data_i]))
                        {earthquake_alarm=1;}
                        else {earthquake_alarm=0;}

                    }
                    if ("interphone".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {interphone_alarm=1;}
                        else {interphone_alarm=0;}
                    }

                    if ("fire".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {fire_alarm=1;}
                        else {fire_alarm=0;}
                    }

                    if ("emergency".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {emergency_alarm=1;}
                        else {emergency_alarm=0;}
                    }
                    if ("gas".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {gas_alarm=1;}
                        else {gas_alarm=0;}
                    }
                    if ("toiletcnt".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {toiletcnt_alarm=0;}
                        else if("O".equals(content[data_i]))
                        {toiletcnt_alarm=0;
                            toiletcnt_alarm_data="1";
                        }
                        else { int toiletcnt =Integer.valueOf(content[data_i]);
                            if(toiletcnt>=10)
                            {toiletcnt_alarm=1;
                            toiletcnt_alarm_data=content[data_i];}}
                    }
                    if ("motiontime".equals(subject[data_i])) {
                        if("2".equals(content[data_i]))
                        {motiontime_alarm=1;
                         motiontime_alarm_data="8시간";
                        }
                        else if("3".equals(content[data_i]))
                        {motiontime_alarm=1;
                            motiontime_alarm_data="12시간";
                        }
                        else if("4".equals(content[data_i]))
                        {motiontime_alarm=1;
                        motiontime_alarm_data="24시간";
                        }
                        else {motiontime_alarm=0;}
                    }
                    if ("toilet".equals(subject[data_i])) {
                        if("X".equals(content[data_i]))
                        {toilet_alarm=1;
                        toilet_alarm_data="화장실에 오래있음";}
                        else if("2".equals(content[data_i]))
                        {toilet_alarm=1;
                            toilet_alarm_data="화장실에 안감";}
                        else {toilet_alarm=0;}
                    }
                    if ("motionplace".equals(subject[data_i])) {
                        if("2".equals(content[data_i]))
                        {motionplace_alarm=1;
                            motionplace_alarm_data ="8시간";}
                        else if("3".equals(content[data_i]))
                        {motionplace_alarm=1;
                            motionplace_alarm_data ="12시간";}
                        else if("4".equals(content[data_i]))
                        {motionplace_alarm=1;
                            motionplace_alarm_data ="24시간";}
                        else {motionplace_alarm=0;}
                    }


                    if ("answer".equals(subject[data_i])) {
                        answer_alarm=1;
                        answer=content[data_i];
                        if(("OutletOn".equals(content[data_i]))||("OutletOff".equals(content[data_i])))
                        { answer_alarm=0; }

                    }

                    if ("id".equals(subject[data_i])) {
                        power_id=content[data_i].substring(0,1);
                    }


                    if ("activitiesHeart".equals(subject[data_i])) { //휴식 심박수
                        if(!content[data_i].equals("없음"))
                        {  float restbpm_int = Float.valueOf(content[data_i]);
                            if(restbpm_int>60.0){bpm_alarm=1;
                                restbpm=restbpm_int;
                            //Log.d("휴식 심박수 이상",""+restbpm);
                                }
                            else{bpm_alarm=0;
                                //Log.d("휴식 심박수 정상",""+restbpm);
                        }
                           }
                    }

                    if ("battery".equals(subject[data_i])) {
                        if(!content[data_i].equals("X"))
                        { float battery = Float.valueOf(content[data_i]);
                        if(battery<5.0)
                        {battery_alarm=1;}
                        else {battery_alarm=0;}}
                    }
                    //Log.d(" subject" + data_i + "result = ", subject[data_i]);
                    //Log.d(" content" + data_i + "result = ", content[data_i]);

                }
        //Log.d("알람내용(전도지부화비가)",""+power_alarm+","+door_alarm+","+earthquake_alarm+","+interphone_alarm+","+fire_alarm+","+emergency_alarm+","+gas_alarm);
                humid_data = humid_data1+humid_data2+humid_data3;
                temp_data = temp_data1+temp_data2+temp_data3;

            }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////MQTT관련 함수//////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Before
    public void setup() {
        System.out.println("\nsetUp");
        assertNotNull(deviceId_app);
        assertFalse(deviceId_app.isEmpty());

        assertNotNull(deviceToken_app);
        assertFalse(deviceToken_app.isEmpty());
        Log.d("Device ID", deviceId_app);
        Log.d("Device Token", deviceToken_app);
        System.out.println("Device ID:" + deviceId_app +"; Device Token:" + deviceToken_app);
        try {
            mqttSession = new MqttSession(deviceId_app, deviceToken_app, callback);
            System.out.println("Connecting to broker: "+ mqttSession.getBrokerUri());
            mqttSession.connect();
            lock = new CountDownLatch(1);
            lock.await(maxWaitingTimeInMs, TimeUnit.MILLISECONDS);//wait for mqtt operation finished
            assertEquals(true, mqttSession.isConnected());
        } catch(ArtikCloudMqttException|InterruptedException e) {
            e.printStackTrace();
            Log.d("mqtt setup error", e.getMessage());
            fail();
        }

    }


    @After
    public void cleanup() {
        System.out.println("cleanup");
        try {
            mqttSession.disconnect();
            lock = new CountDownLatch(1);
            lock.await(maxWaitingTimeInMs, TimeUnit.MILLISECONDS);
            assertEquals(false, mqttSession.isConnected());
        } catch(ArtikCloudMqttException|InterruptedException e) {
            e.printStackTrace();
            fail();
        }
        System.out.println("====================");
    }

    @Test
    public void connectionSuccessTest() {
        System.out.println("Test: connectionSuccessTest()");
        // This test case will trigger setup() and cleanup();
        // Should anything is wrong, it will trigger assertion there.
    }

    @Test
    public void publishTest() {
        System.out.println("Test: publishTest()");
        try {
            String payload    =  "{\"state\":true}";
            System.out.println("Publishing to topic: " + mqttSession.getPublishTopicPath() + "; message payload: " + payload );
            Log.d("Publishing to topic",mqttSession.getPublishTopicPath() + "; message payload: " + payload);

            mqttSession.publish(qos, payload);
            lock = new CountDownLatch(1);
            lock.await(maxWaitingTimeInMs, TimeUnit.MILLISECONDS);//wait for mqtt operation finished
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void subscribeToActionsTopicTest() {
        System.out.println("Test: subscribeToActionTopicTest()");
        Log.d("Test","subscribeToActionTopicTest");

        try {
            Log.d("actions topic",mqttSession.getSubscribeActionsTopicPath());
            System.out.println("Subscribing to actions topic: " + mqttSession.getSubscribeActionsTopicPath());
            mqttSession.subscribe(Topics.SUBSCRIBE_TOPIC_ACTIONS);
            lock = new CountDownLatch(1);
            lock.await(maxWaitingTimeInMs, TimeUnit.MILLISECONDS);//wait for mqtt operation finished

        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void subscribeToErrorsTopicTest() {
        System.out.println("Test: subscribeToErrorsTopicTest()");

        try {
            System.out.println("Subscribing to error topic: " + mqttSession.getSubscribeErrorsTopicPath());
            mqttSession.subscribe(Topics.SUBSCRIBE_TOPIC_ERRORS);
            lock = new CountDownLatch(1);
            lock.await(maxWaitingTimeInMs, TimeUnit.MILLISECONDS);//wait for mqtt operation finished
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    ////////////////////////////////////////여기까지 MQTT관련 함수//////////////////////////////////////////////////////

    private void popupstart() {
        Intent popupintent = new Intent(this, PopupActivity.class);
        alarm_data="";
        if(power_alarm==1)
        {alarm_data=alarm_data+"전력차단(ID:"+power_id+"), ";}
        if(door_alarm==1)
        {alarm_data=alarm_data+"문열림, ";}
        if(interphone_alarm==1)
        {alarm_data=alarm_data+"방문자, ";}
        if(earthquake_alarm==1)
        {alarm_data=alarm_data+"지진, ";}
        if(fire_alarm==1)
        {alarm_data=alarm_data+"화재, ";}
        if(emergency_alarm==1)
        {alarm_data=alarm_data+"비상벨, ";}
        if(temp_alarm==1)
        {alarm_data=alarm_data+"온도("+temp_text+"ºC), ";}
        if(humid_alarm==1)
        {alarm_data=alarm_data+"습도("+humid_text+"%), ";}
        if(bpm_alarm==1)
        {alarm_data=alarm_data+"심박수("+restbpm+"), ";}
        if(gas_alarm==1)
        {alarm_data=alarm_data+"가스밸브, ";}
        if(answer_alarm==1)
        {alarm_data=alarm_data+"응답:"+answer+", ";}
        if(battery_alarm==1)
        {alarm_data=alarm_data+"배터리, ";}

        if(toilet_alarm==1)
        {alarm_data=alarm_data+"화장실("+toilet_alarm_data+"), ";}
        if(toiletcnt_alarm==1)
        {alarm_data=alarm_data+"화장실cnt("+toiletcnt_alarm_data+"회), ";}
        if(motiontime_alarm==1)
        {alarm_data=alarm_data+"모션T("+motiontime_alarm_data+"동안 없음), ";}
        if(motionplace_alarm==1)
        {alarm_data=alarm_data+"모션P("+motionplace_alarm_data+"동안 한장소), ";}

        if (alarm_data.length() > 0) {
            alarm_data = alarm_data.substring(0, alarm_data.length()-2) +" ";
        }

        popupintent.putExtra("user_name",user_name);
        popupintent.putExtra("action",arrivedmsg);
        popupintent.putExtra("device_id", device_id);
        popupintent.putExtra("alarm_data", alarm_data);
        alarm_state = power_alarm+door_alarm+interphone_alarm+earthquake_alarm+temp_alarm+humid_alarm+bpm_alarm+fire_alarm+emergency_alarm+gas_alarm+battery_alarm+answer_alarm+toilet_alarm+toiletcnt_alarm+motionplace_alarm+motiontime_alarm;
        Log.d("알람팝업데이터 alarm_state", ""+alarm_state);

        if(alarm_state>0) {
            Log.d("알람팝업데이터", alarm_data+" : "+alarm_state);
            //Log.d("알람내용(전도지부화비)_최종",""+power_alarm+","+door_alarm+","+earthquake_alarm+","+interphone_alarm+","+fire_alarm+","+emergency_alarm);
            popupintent.putExtra("alarm_state", ""+alarm_state);
            popupintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);     //팝업이 켜져있으면 끄고 다시 실행
            List<ActivityManager.RunningTaskInfo> info;
            info = activityManager.getRunningTasks(7);
            for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
                ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
                Log.i("RunningTaskInfo",runningTaskInfo.topActivity.getClassName());
                if(runningTaskInfo.topActivity.getClassName().equals("cloud.artik.example.hellocloud.PopupActivity")) {
                    Log.d("PopupActivity","PopupActivity is running");
                    PopupActivity popupActivity = (PopupActivity)PopupActivity.popupActivity;
                    popupActivity.finish();}
            }



            startActivity(popupintent);
            alarmstate(); //상단 알람바 실행

            power_alarm=0; door_alarm=0;  earthquake_alarm=0; interphone_alarm=0; fire_alarm=0; emergency_alarm=0;
            temp_alarm=0; humid_alarm=0;  bpm_alarm=0; gas_alarm=0; answer_alarm=0; answer=""; humid_text="";temp_text="";
            toiletcnt_alarm=0; toilet_alarm=0; motiontime_alarm=0; motionplace_alarm=0;

        }}



    //액티비티를 열때 저장되어 있는 유저목록 불러오기
    public void open_userlist_data() {
                String fileData2 = null;
                FileInputStream inFs;
                String fileName = "UserList.txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File files = new File(dirPath+"/"+fileName);
                //파일 유무를 확인합니다.
                if(files.exists()==true) {//파일이 있을시
                    try {
                        inFs = openFileInput(fileName);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        inFs.close();
                        fileData2 = (new String(txt)).trim();
                        Log.d("데이터 불러오기(팝업) result", fileName + ":" + fileData2);
                        int count = getCharNumber(fileData2, ',') + 1;  // count(아이템개수) = 쉼표의 개수 +1
                        Log.d("데이터 불러오기(팝업) count", "" + count);

                        StringTokenizer stok = new StringTokenizer(fileData2, ",", false);
                        for(int data_i = 1; data_i <= count; data_i++) {
                            mArrayList2.add(stok.nextToken());
                            Log.d("데이터 불러오기(팝업) 추가", mArrayList2.get(data_i-1));

                        }
                        for (int data_i = 1; data_i <= count; data_i++) {
                            StringTokenizer stok2 = new StringTokenizer(mArrayList2.get(data_i-1), "_", false);
                            position=  stok2.nextToken();
                            user_name = stok2.nextToken();
                            if(mArrayList2.get(data_i-1).length()>20)
                            {user_device_id= stok2.nextToken();
                             if(count>=3){user_bracelet_id=stok2.nextToken();user_webcam_id=stok2.nextToken();}
                                if(device_id.equals(user_device_id)) {
                                    popupstart();
                                    Log.d("데이터 불러오기(팝업1)"+(data_i-1), position+","+user_name+ "," + user_device_id);

                                }
                                if(device_id.equals(user_bracelet_id)) {
                                    popupstart();
                                    Log.d("데이터 불러오기(팝업2)"+(data_i-1), position+","+user_name+ "," + user_bracelet_id);

                                }
                                if(device_id.equals(user_webcam_id)) {
                                    popupstart();
                                    Log.d("데이터 불러오기(팝업3)"+(data_i-1), position+","+user_name+ "," + user_webcam_id);

                                }
                                Log.d("데이터 불러오기(팝업0)"+(data_i-1), position+","+user_name+ "," + user_device_id+","+user_bracelet_id+","+user_webcam_id);

                            }
                        }

                    } catch (IOException e) {
                        Log.d("Read data result", fileName + ": faled");
                    }
                }


            }


    //액티비티를 열때 저장되어 있는 유저목록 불러오기
    public void open_appcode_data() {
        String fileData2 = null;
        FileInputStream inFs;
        String fileName = "Appcode.txt";
        String dirPath = getFilesDir().getAbsolutePath();
        File files = new File(dirPath+"/"+fileName);
        //파일 유무를 확인합니다.
        if(files.exists()==true) {//파일이 있을시
            try {
                inFs = openFileInput(fileName);
                byte[] txt = new byte[500];
                inFs.read(txt);
                inFs.close();
                fileData2 = (new String(txt)).trim();
                Log.d("데이터 불러오기(앱코드) result", fileName + ":" + fileData2);

                Appcode= fileData2;

            } catch (IOException e) {
                Log.d("Read data result", fileName + ": faled");
            }
        }


    }

    private void alarmstate() {
        ///////////////////////////////상태 알림참///////////////////////////////////
        Intent alarmIntent = new Intent(this, MessageActivity.class);
        StringTokenizer stok2 = new StringTokenizer(user_name, "(", false);
        String name = stok2.nextToken();
        String name2 = stok2.nextToken();
        StringTokenizer stok3 = new StringTokenizer(name2, ")", false);
        String age = stok3.nextToken();
        alarmIntent.putExtra("name", name);
        alarmIntent.putExtra("age", age);
        position_int= Integer.parseInt(position)-1;
        alarmIntent.putExtra("position", position_int);

        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(sos)
                        .setContentTitle("비상 알람 발생"+" : "+user_name)
                        .setContentIntent(mPendingIntent)
                        .setContentText("내용 : "+alarm_data + " 총 "+ alarm_state +"개");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(002, mBuilder.build());
        Log.d("알람 상태창", "작동");
    }

    /**
     * 서비스가 시스템에 의해서 또는 강제적으로 종료되었을 때 호출되어
     * 알람을 등록해서 10초 후에 서비스가 실행되도록 한다.
     */
    private void registerRestartAlarm() {

        Log.d("PersistentService", "registerRestartAlarm()");

        Intent intent = new Intent(Alarmservice.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(Alarmservice.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += REBOOT_DELAY_TIMER; // 10초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,REBOOT_DELAY_TIMER, sender);
    }


    /**
     * 기존 등록되어있는 알람을 해제한다.
     */
    private void unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()");
        Intent intent = new Intent(Alarmservice.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(Alarmservice.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

}

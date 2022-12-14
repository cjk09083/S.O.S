/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cloud.artik.api.MessagesApi;
import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.model.Message;
import cloud.artik.model.MessageIDEnvelope;
import cloud.artik.model.UserEnvelope;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;


//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////?????? ?????????///////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MessageActivity extends Activity  {
    private static final String TAG = "MessageActivity";

    private String seniornum ="??????";
    private String address="??????";
    private String name="??????";
    private String fileData="";
    private String age="?";
    private int position;
    private int fatflag =0;
    private String imagepath="??????";
    private String sdid = "";
    private String mid = "";
    private String data = "";
    private String sdid_before = "";
    private String mid_before = "";
    private String data_before = "";
    private String mtime = "0";
    private int DEVICE_err= 0;
    public String arrivedmsg;
    public String temp_data1;
    public String temp_data2;
    public String temp_data3;
    public String temp_data = "1";
    public String humid_data1;
    public String humid_data2;
    public String humid_data3;
    public String humid_data = "1";
    public String restbpm= " ";
    public String fatburnbpm= " ";
    public String peakbpm= " ";
    public String distancesTotal= " ";
    public String stepCount= " ";
    public String fatburnmin= " ";
    private String artik053id;
    private String Year="18";
    private String Month;
    private String Day;
    private String Hour;
    private String Minute;
    private String device_ID ="??????";
    private String brace_ID ="??????";
    public static String webcam_ID ="??????";
    private int update_finish=0;
    public static Activity AActivity;
    public static UsersApi mUsersApi = null;
    public static MessagesApi mMessagesApi = null;
    private String mAccessToken;
    private TextView mWelcome;
    private TextView updatetime;
    private TextView PowerTV;
    private TextView EarthquakeTV;
    private Button InterphoneBtn;
    private TextView DoorTV;
    private TextView FireTV;
    private TextView EmergencyTV;
    private Button tempchartBtn;
    private Button humidchartBtn;
    private Button heartrate;
    private Button batteryBtn;
    private Button camBtn;
    private Button motionwebBtn;
    private Button sendtextBtn;
    public Button numBtn;
    public Button editBtn;
    public Button ageBtn;
    public Button adressBtn;
    public Button nameBtn;
    private Switch gas_switch;
    private Switch outlet_switch;
    private Switch switch3;
    private Switch switch4;
    private ImageButton buttonLoadPic;
    private ImageView senior_image_view;
    public static Activity mainactivity;
    public static boolean MainRunned = false;
    private static int PICK_IMAGE_REQUEST = 1;
    private static int EditMode = 0;
    static public long before_downloadTime = 0;
    static public long now_downloadTime = 0;
    private int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE ;
    private int takeFlags;
    private Uri uri2;
    private int device_id_flag=0;
    private int brace_id_flag=0;
    private int webcam_id_flag=0;
    private int switch_flag=0;
    private int switch_flag2=0;
    private int downloading_flag=0;
    private String webcammode="";
    private String userid=" ";
    private String userpw="";
    private String username="";
    private String useraddress="";
    private String usernum="";
    private String userreference="";
    private Timer timer;
    private int timerflag =0;
    //////////////////////////////////////////////////////////////////////////////////???????????? MQTT ?????? ///////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////        onCreate            ///////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String from = " "+ intent.getStringExtra("from");
        if(from.equals(" login"))
        {AuthStateDAL authStateDAL = new AuthStateDAL(this);
            mAccessToken = authStateDAL.readAuthState().getAccessToken();}  //????????????????????? ?????? ?????? ????????????
        else {
            //mAccessToken = "2270f89102494488af1e8fa560a7e478";  //??? ?????? (????????? ???????????? ??????)
            //mAccessToken = "98264d32b8f24887b05172cad3fac927";  //??? ??????2 (??? ????????????)
            mAccessToken = Config.mAccessToken; //??? ??????2 (??? ???????????? + ??????2)
            //mAccessToken = "30be00c7990843838edf29be32a1d36e";  //????????? ??????
        }
        Log.v(TAG, "::onCreate get access token = " + mAccessToken);
        String user_name = intent.getStringExtra("name");
        String user_age = intent.getStringExtra("age");
        device_ID = " " + intent.getStringExtra("sdid");
        if (device_ID.equals(" null"))
        {device_ID="??????";}
        else{device_ID =device_ID.substring(1);}

        brace_ID = " " + intent.getStringExtra("brid");
        if (brace_ID.equals(" null"))
        {brace_ID="??????";}
        else{brace_ID =brace_ID.substring(1);}
        position =  intent.getIntExtra("position",0);
        //if(device_ID.equals("bfff16ca56be4c5896a0cfd8c3ce7e66"))
        //{user_name="?????????"; user_age="27";}
        Log.d("????????? ?????????",position+","+user_name+","+user_age+","+device_ID+","+brace_ID+","+position);
        name=user_name;
        age=user_age;
        open_user_data(user_name, user_age);
        if(device_ID.length()>20)  //???????????? ID??? ????????? ???????????????????????? ???????????? ??????
        {Intent loadingintent = new Intent(this, LoadingActivity.class);
        startActivity(loadingintent);}
        AActivity = MessageActivity.this;


        //???????????? ?????????
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainRunned = true;
        setContentView(R.layout.activity_message);
        mainactivity = MessageActivity.this;
        final EditText senior_num = (EditText) findViewById(R.id.senior_phone_num);
        final EditText name_data = (EditText) findViewById(R.id.name);
        final EditText age_data = (EditText) findViewById(R.id.age);
        final EditText address_data = (EditText) findViewById(R.id.address);
        final EditText device_idET = (EditText) findViewById(R.id.deviceid_num);
        final EditText brace_idET = (EditText) findViewById(R.id.braceid_num);
        final EditText webcam_idET = (EditText) findViewById(R.id.webcam_num);
        gas_switch = (Switch) findViewById(R.id.switch1);
        outlet_switch = (Switch) findViewById(R.id.switch2);
        switch3 = (Switch) findViewById(R.id.switch3);
        switch4 = (Switch) findViewById(R.id.switch4);
        Button sendMsgBtn = (Button) findViewById(R.id.send_btn);
        Button getLatestMsgBtn = (Button) findViewById(R.id.getlatest_btn);
        Button call_to_senior = (Button) findViewById(R.id.call_to_senior_btn);
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        Button reservationBtn = (Button) findViewById(R.id.reservation_btn);
        final Button seniornumBtn = (Button) findViewById(R.id.senior_phone_Btn);
        final Button ageBtn = (Button) findViewById(R.id.ageBtn);
        final Button adressBtn = (Button) findViewById(R.id.addressBtn);
        final Button nameBtn = (Button) findViewById(R.id.nameBtn);
        final Button deviceidBtn = (Button) findViewById(R.id.deviceid_Btn);
        final Button braceidBtn = (Button) findViewById(R.id.braceid_Btn);
        final Button webcamidBtn = (Button) findViewById(R.id.webcam_Btn);

        final Button editBtn = (Button) findViewById(R.id.Edit_btn);
        final Button editcancelBtn = (Button) findViewById(R.id.Editcancel_btn);
        final ImageButton buttonLoadPic = (ImageButton) findViewById(R.id.seniorimagebtn);
        final Button temphumidBtn1 = (Button) findViewById(R.id.temphumidchart_btn1);
        final Button temphumidBtn2 = (Button) findViewById(R.id.temphumidchart_btn2);
        final Button temphumidBtn3 = (Button) findViewById(R.id.temphumidchart_btn3);
        final Button motionBtn1 = (Button) findViewById(R.id.motionchart_btn1);
        final Button motionBtn2 = (Button) findViewById(R.id.motionchart_btn2);
        final Button motionBtn3 = (Button) findViewById(R.id.motionchart_btn3);
        sendtextBtn=(Button)findViewById(R.id.sendtext_btn);
        camBtn = (Button) findViewById(R.id.cam_btn);
        heartrate = (Button) findViewById(R.id.getlatest_response_mdata_bpm);
        tempchartBtn = (Button) findViewById(R.id.tempchart_btn);
        humidchartBtn = (Button) findViewById(R.id.humidchart_btn);
        batteryBtn = (Button) findViewById(R.id.mbatery);
        mWelcome = (TextView) findViewById(R.id.welcome);
        updatetime = (TextView) findViewById(R.id.updatetime);
        PowerTV = (TextView) findViewById(R.id.getlatest_response_mdata_power);
        DoorTV = (TextView) findViewById(R.id.getlatest_response_mdata_door);
        FireTV = (TextView) findViewById(R.id.fire);
        EarthquakeTV = (TextView) findViewById(R.id.getlatest_response_mdata_earthquake);
        EmergencyTV = (TextView) findViewById(R.id.emergency);
        InterphoneBtn = (Button) findViewById(R.id.getlatest_response_mdata_interphone);
        motionwebBtn = (Button) findViewById(R.id.getlatest_response_mdata_motion);
        senior_image_view = (ImageView) findViewById(R.id.seniorimageview);




        open_user_data2();
        name_data.setText(name);
        age_data.setText(age);
        address_data.setText(address);
        senior_num.setText(seniornum);
        device_idET.setText(device_ID);
        brace_idET.setText(brace_ID);
        webcam_idET.setText(webcam_ID);
        name_data.setText(user_name);
        age_data.setText(user_age);
        nameBtn.setText(name_data.getText().toString());    //????????? ??????????????? ????????? ??????
        ageBtn.setText(age_data.getText().toString());    //????????? ??????????????? ????????? ??????
        adressBtn.setText(address_data.getText().toString());    //????????? ??????????????? ????????? ??????
        seniornumBtn.setText(senior_num.getText().toString());    //????????? ??????????????? ????????? ??????
        deviceidBtn.setText(device_idET.getText().toString()); //????????? device_id????????? ????????? ??????
        braceidBtn.setText(brace_idET.getText().toString());
        webcamidBtn.setText(webcam_idET.getText().toString());
        uploadimage(imagepath);
        Log.d("????????? ????????????",imagepath+","+takeFlags);




        setupArtikCloudApi();
        getUserInfo();


        if(!device_ID.equals("??????"))
        {backintentstart(1); } //     ?????????????????? ????????? ?????? ????????? ????????????
        if(!brace_ID.equals("??????"))
        { backintentstart(2);}
        if(!webcam_ID.equals("??????"))
        { backintentstart(3);}



        new Handler().postDelayed(new Runnable() { //1 ??? ?????? ??????
            @Override
            public void run() { // ????????? ?????? ??????
                switch_flag = 1;
                switch_flag2 = 1;
            }
        }, 3000);

//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////    ?????? ??? ????????? ?????????  ///////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        // 30????????? ????????? ??????
        TimerTask tt =new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, name+": Timer Active.");
                if(EditMode==0)
                {
                    timerflag++;
                  downloadflow();
                  if(timerflag==10)
                  {timer.cancel();}
                }
            }
        };
        timer = new Timer();
        timer.schedule(tt,0,30000);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////// ?????????????????? ????????????/////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        call_to_senior.setOnClickListener(new View.OnClickListener() {  //???????????? ????????? ?????????
            public void onClick(View v) {
                seniornum = senior_num.getText().toString();
                if (!seniornum.isEmpty()) {
                    String tel = "tel:" + seniornum;
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                } else {
                    Toast.makeText(MessageActivity.this, "????????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        seniornumBtn.setOnClickListener(new View.OnClickListener() {      //??????????????? ?????????
            public void onClick(View v) {
                seniornum = senior_num.getText().toString();
                if (!seniornum.isEmpty()) {
                    String tel = "tel:" + seniornum;
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                } else {
                    Toast.makeText(MessageActivity.this, "????????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////// ?????????????????? ??????????????? ////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        sendtextBtn.setOnClickListener(new View.OnClickListener() {  //???????????? ????????? ?????????
            public void onClick(View v) {
                Intent ncloud_text = new Intent(MessageActivity.this, ncloud_text.class);
                ncloud_text.putExtra("device_ID", webcam_ID);
                startActivity(ncloud_text);

            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////// ?????? ?????? ??????&?????? //////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (EditMode == 0) {
                    editBtn.setText("??????");
                    Log.d("????????????", "??????");
                    EditMode = 1;
                    adressBtn.setVisibility(View.GONE);                //?????? ?????? ??????
                    address_data.setVisibility(View.VISIBLE);         //?????? edit????????? ??????
                    nameBtn.setVisibility(View.GONE);                //?????? ?????? ??????
                    name_data.setVisibility(View.VISIBLE);         //?????? edit????????? ??????
                    ageBtn.setVisibility(View.GONE);                //?????? ?????? ??????
                    age_data.setVisibility(View.VISIBLE);         //?????? edit????????? ??????
                    seniornumBtn.setVisibility(View.GONE);                //???????????? ?????? ??????
                    senior_num.setVisibility(View.VISIBLE);         //???????????? edit????????? ??????
                    deviceidBtn.setVisibility(View.GONE);           //???????????? ID ?????? ??????
                    device_idET.setVisibility(View.VISIBLE);          //???????????? ID edit????????? ??????
                    braceidBtn.setVisibility(View.GONE);           //?????? ID ?????? ??????
                    brace_idET.setVisibility(View.VISIBLE);          //?????? ID edit????????? ??????
                    webcamidBtn.setVisibility(View.GONE);           //?????? ID ?????? ??????
                    webcam_idET.setVisibility(View.VISIBLE);          //?????? ID edit????????? ??????
                    editcancelBtn.setVisibility(View.VISIBLE);      //?????? ?????? ??????
                    buttonLoadPic.setVisibility(View.VISIBLE);                  //???????????? ??????
                    senior_image_view.setVisibility(View.GONE);                //????????? ??????

                    return;
                }
                if (EditMode == 1) {
                    editBtn.setText("??????");
                    Log.d("????????????", "??????(????????????)");
                    EditMode = 0;
                    adressBtn.setVisibility(View.VISIBLE);                 //?????? ?????? ??????
                    address_data.setVisibility(View.GONE);                 //?????? edit????????? ??????
                    nameBtn.setVisibility(View.VISIBLE);                   //?????? ?????? ??????
                    name_data.setVisibility(View.GONE);                    //?????? edit????????? ??????
                    ageBtn.setVisibility(View.VISIBLE);                   //?????? ?????? ??????
                    age_data.setVisibility(View.GONE);                    //?????? edit????????? ??????
                    seniornumBtn.setVisibility(View.VISIBLE);                  //???????????? ?????? ??????
                    senior_num.setVisibility(View.GONE);                //???????????? edit????????? ??????
                    deviceidBtn.setVisibility(View.VISIBLE);           //???????????? ID ?????? ??????
                    device_idET.setVisibility(View.GONE);          //???????????? ID edit????????? ??????
                    braceidBtn.setVisibility(View.VISIBLE);           //?????? ID ?????? ??????
                    brace_idET.setVisibility(View.GONE);          //?????? ID edit????????? ??????
                    webcamidBtn.setVisibility(View.VISIBLE);           //?????? ID ?????? ??????
                    webcam_idET.setVisibility(View.GONE);          //?????? ID edit????????? ??????
                    editcancelBtn.setVisibility(View.GONE);               //?????? ?????? ??????
                    buttonLoadPic.setVisibility(View.GONE);                  //???????????? ??????
                    senior_image_view.setVisibility(View.VISIBLE);                //????????? ??????

                    seniornum = senior_num.getText().toString();
                    age = age_data.getText().toString();
                    name = name_data.getText().toString();
                    address = address_data.getText().toString();
                    device_ID = device_idET.getText().toString();
                    brace_ID = brace_idET.getText().toString();
                    webcam_ID = webcam_idET.getText().toString();
                    if (seniornum.isEmpty()) {
                        seniornum = "??????";
                    }
                    if (address.isEmpty()) {
                        address = "??????";
                    }
                    if (imagepath.isEmpty()){
                        imagepath="??????";
                    }
                    if (device_ID.isEmpty()){
                        device_ID="??????";
                    }
                    if (brace_ID.isEmpty()){
                        brace_ID="??????";
                    }
                    if (webcam_ID.isEmpty()){
                        webcam_ID="??????";
                    }
                    nameBtn.setText(name);    //????????? ??????????????? ????????? ??????
                    seniornumBtn.setText(seniornum);    //????????? ????????????????????? ????????? ??????
                    ageBtn.setText(age);    //????????? ??????????????? ????????? ??????
                    adressBtn.setText(address);    //????????? ??????????????? ????????? ??????
                    deviceidBtn.setText(device_ID);    //????????? ????????????????????? ????????? ??????
                    braceidBtn.setText(brace_ID);    //????????? ????????????????????? ????????? ??????
                    webcamidBtn.setText(webcam_ID);    //????????? ????????????????????? ????????? ??????


                    /////////////////////////////////////////// ????????? ?????? ????????? ?????? //////////////////////////////////////////

                    String fileName = name + "_" + age + ".txt";
                    String dirPath = getFilesDir().getAbsolutePath();
                    File file = new File(dirPath);
                    // ???????????? ????????? ????????? ??????
                    if (!file.exists()) {
                        file.mkdirs();
                        Toast.makeText(getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    }
                    // txt ?????? ??????
                    fileData = name + "_" + age + "_" + address + "_" + seniornum +"_"+imagepath+"_"+device_ID+"_"+brace_ID+"_"+webcam_ID;
                        File savefile = new File(dirPath + "/" + fileName);
                    try {
                        FileOutputStream fos = new FileOutputStream(savefile);
                        fos.write(fileData.getBytes());
                        fos.close();
                        Toast.makeText(getApplicationContext(), fileName + " ???  ?????????", Toast.LENGTH_SHORT).show();
                        Log.d(fileName + " ???  ?????????", fileData);

                    } catch (IOException e) {
                    }

                    new SendPost().execute();

                    return;
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////// ????????? ???????????? ////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        editcancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editBtn.setText("??????");
                Log.d("????????????", "??????(????????????)");
                EditMode = 0;
                adressBtn.setVisibility(View.VISIBLE);                 //?????? ?????? ??????
                address_data.setVisibility(View.GONE);                 //?????? edit????????? ??????
                nameBtn.setVisibility(View.VISIBLE);                   //?????? ?????? ??????
                name_data.setVisibility(View.GONE);                    //?????? edit????????? ??????
                ageBtn.setVisibility(View.VISIBLE);                   //?????? ?????? ??????
                age_data.setVisibility(View.GONE);                    //?????? edit????????? ??????
                seniornumBtn.setVisibility(View.VISIBLE);                  //???????????? ?????? ??????
                senior_num.setVisibility(View.GONE);                //???????????? edit????????? ??????
                editcancelBtn.setVisibility(View.GONE);               //?????? ?????? ??????
            }
        });



        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////   ?????? ?????? ??????   /////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////


        motionBtn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (device_ID.length() > 10) {
                    Intent week_motion_intent = new Intent(MessageActivity.this, motion_web_week.class);
                    week_motion_intent.putExtra("DEVICE_ID", device_ID);
                    startActivity(week_motion_intent);
                    Log.d("????????? ?????? ?????? ", "with ID : " + device_ID);} else {
                    Log.d("?????????????????? ", "????????????");
                    Toast.makeText(MessageActivity.this, "device_ID??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        motionBtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (device_ID.length() > 10) {
                    Intent MotionWeb = new Intent(MessageActivity.this, motionPopup.class);
                    MotionWeb.putExtra("DEVICE_ID",device_ID);
                    startActivity(MotionWeb);
                    Log.d("?????????????????? ?????? ", "with ID : " + device_ID);
                } else {
                    Log.d("?????????????????? ", "????????????");
                    Toast.makeText(MessageActivity.this, "device_ID??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        motionBtn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (device_ID.length() > 10) {
                    Intent MotionWeb = new Intent(MessageActivity.this, motionsitPopup.class);
                    MotionWeb.putExtra("DEVICE_ID",device_ID);
                    startActivity(MotionWeb);
                    Log.d("?????????????????? ?????? ", "with ID : " + device_ID);
                } else {
                    Log.d("?????????????????? ", "????????????");
                    Toast.makeText(MessageActivity.this, "device_ID??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////// ????????? ???????????? //////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        temphumidBtn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (device_ID.length() > 10) {
                    Intent temphumid_intent = new Intent(MessageActivity.this, temphumid_web_total.class);
                    temphumid_intent.putExtra("DEVICE_ID", device_ID);
                    temphumid_intent.putExtra("Mode", 0);
                    startActivity(temphumid_intent);
                    Log.d("????????? ???????????? ?????? ", "with ID : " + device_ID);} else {
                    Log.d("?????????????????? ", "????????????");
                    Toast.makeText(MessageActivity.this, "device_ID??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        temphumidBtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("?????? ??????","?????? ?????? ??????");
                Intent temphumid_intent = new Intent(MessageActivity.this, temphumid_web_total.class);
                temphumid_intent.putExtra("DEVICE_ID", device_ID);
                temphumid_intent.putExtra("year", Year);
                temphumid_intent.putExtra("mode", 1);
                startActivity(temphumid_intent);
                Log.d("????????? ???????????? ?????? ", "with ID : " + device_ID+", Year : "+ Year);
            }
        });

        temphumidBtn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (device_ID.length() > 10) {
                    Intent week_motion_intent = new Intent(MessageActivity.this, temphumidPopup.class);
                    week_motion_intent.putExtra("DEVICE_ID", device_ID);
                    startActivity(week_motion_intent);
                    Log.d("????????????????????? ?????? ", "with ID : " + device_ID);} else {
                    Log.d("?????????????????? ", "????????????");
                    Toast.makeText(MessageActivity.this, "device_ID??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////// ??????????????? ?????? /////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        heartrate.setOnClickListener(new View.OnClickListener() {  //???????????? ????????? ?????????
            public void onClick(View v) {
                Log.d("??????????????? ??????","?????? ??????");
                Intent smartband_intent = new Intent(MessageActivity.this, smartbandPopup.class);
                smartband_intent.putExtra("restbpm", restbpm);
                smartband_intent.putExtra("fatburnbpm", fatburnbpm);
                smartband_intent.putExtra("fatburnmin", fatburnmin);
                smartband_intent.putExtra("distancesTotal", distancesTotal);
                smartband_intent.putExtra("peakbpm", peakbpm);
                smartband_intent.putExtra("stepCount", stepCount);
                startActivity(smartband_intent);
            }
        });


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////// ??????????????? ??? ??????????????? (???????????????)  ///////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, ": send button is clicked.");
                postMsg();
                SystemClock.sleep(1000);      // sleep , delay ??????!!!!

                Toast.makeText(MessageActivity.this, mid + "," + data, Toast.LENGTH_SHORT).show();

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////   ?????? ???????????? ??????   /////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        motionwebBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (webcammode.equals("???????????????")) {
                    //???????????? ?????? ??????
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bmxuihsnbqbd946044.cdn.ntruss.com/live/r8nuotiqgverhn3h/playlist.m3u8")));
                } else if(webcammode.equals("???????????????"))
                {//??????????????? ??????
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kr.objectstorage.ncloud.com/artiksos/jpg/Image.jpg")));
                }else{//??????????????? ??????
                    Message msg = new Message();
                    msg.setSdid(webcam_ID);
                    msg.getData().put("order", "jpg");
                    try {
                        mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                            @Override
                            public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                                processFailure("wabcam send result", exc);
                            }
                            @Override
                            public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                                Log.v("wabcam send result", " onSuccess response to sending message = " + result.getData().toString());

                            }
                            @Override
                            public void onUploadProgress(long bytes, long contentLen, boolean done) {
                            }
                            @Override
                            public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                            }
                        });
                    } catch (ApiException exc) {
                        processFailure("wabcam send result", exc);
                    }
                    Toast.makeText(MessageActivity.this, "????????? ????????????", Toast.LENGTH_SHORT).show();


                }
            }
        });

         camBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Message msg = new Message();
                msg.setSdid(webcam_ID);
                    msg.getData().put("order", "StreamingOff");
                    try {
                        mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                            @Override
                            public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                                processFailure("wabcam send result", exc);
                            }
                            @Override
                            public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                                Log.v("wabcam send result", " onSuccess response to sending message = " + result.getData().toString());
                            }
                            @Override
                            public void onUploadProgress(long bytes, long contentLen, boolean done) {
                            }
                            @Override
                            public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                            }
                        });
                    } catch (ApiException exc) {
                        processFailure("wabcam send result", exc);
                    }

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////// ???????????? ????????? ??????(???????????? ??????) /////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        getLatestMsgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, ": get latest message button is clicked.");
                downloading_flag=0;
                if(!device_ID.equals("??????"))
                {backintentstart(1); } //     ?????????????????? ????????? ?????? ????????? ????????????
                if(!brace_ID.equals("??????"))
                { backintentstart(2);}
                if(!webcam_ID.equals("??????"))
                { backintentstart(3);}

                if(DEVICE_err==0)
                {Log.d("???????????????", "sdid=" + Alarmservice.sdid + " mid=" + Alarmservice.mid + " data=" + Alarmservice.data);
                    //downpopupstart(Alarmservice.device_id, Alarmservice.message_id, Alarmservice.message_data);
                }

                new Handler().postDelayed(new Runnable() { //1 ??? ?????? ??????
                    @Override
                    public void run() { // ????????? ?????? ??????
                        switch_flag = 1;
                        switch_flag2 = 1;
                        Toast.makeText(MessageActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();

                    }
                }, 1500);

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////// ?????? ?????????(??????) ????????? ??????///////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////


        buttonLoadPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, ": ????????? ????????? ?????? ??????");
                //Intent ??????
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //ACTION_PIC??? ??????????
                intent.setType("image/*"); //???????????? ?????????
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                //Intent ?????? - ??????????????? ????????? ????????? ???????????? ????????? ??? ??????.
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                takeFlags = intent.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////// ????????? ?????? (???????????????) ??????????????? /////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////



        gas_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(MessageActivity.this, "???????????? = " + isChecked, Toast.LENGTH_SHORT).show();
                Message msg = new Message();
                msg.setSdid(device_ID);
                Log.d("switch state", "is" + isChecked);
                if (true == isChecked) {
                    msg.getData().put("gas_control", 1);
                    Log.d("switch state", "is 1(2)");
                }
                if (false == isChecked) {
                    msg.getData().put("gas_control", 0);
                    Log.d("gas switch state", "is 0");
                }
                if(switch_flag==1) {
                    try {
                        mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                            @Override
                            public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                                processFailure("switch send result", exc);
                            }

                            @Override
                            public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                                Log.v("switch send result", " onSuccess response to sending message = " + result.getData().toString());

                            }

                            @Override
                            public void onUploadProgress(long bytes, long contentLen, boolean done) {
                            }

                            @Override
                            public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                            }
                        });
                    } catch (ApiException exc) {
                        processFailure("switch send result", exc);
                    }
                    switch_flag=0;
                }
            }
        });

        outlet_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(MessageActivity.this, "???????????? = " + isChecked, Toast.LENGTH_SHORT).show();
                Message msg = new Message();
                msg.setSdid(webcam_ID);
                Log.d("switch state", "is" + isChecked);
                if (true == isChecked) {
                    msg.getData().put("order", "poweron");
                    Log.d("switch state", "is 1");
                }
                if (false == isChecked) {
                    msg.getData().put("order", "poweroff");
                    Log.d("switch state", "is 0");
                }

                if(switch_flag2==1) {
                try {
                    mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                        @Override
                        public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                            processFailure("switch send result", exc);
                        }

                        @Override
                        public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                            Log.v("switch send result", " onSuccess response to sending message = " + result.getData().toString());
                        }

                        @Override
                        public void onUploadProgress(long bytes, long contentLen, boolean done) {
                        }

                        @Override
                        public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                        }
                    });
                } catch (ApiException exc) {
                    processFailure("switch send result", exc);
                }
            }
                switch_flag2=0;
            }
        });


        switch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(MessageActivity.this, "???????????? = " + isChecked, Toast.LENGTH_SHORT).show();
                Message msg = new Message();
                msg.setSdid(device_ID);
                Log.d("switch state", "is" + isChecked);
                if (true == isChecked) {
                    msg.getData().put("earthquake", "1");
                    Log.d("switch state", "is 1");
                }
                if (false == isChecked) {
                    msg.getData().put("earthquake", "2.0");
                    Log.d("switch state", "is 0");
                }

                if(update_finish==1) {
                    try {
                        mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                            @Override
                            public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                                processFailure("switch send result", exc);
                            }

                            @Override
                            public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                                Log.v("switch send result", " onSuccess response to sending message = " + result.getData().toString());
                            }

                            @Override
                            public void onUploadProgress(long bytes, long contentLen, boolean done) {
                            }

                            @Override
                            public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                            }
                        });
                    } catch (ApiException exc) {
                        processFailure("switch send result", exc);
                    }
                }
            }
        });


        switch4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(MessageActivity.this, "???????????? = " + isChecked, Toast.LENGTH_SHORT).show();
                Message msg = new Message();
                msg.setSdid(device_ID);
                Log.d("switch state", "is" + isChecked);
                if (true == isChecked) {
                    msg.getData().put("fire", "1");
                    Log.d("switch state", "is 1");
                }
                if (false == isChecked) {
                    msg.getData().put("fire", "0");
                    Log.d("switch state", "is 0");
                }
                try {
                    mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                        @Override
                        public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                            processFailure("switch send result", exc);
                        }

                        @Override
                        public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                            Log.v("switch send result", " onSuccess response to sending message = " + result.getData().toString());
                        }

                        @Override
                        public void onUploadProgress(long bytes, long contentLen, boolean done) {
                        }

                        @Override
                        public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                        }
                    });
                } catch (ApiException exc) {
                    processFailure("switch send result", exc);
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////// ???????????? ???????????? ///////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        reservationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (device_ID.length() > 10) {
                    Intent reservation = new Intent(MessageActivity.this, reservationPopup.class);
                    startActivity(reservation);
                    Log.d("?????????????????? ?????? ", "with requestCode : " + 0);
                } else {
                    Log.d("?????????????????? ", "????????????");
                    Toast.makeText(MessageActivity.this, "device_ID??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }







        /*


        ????????? ??? ???????????? ?????????


         */


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////// ???????????? ???  //////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        timer.cancel();

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.putExtra("return_position", position);
            if(device_id_flag==1)
            {mainActivityIntent.putExtra("return_device_ID", device_ID);}
            else  {mainActivityIntent.putExtra("return_device_ID", "??????");}
            if(brace_id_flag==1)
            {mainActivityIntent.putExtra("return_bracelet_ID", brace_ID);}
            else  {mainActivityIntent.putExtra("return_bracelet_ID", "??????");}
            if(webcam_id_flag==1)
            {mainActivityIntent.putExtra("return_webcam_ID", webcam_ID);}
            else  {mainActivityIntent.putExtra("return_webcam_ID", "??????");}

        Log.d("MessageActivity", "?????? with flag " +device_id_flag+","+ brace_id_flag+","+webcam_id_flag);

        if(name.isEmpty())
            {name = "??????";}
            mainActivityIntent.putExtra("return_name", name);
        if(age.isEmpty())
        {age = "?";}
            mainActivityIntent.putExtra("return_age", age);

            startActivity(mainActivityIntent);

            Log.d("MessageActivity", "?????? with data" +position+","+ name+","+age+","+device_ID+","+brace_ID);

        if (webcammode.equals("???????????????")) {
            ///?????? ???????????? ??????
            Message msg = new Message();
            msg.setSdid(webcam_ID);
            msg.getData().put("order", "StreamingOff");
            try {
                mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                    @Override
                    public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                        processFailure("wabcam send result", exc);
                    }

                    @Override
                    public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                        Log.v("wabcam send result", " onSuccess response to sending message = " + result.getData().toString());
                    }

                    @Override
                    public void onUploadProgress(long bytes, long contentLen, boolean done) {
                    }

                    @Override
                    public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                    }
                });
            } catch (ApiException exc) {
                processFailure("wabcam send result", exc);
            }
        }

        finish();

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////// ?????????????????? ??????////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setupArtikCloudApi() {
        ApiClient mApiClient = new ApiClient();
        mApiClient.setAccessToken(mAccessToken);

        mUsersApi = new UsersApi(mApiClient);
        mMessagesApi = new MessagesApi(mApiClient);
    }

    private void getUserInfo() {
        final String tag = TAG + " getSelfAsync";
        try {
            mUsersApi.getSelfAsync(new ApiCallback<UserEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int statusCode, Map<String, List<String>> map) {
                    processFailure(tag, exc);
                }

                @Override
                public void onSuccess(UserEnvelope result, int statusCode, Map<String, List<String>> map) {
                    Log.v(TAG, "getSelfAsync::setupArtikCloudApi self name = " + result.getData().getFullName());
                    //updateWelcomeViewOnUIThread("Welcome " + result.getData().getFullName());
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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// ??????????????? ??????????????? //////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void postMsg() {
        final String tag = TAG + " sendMessageActionAsync";
        final Message msg = new Message();
        msg.setSdid(webcam_ID);
        /*
        msg.getData().put("humid_server1", "3738394040414243");
        msg.getData().put("humid_server2", "4238363636373738");
        msg.getData().put("humid_server3", "3737373839373636");
        msg.getData().put("temp_server1", "2323252526262627");
        msg.getData().put("temp_server2", "2726262626252626");
        msg.getData().put("temp_server3", "2525283539252423");
        */
        msg.getData().put("order", "AppText");

        Log.d("postMsg DEVICE_ID",webcam_ID);
        Log.d("postMsg DATA",msg.getData().toString());

        try {
            mMessagesApi.sendMessageAsync(msg, new ApiCallback<MessageIDEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                }

                @Override
                public void onSuccess(MessageIDEnvelope result, int i, Map<String, List<String>> stringListMap) {
                    Log.v("onSuccess", " onSuccess response to sending message = " + result.getData().toString());
                }

                @Override
                public void onUploadProgress(long bytes, long contentLen, boolean done) {
                }

                @Override
                public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                }
            });
        } catch (ApiException exc) {
        }


    }




    static void showErrorOnUIThread(final String text, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// ?????? ????????? ?????? ??????//////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    int getCharNumber(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////?????????????????? ????????? ???????????? UI??? ?????????/////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void updateGetResponseOnUIThread(final String sdid, final String mid, final String msgData) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(" msgData  =", msgData);
                int count = getCharNumber(msgData, ',') + 1;  // count(???????????????) = ????????? ?????? +1


                //Log.d(" msgData.length() :", " "+ msgData.length());
                Log.d(" msgData count(???????????????) =", " " + count);

                String subject0 = null;
                String subject1 = msgData.substring(1);         //???????????? 1?????? ????????? ????????????
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

                if (count > 1) {    // ???????????? 2??? ???????????? ?????????
                    StringTokenizer stok = new StringTokenizer(msgData, ",", false);
                    subject1 = stok.nextToken().substring(1);
                    subject2 = stok.nextToken().substring(1);
                    if (count > 2) {
                        subject3 = stok.nextToken().substring(1);
                    }
                    if (count > 3) {
                        subject4 = stok.nextToken().substring(1);
                    }
                    if (count > 4) {
                        subject5 = stok.nextToken().substring(1);
                    }
                    if (count > 5) {
                        subject6 = stok.nextToken().substring(1);
                    }
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
                        Log.d(" activitiesHeart ??????", subject[data_i]+","+content[data_i]);}
                        else{content[data_i] = "??????";}
                    }
                    if(subject[data_i].equals("FatBurn"))
                    {   if(content[data_i].equals("{min"))
                    {fatburnbpm = stok2.nextToken();
                    fatflag =1;
                        Log.d(" FatBurn ??????", fatburnbpm);
                         heartrate.setText(fatburnbpm);
                    }
                    else{content[data_i] = "??????";}
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
                        humidchartBtn.setText(content[data_i] + "%");


                    }
                    if ("temp".equals(subject[data_i])) {
                        tempchartBtn.setText(content[data_i] + "??C");


                    }
                    if ("power".equals(subject[data_i])) {
                        PowerTV.setText(content[data_i]);
                        PowerTV.setTextSize(20);
                        if ("O".equals(content[data_i])) {
                            PowerTV.setTextColor(Color.parseColor("#00cc00"));
                        }
                        if ("X".equals(content[data_i])) {
                            PowerTV.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }
                    if ("door".equals(subject[data_i])) {
                        DoorTV.setText(content[data_i]);
                        DoorTV.setTextSize(20);
                        if ("O".equals(content[data_i])) {
                            DoorTV.setTextColor(Color.parseColor("#00cc00"));
                        }
                        if ("X".equals(content[data_i])) {
                            DoorTV.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }
                    if ("earthquake".equals(subject[data_i])) {
                        EarthquakeTV.setText(content[data_i]);
                        EarthquakeTV.setTextSize(20);
                        if ("O".equals(content[data_i])) {
                            EarthquakeTV.setTextColor(Color.parseColor("#00cc00"));
                        }
                        if ("X".equals(content[data_i])) {
                            EarthquakeTV.setTextColor(Color.parseColor("#000000"));
                        }
                        if ("2.0".equals(content[data_i])) {
                            EarthquakeTV.setText("X");
                            EarthquakeTV.setTextSize(20);
                            EarthquakeTV.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }
                    if ("interphone".equals(subject[data_i])) {
                        InterphoneBtn.setText(content[data_i]);
                        InterphoneBtn.setTextSize(20);
                        if ("O".equals(content[data_i])) {
                            InterphoneBtn.setTextColor(Color.parseColor("#00cc00"));
                        }
                        if ("X".equals(content[data_i])) {
                            InterphoneBtn.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }
                    if ("fire".equals(subject[data_i])) {
                        FireTV.setText(content[data_i]);
                        FireTV.setTextSize(20);
                        if ("O".equals(content[data_i])) {
                            FireTV.setTextColor(Color.parseColor("#00cc00"));
                        }
                        if ("X".equals(content[data_i])) {
                            FireTV.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }
                    if ("emergency".equals(subject[data_i])) {
                        EmergencyTV.setText(content[data_i]);
                        EmergencyTV.setTextSize(20);
                        if ("O".equals(content[data_i])) {
                            EmergencyTV.setTextColor(Color.parseColor("#00cc00"));
                        }
                        if ("X".equals(content[data_i])) {
                            EmergencyTV.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }

                    if ("gas".equals(subject[data_i])) {
                        if ("O".equals(content[data_i])) {
                            gas_switch.setChecked(true);
                        }
                        if ("X".equals(content[data_i])) {
                            gas_switch.setChecked(false);
                        }
                    }


                    if ("webcam".equals(subject[data_i])) {
                        if ("StreamingOn".equals(content[data_i])) {
                            motionwebBtn.setText("???????????????");
                            webcammode="???????????????";
                            motionwebBtn.setTextColor(Color.parseColor("#00cc00"));
                        }
                        else if ("UploadJpg".equals(content[data_i])) {
                            motionwebBtn.setText("???????????????");
                            webcammode="???????????????";
                            motionwebBtn.setTextColor(Color.parseColor("#00cc00"));
                        }
                        else if ("StreamingOff".equals(content[data_i])) {
                            motionwebBtn.setText("??????");
                            webcammode="??????";
                            motionwebBtn.setTextColor(Color.parseColor("#000000"));
                        }
                        else {
                            motionwebBtn.setText("??????");
                            webcammode="??????";
                            motionwebBtn.setTextColor(Color.parseColor("#000000"));
                        }


                    }

                    if ("id".equals(subject[data_i])) { //?????? ?????????
                        artik053id=content[data_i];
                    }

                    if ("battery".equals(subject[data_i])) {
                        if(!content[data_i].equals("X"))
                        { batteryBtn.setText(content[data_i]+"V(ID:"+artik053id.substring(0,1)+")");
                            float battery = Float.valueOf(content[data_i]);
                        if(battery<5.0){batteryBtn.setTextColor(Color.parseColor("#ff0000"));}
                        }
                    }

                    if ("answer".equals(subject[data_i])) {
                        if ("OutletOn".equals(content[data_i])) {
                            outlet_switch.setChecked(true);
                        }
                        if ("OutletOff".equals(content[data_i])) {
                            outlet_switch.setChecked(false);
                        }
                    }

                        if ("activitiesHeart".equals(subject[data_i])) { //?????? ?????????
                            if(!content[data_i].equals("??????"))
                            { heartrate.setText(content[data_i]);
                            heartrate.setTextColor(Color.parseColor("#252e69"));
                            float restbpm_int = Float.valueOf(content[data_i]);
                            if(restbpm_int>90.0){heartrate.setTextColor(Color.parseColor("#ff0000"));}
                            restbpm=content[data_i];}
                            //heartrate.setTextSize(20);
                        }


                    if ("minutes".equals(subject[data_i])) { //?????? ?????????
                        if(fatflag==1)
                        { fatburnmin=content[data_i];}
                    }

                    if ("stepCount".equals(subject[data_i])) { //?????? ?????????
                        stepCount=content[data_i];
                    }
                    if ("distancesTotal".equals(subject[data_i])) { //?????? ?????????
                        distancesTotal=content[data_i];
                    }

                    Log.d(" subject" + data_i + " result =", subject[data_i]);
                        Log.d(" content" + data_i + " result =", content[data_i]);

                    }

                    humid_data = humid_data1 + humid_data2 + humid_data3;
                    temp_data = temp_data1 + temp_data2 + temp_data3;
                    TimeZone timezone = TimeZone.getTimeZone("Etc/GMT-9");
                    TimeZone.setDefault(timezone);
                    SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
                    Date current = new Date();
                    mtime = formater.format(current);
                    System.out.println(mtime);
                    updatetime.setText("????????? ???????????? :" + mtime);
                    update_finish = 1;
                    downloading_flag=0;
                    fatflag =0;
            }
        });
    }


    private void processFailure(final String context, ApiException exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
        showErrorOnUIThread(context + errorDetail, MessageActivity.this);
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("cloud.artik.example.hellocloud.Alarmservice".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void backintentstart(final int mode) {
        update_finish=0;
        downloading_flag=1;

        Intent backintent = new Intent(
                getApplicationContext(),//??????????????????
                Alarmservice.class); // ????????? ????????????
        if (mode == 1) {
            backintent.putExtra("DEVICE_ID", device_ID);
            backintent.putExtra("mode", mode);
            Log.d("????????? start??? DEVICE_ID", device_ID);
            startService(backintent); // ??????????????? ?????????
        }
        if(mode ==2){
            backintent.putExtra("DEVICE_ID", brace_ID);
            backintent.putExtra("mode", mode);
            Log.d("????????? start??? BRACE_ID", brace_ID);
            startService(backintent); // ??????????????? ?????????
        }

        if(mode ==3){
            backintent.putExtra("DEVICE_ID", webcam_ID);
            backintent.putExtra("mode", mode);
            Log.d("????????? start??? WEBCAM_ID", webcam_ID);
            startService(backintent); // ??????????????? ?????????
        }


        new Handler().postDelayed(new Runnable() { //1 ??? ?????? ??????
            @Override
            public void run() { // ????????? ?????? ??????
            if(mode==1)                    {
                device_id_flag=Alarmservice.device_id_flag;
                Log.d("DNTime com","????????? ???????????? ID"+Alarmservice.data_buf[0]);}
            else if(mode==2){
                brace_id_flag=Alarmservice.brace_id_flag;
                Log.d("DNTime com","????????? ?????? ID"+Alarmservice.brace_data_buf[0]); }
            else if(mode==3){
                webcam_id_flag=Alarmservice.webcam_id_flag;
                Log.d("DNTime com","????????? ?????? ID"+Alarmservice.webcam_data_buf[0]); }

                    if(device_id_flag==1)
                    {
                        for (int data_i = (Alarmservice.messageCount - 1) ; data_i >=0 ; data_i--) {
                            if(!Alarmservice.data_buf[0].equals("0"))
                            {updateGetResponseOnUIThread(Alarmservice.sdid_buf[data_i], Alarmservice.mid_buf[data_i], Alarmservice.data_buf[data_i]);}
                        }
                    }
            if(brace_id_flag==1)
            {
                for (int data_i = (Alarmservice.messageCount2 - 1) ; data_i >=0 ; data_i--) {
                    if(!Alarmservice.brace_data_buf[0].equals("0"))
                    {updateGetResponseOnUIThread(Alarmservice.brace_sdid_buf[data_i], Alarmservice.brace_mid_buf[data_i], Alarmservice.brace_data_buf[data_i]);}
                }
            }
            if(webcam_id_flag==1)
            {
                for (int data_i = (Alarmservice.messageCount3 - 1) ; data_i >=0 ; data_i--) {
                    if(!Alarmservice.webcam_data_buf[0].equals("0"))
                    {updateGetResponseOnUIThread(Alarmservice.webcam_sdid_buf[data_i], Alarmservice.webcam_mid_buf[data_i], Alarmservice.webcam_data_buf[data_i]);}
                }
            }
                    TimeZone timezone = TimeZone.getTimeZone("Etc/GMT-9");
                    TimeZone.setDefault(timezone);
                    SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
                    Date current = new Date();
                    String mtime = formater.format(current);
                    System.out.println(mtime);
                    updatetime.setText("????????? ???????????? :" + mtime);
                    before_downloadTime =now_downloadTime;  DEVICE_err =0;

            }

        }, 1500);}



    private void downloadflow() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

             downloading_flag=0;
                if(!device_ID.equals("??????"))
                {backintentstart(1); } //     ?????????????????? ????????? ?????? ????????? ????????????
                if(!brace_ID.equals("??????"))
                { backintentstart(2);}
                if(!webcam_ID.equals("??????"))
                { backintentstart(3);}


                new Handler().postDelayed(new Runnable() { //1 ??? ?????? ??????
                    @Override
                    public void run() { // ????????? ?????? ??????
                        switch_flag = 1;
                        switch_flag2 = 1;
                        Toast.makeText(MessageActivity.this, "?????? ???????????? ??????", Toast.LENGTH_SHORT).show();

                    }
                }, 1500);}

            });}




    //????????? ???????????? ??????
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult ??????", "with :" +requestCode + resultCode);

        try {
            //???????????? ?????? ????????????
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data?????? ??????????????? ???????????? ?????????
                Log.d("requestCode", "" + requestCode);
                Log.d("resultCode ", "" + resultCode );
                Log.d("Intent data", "" + data);

                Uri uri = data.getData();
                imagepath = uri.toString();
                uri2 = Uri.parse(imagepath);
                String imagepath2 = uri.getPath();
                Log.d("?????? ??????(uri)", "" + uri);
                Log.d("?????? ??????(path)", imagepath+","+imagepath2);
                Log.d("?????? ??????(uri2)", ""+uri2);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);

                //???????????? ????????????(?) ?????? ?????? ?????? ???????????? ???????????? ?????? ??????.
                int Height = bitmap.getHeight();
                int Width = bitmap.getWidth();
                Log.d("?????? ??????", "" + bitmap.getHeight());
                Log.d("?????? ??????", "" + bitmap.getWidth());
                Matrix matrix = new Matrix();

                if (bitmap.getHeight() < bitmap.getWidth()) {
                    matrix.setRotate(90);
                }
                int nh = (int) (bitmap.getHeight() * (200.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 200, nh, true);
                buttonLoadPic = (ImageButton) findViewById(R.id.seniorimagebtn);
                senior_image_view = (ImageView) findViewById(R.id.seniorimageview);
                buttonLoadPic.setImageBitmap(scaled);
                senior_image_view.setImageBitmap(scaled);

                //????????? ??? ?????? ???????????? ????????? 90??? ??????
                if (Height < Width) {
                    Log.d("?????? ??????", Height + "vs" + Width + "??????");
                    buttonLoadPic.setRotation(90);
                }                else {buttonLoadPic.setRotation(0);}
                //????????? ??? ?????? ???????????? ????????? 90??? ??????
                if (Height < Width) {
                    Log.d("?????? ??????", Height + "vs" + Width + "??????");
                    senior_image_view.setRotation(90);
                }                else {senior_image_view.setRotation(0);}
                Log.d("?????? ????????? ??????(uri2)",  uri2+","+takeFlags);

            } else {
                Toast.makeText(this, "?????? ???????????????.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! ????????? ????????? ????????????.", Toast.LENGTH_LONG).show();
            Log.e("?????? ????????? ??????", e.getMessage());
            e.printStackTrace();
        }
    }


    //// ???????????? ????????? ????????????
    protected void uploadimage(String path) {

        if (path != null && !path.isEmpty()) {

            try {
                //???????????? ?????? ????????????
                //data?????? ??????????????? ???????????? ?????????
                imagepath = path;
                uri2 = Uri.parse(imagepath);
                Log.d("?????? ??????(path)", imagepath);
                Log.d("?????? ??????(uri2)", "" + uri2);
                takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(uri2, takeFlags);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                //???????????? ????????????(?) ?????? ?????? ?????? ???????????? ???????????? ?????? ??????.
                int Height = bitmap.getHeight();
                int Width = bitmap.getWidth();
                Log.d("?????? ??????", "" + bitmap.getHeight());
                Log.d("?????? ??????", "" + bitmap.getWidth());
                Matrix matrix = new Matrix();

                if (bitmap.getHeight() < bitmap.getWidth()) {
                    matrix.setRotate(90);
                }
                int nh = (int) (bitmap.getHeight() * (200.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 200, nh, true);
                buttonLoadPic = (ImageButton) findViewById(R.id.seniorimagebtn);
                senior_image_view = (ImageView) findViewById(R.id.seniorimageview);
                buttonLoadPic.setImageBitmap(scaled);
                senior_image_view.setImageBitmap(scaled);

                //????????? ??? ?????? ???????????? ????????? 90??? ??????
                if (Height < Width) {
                    Log.d("?????? ??????", Height + "vs" + Width + "??????");
                    buttonLoadPic.setRotation(90);
                }                else {buttonLoadPic.setRotation(0);}
                //????????? ??? ?????? ???????????? ????????? 90??? ??????
                if (Height < Width) {
                    Log.d("?????? ??????", Height + "vs" + Width + "??????");
                    senior_image_view.setRotation(90);
                }                else {senior_image_view.setRotation(0);}

                Log.d("?????? ????????? ??????(uri2)",  uri2+","+takeFlags);

            } catch (Exception e) {
                //Toast.makeText(this, "Oops! ????????? ????????? ????????????.", Toast.LENGTH_LONG).show();
                Log.e("?????? ????????? ??????", e.getMessage());
                e.printStackTrace();
            }
        }  else{ Log.d("?????? ??????", "????????????" );}

    }


    //???????????? ??????????????? ?????? ????????? ???????????? ????????????
    public void save_user_data(final String Name, final String Age) {
        String fileName = name + "_" + age + ".txt";
        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        // ???????????? ????????? ????????? ??????
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }
        // txt ?????? ??????
        fileData = name + "_" + age + "_" + address + "_" + seniornum +"_"+imagepath+"_"+device_ID+"_"+brace_ID+"_"+webcam_ID;
        File savefile = new File(dirPath + "/" + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(fileData.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), fileName + " ???  ?????????", Toast.LENGTH_SHORT).show();
            Log.d(fileName + " ???  ?????????", fileData);

        } catch (IOException e) {
        }

    }

    //???????????? ??????????????? ?????? ???????????? ?????? ???????????? ????????????
    public void open_user_data(final String Name, final String Age) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fileData2 = null;
                FileInputStream inFs;
                String fileName = Name + "_" + Age + ".txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File files = new File(dirPath+"/"+fileName);
                //?????? ????????? ???????????????.
                if(files.exists()==true) {//????????? ?????????
                    try {
                        inFs = openFileInput(fileName);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        inFs.close();
                        fileData2 = (new String(txt)).trim();
                        Log.d("????????? ???????????? result", fileName + " : " + fileData2);
                        int count = getCharNumber(fileData2, '_') + 1;  // count(???????????????) = ????????? ?????? +1
                        StringTokenizer stok = new StringTokenizer(fileData2, "_", false);
                         name = stok.nextToken();
                         age = stok.nextToken();
                        if(count>2)
                        {address = stok.nextToken();}
                        if(count>3)
                        {
                            seniornum = stok.nextToken();}
                         if(count>4)
                         {imagepath=stok.nextToken();}
                        if(count>5)
                        {device_ID=stok.nextToken();}
                        if(count>6)
                        {brace_ID=stok.nextToken();}
                        if(count>7)
                        {webcam_ID=stok.nextToken();}
                        Log.d("????????? ???????????? count",""+ count);

                    } catch (IOException e) {
                        Log.d("Read data result", fileName + ": faled");
                    }
                }


            }
        });
    }


    public void mOnClose(View v){ //????????? 1 ??????

    }


    public class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {
            String content = executeClient();
            return content;
        }}

    protected void onPostExecute(String result) {
        // ?????? ????????? ????????? ????????? ??? (????????? ??????)
    }

    // ?????? ???????????? ??????
    public String executeClient() {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("input", "update"));
        post.add(new BasicNameValuePair("userID", userid));
        post.add(new BasicNameValuePair("userPw", userpw));
        post.add(new BasicNameValuePair("userRef", userreference));
        post.add(new BasicNameValuePair("senID", name + "_" + age+","));
        post.add(new BasicNameValuePair("seninfo", fileData));

        // ?????? HttpClient ?????? ??????
        HttpClient client = new DefaultHttpClient();

        // ?????? ?????? ?????? ??????, ?????? ???????????? ??????
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post?????? ??????
        HttpPost httpPost = new HttpPost("http://minsanggyu2.cafe24.com/user_data.php");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response=client.execute(httpPost);
            Log.d("Post ??????", ""+response);
            /*
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse("http://minsanggyu2.cafe24.com/user_data.php");
            i.setData(u);
            startActivity(i);
            */
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //????????? ?????? ??????????????? ???????????? ?????? ??????????????? ????????????
    public void open_user_data2() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fileData2 = null;
                FileInputStream inFs;
                String fileName = "UserData.txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File files = new File(dirPath+"/"+fileName);
                //?????? ????????? ???????????????.
                if(files.exists()==true) {//????????? ?????????
                    try {
                        inFs = openFileInput(fileName);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        inFs.close();
                        fileData2 = (new String(txt)).trim();
                        Log.d("?????? ????????? ???????????? result", fileName + ":" + fileData2);
                        int count = getCharNumber(fileData2, '_') + 1;  // count(???????????????) = ????????? ?????? +1
                        StringTokenizer stok = new StringTokenizer(fileData2, "_", false);
                        Log.d("?????? ????????? ???????????? count", "" + count);
                        userid=stok.nextToken();
                        userpw=stok.nextToken();
                        username=stok.nextToken();
                        useraddress=stok.nextToken();
                        usernum=stok.nextToken();
                        userreference=stok.nextToken();

                        updateWelcomeViewOnUIThread(userid);
                    } catch (IOException e) {
                        Log.d("Read data result", fileName + ": faled");
                    }
                }
            }
        });}

    private void updateWelcomeViewOnUIThread(final String text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWelcome.setText("UserID : "+text);
            }
        });
    }

} //MessageActivity


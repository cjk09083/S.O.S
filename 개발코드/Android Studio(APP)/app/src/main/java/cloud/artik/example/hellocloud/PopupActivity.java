package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Created by user on 2018-08-08.
 */
public class PopupActivity extends Activity {
    private static String device_id;
    private  String user_name;
    private String user_namedata;
    private  String user_device_id= " ";
    private String user_bracelet_id=" ";
    private  String data;
    private  String user_id;
    private  String state;
    private int position=0;
    private static ArrayList<String> mArrayList2;
    public static Activity popupActivity;

    TextView txtText1;
    TextView txtText2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("팝업데이터","받기실행");
        // 화면이 꺼졌을때도 팝업 띄우기
        getWindow().addFlags(

                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |

                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        mArrayList2 = new ArrayList<String>();
        popupActivity = PopupActivity.this;

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //UI 객체생성
        txtText1 = (TextView)findViewById(R.id.txtText1);

        txtText2 = (TextView)findViewById(R.id.txtText2);

        //데이터 가져오기
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        device_id = intent.getStringExtra("device_id");
        data = intent.getStringExtra("alarm_data");
        state = intent.getStringExtra("alarm_state");

        txtText1.setText("알람기기 : " + user_name);
        txtText2.setText("알람내용 : " + data +" 총"+state+"개의 이상이 발견되었습니다.");
        Log.d("받은 팝업데이터",data);
    }

    //확인 버튼 클릭
    public void mOnMove(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        Log.d("open activity state",getIntent().getFlags()+ "," + Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        if (getIntent().getFlags() != 0) {
            // Activity is being brought to front and not being  created again,
            // Thus finishing this activity will bring the last viewed activity to foreground

            boolean MessageRunning = Alarmservice.MessageRunning;
            boolean MainRunning = Alarmservice.MainRunning;
            Log.i("RunningTaskInfo",MessageRunning+ "," + MainRunning);
            MainActivity BBActivity = (MainActivity)MainActivity.AActivity;
            MessageActivity AAActivity = (MessageActivity)MessageActivity.AActivity;
            if(MessageRunning)
            {AAActivity.finish();}
            if(MainRunning)
            {BBActivity.finish();}
            //액티비티(팝업) 닫기
            finish();
            mainstart(device_id);}
        //액티비티(팝업) 닫기
        else {finish();}
    }


    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        Log.d("open activity state",getIntent().getFlags()+ "," + Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

      finish();
    }

    private void mainstart(String sdid) {
        boolean openflag =open_userlist_data();
        Intent mainstart = new Intent(this, MessageActivity.class);
        StringTokenizer stok2 = new StringTokenizer(user_name, "(", false);
        String name = stok2.nextToken();
        String name2 = stok2.nextToken();
        StringTokenizer stok3 = new StringTokenizer(name2, ")", false);
        String age = stok3.nextToken();
        mainstart.putExtra("name", name);
        mainstart.putExtra("age", age);
        mainstart.putExtra("sdid", device_id);
        mainstart.putExtra("position", position);
        startActivity(mainstart);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
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

    //액티비티를 열때 저장되어 있는 유저목록 불러오기
    public boolean open_userlist_data() {
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
                    Log.d("데이터 불러오기(팝업) 길이", "" + mArrayList2.get(data_i-1).length());

                }
                for (int data_i = 1; data_i <= count; data_i++) {
                    StringTokenizer stok2 = new StringTokenizer(mArrayList2.get(data_i-1), "_", false);
                    user_id=  stok2.nextToken();
                    user_namedata = stok2.nextToken();
                    if(getCharNumber(mArrayList2.get(data_i-1),'_')>1)
                    {user_device_id= stok2.nextToken();}
                    if(getCharNumber(mArrayList2.get(data_i-1),'_')>2)
                    {user_bracelet_id= stok2.nextToken();}
                    if(user_namedata.equals(user_name))
                    { position= Integer.parseInt(user_id.toString())-1;
                        return true;
                    }
                    Log.d("데이터 불러오기(팝업) "+(data_i-1), user_id+","+user_name+ "," + user_device_id+","+user_bracelet_id+","+position);
                }

            } catch (IOException e) {
                Log.d("Read data result", fileName + ": faled");

            }
        }

        return true;
    }


}


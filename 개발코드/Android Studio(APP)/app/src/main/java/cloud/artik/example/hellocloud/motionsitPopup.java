package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by user on 2018-08-08.
 */
public class motionsitPopup extends Activity {
    private int mode=0;
    private String name="";
    private String reference ="";
    public String restbpm;
    public String fatburnbpm;
    public String peakbpm;
    public String distancesTotal;
    public String stepCount;
    public String fatburnmin;
    private String pw="";
    private String num="";
    private String address="";
    private String DEVICE_ID;
    private TextView fatburnminET;
    private TextView stepCountET;
    private TextView distancesTotalET;
    private TextView fatburnbpmET;
    private TextView restbpmET;
    static public String[] data_buf = new String[100];
    phpDown task;
    phpDown task2;
    phpDown task3;
    phpDown task4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.motionsit_popup);
        stepCountET = (TextView)findViewById(R.id.edtstep);
        distancesTotalET = (TextView)findViewById(R.id.edtPw);
        fatburnminET = (TextView)findViewById(R.id.edtName);
        restbpmET = (TextView)findViewById(R.id.edtNum);

        Intent intent = getIntent();
        String From = " "+ intent.getStringExtra("DEVICE_ID");
        DEVICE_ID=From.substring(1);
        task = new phpDown();
        task2 = new phpDown();
        task3 = new phpDown();
        task4 = new phpDown();

        task.execute("http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id="+DEVICE_ID+"&toilet_id=24");

         }



    private class phpDown extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... urls) {
            int i =0;

            StringBuilder jsonHtml = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    data_buf = new String[100];
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            data_buf[i] = br.readLine();
                            if(data_buf[i] == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            Log.d("DB아이템"+mode+" : ",data_buf[i]);
                            if(mode==0)
                            {stepCount=data_buf[i];}
                            else if(mode==1)
                            {distancesTotal=data_buf[i];}
                            else if(mode==2)
                            {fatburnmin=data_buf[i];}
                            else if(mode==3)
                            {restbpm=data_buf[i];}
                            i= i+1;
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }

            return jsonHtml.toString();

        }

        protected void onPostExecute(String str){
            if(mode==0)
            {stepCountET.setText(stepCount);
            mode=1;
            task2.execute("http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id="+DEVICE_ID+"&toilet_id=24&toilet_times");}
            else if (mode==1) {
                distancesTotalET.setText(distancesTotal);
                mode=2;
                task3.execute("http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id="+DEVICE_ID+"&time_data");}

            else if (mode ==2) {
                fatburnminET.setText(fatburnmin);
                mode = 3;
                task4.execute("http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id=" + DEVICE_ID + "&one_place");
            }
            else if (mode ==3) {
                restbpmET.setText(restbpm);
            }
            }

        }


    //확인 버튼 클릭
    public void mOnClose(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        finish();
    }
}


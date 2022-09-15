package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


/**
 * Created by user on 2018-08-08.
 */
public class smartbandPopup extends Activity {
    private String Today;
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
    private TextView fatburnminET;
    private TextView stepCountET;
    private TextView distancesTotalET;
    private TextView fatburnbpmET;
    private TextView restbpmET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        restbpm=intent.getStringExtra("restbpm");
        fatburnbpm=intent.getStringExtra("fatburnbpm");
        fatburnmin=intent.getStringExtra("fatburnmin");
        distancesTotal=intent.getStringExtra("distancesTotal");
        peakbpm=intent.getStringExtra("peakbpm");
        stepCount=intent.getStringExtra("stepCount");
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.smartband_popup);
        stepCountET = (TextView)findViewById(R.id.edtstep);
        distancesTotalET = (TextView)findViewById(R.id.edtPw);
        fatburnminET = (TextView)findViewById(R.id.edtName);
        restbpmET = (TextView)findViewById(R.id.edtNum);
        fatburnbpmET = (TextView)findViewById(R.id.edtAddress);

        stepCountET.setText(stepCount);
        distancesTotalET.setText(distancesTotal);
        fatburnminET.setText(fatburnmin);
        restbpmET.setText(restbpm);
        fatburnbpmET.setText(fatburnbpm);





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


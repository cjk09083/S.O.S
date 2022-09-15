package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Created by user on 2018-08-08.
 */
public class motionPopup extends Activity {
    private String Today;
    private String device_id="";
    private String Year;
    private String Month;
    private String Day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd-HH-mm-ss-SSS", Locale.KOREA);
        Date date = new Date();
        Today = formatter.format(date);
        StringTokenizer datestok = new StringTokenizer(Today, "-");
        Year=datestok.nextToken();
        Month=datestok.nextToken();
        Day=datestok.nextToken();
        int day = Integer.valueOf(Day);
        if(day>9)
        {Day=  String.format("%02d",day);}
        else
        {Day=  String.format("%01d",day);}
        Intent intent = getIntent();
        device_id = " "+ intent.getStringExtra("DEVICE_ID");
        if(device_id.length()>10)
        {device_id=device_id.substring(1);}
        Log.d("메세지액티비티에서 가져온 ID",device_id);

        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.motion_popup);
        final DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);



        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////// 달력에서 날짜 지정하기//////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("달력에서 선택 ", year+","+ monthOfYear+","+dayOfMonth);
                        Year = String.format("%02d",year-2000);
                        Month=  String.format("%02d",(monthOfYear+1)); //왜 한달이 적지
                        if(dayOfMonth>9)
                        {Day=  String.format("%02d",dayOfMonth);}
                        else
                        {Day=  String.format("%01d",dayOfMonth);}
                    }
                });


    }


    //데이터보기 버튼 클릭
    public void mOnMove(View v){
        Log.d("모션 팝업","데이터보기 버튼 클릭");
        Intent day_motion_intent = new Intent(motionPopup.this, motion_web_day.class);
        day_motion_intent.putExtra("DEVICE_ID", device_id);
        day_motion_intent.putExtra("year", Year);
        day_motion_intent.putExtra("month", Month);
        day_motion_intent.putExtra("day", Day);
        startActivity(day_motion_intent);
        Log.d("선택한날짜 모션 열기 ", "with ID : "+ device_id);
        finish();
    }

    //취소 버튼 클릭
    public void mOnClose(View v){

        Log.d("모션 팝업","취소버튼 클릭");
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


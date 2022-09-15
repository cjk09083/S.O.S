package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Created by user on 2018-08-08.
 */
public class temphumidPopup extends Activity {
    private String Today;

    private String device_id="";
    private String Year;
    private String Month;
    private String Day;
    private String EndYear;
    private String EndMonth;
    private String EndDay;
    private Button StartBtn;
    private Button EndBtn;
    private DatePicker StartDatePicker;
    private DatePicker EndDatePicker;


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
        EndYear=Year;EndMonth=Month;EndDay=Day;

        Intent intent = getIntent();
        device_id = " "+ intent.getStringExtra("DEVICE_ID");
        if(device_id.length()>10)
        {device_id=device_id.substring(1);}
        Log.d("메세지액티비티에서 가져온 ID",device_id);

        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.temphumid_popup);
        StartDatePicker = (DatePicker)findViewById(R.id.datePicker);
        EndDatePicker = (DatePicker)findViewById(R.id.TimePicker);
        StartBtn = (Button)findViewById(R.id.start_date_btn);
        EndBtn = (Button)findViewById(R.id.end_date_btn);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////// 달력에서 시작날짜 지정하기////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        StartDatePicker.init(StartDatePicker.getYear(), StartDatePicker.getMonth(), StartDatePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("달력에서 선택 (시작)", year+","+ monthOfYear+","+dayOfMonth);
                        Year = String.format("%02d",year-2000);
                        Month=  String.format("%02d",(monthOfYear+1)); //왜 한달이 적지
                        if(dayOfMonth>9)
                        {Day=  String.format("%02d",dayOfMonth);}
                        else
                        {Day=  String.format("%01d",dayOfMonth);}                    }
                });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////// 달력에서 종료날짜 지정하기////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        EndDatePicker.init(EndDatePicker.getYear(), EndDatePicker.getMonth(), EndDatePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("달력에서 선택 (종료)", year+","+ monthOfYear+","+dayOfMonth);
                        EndYear = String.format("%02d",year-2000);
                        EndMonth=  String.format("%02d",(monthOfYear+1)); //왜 한달이 적지
                        if(dayOfMonth>9)
                        {EndDay=  String.format("%02d",dayOfMonth);}
                        else
                        {EndDay=  String.format("%01d",dayOfMonth);}
                    }
                });



        //From 버튼 클릭
        StartBtn.setOnClickListener(new View.OnClickListener() {
                                         public void onClick(View v) {
            Log.d("온습도 팝업","From버튼 클릭");
            int backColor = getResources().getColor(R.color.theme_color);
            StartBtn.setBackgroundColor(backColor);
            int backColor2 = getResources().getColor(R.color.theme_color2);
            EndBtn.setBackgroundColor(backColor2);
            StartDatePicker.setVisibility(View.VISIBLE);
            EndDatePicker.setVisibility(View.GONE);
        }});

        //To 버튼 클릭
        EndBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
            Log.d("온습도 팝업","To버튼 클릭");
                    int backColor = getResources().getColor(R.color.theme_color);
                    int backColor2 = getResources().getColor(R.color.theme_color2);
                    StartBtn.setBackgroundColor(backColor2);
                    EndBtn.setBackgroundColor(backColor);
            EndDatePicker.setVisibility(View.VISIBLE);
            StartDatePicker.setVisibility(View.GONE);
                }});


    }


    //데이터보기 버튼 클릭
    public void mOnMove(View v){
        Log.d("온습도 팝업","선택한기간 버튼 클릭");
        Log.d("온습도 팝업데이터",Year+","+Month+","+Day+","+EndYear+","+EndMonth+","+EndDay);
        int Startdate = Integer.valueOf(Year+Month+Day);
        String EndDay2 = "";
        if(EndDay.length()==1)
        { EndDay2 = "0"+EndDay;}
        else{ EndDay2 = EndDay;}
        int Enddate = Integer.valueOf(EndYear+EndMonth+EndDay2);
        Log.d("온습도 팝업데이터2",Startdate+"vs"+Enddate);
        if(Startdate<Enddate){
        Intent temphumid_intent = new Intent(temphumidPopup.this, temphumid_web_total.class);
        temphumid_intent.putExtra("DEVICE_ID", device_id);
        temphumid_intent.putExtra("year", Year);
        temphumid_intent.putExtra("month", Month);
        temphumid_intent.putExtra("day", Day);
        temphumid_intent.putExtra("endyear", EndYear);
        temphumid_intent.putExtra("endmonth", EndMonth);
        temphumid_intent.putExtra("endday", EndDay);
        temphumid_intent.putExtra("mode", 2);
        startActivity(temphumid_intent);
        Log.d("선택한기간 온습도 열기 ", "with ID : "+ device_id);
        finish();
        } else { Toast.makeText(temphumidPopup.this, "기간을 재선택 해주세요", Toast.LENGTH_SHORT).show(); }
    }


    //월별 버튼 클릭
    public void mOnMiddle(View v){
        Log.d("온습도 팝업","월별 버튼 클릭");
        Intent temphumid_intent = new Intent(temphumidPopup.this, temphumid_web_total.class);
        temphumid_intent.putExtra("DEVICE_ID", device_id);
        temphumid_intent.putExtra("year", Year);
        temphumid_intent.putExtra("mode", 1);
        startActivity(temphumid_intent);
        Log.d("온습도 월별평균 열기 ", "with ID : " + device_id+", Year : "+ Year);
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
        finish();
    }


/*
    온습도 전체평균 : 아이디
    http://minsanggyu2.cafe24.com/temphumid_total_graph.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e

    온습도 월별 평균 : 아이디, 연도
    http://minsanggyu2.cafe24.com/temphumid_month_graph.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&year_time=18

    온습도 특정기간 : 시작년도&시작월&시작일&최종년도&최종월&최종일
    http://minsanggyu2.cafe24.com/temphumid_select_graph.php?year_time_start=18&month_time_start=08&day_time_start=26&year_time_end=18&month_time_end=08&day_time_end=31&device_id=9c8deecc68ba40bb89a1635f397c5f5e
*/

}


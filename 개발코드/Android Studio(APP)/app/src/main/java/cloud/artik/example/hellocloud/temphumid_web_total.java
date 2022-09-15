package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by user on 2018-08-12.
 */
public class temphumid_web_total extends Activity {

    private WebView mWebView;

    private WebSettings mWebSettings;
    private String device_id="";
    private int mode=0;
    private String year="";
    private String month="";
    private String day="";
    private String Endyear;
    private String Endmonth;
    private String Endday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temphumid_web_total);
        Intent intent = getIntent();
        device_id = " "+ intent.getStringExtra("DEVICE_ID");
        if(device_id.length()>10)
        {device_id=device_id.substring(1);}
        Log.d("모션에서 가져온 ID",device_id);
        year = " "+ intent.getStringExtra("year");
        if(year.length()>1)
        {year=year.substring(1);}
        month = " "+ intent.getStringExtra("month");
        if(month.length()>1)
        {month=month.substring(1);}
        day = " "+ intent.getStringExtra("day");
        if(day.length()>1)
        {day=day.substring(1);}
        Endyear = " "+ intent.getStringExtra("endyear");
        if(Endyear.length()>1)
        {Endyear=Endyear.substring(1);}
        Endmonth = " "+ intent.getStringExtra("endmonth");
        if(Endmonth.length()>1)
        {Endmonth=Endmonth.substring(1);}
        Endday = " "+ intent.getStringExtra("endday");
        if(Endday.length()>1)
        {Endday=Endday.substring(1);}
        mode = intent.getIntExtra("mode",0);
        Log.d("온습도 선택창에서 가져온 데이터","mode:"+mode+" Start:"+year+"-"+ month+"-"+day+" End:"+Endyear+"-"+Endmonth+"-"+Endday );


        mWebView = (WebView)findViewById(R.id.temphumid_webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings.setSupportZoom(true);
        if(mode==0)
        {mWebView.loadUrl("http://minsanggyu2.cafe24.com/temphumid_total_graph.php?device_id="+device_id);
        Log.d("온습도 전체 평균","http://minsanggyu2.cafe24.com/temphumid_total_graph.php?device_id="+device_id);}

        if(mode==1)
        {mWebView.loadUrl("http://minsanggyu2.cafe24.com/temphumid_month_graph.php?device_id="+device_id+"&year_time="+year);
            Log.d("온습도 월별 평균","http://minsanggyu2.cafe24.com/temphumid_month_graph.php?device_id="+device_id+"&year_time="+year);}


        if(mode==2)
        {mWebView.loadUrl("http://minsanggyu2.cafe24.com/temphumid_select_graph.php?year_time_start="+year+"&month_time_start="+month+"&day_time_start="+day+"" +
                "&year_time_end="+Endyear+"&month_time_end="+Endmonth+"&day_time_end="+Endday+"&device_id="+device_id);
            Log.d("온습도 기간별 평균","http://minsanggyu2.cafe24.com/temphumid_select_graph.php?year_time_start="+year+"&month_time_start="+month+"&day_time_start="+day+"" +
                    "&year_time_end="+Endyear+"&month_time_end="+Endmonth+"&day_time_end="+Endday+"&device_id="+device_id);}
/*
    온습도 전체평균 : 아이디
    http://minsanggyu2.cafe24.com/temphumid_total_graph.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e

    온습도 월별 평균 : 아이디, 연도
    http://minsanggyu2.cafe24.com/temphumid_month_graph.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&year_time=18

    온습도 특정기간 : 시작년도&시작월&시작일&최종년도&최종월&최종일
    http://minsanggyu2.cafe24.com/temphumid_select_graph.php?year_time_start=18&month_time_start=08&day_time_start=26&year_time_end=18&month_time_end=08&day_time_end=31&device_id=9c8deecc68ba40bb89a1635f397c5f5e
*/

    }}

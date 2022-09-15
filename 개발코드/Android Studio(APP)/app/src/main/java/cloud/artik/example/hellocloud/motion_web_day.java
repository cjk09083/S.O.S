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
public class motion_web_day extends Activity {

    private WebView mWebView_sun;
    private WebSettings mWebSettings_sun;
    private String device_id="";
    private String year="";
    private String month="";
    private String day="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_web_day);
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
        Log.d("모션 선택창에서 가져온 날짜",year+","+ month+","+day);


        //!~~~~~~~~~~일요일~~~~~~~~~~~~~~~~~!//
        mWebView_sun = (WebView)findViewById(R.id.motion_webview);
        mWebView_sun.setWebViewClient(new WebViewClient());
        mWebSettings_sun = mWebView_sun.getSettings();
        mWebSettings_sun.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_sun.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_sun.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_sun.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_sun.setSupportZoom(true);
        mWebView_sun.loadUrl("http://minsanggyu2.cafe24.com/motion_day_graph.php?year_time="+year+"&month_time="+month+"&day_time="+day+"&device_id="+device_id);
        Log.d("모션 날짜","http://minsanggyu2.cafe24.com/motion_day_graph.php?year_time="+year+"&month_time="+month+"&day_time="+day+"&device_id="+device_id);

        // 특정날 : 년도&월&일
        //http://minsanggyu2.cafe24.com/motion_day_graph.php?year_time=18&month_time=08&day_time=30&device_id=9c8deecc68ba40bb89a1635f397c5f5e
        //http://minsanggyu2.cafe24.com/motion_day_graph.php?year_time=18&month_time=07&day_time=31&device_id=9c8deecc68ba40bb89a1635f397c5f5e
    }}

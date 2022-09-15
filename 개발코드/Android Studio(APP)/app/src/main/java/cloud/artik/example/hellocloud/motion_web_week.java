package cloud.artik.example.hellocloud;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost;

/**
 * Created by user on 2018-08-12.
 */
public class motion_web_week extends TabActivity {

    private WebView mWebView_sun;
    private WebView mWebView_mon;
    private WebView mWebView_tue;
    private WebView mWebView_wed;
    private WebView mWebView_thu;
    private WebView mWebView_fri;
    private WebView mWebView_sat;
    private WebSettings mWebSettings_sun;
    private WebSettings mWebSettings_mon;
    private WebSettings mWebSettings_tue;
    private WebSettings mWebSettings_wed;
    private WebSettings mWebSettings_thu;
    private WebSettings mWebSettings_fri;
    private WebSettings mWebSettings_sat;
    private String device_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        device_id = " "+ intent.getStringExtra("DEVICE_ID");
        if(device_id.length()>10)
        {device_id=device_id.substring(1);}
        Log.d("모션메서 가져온 ID",device_id);


        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.activity_motion_web_week, tabHost.getTabContentView(), true);

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("일요일")
                .setContent(R.id.sun_webview));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("월요일")
                .setContent(R.id.mon_webview));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("화요일")
                .setContent(R.id.tue_webview));
        tabHost.addTab(tabHost.newTabSpec("tab4")
                .setIndicator("수요일")
                .setContent(R.id.wed_webview));
        tabHost.addTab(tabHost.newTabSpec("tab5")
                .setIndicator("목요일")
                .setContent(R.id.thu_webview));
        tabHost.addTab(tabHost.newTabSpec("tab6")
                .setIndicator("금요일")
                .setContent(R.id.fri_webview));
        tabHost.addTab(tabHost.newTabSpec("tab7")
                .setIndicator("토요일")
                .setContent(R.id.sat_webview));

        //!~~~~~~~~~~일요일~~~~~~~~~~~~~~~~~!//
        mWebView_sun = (WebView)findViewById(R.id.sun_webview);
        mWebView_sun.setWebViewClient(new WebViewClient());
        mWebSettings_sun = mWebView_sun.getSettings();
        mWebSettings_sun.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_sun.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_sun.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_sun.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_sun.setSupportZoom(true);
        mWebView_sun.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Sun&device_id="+device_id);

        //!~~~~~~~~~~월요일~~~~~~~~~~~~~~~~~!//
        mWebView_mon = (WebView)findViewById(R.id.mon_webview);
        mWebView_mon.setWebViewClient(new WebViewClient());
        mWebSettings_mon = mWebView_mon.getSettings();
        mWebSettings_mon.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_mon.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_mon.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_mon.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_mon.setSupportZoom(true);
        mWebView_mon.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Mon&device_id="+device_id);

        //!~~~~~~~~~~화요일~~~~~~~~~~~~~~~~~!//
        mWebView_tue = (WebView)findViewById(R.id.tue_webview);
        mWebView_tue.setWebViewClient(new WebViewClient());
        mWebSettings_tue = mWebView_tue.getSettings();
        mWebSettings_tue.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_tue.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_tue.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_tue.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_tue.setSupportZoom(true);
        mWebView_tue.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Tue&device_id="+device_id);

        //!~~~~~~~~~~수요일~~~~~~~~~~~~~~~~~!//
        mWebView_wed = (WebView)findViewById(R.id.wed_webview);
        mWebView_wed.setWebViewClient(new WebViewClient());
        mWebSettings_wed = mWebView_wed.getSettings();
        mWebSettings_wed.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_wed.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_wed.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_wed.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_wed.setSupportZoom(true);
        mWebView_wed.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Wed&device_id="+device_id);

        //!~~~~~~~~~~목요일~~~~~~~~~~~~~~~~~!//
        mWebView_thu = (WebView)findViewById(R.id.thu_webview);
        mWebView_thu.setWebViewClient(new WebViewClient());
        mWebSettings_thu = mWebView_thu.getSettings();
        mWebSettings_thu.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_thu.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_thu.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_thu.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_thu.setSupportZoom(true);
        mWebView_thu.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Thu&device_id="+device_id);

        //!~~~~~~~~~~금요일~~~~~~~~~~~~~~~~~!//
        mWebView_fri = (WebView)findViewById(R.id.fri_webview);
        mWebView_fri.setWebViewClient(new WebViewClient());
        mWebSettings_fri = mWebView_fri.getSettings();
        mWebSettings_fri.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_fri.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_fri.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_fri.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_fri.setSupportZoom(true);
        mWebView_fri.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Fri&device_id="+device_id);

        //!~~~~~~~~~~토요일~~~~~~~~~~~~~~~~~!//
        mWebView_sat = (WebView)findViewById(R.id.sat_webview);
        mWebView_sat.setWebViewClient(new WebViewClient());
        mWebSettings_sat = mWebView_sat.getSettings();
        mWebSettings_sat.setJavaScriptEnabled(true);
        //화면비율관련
        mWebSettings_sat.setUseWideViewPort(true); //wide viewport를 사용하도록 설정
        mWebSettings_sat.setLoadWithOverviewMode(true); //컨텐츠가 웹뷰보다 클 경우 스크린 크기에맞게 조정
        //웹뷰 멀티 터치 가능하게 (줌기능)
        mWebSettings_sat.setBuiltInZoomControls(true); //줌 아이콘 사용 설정
        mWebSettings_sat.setSupportZoom(true);
        mWebView_sat.loadUrl("http://minsanggyu2.cafe24.com/motion_daily_graph.php?daily_time=Sat&device_id="+device_id);

    }}

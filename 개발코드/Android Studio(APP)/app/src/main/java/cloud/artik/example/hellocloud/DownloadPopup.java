package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


/**
 * Created by user on 2018-08-08.
 */
public class DownloadPopup extends Activity {

    TextView txtText1;
    TextView txtText2;
    TextView txtText3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.download_popup);

        //UI 객체생성
        txtText1 = (TextView)findViewById(R.id.txtText1);
        txtText2 = (TextView)findViewById(R.id.txtText2);
        txtText3 = (TextView)findViewById(R.id.txtText3);

        //데이터 가져오기
        Intent intent = getIntent();
        String sdid = intent.getStringExtra("sdid");
        String mid = intent.getStringExtra("mid");
        String data = intent.getStringExtra("data");

        txtText1.setText("디바이스 ID: " + sdid);
        txtText2.setText("메세지 ID : " + mid);
        txtText3.setText("데이터 : " + data);

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        Log.d("state",getIntent().getFlags()+ "," + Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
       finish();
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
        finish();
        return;
    }
}


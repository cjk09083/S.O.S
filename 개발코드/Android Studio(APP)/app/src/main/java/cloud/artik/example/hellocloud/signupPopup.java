package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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


/**
 * Created by user on 2018-08-08.
 */
public class signupPopup extends Activity {

    private String name="";
    private String reference ="";
    private String id="";
    private String pw="";
    private String num="";
    private String address="";
    private EditText nameET;
    private EditText ageET;
    private EditText idET;
    private EditText pwET;
    private EditText addressET;
    private EditText numET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup_popup);
        nameET = (EditText)findViewById(R.id.edtName);
        ageET = (EditText)findViewById(R.id.edtAge);
        idET = (EditText)findViewById(R.id.edtId);
        pwET = (EditText)findViewById(R.id.edtPw);
        addressET = (EditText)findViewById(R.id.edtAddress);
        numET = (EditText)findViewById(R.id.edtNum);








    }










    //데이터보기 버튼 클릭
    public void mOnMove(View v){
        Log.d("회원가입 팝업","회원가입 버튼 클릭");
        name=nameET.getText().toString();
        reference =ageET.getText().toString();
        id=idET.getText().toString();
        pw=pwET.getText().toString();
        address=addressET.getText().toString();
        num=numET.getText().toString();
        if(name.isEmpty()||reference.isEmpty()||id.isEmpty()||pw.isEmpty()||address.isEmpty()||num.isEmpty())
        {        Toast.makeText(this, "정보중에 공백이 존재합니다.", Toast.LENGTH_LONG).show();
        }
        else { new SendPost().execute();
        String user_data;
        user_data=id+"_"+pw+"_"+name+"_"+address+"_"+num+"_"+reference;
            save_user_data(user_data);
            finish();}

    }

    //취소 버튼 클릭
    public void mOnClose(View v){
        Log.d("회원가입 팝업","취소버튼 클릭");
        Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
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

    public class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {
            String content = executeClient();
            return content;
        }}

    protected void onPostExecute(String result) {
        // 모두 작업을 마치고 실행할 일 (메소드 등등)
    }

    // 실제 전송하는 부분
    public String executeClient() {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("input", "userinsert"));
        post.add(new BasicNameValuePair("userID", id));
        post.add(new BasicNameValuePair("userPw", pw));
        post.add(new BasicNameValuePair("userName", name));
        post.add(new BasicNameValuePair("userAdd", address));
        post.add(new BasicNameValuePair("userNum", num));
        post.add(new BasicNameValuePair("userRef", reference));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://minsanggyu2.cafe24.com/user_data.php");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response=client.execute(httpPost);
            Log.d("Post 결과", ""+response);
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

    public void save_user_data(final String data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // 전체 데이터를 txt로 저장
                String fileName = "UserData" + ".txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath);
                // 일치하는 폴더가 없으면 생성
                if (!file.exists()) {
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
                }
                // txt 파일 생성
                String fileData = data;
                Log.d("저장되는 유저 데이터",data);

                File savefile = new File(dirPath + "/" + fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(savefile);
                    fos.write(fileData.getBytes());
                    fos.close();
                    Toast.makeText(getApplicationContext(), fileName + "가  저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }
        });
    }
}


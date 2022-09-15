package cloud.artik.example.hellocloud;

import android.os.AsyncTask;

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
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Administrator on 2018-09-04.
 */
public class Httppost extends AsyncTask<Void, Void, String> {
    protected String doInBackground(Void... unused) {
        String content = executeClient();
        return content;
    }

    protected void onPostExecute(String result) {
        // 모두 작업을 마치고 실행할 일 (메소드 등등)
    }

    // 실제 전송하는 부분
    public String executeClient() {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("userId", "4321"));
        post.add(new BasicNameValuePair("userName", "1234"));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://minsanggyu2.cafe24.com/user_data.php");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response=client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
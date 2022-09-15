package cloud.artik.example.hellocloud;

/**
 * Created by user on 2018-09-04.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

public class php_join extends Activity {

    String name ="";
    String age ="";
    String address ="";
    String seniornum ="";
    String imagepath ="";
    String device_ID ="";
    String brace_ID ="";
    String webcam_ID ="";
    String total_item ="";

    private String userdata="";
    static public String[] data_buf = new String[100];

    TextView txtView;
    phpDown task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.phpjoin);
        task = new phpDown();
        //txtView = (TextView)findViewById(R.id.txtView);
        Intent intent = getIntent();
        String From = " "+ intent.getStringExtra("from");
        String ID = " "+ intent.getStringExtra("ID");
        String PW = " "+ intent.getStringExtra("PW");
        String Ref = " "+ intent.getStringExtra("Ref");
        From=From.substring(1);
        ID=ID.substring(1);
        PW=PW.substring(1);
        Ref=Ref.substring(1);
        if(From.equals("login"))
        {task.execute("http://minsanggyu2.cafe24.com/get_user_data.php?userID="+ID+"&userPw="+PW+"&userRef="+Ref);
        Log.d("로그인:"+ID+","+PW,"성공");}
        else { task.execute("http://minsanggyu2.cafe24.com/get_user_data.php?userID=재국2&userPw=123&userRef=1"); }

    }


    private class phpDown extends AsyncTask<String, Integer,String>{
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
                            Log.d("DB아이템0 : "+i,data_buf[i]);
                            i= i+1;
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }

            for(int j=0;j<i;j++)
            {            Log.d("DB아이템1 : "+j,data_buf[j]);
                save_user_data(data_buf[j]);}

            return jsonHtml.toString();

        }

        protected void onPostExecute(String str){
            if(!total_item.isEmpty())
            {save_userlist_data(total_item.substring(1));}
            else{save_userlist_data("");}
            Intent mainActivityIntent = new Intent(php_join.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }

    }


    //유저정보 액티비티를 열때 가져온 기본정보 저장하기
    public void save_user_data(final String data) {
        Log.d("DB아이템2",data);
        String data2="";
        String data3="";
        if(data.length()>10) {
            data2 = data;
            String name = " ";
            String age = " ";
            StringTokenizer stok = new StringTokenizer(data2, ",", false);
            String data_dum=stok.nextToken();
            data3=stok.nextToken();
            Log.d("DB아이템3", data3);

            StringTokenizer stok2 = new StringTokenizer(data3, "_", false);
            name=stok2.nextToken();
            age=stok2.nextToken();
            Log.d("DB아이템4", name+"_"+age+".txt");
            String fileName = name + "_" + age + ".txt";
            String dirPath = getFilesDir().getAbsolutePath();
            File file = new File(dirPath);
            // 일치하는 폴더가 없으면 생성
            if (!file.exists()) {
                file.mkdirs();
                Toast.makeText(getApplicationContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
            }
            // txt 파일 생성
            String fileData = data3;
            File savefile = new File(dirPath + "/" + fileName);
            try {
                FileOutputStream fos = new FileOutputStream(savefile);
                fos.write(fileData.getBytes());
                fos.close();
                //Toast.makeText(getApplicationContext(), fileName + " 이  저장됨", Toast.LENGTH_SHORT).show();
                Log.d("DB아이템"+fileName + " 이  저장됨", fileData);
                String listdata= data.substring(0,3)+"_"+name+"("+age+")"+"_없음_없음_없음";
                Log.d("DB아이템 listdata",listdata);
                total_item =total_item+","+ listdata;
            } catch (IOException e) {
            }


        }
    }

    // 읽어온 유저목록 저장
    public void save_userlist_data(final String data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // 전체 데이터를 txt로 저장
                String fileName = "UserList" + ".txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath);
                // 일치하는 폴더가 없으면 생성
                if (!file.exists()) {
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
                }
                // txt 파일 생성
                String fileData = data;
                Log.d("저장되는 유저목록 데이터",data);

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



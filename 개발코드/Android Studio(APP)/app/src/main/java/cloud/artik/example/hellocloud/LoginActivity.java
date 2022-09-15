/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends Activity {
    static final String TAG = "LoginActivity";
    private EditText ID_ET;
    private EditText PW_ET;
    private EditText CODE_ET;

    private String ID="";
    private String PW="";
    private String CODE="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "::onCreate");
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ID_ET=(EditText)findViewById(R.id.txtText1) ;
        PW_ET=(EditText)findViewById(R.id.txtText2) ;
        CODE_ET=(EditText)findViewById(R.id.txtText3) ;

    }


    //로그인 버튼 클릭
    public void mOnMove(View v){
        Log.d("로그인 액티비티","로그인 버튼 클릭");
        ID=ID_ET.getText().toString();
        PW=PW_ET.getText().toString();
        CODE=CODE_ET.getText().toString();
        if(!ID.isEmpty()&&!PW.isEmpty()){
        Intent phpstart = new Intent(this, php_join.class);
        phpstart.putExtra("from","login");
        phpstart.putExtra("ID",ID);
        phpstart.putExtra("PW",PW);
        phpstart.putExtra("Ref","1");
        startActivity(phpstart);
        String user_data = ID + "_" + PW +"_" + " " + "_" + " " + "_" + " " + "_" + "1";
            save_user_data(user_data);
            if(!CODE.isEmpty())
            {save_appcode_data(CODE);}
        }
        else {

        }
        finish();
    }


    //회원가입 버튼 클릭
    public void mOnMiddle(View v){
        Log.d("로그인 액티비티","회원가입 버튼 클릭");


        Intent MotionWeb = new Intent(LoginActivity.this, signupPopup.class);
        startActivity(MotionWeb);
    }

    private void startmain() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra("from","login");
        startActivity(mainActivityIntent);
    }


    public void save_appcode_data(final String data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // 전체 데이터를 txt로 저장
                String fileName = "Appcode" + ".txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath);
                // 일치하는 폴더가 없으면 생성
                if (!file.exists()) {
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
                }
                // txt 파일 생성
                String fileData = data;
                Log.d("저장되는 앱코드 데이터",data);

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

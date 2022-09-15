package cloud.artik.example.hellocloud;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import cloud.artik.example.hellocloud.databinding.ActivityMainBinding;
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

public class MainActivity extends android.support.v7.app.AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static ArrayList<String> mArrayList;
    private static ArrayList<String> del_ArrayList;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private static final String TAG = "MainActivity";
    private static  String UserData ;
    private int delmode=0;
    private String[] names = {"회원1","회원2","회원3","회원4",};
    private static final int LAYOUT = R.layout.activity_main;
    private ActivityMainBinding mainBinding;
    private RecyclerView.Adapter adapter;
    private EditText nameET;
    private EditText ageET;
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();
    private int return_position;
    private String return_name;
    private String return_age;
    private String return_device_ID= "디바이스 ID";
    private String return_bracelet_ID= "팔찌 ID";
    private String return_webcam_ID= "웹캠 ID";
    private WebView mWebView;
    public static Activity AActivity;
    private String name="";
    private String reference ="";
    private String id="";
    private String pw="";
    private String num="";
    private String address="";
    private String sen_id="";
    private String sen_info="";
    private String mode="add";
    private String name_value;
    private String age_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ArrayList 생성
        mArrayList = new ArrayList<String>();
        del_ArrayList= new ArrayList<String>();
        open_userlist_data();
        // ArrayList 값 추가 (예시)
        //mArrayList.add("001_최재국(27)");
        //mArrayList.add("002_홍길동(25)");
        AActivity = MainActivity.this;
        mainBinding = DataBindingUtil.setContentView(this,LAYOUT);
        Button addBtn = (Button) findViewById(R.id.add_item_btn);
        final Button delBtn = (Button) findViewById(R.id.del_item_btn);
        Button delcancelBtn = (Button) findViewById(R.id.del_cancel_btn);

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        Button phpBtn = (Button)findViewById(R.id.phpjoin_btn);
        Button phpPost = (Button)findViewById(R.id.phpPost_btn) ;

        Intent intent = getIntent();
        return_position = intent.getIntExtra("return_position",10000);
        return_name = intent.getStringExtra("return_name");
        return_age = intent.getStringExtra("return_age");
        return_device_ID = intent.getStringExtra("return_device_ID");
        return_bracelet_ID = intent.getStringExtra("return_bracelet_ID");
        return_webcam_ID = intent.getStringExtra("return_webcam_ID");

        Log.d("메인 with",return_position+","+return_name +","+return_age +","+return_device_ID+","+return_bracelet_ID+","+return_webcam_ID);
        Log.d("메인 with",return_position+","+mArrayList.size());

        if(return_position <= mArrayList.size())
        {mArrayList.set(return_position,String.format("%03d",(return_position+1))+"_"+return_name+"("+return_age+")"+"_"+return_device_ID+"_"+return_bracelet_ID+"_"+return_webcam_ID);}

        Intent backintent = new Intent(
                getApplicationContext(),//현재제어권자
                Alarmservice.class); // 이동할 컴포넌트
        backintent.putExtra("DEVICE_ID","bfff16ca56be4c5896a0cfd8c3ce7e66");
        startService(backintent); // 백그라운드 서비스

        setRecyclerView();
        setRefresh();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //저장소 권한 얻기
        { checkVerify(); }
        else  {  startApp();  }


///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////



        addBtn.setOnClickListener(new View.OnClickListener() {  //회원추가
            public void onClick(View v) {
                inputdata();
                Log.d("삭제모드","끄기");
                delBtn.setText("삭제");
                delmode=0;
            }
        });

/////////////////////////////////////////////////////// 삭제 모드 버튼///////////////////////////////////////////////////

        delBtn.setOnClickListener(new View.OnClickListener() {  //마지막 회원삭제
            public void onClick(View v) {
                if(delmode==0)
                {Log.d("삭제모드","켜기");
                    del_ArrayList= new ArrayList<String>();

                    for(int i = 0; i < mArrayList.size(); i++)
                        {del_ArrayList.add(i,mArrayList.get(i));}
                    delBtn.setText("확인");
                    delmode=1;
                    return ;}
                if(delmode==1)
                {Log.d("삭제모드","끄기");
                    delBtn.setText("삭제");
                    delmode=0;
                    return ;}
            }
        });


/////////////////////////////////////////////////////// 삭제 취소버튼 ///////////////////////////////////////////////////

        delcancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(delmode==1)
                {Log.d("삭제모드","끄기");
                delBtn.setText("삭제");
                delmode=0;
                    mArrayList= new ArrayList<String>();

                    for(int i = 0; i < del_ArrayList.size(); i++)
                    {mArrayList.add(i,del_ArrayList.get(i));}
                    setData();}
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {  //로그인 화면
            public void onClick(View v) {
                loginstart();
                finish();
            }
        });

        phpBtn.setOnClickListener(new View.OnClickListener() {  //php get
            public void onClick(View v) {
                phpstart();
                Toast.makeText(MainActivity.this, "리스트 다운로드 성공.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        phpPost.setOnClickListener(new View.OnClickListener() {  //php post
            public void onClick(View v) {
        open_user_data();
        mode="add";
        new SendPost().execute();
                Toast.makeText(MainActivity.this, "리스트 업로드 성공.", Toast.LENGTH_SHORT).show();

            }
        });



    }



    private void loginstart() {
        Intent loginstart = new Intent(this, LoginActivity.class);
        startActivity(loginstart);}



    private void phpstart() {

        Intent phpstart = new Intent(this, php_join.class);
        startActivity(phpstart);

    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// 뒤로가기 키 두번으로 종료하기 //////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            //moveTaskToBack(true);   //백그라운드로
            //finishAffinity(); // 해당 앱의 루트 액티비티를 종료시킨다. (API  16미만은 ActivityCompat.finishAffinity())
            //System.runFinalization(); // 간단히 말해 현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
            //System.exit(0); // 현재 액티비티를 종료시킨다
            /*
            Intent backintent = new Intent(
                    getApplicationContext(),//현재제어권자
                    Alarmservice.class); // 이동할 컴포넌트
            backintent.putExtra("DEVICE_ID","9c8deecc68ba40bb89a1635f397c5f5e");
            stopService(backintent);
            startService(backintent); // 백그라운드 서비스
            */
            finish();
            //android.os.Process.killProcess(android.os.Process.myPid());       //어플 종료

        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "뒤로가기 버튼을 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        }


    }





    private void setRecyclerView(){
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        mainBinding.recyclerView.setHasFixedSize(true);

        // RecyclerView 에 Adapter 를 설정해줍니다.
        adapter = new RecyclerAdapter(mItems);

        mainBinding.recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());
        mainBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        //mainBinding.recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(48));

        mainBinding.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mainBinding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        if(delmode==0)
                        {
                            //Log.d("아이템정보",mArrayList.get(position));
                        UserData=mArrayList.get(position);
                        if(UserData.length()>3){
                        startMessageActivity(UserData, position);
                        finish();}
                        else{Toast.makeText(getApplicationContext(),"이름과 나이를 작성해주세요",Toast.LENGTH_SHORT).show();}}
                        if(delmode==1)
                        {
                            UserData=mArrayList.get(position);

                            Log.d("아이템정보",UserData);
                        String size = ""+(mArrayList.size()-1);
                            Log.d("아이템사이즈",position+","+size);

                            if(position==(mArrayList.size()-1))
                        {mArrayList.remove(mArrayList.size()-1);
                            Log.d((mArrayList.size()-1)+"번째 아이템","삭제1");
                            StringTokenizer stok = new StringTokenizer(UserData, "_", false);
                            String id=  stok.nextToken();
                            String name0=  stok.nextToken();
                            StringTokenizer stok2 = new StringTokenizer(name0, "(", false);
                            String name = stok2.nextToken();
                            String name2 = stok2.nextToken();
                            StringTokenizer stok3 = new StringTokenizer(name2, ")", false);
                            String age = stok3.nextToken();
                            name_value=name;
                            age_value=age;
                            mode="del";
                            Log.d("다음 아이템 삭제",name_value+"_"+age_value);

                            open_user_data();
                            new SendPost2().execute();

                        }
                        else
                        { mArrayList.remove(mArrayList.get(position));
                           Log.d(mArrayList.get(position)+"번째 아이템","삭제2");
                            StringTokenizer stok = new StringTokenizer(mArrayList.get(position), "_", false);
                            String id=  stok.nextToken();
                            String name0=  stok.nextToken();
                            StringTokenizer stok2 = new StringTokenizer(name0, "(", false);
                            String name = stok2.nextToken();
                            String name2 = stok2.nextToken();
                            StringTokenizer stok3 = new StringTokenizer(name2, ")", false);
                            String age = stok3.nextToken();
                            name_value=name;
                            age_value=age;
                            mode="del";
                            Log.d("다음 아이템 삭제2",name_value+"_"+age_value);

                            open_user_data();
                            new SendPost2().execute();

                        }
                            setData();
                        }

                        }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                        //Log.d("아이템정보",mArrayList.get(position));

                    }
                }));
        setData();
    }

    private void setRefresh(){
        mainBinding.swipeRefreshLo.setOnRefreshListener(this);
        mainBinding.swipeRefreshLo.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
    }


    @Override
    public void onRefresh() {
        mainBinding.recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mainBinding.recyclerView,"Refresh Success",Snackbar.LENGTH_SHORT).show();
                mainBinding.swipeRefreshLo.setRefreshing(false);
            }
        },500);
        setData();
    }

    private void setData(){
        mItems.clear();
        String total_item="";

        // RecyclerView 에 들어갈 데이터를 추가합니다.
        for(int i = 0; i < mArrayList.size(); i++){
            Log.d("아이템 리스트크기",""+mArrayList.size());
            String replace_item;
            if(getCharNumber(mArrayList.get(i),'_')>0)
            {replace_item =""+mArrayList.get(i);
            Log.d("아이템 내용",mArrayList.get(i));
            replace_item = String.format("%03d",(i+1))+replace_item.substring(3);
            Log.d("바뀐 아이템 내용",replace_item);
            mArrayList.set(i,replace_item);
            mItems.add(new RecyclerItem(replace_item));
            total_item =total_item+","+replace_item;}
        }

        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
        if(!total_item.isEmpty())
        {Log.d("최종아이템 내용",total_item.substring(1));
        save_userlist_data(total_item.substring(1));}
    }



    private void inputdata(){

        Context context = getApplicationContext();
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog,null);
        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("회원추가");       // 제목 설정
        ad.setView(layout);   // 내용 설정
// EditText 삽입하기
        final EditText nameET = (EditText)layout.findViewById(R.id.edtName);        //중간에 layout 넣는거 필수!!!!!!
        final EditText ageET = (EditText)layout.findViewById(R.id.edtAge);



// 확인 버튼 설정
        ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "Yes Btn Click");
                // Text 값 받아서 로그 남기기
                name_value = nameET.getText().toString();
                age_value = ageET.getText().toString();
                Log.d("회원이름", name_value);
                Log.d("회원나이", age_value);

                dialog.dismiss();     //닫기
                if(name_value.isEmpty())
                {name_value="공백";}
                if(age_value.isEmpty())
                {age_value="?";}
                mArrayList.add(String.format("%03d",(mArrayList.size()+1))+"_"+name_value+"("+age_value+")"+"_없음_없음_없음");
                mode="add";
                if(!name_value.equals("공백"))
                {open_user_data();
                new SendPost2().execute();}
                setData();
                // Event
            }
        });

// 취소 버튼 설정
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG,"No Btn Click");
                dialog.dismiss();     //닫기
                // Event
            }
        });
// 창 띄우기
        ad.show();
////////////////////////////////////////////////////////////////여기까지 대화상자//////////////////////////////////////////////
////////////////////////////////////////////////////////////////여기까지 대화상자//////////////////////////////////////////////
    }



    private void startMessageActivity(String data, int position) {
        Intent msgActivityIntent = new Intent(this, MessageActivity.class);
        StringTokenizer stok = new StringTokenizer(data, "_", false);
        String id=  stok.nextToken();
        String name0=  stok.nextToken();
        StringTokenizer stok2 = new StringTokenizer(name0, "(", false);
        String name = stok2.nextToken();
        String name2 = stok2.nextToken();
        StringTokenizer stok3 = new StringTokenizer(name2, ")", false);
        String age = stok3.nextToken();
        msgActivityIntent.putExtra("name", name);
        msgActivityIntent.putExtra("age", age);
        msgActivityIntent.putExtra("position", position);

        //msgActivityIntent.putExtra("sdid","bfff16ca56be4c5896a0cfd8c3ce7e66" );

        startActivity(msgActivityIntent);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// 특정 문자열 개수 세기//////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    int getCharNumber(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    // 현재 유저목록 저장
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
                    //Toast.makeText(getApplicationContext(), fileName + "가  저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }
        });
    }


    //액티비티를 열때 저장되어 있는 유저목록 불러오기
    public void open_userlist_data() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fileData2 = null;
                FileInputStream inFs;
                String fileName = "UserList.txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File files = new File(dirPath+"/"+fileName);
                //파일 유무를 확인합니다.
                if(files.exists()==true) {//파일이 있을시
                    try {
                        inFs = openFileInput(fileName);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        inFs.close();
                        fileData2 = (new String(txt)).trim();
                        Log.d("유저목록 데이터 불러오기 result", fileName + ":" + fileData2);
                        if(!fileData2.isEmpty()) {
                            int count = getCharNumber(fileData2, ',') + 1;  // count(아이템개수) = 쉼표의 개수 +1
                            StringTokenizer stok = new StringTokenizer(fileData2, ",", false);
                            Log.d("유저목록 데이터 불러오기 count", "" + count);

                            for (int data_i = 1; data_i <= count; data_i++) {
                                mArrayList.add(stok.nextToken());
                            }
                        }

                    } catch (IOException e) {
                        Log.d("Read data result", fileName + ": faled");
                    }
                }
            }
        });}


    //리스트 정보 업로드할때 저장되어 있는 유저데이터 불러오기
    public void open_user_data() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fileData2 = null;
                FileInputStream inFs;
                String fileName = "UserData.txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File files = new File(dirPath+"/"+fileName);
                //파일 유무를 확인합니다.
                if(files.exists()==true) {//파일이 있을시
                    try {
                        inFs = openFileInput(fileName);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        inFs.close();
                        fileData2 = (new String(txt)).trim();
                        Log.d("유저 데이터 불러오기 result", fileName + ":" + fileData2);
                        int count = getCharNumber(fileData2, '_') + 1;  // count(아이템개수) = 쉼표의 개수 +1
                        StringTokenizer stok = new StringTokenizer(fileData2, "_", false);
                        Log.d("유저 데이터 불러오기 count", "" + count);
                        id=stok.nextToken();
                        pw=stok.nextToken();
                        name=stok.nextToken();
                        address=stok.nextToken();
                        num=stok.nextToken();
                        reference=stok.nextToken();
                    } catch (IOException e) {
                        Log.d("Read data result", fileName + ": faled");
                    }
                }
            }
        });}
            /*
            Post 방식으로 Http 전송하기
            */
    public class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {

            String content="";

            for(int i = 0; i < mArrayList.size(); i++){
                //sen_id=String.format("%03d",(i+1));

                StringTokenizer stok = new StringTokenizer(mArrayList.get(i), "_", false);
                String id=  stok.nextToken();
                String name0=  stok.nextToken();
                StringTokenizer stok2 = new StringTokenizer(name0, "(", false);
                String name = stok2.nextToken();
                String name2 = stok2.nextToken();
                StringTokenizer stok3 = new StringTokenizer(name2, ")", false);
                String age = stok3.nextToken();
                open_user_data2(name,age);
                content = executeClient(sen_id,sen_info);
                Log.d("Post 아이템", sen_id+","+sen_info);
            }
            return content;
        }}


    public class SendPost2 extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {

            String content="";

            sen_id=name_value+"_"+age_value;
            sen_info=sen_id+"_없음_없음_없음_없음_없음_없음";
                content = executeClient(sen_id,sen_info);
                Log.d("Post 아이템", sen_id+","+sen_info);

            return content;
        }}

    protected void onPostExecute(String result) {
        // 모두 작업을 마치고 실행할 일 (메소드 등등)
    }

    // 실제 전송하는 부분
    public String executeClient(String senid, String seninfo) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        if(mode.equals("del"))
        {        post.add(new BasicNameValuePair("input", "delete"));

            post.add(new BasicNameValuePair("userID", id));
            post.add(new BasicNameValuePair("userPw", pw));
            post.add(new BasicNameValuePair("userRef", reference));
            post.add(new BasicNameValuePair("senID", senid+","));
            Log.d("Post 아이템 del", mode+","+id+","+pw+","+name+","+address+","+num+","+reference);

        }
        else {
            post.add(new BasicNameValuePair("input", "seninsert"));
            post.add(new BasicNameValuePair("userID", id));
            post.add(new BasicNameValuePair("userPw", pw));
            post.add(new BasicNameValuePair("userName", name));
            post.add(new BasicNameValuePair("userAdd", address));
            post.add(new BasicNameValuePair("userNum", num));
            post.add(new BasicNameValuePair("userRef", reference));
            post.add(new BasicNameValuePair("senID", senid+","));
            post.add(new BasicNameValuePair("seninfo", seninfo));
            Log.d("Post 아이템 add", mode+","+id+","+pw+","+name+","+address+","+num+","+reference);

        }

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
            //Log.d("Post 결과", ""+response);
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

    public void open_user_data2(final String Name, final String Age) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fileData2 = null;
                FileInputStream inFs;
                String fileName = Name + "_" + Age + ".txt";
                String dirPath = getFilesDir().getAbsolutePath();
                File files = new File(dirPath+"/"+fileName);
                //파일 유무를 확인합니다.
                if(files.exists()==true) {//파일이 있을시
                    try {
                        inFs = openFileInput(fileName);
                        byte[] txt = new byte[500];
                        inFs.read(txt);
                        inFs.close();
                        fileData2 = (new String(txt)).trim();
                        Log.d("데이터 불러오기 result", fileName + " : " + fileData2);
                        sen_info=fileData2;
                        StringTokenizer stok = new StringTokenizer(fileData2, "_", false);
                        String senname=  stok.nextToken();
                        String senage=  stok.nextToken();
                        sen_id= senname+"_"+senage;
                    } catch (IOException e) {
                        Log.d("Read data result", fileName + ": faled");
                    }
                }


            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// 저장소 런타임 권한 얻기 함수들 /////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void startApp()
    {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String strDir = file.getAbsolutePath();

        File oFile = new File(strDir+"/abc.txt");
        FileOutputStream oFos;
        try
        {
            oFos = new FileOutputStream(oFile);
            oFos.write("abc".getBytes(), 0, 3);
            oFos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void checkVerify()
    {
        if (
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )
        {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // ...
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        else
        {
            startApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (grantResults.length > 0)
            {
                for (int i=0; i<grantResults.length; ++i)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                        // 하나라도 거부한다면.
                        new android.app.AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        AActivity.finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();

                        return;
                    }
                }
                startApp();
            }
        }
    }

}


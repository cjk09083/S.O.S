package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.FileEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;


/**
 * Created by user on 2018-08-08.
 */
public class reservationPopup extends Activity {

    private String device_id="";
    private String Today;
    private static String Year;
    private static String Month;
    private static String Day;
    private static String Hour;
    private static String Minute;
    private static Button StartBtn;
    private static Button EndBtn;
    private DatePicker DatePicker;
    private TimePicker TimePicker;
    private TextView textView;
    private EditText textdataET;
    private String textdata;
    private String reservationTime;
    private static final String CHARSET_NAME = "UTF-8";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String AWS_ALGORITHM = "AWS4-HMAC-SHA256";

    private static final String SERVICE_NAME = "s3";
    private static final String REQUEST_TYPE = "aws4_request";

    private static final String UNSIGNED_PAYLOAD = "UNSIGNED-PAYLOAD";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd").withZoneUTC();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd\'T\'HHmmss\'Z\'").withZoneUTC();

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
        Hour=datestok.nextToken();
        Minute=datestok.nextToken();
        Log.d("?????? ?????? ??????",Year+"-"+Month+"-"+Day+"-"+Hour+"-"+Minute);


        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //???????????? ?????????
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reservation_popup);
        DatePicker = (DatePicker)findViewById(R.id.datePicker);
        TimePicker = (TimePicker) findViewById(R.id.TimePicker);
        StartBtn = (Button)findViewById(R.id.start_date_btn);
        EndBtn = (Button)findViewById(R.id.end_date_btn);
        textView= (TextView)findViewById(R.id.textview2);
        textdataET= (EditText)findViewById(R.id.textdata);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////// ???????????? ?????? ????????????////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        DatePicker.init(DatePicker.getYear(), DatePicker.getMonth(), DatePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("???????????? ??????", year+","+ monthOfYear+","+dayOfMonth);
                        Year = String.format("%02d",year-2000);
                        Month=  String.format("%02d",(monthOfYear+1)); //??? ????????? ??????
                        Day=  String.format("%02d",dayOfMonth);
                    }
                });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////// ???????????? ?????? ????????????////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        TimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d("???????????? ??????", hourOfDay+","+ minute);
                Hour=String.format("%02d",hourOfDay);
                Minute=  String.format("%02d",minute);

                //timeText.setText(hourOfDay+"??? "+minute+"???");
            }
        });


        //From ?????? ??????
        StartBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("????????? ??????","From?????? ??????");
                StartBtn.setBackgroundColor(Color.rgb(37,46,105));
                EndBtn.setBackgroundColor(Color.rgb(78,83,115));
                DatePicker.setVisibility(View.VISIBLE);
                TimePicker.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textdataET.setVisibility(View.GONE);
            }});

        //To ?????? ??????
        EndBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("????????? ??????","To?????? ??????");
                EndBtn.setBackgroundColor(Color.rgb(37,46,105));
                StartBtn.setBackgroundColor(Color.rgb(78,83,115));
                TimePicker.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                textdataET.setVisibility(View.VISIBLE);
                DatePicker.setVisibility(View.GONE);
            }});


    }


    //??????????????? ?????? ??????
    public void mOnMove(View v){
        Log.d("???????????? ??????","?????? ?????? ??????");
        textdata=textdataET.getText().toString();
        int minute = TimePicker.getMinute();
        /*
        if((6<minute)||(minute<=23))
        {minute=15;}
        else if((23<minute)||(minute<=38))
        {minute=30;}
        else if((38<minute)||(minute<=53))
        {minute=45;}
        else if((53<minute)||(minute<=6))
        {minute=0;}
        */
        reservationTime=String.format("%04d",DatePicker.getYear())+"-"+String.format("%02d",DatePicker.getMonth()+1)+"-"+String.format("%02d",DatePicker.getDayOfMonth())
                +"-"+String.format("%02d",TimePicker.getHour())+"-"+String.format("%02d",minute);
        textdata= reservationTime+" "+textdata;
        Log.d("???????????? ??????",textdata);
        if(!textdata.isEmpty())
        {new Thread() {
            public void run() {
                try {
                    upload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
            finish();}
    }



    //?????? ?????? ??????
    public void mOnClose(View v){

        Log.d("???????????? ??????","???????????? ??????");
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //??????????????? ????????? ????????????
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private static byte[] sign(String stringData, byte[] key) throws Exception {
        byte[] data = stringData.getBytes(CHARSET_NAME);
        Mac e = Mac.getInstance(HMAC_ALGORITHM);
        e.init(new SecretKeySpec(key, HMAC_ALGORITHM));
        return e.doFinal(data);
    }

    private static String hash(String text) throws Exception {
        MessageDigest e = MessageDigest.getInstance(HASH_ALGORITHM);
        e.update(text.getBytes(CHARSET_NAME));
        return new String(Hex.encodeHex(e.digest()));
    }

    private static void authorization(HttpUriRequest request, String regionName, String accessKey, String secretKey) throws Exception {
        Date now = new Date();
        String datestamp = DATE_FORMATTER.print(now.getTime());
        String timestamp = TIME_FORMATTER.print(now.getTime());
        request.addHeader("X-Amz-Date", timestamp);

        request.addHeader("X-Amz-Content-Sha256", UNSIGNED_PAYLOAD);

        TreeMap<String, String> sortedHeaders = new TreeMap<String, String>();
        for (Header header : request.getAllHeaders()) {
            sortedHeaders.put(header.getName(), header.getValue());
        }
        StringBuilder signedHeadersBuilder = new StringBuilder();
        for (String headerName : sortedHeaders.keySet()) {
            signedHeadersBuilder.append(headerName.toLowerCase()).append(";");
        }
        String signedHeaders = signedHeadersBuilder.toString();

        StringBuilder standardizedHeadersBuilder = new StringBuilder();
        for (String headerName : sortedHeaders.keySet()) {
            standardizedHeadersBuilder.append(headerName.toLowerCase()).append(":").append(sortedHeaders.get(headerName)).append("\n");
        }
        String standardizedHeaders = standardizedHeadersBuilder.toString();

        StringBuilder canonicalRequestBuilder = new StringBuilder(request.getMethod());
        canonicalRequestBuilder.append("\n")
                .append(request.getURI().getPath()).append("\n") // need to encoding
                .append("").append("\n") // need to sort by parameter name
                .append(standardizedHeaders).append("\n")
                .append(signedHeaders).append("\n")
                .append(UNSIGNED_PAYLOAD);

        String canonicalRequest = canonicalRequestBuilder.toString();
        System.out.println("--------------------------------------------------------------");
        System.out.println("canonicalRequest : " + canonicalRequest);
        System.out.println("--------------------------------------------------------------");

        StringBuilder scopeBuilder = new StringBuilder();
        scopeBuilder.append(datestamp).append("/").append(regionName).append("/").append(SERVICE_NAME).append("/").append(REQUEST_TYPE);
        String scope = scopeBuilder.toString();

        StringBuilder stringToSignBuilder = new StringBuilder(AWS_ALGORITHM);
        stringToSignBuilder.append("\n")
                .append(timestamp).append("\n")
                .append(scope).append("\n")
                .append(hash(canonicalRequest));

        String stringToSign = stringToSignBuilder.toString();
        System.out.println("--------------------------------------------------------------");
        System.out.println("stringToSign : " + stringToSign);
        System.out.println("--------------------------------------------------------------");

        byte[] kSecret = ("AWS4" + secretKey).getBytes(CHARSET_NAME);
        byte[] kDate = sign(datestamp, kSecret);
        byte[] kRegion = sign(regionName, kDate);
        byte[] kService = sign(SERVICE_NAME, kRegion);
        byte[] signingKey = sign(REQUEST_TYPE, kService);

        String signature = new String(Hex.encodeHex(sign(stringToSign, signingKey)));

        StringBuilder hashKeyBuilder = new StringBuilder(secretKey);
        hashKeyBuilder.append("-").append(regionName).append("-").append(SERVICE_NAME).toString();
        stringToSign.getBytes(CHARSET_NAME);

        String signingCredentials = accessKey + "/" + scope;
        String credential = "Credential=" + signingCredentials;
        String signerHeaders = "SignedHeaders=" + signedHeaders;
        String signatureHeader = "Signature=" + signature;
        StringBuilder authHeaderBuilder = new StringBuilder();
        authHeaderBuilder.append(AWS_ALGORITHM).append(" ").append(credential).append(", ").append(signerHeaders).append(", ").append(signatureHeader);

        String authorization = authHeaderBuilder.toString();

        request.addHeader("Authorization", authorization);
    }

    private void upload() throws Exception {
        String regionName = "kr-standard";
        String endpoint = "https://kr.objectstorage.ncloud.com";
        String accessKey = "h65G5WrYAwGDCYvidU3I";
        String secretKey = "yiUDaWiNQNWv3HSJXlC1655q0dEeUkwrPa5jEI8x";
        String bucketName = "artiksos/reservation";
        String objectName = reservationTime+".txt";

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPut request = new HttpPut(endpoint + "/" + bucketName + "/" + objectName);
        request.addHeader("Host", request.getURI().getHost());


        String fileName = "AppText.txt";
        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        // ???????????? ????????? ????????? ??????
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }
        // txt ?????? ??????
        String fileData = textdata;
        File savefile = new File(dirPath + "/" + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(fileData.getBytes());
            fos.close();
            Log.d(fileName + "???  ?????????:", fileData);

        } catch (IOException e) {
        }


        // test to tempfile
        request.setEntity(new FileEntity(savefile));

        if(savefile == null){
            request.setEntity(new FileEntity(savefile));
            System.out.println("Error at creating file, check storage permissions :");
            return;
        }

        authorization(request, regionName, accessKey, secretKey);

        HttpResponse response = httpClient.execute(request);
        System.out.println("--------------------------------------------------------------");
        System.out.println("upload response" + response.toString());
        System.out.println("--------------------------------------------------------------");
    }

}


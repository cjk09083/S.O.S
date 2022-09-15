package cloud.artik.example.hellocloud;

/**
 * Created by micom_hj on 2018-09-17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cloud.artik.api.MessagesApi;
import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiClient;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.FileEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class ncloud_text extends Activity {
    private MessagesApi mMessagesApi = null;
    private UsersApi mUsersApi = null;
    private String mAccessToken;

    private static final String CHARSET_NAME = "UTF-8";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String AWS_ALGORITHM = "AWS4-HMAC-SHA256";

    private static final String SERVICE_NAME = "s3";
    private static final String REQUEST_TYPE = "aws4_request";

    private static final String UNSIGNED_PAYLOAD = "UNSIGNED-PAYLOAD";
    private String textdata;
    private EditText textdataET;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd").withZoneUTC();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd\'T\'HHmmss\'Z\'").withZoneUTC();
    private static  Context context;
    private String device_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ncloud_textentry);
        mAccessToken = Config.mAccessToken; //내 토큰2 (내 클라우드 + 어플2)
        textdataET = (EditText) findViewById(R.id.textdata);
        setupArtikCloudApi();
        Intent intent = getIntent();
        device_ID = " "+ intent.getStringExtra("device_ID");
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context = this.getApplicationContext();

    }


    //확인 버튼 클릭
    public void mOnClose(View v){

        textdata=textdataET.getText().toString();
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
        MessageActivity.postMsg();

        finish();}
    }


    private void setupArtikCloudApi() {
        ApiClient mApiClient = new ApiClient();
        mApiClient.setAccessToken(mAccessToken);

        mUsersApi = new UsersApi(mApiClient);
        mMessagesApi = new MessagesApi(mApiClient);
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
        String bucketName = "artiksos/txt";
        String objectName = "AppText.txt";

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPut request = new HttpPut(endpoint + "/" + bucketName + "/" + objectName);
        request.addHeader("Host", request.getURI().getHost());


        String fileName = "AppText.txt";
        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        // 일치하는 폴더가 없으면 생성
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(getApplicationContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
        }
        // txt 파일 생성
        String fileData = textdata;
        File savefile = new File(dirPath + "/" + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(fileData.getBytes());
            fos.close();
            Log.d(fileName + "이  저장됨:", fileData);

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


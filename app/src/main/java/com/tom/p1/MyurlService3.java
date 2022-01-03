package com.tom.p1;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import androidx.annotation.Nullable;

public class MyurlService3 extends Service {


    String result="";
//    Double t1;
//    Double t2;
    public MyurlService3() {
    }

    @Override
    public void onCreate() {
        // 僅初次建立時呼叫
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 每次startService時會呼叫
        Log.d("HelloService","onmyurlservice");
        handleSSLHandshake();

        Bundle bundle=intent.getExtras();






        Double parcelableData = bundle.getDouble("DATA_KEY1");

//通過相應的key,取出相應的值

        Double testBundleString = bundle.getDouble("DATA_KEY2");



        Log.d("TEST", "inurl");
        Thread thread = new Thread(mutiThread);

        thread.start();


        stopSelf();  // 停止Service



        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("HelloService", "onDestroy");
    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private Runnable mutiThread = new Runnable(){




        public void run()
        {
            int con=0;
            String line="";
            String test="LALALA";
            double longitude=121.5890;
            double dimensionality=25.0552;
            //longitude=per
             longitude=GlobalVariable.Companion.getLon();
             dimensionality=GlobalVariable.Companion.getLat();
            try {

                URL puturl = new URL("https://140.136.151.140/data.php");
                HttpsURLConnection putconnection=(HttpsURLConnection) puturl.openConnection();
                putconnection.setRequestProperty("Charset", "UTF-8");
                putconnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                putconnection.setRequestMethod("POST");
                putconnection.setDoOutput(true);

                putconnection.setDoInput(true);
                putconnection.setUseCaches(false);

                OutputStream os = putconnection.getOutputStream();

                DataOutputStream out = new DataOutputStream(os);

                JSONObject object = new JSONObject();
                object.put("longitude",longitude);
                object.put("dimensionality", dimensionality);
                out.writeBytes(object.toString());
                out.flush();
                out.close();
                os.flush();
                os.close();

                InputStream inputStream = putconnection.getInputStream();
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                while((line = bufReader.readLine()) != null) {
                    result=line;
                }

                inputStream.close();




            } catch (Exception e ) {

                result = e.toString();
            }

            result="true";
            if(!result.equals("false")) {

                GlobalVariable.Companion.settf(true);
                Log.d("TEST1", result);
            }
            if(GlobalVariable.Companion.getTorf()) {
                Log.d("TEST", result);
            }else{
                GlobalVariable.Companion.settf(false);
                Log.d("TEST", result);
            }
        }


    };

}
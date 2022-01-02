package com.tom.p1;
import java.nio.charset.*;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tom.p1.databinding.ActivityMapsBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
  //舊的沒用到
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =0 ;
    private GoogleMap map;
    private ActivityMapsBinding binding;
    Boolean locationPermissionGranted=false;
    String result="";

    String[] LG=new String[2000];
    String[] DL=new String[2000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleSSLHandshake();
        Thread thread = new Thread(mutiThread);
        thread.start();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLocationPermission();


     for(int i=1;i<10;i++) {


     }
        updateLocationUI();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(25.035, 121.54), 13f));



    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
             locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }

    }


    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);

                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private Runnable mutiThread = new Runnable(){


        public void run()
        {
            int    con=0;
            String line="";

            int    LGi=1;
            int    DLi=0;
            try {
                URL puturl = new URL("https://140.136.151.140/getdata.php");
                HttpsURLConnection putconnection=(HttpsURLConnection) puturl.openConnection();
                putconnection.setRequestProperty("Charset", "UTF-8");
                putconnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                putconnection.setRequestMethod("POST");
                putconnection.setDoOutput(true);
                putconnection.setDoInput(true);
                putconnection.setUseCaches(false);
                InputStream inputStream = putconnection.getInputStream();
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                while((line = bufReader.readLine()) != null) {
                    if(line!=null) {
                        if (con % 2 != 0) {
                            LG[LGi] =line ;
                            LGi++;
                        } else {
                            DL[DLi] = line ;
                            DLi++;
                        }
                    }
                    con=con+1;



                }
                LGi=1;
                DLi=0;
                con=0;

                inputStream.close();
                //因为我傳的是String類，所以要用例如Double.parseDouble(LG[1])来接值
                //LG是經度 DL是緯度 兩個陣列的第一個值都在陣列的第一個位置 像是LG[1] DL[1]
            } catch (Exception e ) {
                result = e.toString();
            }

//            runOnUiThread(new Runnable() {
//                public void run() {
//                    textView.setText(result);
//                }
//            })
            ;


        }


    };


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

    public static String getRandomString2(int length){
        //產生亂數
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //回圈length次
        for(int i=0; i<length; i++){
            //產生0-2個亂數，既與a-z，A-Z，0-9三種可能
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                //如果number產生的是數字0；
                case 0:
                    //產生A-Z的ASCII碼
                    result=Math.round(Math.random()*25+65);
                    //將ASCII碼轉換成字符
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    //產生a-z的ASCII碼
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    //產生0-9的數字
                    sb.append(String.valueOf
                            (new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }


}





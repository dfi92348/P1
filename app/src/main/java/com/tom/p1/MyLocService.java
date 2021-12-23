package com.tom.p1;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;




public class MyLocService extends Service  implements LocationListener{

    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    private Location location;

    public MyLocService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        // 僅初次建立時呼叫

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 每次startService時會呼叫
       // Log.d("123", "onHandlerClick");
       //
       new Thread(new Runnable() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void run() {
               Log.d("lo", "onStartCommand Start");



                       try {
                           Log.d("lo", "12");
while (true) {
    Thread.sleep(5000);
    getLocal();
    //  wait(endTime);
}
                       } catch (Exception e) {
                           // e.printStackTrace();
                       }
                  // }

//               }
//               Log.d("HelloService", "onStartCommand End");

//               //stopSelf();  // 停止Service

           }
       }).start();
        return START_STICKY;

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocal() {
        /**沒有權限則返回*/
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d("lo", "123");
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);

        /**知道位置後..*/


        Criteria criteria = new Criteria();  //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        Location location = lms.getLastKnownLocation(bestProvider);

        showLocation(location);
//
    }
    /**監聽位置變化*/

    private void showLocation(Location location) { //將定位資訊顯示在畫面中
        if(location != null) {
            Double longitude = location.getLongitude();   //取得經度
            Double latitude = location.getLatitude();     //取得緯度
            Log.d("lo", "1111");

//new一個Bundle物件，並將要傳遞的資料傳入

//將Bundle物件傳給intent
            Intent i = new Intent(this,MyurlService3.class);
            i.putExtra("DATA_KEY1",longitude );
            i.putExtra("DATA_KEY2",latitude);
            startService(i);




            Log.d("lo", String.valueOf("經度:"+longitude));
            Log.d("lo", String.valueOf("緯度:"+latitude));
        }
        else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("lo", "onDestroy");
    }


    @Override
    public void onLocationChanged(Location location) {  //當地點改變時
        // TODO 自動產生的方法 Stub
        showLocation(location);
    }






}
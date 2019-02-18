package com.koreais.a5_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Address;
import java.util.List;
import java.util.Locale;

public class GetMyLocation extends AppCompatActivity {

    private ImageButton btn_gps;     // btn_gps 버튼
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    // GPSTracker class
    private GPSInfo gpsinfo;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //btn_gps = (ImageButton) findViewById(R.id.btn_gps);    // 기존 btn_gps로 변경

        gpsinfo = new GPSInfo(GetMyLocation.this);

        // GPS 사용유무 가져오기
        if ( gpsinfo.isGetLocation() ) {
            double latitude = gpsinfo.getLatitude();
            double longitude = gpsinfo.getLongitude();

            Log.d("mylog","latitude : " + String.valueOf(latitude));
            Log.d("mylog","longitude : " + String.valueOf(longitude));

            Toast.makeText(
                    getApplicationContext(),
                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                    Toast.LENGTH_LONG).show();

            // 위도 경도 넣어서 현재 주소 String으로 반환
            String mylocation = getAddress( latitude,longitude );

            Log.d("mylog", "myLocation : " + mylocation);

        } else {
            // GPS 를 사용할수 없으므로
            gpsinfo.showSettingsAlert();
        }

    }


    public String getAddress( double latitude, double longitude ) {
        // 내 위치
        String address = null;

        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //주소 목록을 담기 위한 HashMap
        List<Address> list = null;

        try {
            list = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ( list == null ) {
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if ( list.size() > 0) {
            Address addr = list.get(0);
            //  앞의 순서대로    ~시 /                      ~구 /                     ~동 으로 출력
            address = addr.getAdminArea() + " " + addr.getLocality() + " " + addr.getThoroughfare();
        }

        return address;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if ( requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;
        }
        else if ( requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            isAccessCoarseLocation = true;
        }

        if ( isAccessFineLocation && isAccessCoarseLocation ) {
            isPermission = true;
        }
    }

}

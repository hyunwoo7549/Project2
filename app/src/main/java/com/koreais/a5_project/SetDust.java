package com.koreais.a5_project;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

public class SetDust {
    LinearLayout background;
    ImageView iv_nowDust, pm10_img, pm25_img, no2_img, o3_img, co_img, so2_img; // 이미지

    // 생성자에 객체전달해서 세팅
    public SetDust ( Bundle bundle, Map<String, Object> map ) {
        Log.d("mylog", "setDust 생성자 들어옴");

        // bundle이 null일 때
        if ( bundle == null ) {
            Log.d("mylog", "bundle에 값이 없습니다");
            return;
        }

        // 이미지 뷰 멤버변수객체에 저장해둠
        background = (LinearLayout)map.get("background");
        iv_nowDust = (ImageView)map.get("iv_nowDust");
        pm10_img   = (ImageView)map.get("pm10_img");
        pm25_img   = (ImageView)map.get("pm25_img");
        no2_img    = (ImageView)map.get("no2_img");
        o3_img     = (ImageView)map.get("o3_img");
        co_img     = (ImageView)map.get("co_img");
        so2_img    = (ImageView)map.get("so2_img");

        // bundle에서 받아오는 값들
        String str_stationName = bundle.getString("stationName");
        String str_dataTime    = bundle.getString("dataTime");
        String str_pm10Value   = bundle.getString("pm10Value");
        String str_pm25Value   = bundle.getString("pm25Value");
        String str_no2Value    = bundle.getString("no2Value");
        String str_o3Value     = bundle.getString("o3Value");
        String str_coValue     = bundle.getString("coValue");
        String str_so2Value    = bundle.getString("so2Value");
        String str_pm10Grade   = bundle.getString("pm10Grade");
        String str_pm25Grade   = bundle.getString("pm25Grade");
        String str_no2Grade    = bundle.getString("no2Grade");
        String str_o3Grade     = bundle.getString("o3Grade");
        String str_coGrade     = bundle.getString("coGrade");
        String str_so2Grade    = bundle.getString("so2Grade");


        /** --- 화면에 보여줄 TextView 값 세팅 --- */

        // 지역명, 측정일시 세팅
        ( (TextView)map.get("stationName") ).setText( str_stationName );
        ( (TextView)map.get("dataTime") ).setText( str_dataTime );

        // 미세먼지, 초미세먼지 등급 사용해서 평균대기등급 세팅
        String dustAverage = getDustAverage( str_pm10Grade, str_pm25Grade );
        ( (TextView)map.get("dustAverage") ).setText( dustAverage );

        // 평균대기등급의 결과에 따른 행동요령 세팅
        ( (TextView)map.get("dustGuide") ).setText( getDustGuide( dustAverage ) );

        // 대기 수치 세팅
        ( (TextView)map.get("pm10Value") ).setText( str_pm10Value );
        ( (TextView)map.get("pm25Value") ).setText( str_pm25Value );
        ( (TextView)map.get("no2Value") ).setText( str_no2Value );
        ( (TextView)map.get("o3Value") ).setText( str_o3Value );
        ( (TextView)map.get("coValue") ).setText( str_coValue );
        ( (TextView)map.get("so2Value") ).setText( str_so2Value );

        /** --- 대기등급 + 이미지 세팅 --- */
        String pm10Grade = getGrade( str_pm10Grade ); // 미세먼지 등급(1,2,3,4)로 좋음보통나쁨 구함
        ( (TextView)map.get("pm10Grade") ).setText( pm10Grade );
        setImg( pm10_img, pm10Grade );  // 이미지 세팅

        String pm25Grade = getGrade( str_pm25Grade );
        ( (TextView)map.get("pm25Grade") ).setText( pm25Grade );
        setImg( pm25_img, pm25Grade );  // 이미지 세팅

        String no2Grade = getGrade( str_no2Grade );
        ( (TextView)map.get("no2Grade") ).setText( no2Grade );
        setImg( no2_img, no2Grade );  // 이미지 세팅

        String o3Grade = getGrade( str_o3Grade );
        ( (TextView)map.get("o3Grade") ).setText( o3Grade );
        setImg( o3_img, o3Grade );  // 이미지 세팅

        String coGrade = getGrade( str_coGrade );
        ( (TextView)map.get("coGrade") ).setText( coGrade );
        setImg( co_img, coGrade );  // 이미지 세팅

        String so2Grade = getGrade( str_so2Grade );
        ( (TextView)map.get("so2Grade") ).setText( so2Grade );
        setImg( so2_img, so2Grade );  // 이미지 세팅
    }


    // 이미지 세팅하는 함수
    public void setImg ( ImageView tempImageView, String dustGrade ) {
        Log.d("mylog","setImg 입장");

        switch ( dustGrade ) {
            case "좋음" :
                tempImageView.setImageResource(R.drawable.dust1);
                break;
            case "보통" :
                tempImageView.setImageResource(R.drawable.dust2);
                break;
            case "나쁨" :
                tempImageView.setImageResource(R.drawable.dust3);
                break;
            case "매우나쁨" :
                tempImageView.setImageResource(R.drawable.dust4);
                break;
            default :
                tempImageView.setImageResource(R.drawable.dust0);
        }

    }


    /** --- DustGuide 구할 함수 ---*/
    @SuppressLint("ResourceAsColor")
    private String getDustGuide (String dustAverage ) {
        String dustGuide = "";

        switch ( dustAverage ) {
            case "" :
                dustGuide = "알수없음";
                background.setBackgroundColor(Color.parseColor("#FFFF44"));
                iv_nowDust.setImageResource(R.drawable.dust0);
                break;
            case "좋음" :
                dustGuide = "깨끗한 날씨입니다^-^";
                background.setBackgroundColor(Color.parseColor("#AAFFFF"));
                iv_nowDust.setImageResource(R.drawable.dust1);
                break;
            case "보통" :
                dustGuide = "무난해요^ ^";
                background.setBackgroundColor(Color.parseColor("#88FF88"));
                iv_nowDust.setImageResource(R.drawable.dust2);
                break;
            case "나쁨" :
                dustGuide = "미세먼지 많아요!";
                background.setBackgroundColor( Color.parseColor("#FF6666") );
                iv_nowDust.setImageResource(R.drawable.dust3);
                break;
            case "매우나쁨" :
                dustGuide = "최악!! 외출 금지!!!";
                background.setBackgroundColor( Color.parseColor("#AAAAAA") );
                iv_nowDust.setImageResource(R.drawable.dust4);
                break;

        }

        return dustGuide;
    }

    /** --- DustAverage (전체 대기등급) 구할 함수 (미세먼지, 초미세먼지 등급을 매개변수로 줌 --- */
    private String getDustAverage ( String pm10Grade, String pm25Grade ) {
        int pm10 = Integer.parseInt( pm10Grade ); // 미세먼지   -> 정수로 변환
        int pm25 = Integer.parseInt( pm25Grade ); // 초미세먼지 -> 정수로 변환
        int res  = 0;                             // 전체 대기등급 지정

        String dustAverage = "";                  // 반환할 평균대기등급

        if ( pm10 > pm25 ) {
            res = pm10;
        }
        else if ( pm10 < pm25 ) {
            res = pm25;
        }
        else if ( pm10 == pm25 ) {
            res = pm10;
        }
        else {
            res = 0;
        }

        switch ( res ) {
            case 0 :
                dustAverage = "알수없음";
                break;
            case 1 :
                dustAverage = "좋음";
                break;
            case 2 :
                dustAverage = "보통";
                break;
            case 3 :
                dustAverage = "나쁨";
                break;
            case 4 :
                dustAverage = "매우나쁨";
                break;
        }

        return dustAverage;

    }

    /** --- 대기 등급에따라 좋음~나쁨으로 바꿔주는 함수 ---*/
    private String getGrade( String grade ) {
        // 결과값 변수
        String res = "";

        // grade가 null 일때
        if ( grade == null ) {
            res = "알수없음";
            return res;
        }

        // grade(등급) 에 따라 좋음~나쁨
        switch ( grade ) {
            case "1" :
                res = "좋음";
                break;
            case "2" :
                res = "보통";
                break;
            case "3" :
                res = "나쁨";
                break;
            case "4" :
                res = "매우나쁨";
                break;
        }

        return res;
    }

}

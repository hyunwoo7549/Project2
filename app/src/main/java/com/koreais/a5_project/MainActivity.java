package com.koreais.a5_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LinearLayout ll_cover;

    /** 메뉴슬라이드 버튼, 검색버튼 */
    ImageButton btn_menu;   // 좌측 상단 메뉴 버튼
    ImageButton btn_search; // 우측 상단 검색 버튼
    MenuDrawer left_drawer;

    /** 뷰 페이징시 필요한 변수 */
    ViewPager pager;
    int count; // 페이지 전환할때 마다 증가시킬 변수

    /** PageFragment1 의 view 들 */
    LinearLayout background;
    ImageView iv_nowDust, pm10_img, pm25_img, no2_img, o3_img, co_img, so2_img;
    TextView stationName, dataTime, dustAverage, dustGuide;
    TextView pm10Value, pm25Value, no2Value, o3Value, coValue, so2Value;
    TextView pm10Grade, pm25Grade, no2Grade, o3Grade, coGrade, so2Grade;
    TextView tv_today_forecast, tv_tomorrow_forecast;
    ImageView iv_expend; // 이미지 확대
    ViewTarget<ImageView, Drawable> target;

    /** 한국, 아시아 대기질 농도 전망 */
    Bitmap bitmap;
    final private static String URL_HEAD  = "https://www.airkorea.or.kr/file/viewImage2/?fileNm=dust/AQFv1_15h.";  // 공통 URL 앞주소
    final private static String URL_TAIL_KOREA_PM10  = ".KNU_09_01.PM10.2days.ani.gif&key=12ee13fb-06c1-4b48-91ad-cee3f1a5fedc";  // 한국 미세먼지 뒷주소
    final private static String URL_TAIL_KOREA_PM25  = ".KNU_09_01.PM2P5.2days.ani.gif&key=12ee13fb-06c1-4b48-91ad-cee3f1a5fedc";  // 한국 초미세먼지 뒷주소
    final private static String URL_TAIL_ASIA_PM10   = ".KNU_27_01.PM10.2days.ani.gif&key=12ee13fb-06c1-4b48-91ad-cee3f1a5fedc";  // 한국 초미세먼지 뒷주소
    final private static String URL_TAIL_ASIA_PM25   = ".KNU_27_01.PM2P5.2days.ani.gif&key=12ee13fb-06c1-4b48-91ad-cee3f1a5fedc";  // 한국 초미세먼지 뒷주소

    private String korea_pm10_resURL, korea_pm25_resURL, asia_pm10_resURL, asia_pm25_resURL;
    ImageView iv_korea_pm10_map, iv_korea_pm25_map, iv_asia_pm10_map, iv_asia_pm25_map;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        //setContentView(R.layout.activity_main);
        Log.d("mylog","Main시작 ");

        /** ------ 좌측상단 메뉴바 ------ */
        left_drawer = MenuDrawer.attach(this,
                      MenuDrawer.Type.BEHIND,
                      Position.LEFT,
                      MenuDrawer.MENU_DRAG_WINDOW );

        // 레이아웃 xml 파일을 '객체'로 생성
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        LinearLayout linearLayout = (LinearLayout) inflater.inflate( R.layout.activity_left_side_menu, null );

        left_drawer.setContentView( R.layout.activity_main ); // 기본 레이아웃 지정
        left_drawer.setMenuView( linearLayout );   // 서랍 레이아웃 지정

        // 객체에서 텍스트뷰 찾기
        TextView tv_info = linearLayout.findViewById(R.id.tv_info);

        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mylog", "메뉴바 버튼 클릭됨");
                Intent refIntent = new Intent(MainActivity.this, Dust_Info.class);
                startActivityForResult( refIntent, 2);
            }
        });

        // 화면 사이즈 얻어오기
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)getSystemService( WINDOW_SERVICE );
        wm.getDefaultDisplay().getMetrics( dm );

        left_drawer.setMenuSize( dm.widthPixels / 6 * 5 );  // 서랍 사이즈
        left_drawer.setTouchBezelSize( 0 );                 // 베젤 터치 영역 지정

        btn_menu = (ImageButton)findViewById(R.id.btn_menu); // 메뉴버튼 찾기
        btn_menu.setOnClickListener( click );                // 버튼 클릭 시 수행


        /** ------ 뷰 페이징 ------*/
        pager = findViewById( R.id.pager ); // pager 아이디 찾기

        // Adapter 가 필요하다. PagerAdapter를 상속 받은 객체 생성
        pager.setAdapter( new MyPagerAdapter( getSupportFragmentManager() ) );

        // setCurrentItem() : 몇 번째 페이지를 보여줄 것인지 지정
        pager.setCurrentItem( PageInfo.PAGE1 );


        /** ------ 위치 검색 (우측상단) 버튼 ------*/
        btn_search = findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mylog", "검색버튼 클릭됨");
                //ll_secrect = findViewById(R.id.ll_secrect);
                //ll_secrect.setVisibility(View.GONE);
                Intent refIntent = new Intent( MainActivity.this, LocationSearch.class );
                startActivityForResult( refIntent, 1);
            }
        });





    } // onCreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 여기로 넘어온다. requestCode는 start할 때 넘겼던 1 값이다. (요청 당시의 값)
        // resultCode = 10 으로 설정해뒀었음

        if ( resultCode == 10 ) {
            switch ( requestCode ) {

                /** 메인화면으로 돌아오는곳 */
                case 1 :
                    Log.d("mylog", "DustTask, bundle 받음");

                    // 서브액티비티에서 전달해준 intent 에서 bundle 획득
                    Bundle bundle = data.getExtras();

                    /* 아이디 구해오기 */
                    background = findViewById(R.id.background);

                    stationName = findViewById(R.id.tv_stationName);    // 시 구 이름
                    dataTime    = findViewById(R.id.tv_dataTime);       // 측정시간
                    iv_nowDust  = findViewById(R.id.iv_nowDust);        // 메인사진
                    dustAverage = findViewById(R.id.tv_dustAverage);    // 평균대기
                    dustGuide   = findViewById(R.id.tv_dustGuide);      // 주의사항

                    pm10Value   = findViewById(R.id.tv_pm10Value);      // 미세먼지 수치
                    pm25Value   = findViewById(R.id.tv_pm25Value);      // 초미세먼지 수치
                    no2Value    = findViewById(R.id.tv_no2Value);       // 이산화질소 수치
                    o3Value     = findViewById(R.id.tv_o3Value);        // 오존 이미지
                    coValue     = findViewById(R.id.tv_coValue);        // 일산화탄소 이미지
                    so2Value    = findViewById(R.id.tv_so2Value);       // 아황산가스 이미지

                    pm10Grade   = findViewById(R.id.tv_pm10Grade);      // 미세먼지 등급
                    pm25Grade   = findViewById(R.id.tv_pm25Grade);      // 초미세먼지 등급
                    no2Grade    = findViewById(R.id.tv_no2Grade);       // 이산화질소 등급
                    o3Grade     = findViewById(R.id.tv_o3Grade);        // 오존 등급
                    coGrade     = findViewById(R.id.tv_coGrade);        // 일산화탄소 등급
                    so2Grade    = findViewById(R.id.tv_so2Grade);       // 아황산가스 등급

                    pm10_img    = findViewById(R.id.pm10_img);          // 미세먼지 이미지
                    pm25_img    = findViewById(R.id.pm25_img);          // 초미세먼지 이미지
                    no2_img     = findViewById(R.id.no2_img);           // 이산화질소 이미지
                    o3_img      = findViewById(R.id.o3_img);            // 오존 이미지
                    co_img      = findViewById(R.id.co_img);            // 일산화탄소 이미지
                    so2_img     = findViewById(R.id.so2_img);           // 아황산가스 이미지

                    // 찾아온 텍스트뷰 값들을 map에 저장
                    Map<String, Object> map = new HashMap<>();
                    map.put("background", background);

                    map.put("stationName", stationName);
                    map.put("dataTime", dataTime);
                    map.put("iv_nowDust", iv_nowDust);
                    map.put("dustAverage", dustAverage);
                    map.put("dustGuide", dustGuide);

                    map.put("pm10Value", pm10Value);
                    map.put("pm25Value", pm25Value);
                    map.put("no2Value", no2Value);
                    map.put("o3Value", o3Value);
                    map.put("coValue", coValue);
                    map.put("so2Value", so2Value);

                    map.put("pm10Grade", pm10Grade);
                    map.put("pm25Grade", pm25Grade);
                    map.put("no2Grade", no2Grade);
                    map.put("o3Grade", o3Grade);
                    map.put("coGrade", coGrade);
                    map.put("so2Grade", so2Grade);

                    map.put("pm10_img", pm10_img);
                    map.put("pm25_img", pm25_img);
                    map.put("no2_img", no2_img);
                    map.put("o3_img", o3_img);
                    map.put("co_img", co_img);
                    map.put("so2_img", so2_img);

                    // setDust 클래스에 생성자로 bundle 객체, map 전달
                    SetDust sd = new SetDust( bundle, map );

                    /* ----------------------------------------------------------------------------------- */

                    Log.d("mylog", "ForeTask, bundle 2 받음");

                    tv_today_forecast    = findViewById(R.id.tv_today_forecast); // 오늘 예보 뿌릴 TextView
                    tv_tomorrow_forecast = findViewById(R.id.tv_tomorrow_forecast); // 내일 예보 뿌릴 TextView

                    tv_today_forecast.setText( "\n○ [ " + bundle.getString("today_dataTime") + " ]\n" +
                                                               bundle.getString("today_informCause")  + "\n" +
                                                               bundle.getString("today_informOverall") + "\n" +
                                               "○ [지역별 예보] \n" + bundle.getString("today_informGrade") + "\n");

                    tv_tomorrow_forecast.setText( "\n○ [ " + bundle.getString("tom_dataTime") + " ]\n" +
                                                                  bundle.getString("tom_informCause")  + "\n" +
                                                                  bundle.getString("tom_informOverall") + "\n" +
                                             "○ [지역별 예보] \n" + bundle.getString("tom_informGrade") + "\n");

                    /* ----------------------------------------------------------------------------------- */

                    Calendar calendar = new GregorianCalendar();
                    calendar.add(Calendar.DATE, -1); // 오늘날짜로부터 -1 해야 오늘나옴
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd"); // 날짜 포맷
                    String date = simpleDateFormat.format( calendar.getTime() );

                    korea_pm10_resURL  = URL_HEAD + date + URL_TAIL_KOREA_PM10;
                    korea_pm25_resURL  = URL_HEAD + date + URL_TAIL_KOREA_PM25;
                    asia_pm10_resURL   = URL_HEAD + date + URL_TAIL_ASIA_PM10;
                    asia_pm25_resURL   = URL_HEAD + date + URL_TAIL_ASIA_PM25;

                    iv_korea_pm10_map = (ImageView) findViewById(R.id.iv_korea_pm10_map);
                    iv_korea_pm25_map = (ImageView) findViewById(R.id.iv_korea_pm25_map);
                    iv_asia_pm10_map = (ImageView) findViewById(R.id.iv_asia_pm10_map);
                    iv_asia_pm25_map = (ImageView) findViewById(R.id.iv_asia_pm25_map);

                    Glide.with(this).load( korea_pm10_resURL ).into( iv_korea_pm10_map );
                    Glide.with(this).load( korea_pm25_resURL ).into( iv_korea_pm25_map );
                    Glide.with(this).load( asia_pm10_resURL ).into( iv_asia_pm10_map );
                    Glide.with(this).load( asia_pm25_resURL ).into( iv_asia_pm25_map );

                    expendImage( "iv_korea_pm10_map", iv_korea_pm10_map );
                    expendImage( "iv_korea_pm25_map",iv_korea_pm25_map );
                    expendImage( "iv_asia_pm10_map", iv_asia_pm10_map );
                    expendImage( "iv_asia_pm25_map", iv_asia_pm25_map );

                    // 초기 위치지정화면 숨기기
                    ll_cover = findViewById(R.id.ll_cover);
                    ll_cover.setVisibility(View.GONE);

                    break;

                /** 대기상식 버튼클릭후 돌아오는곳 */
                case 2 :

            }
        }
    }

    /** 이미지 확대 함수 */
    private void expendImage( String str, ImageView iv ) {
        final String URL;

        switch ( str ) {
            case "iv_korea_pm10_map" :
                URL = korea_pm10_resURL;
                break;
            case "iv_korea_pm25_map" :
                URL = korea_pm25_resURL;
                break;
            case "iv_asia_pm10_map" :
                URL = asia_pm10_resURL;
                break;
            case "iv_asia_pm25_map" :
                URL = asia_pm25_resURL;
                break;
            default :
                Toast.makeText(MainActivity.this, "잘못된 접근입니다!", Toast.LENGTH_SHORT).show();
                return;
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mylog"," 사진 클릭됨");

                // Dialog Builder 를 선언
                AlertDialog.Builder builder;

                // 사용할 Dialog 를 선언
                AlertDialog alertDialog;

                Context context = MainActivity.this;

                LayoutInflater inflater
                        = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View RelativeLayout = inflater.inflate(R.layout.activity_expend_image, null);

                iv_expend = RelativeLayout.findViewById(R.id.iv_expend);

                Glide.with(context).load( URL ).into( iv_expend );

                builder = new AlertDialog.Builder(context);

                builder.setView(RelativeLayout);

                alertDialog = builder.create();

                alertDialog.show();
            }
        });

    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick( View v ) {
            switch ( v.getId() ){
                // v.getId() 가 메뉴 버튼일 때
                case R.id.btn_menu:
                    left_drawer.openMenu( true );
                    break;
            } // switch()
        }
    };

    }


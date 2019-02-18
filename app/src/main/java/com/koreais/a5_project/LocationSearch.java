package com.koreais.a5_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LocationSearch extends AppCompatActivity {
    // "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"
    int numOfRows           = 2;         // 한 페이지 결과 수 (한페이지에 보여질 결과 수는 4가 최대라서 4로 지정)

    String siNameAll        = "";        // 내가 선택한 '시' 이름 전체
    String my_si            = "";        // 내가 입력한 '시' 를 저장해 둘 변수
    String my_gu            = "";        // 내가 입력한 '구' 를 저장해 둘 변수

    String resultStationName   = "";     // JSON 배열안에서 찾은 '구' 이름
    JSONObject resultJsonObject;         // 찾아낸 구 의 모든 대기정보를 가져올때 필요한 변수

    ImageButton btn_result;           // 돋보기 버튼

    // 객체 넘겨줄때 필요한 번들, 인텐트
    Bundle bundle;
    Intent intent;

    // 자동완성에 사용할 텍스트뷰
    Spinner spinner_si;
    AutoCompleteTextView actv_gu;

    LinearLayout ll_secrect;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        Log.d("mylog", "온크리에이트~");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        spinner_si = (Spinner) findViewById(R.id.spinner_si);              // '시'
        actv_gu    = (AutoCompleteTextView) findViewById(R.id.actv_gu);    // '구'
        btn_result = findViewById(R.id.btn_result);                        // 돋보기버튼

        spinner_si.setAdapter( new ArrayAdapter<String>
                ( this, android.R.layout.simple_dropdown_item_1line, new Address().getSido() ) );

        spinner_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택한 지역명(예:서울특별시 강남구) 받아와서 공백을 기준으로 split 한 뒤, '시' 명만 따온다
                String select_si = String.valueOf( parent.getItemAtPosition( position ) );

                if ( select_si == null || "시 선택".equals( select_si )) {
                    Toast.makeText( LocationSearch.this, "시를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    siNameAll = select_si;
                    String result_si = strProcessing( select_si ); // 시 이름 가공 (예:서울특별시 -> 서울, 울산광역시 -> 울산)
                    my_si = result_si; // 가공된 '시' 저장

                    Log.d("mylog", " 내가 선택한 시 : " + my_si);

                    // 선택된 시 에 따라 검색되는 '동' 리스트가 다르다
                    sigu_location( my_si );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    // '시' 이름에 따라 검색되는 결과를 다르게 설정하고, 여기서 다음 절차 수행
    private void sigu_location ( String siName ) {
        String[] guArr = null;

        switch ( siName ) {
            case "서울" :
                guArr = new Address().getSeoul();
                break;
            case "강원" :
                guArr = new Address().getGangwon();
                break;
            case "제주" :
                guArr = new Address().getJeju();
                break;
            case "부산" :
                guArr = new Address().getBusan();
                break;
            case "충북" :
                guArr = new Address().getChungbuk();
                break;
            case "충남" :
                guArr = new Address().getChungnam();
                break;
            case "대구" :
                guArr = new Address().getDaegu();
                break;
            case "대전" :
                guArr = new Address().getDaejeon();
                break;
            case "광주" :
                guArr = new Address().getGwangju();
                break;
            case "경북" :
                guArr = new Address().getGyeongbuk();
                break;
            case "경남" :
                guArr = new Address().getGyeongnam();
                break;
            case "경기" :
                guArr = new Address().getGyunggi();
                break;
            case "인천" :
                guArr = new Address().getIncheon();
                break;
            case "전북" :
                guArr = new Address().getJeonbuk();
                break;
            case "전남" :
                guArr = new Address().getJeonnam();
                break;
            case "울산" :
                guArr = new Address().getUlsan();
                break;
        }

        // 검색한 '시' 에 따라 '구'에 검색될 값 세팅 (예: 서울이면 서초구, 강남구, ... 만 검색되게함)
        actv_gu.setAdapter( new ArrayAdapter<String>
                ( this, android.R.layout.simple_dropdown_item_1line, guArr ) );

        actv_gu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 선택한 지역명(예:서울특별시 강남구) 받아와서 공백을 기준으로 split 한 뒤, '시' 명만 따온다
                String select_gu = String.valueOf( parent.getItemAtPosition( position ) );

                Log.d("mylog","선택한 구 : " + select_gu);

                if ( select_gu == null || select_gu == "" ) {
                    Toast.makeText( LocationSearch.this, "시를 다시 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    my_gu = select_gu; // 선택한 '구' 명 저장

                    Log.d("mylog", "셀렉1 : " + my_si);
                    Log.d("mylog", "셀렉2 : " + my_gu);

                    btn_result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bundle = new Bundle();
                            intent = new Intent();
                            // 현재 미세먼지 API
                            new DustTask().execute( my_si ); // '시' 파라미터만 필요함
                            // 미세먼지 에보 API
                            //new ForeTask().execute();   // 파라미터 필요없음
                        }
                    });

                }
            }
        });

    }


    class DustTask extends AsyncTask<String, Void, String> {
        String dustUrl       = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";          // 먼지 요청 URL
        String serviceKey    = "rPNdTrRFHsOppCMZeNj4v%2FCCUmfOOpErbdS%2BzMjQxju%2F5SKwK%2FmhMKpsu9jmx8I2vfEfnXx4WZgX3o4jH9W83A%3D%3D";       //  인증 key
        String serviceKey2   = "UX8hoQIEIB%2FrUlJ2X%2FLe%2B4u16k0yK6Wcz1PCqipW2jDdf3Uj5hN9Dn9MWsRNKGM2jPeX%2BB4oerQH0JmNI5bLlQ%3D%3D";       //  인증 key2

        String sendMsg, receiveMsg; // 서버로 전달할 스트림, 받을 스트림

        @Override
        protected String doInBackground( String... si ) {
            int pageNo = 1;  // 페이지 시작번호 (1페이지부터 x번까지 찾음)
            String buf = ""; // 요청에 따른 결과 값을 읽을 버퍼

            // 통신관련 변수들
            URL url                 = null;
            HttpURLConnection conn  = null;
            OutputStreamWriter osw  = null;
            InputStreamReader tmp   = null;
            BufferedReader reader   = null;

            try {

                // 입력한 my_gu == resultStationName 이 일치하면 끝
                while ( true ) {
                    url = new URL( dustUrl );

                    // 서버 연결
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.connect();

                    // 서버로 전달할 스트림
                    osw = new OutputStreamWriter( conn.getOutputStream() );

                    sendMsg = "numOfRows=" + numOfRows +
                              "&pageNo=" + pageNo +
                              "&sidoName=" + si[0] +
                              "&ServiceKey=" + serviceKey2 +
                              "&_returnType=json" +
                              "&ver=1.3";

                    //Log.d("mylog", "sendMsg>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " + sendMsg);

                    // 서버로 파라미터들 전송
                    osw.write( sendMsg );
                    osw.flush();
                    osw.close();

                    int code = conn.getResponseCode();

                    if ( code == conn.HTTP_OK ) {
                        Log.d("mylog", "DustTask, doInBackground(), in to inputStream");

                        tmp                 = new InputStreamReader( conn.getInputStream(), "UTF-8" );
                        reader              = new BufferedReader( tmp );
                        StringBuffer buffer = new StringBuffer();

                        // 버퍼에 한줄씩 추가. Protocol Exception 나도 무시하고 계속 while문 수행...
                        try {
                            int i = 0;
                            while ( (buf = reader.readLine()) != null ) {
                                buffer.append( buf );
                            }
                        } catch ( Exception e ) {
                            Log.d("mylog", "DustTask, readLine() 오류, continue...");
                            tmp.close();
                            reader.close();
                            continue;
                        }

                        // 사용 후 전부닫음
                        tmp.close();
                        reader.close();

                        // 버퍼에 담긴 값을 receiveMsg에 저장
                        receiveMsg = buffer.toString();

                        // 사용한 버퍼를 비운다
                        buffer.setLength(0);

                        //Log.d("mylog", "DustTask, doInBackground(), step-3, receiveMsg : " + receiveMsg);

                        // 받아온 메세지를 JSONObject로 만듬
                        JSONObject jObj = new JSONObject( receiveMsg );

                        JSONArray jsonArray = jObj.getJSONArray("list"); // 필요한 값들이 저장되어있는 'list' 배열만 추출

                        // 배열에 값이 없으면 빈 문자열 반환
                        if ( jsonArray.length() == 0 ) {
                            Log.d("mylog", "DustTask, doInBackground(), JsonArray 길이가 0 이거나 못찾음");
                            return "";
                        }

                        // jsonArray.length() 길이만큼 for문돌림 ( 4 )
                        for ( int i = 0; i < jsonArray.length(); i++ ) {

                            // 임시변수 temp
                            String temp = jsonArray.getJSONObject(i).getString("stationName");

                            Log.d("mylog", "DustTask, doInBackground(), (*) 차례대로 출력 : " + temp);

                            // 내가 입력한 구를 찾으면 resultCityName에 해당 구 를 저장하고
                            // jsonIndex에 해당 값을 집어넣는다.
                            if ( temp.equals( my_gu ) ) {
                                resultStationName = temp;
                                resultJsonObject  = jsonArray.getJSONObject(i);

                                Log.d("mylog","DustTask, doInBackground(), (*) 일치하는 구 를 찾음");
                                Log.d("mylog","DustTask, doInBackground(), (*) 일치하는 구의 JSON text: " + resultJsonObject);

                                return resultJsonObject.toString();
                            }

                        } // for문 끝

                    } // if문 끝

                    pageNo++; // 내가 입력한 '구'를 못찾으면 페이지 수를 증가시킨다.

                    conn.disconnect(); // conn 연결해제

                } // while문 끝
            } // try 끝

            catch (Exception e) {
                Log.d("mylog", "DustTask, doInBackground(), 오류 메세지 : " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (tmp != null) tmp.close();
                    if (osw != null) osw.close();
                } catch (IOException ioe) {}
            }

            return "";
        }

        @Override
        protected void onPostExecute( String jsonObjectStr ) {
            Log.d("mylog","DustTask : onPostExecute 들어옴");

            try {

                // 넘어올 때마다 (검색할 때마다) 번들, 인텐트 객체 생성
                //Bundle bundle = new Bundle();
                //intent = new Intent();

                if ( jsonObjectStr == null || "".equals( jsonObjectStr ) ) {
                    Log.d("mylog", "검색한 데이터 찾을수 없음");
                    Toast.makeText(
                            LocationSearch.this, "검색결과가 없습니다. 시군구명을 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();

                    //ll_secrect = findViewById(R.id.ll_secrect);
                    //ll_secrect.setVisibility(View.GONE);

                    finish();

                } else {
                    JSONObject jObj = new JSONObject(jsonObjectStr);

                    // 입력한 구, 측정시간 (1시간단위)
                    String stationName  = jObj.getString("stationName");  // 구
                    String dataTime     = jObj.getString("dataTime");     // 측정시간

                    // DUST 수치
                    String pm10Value    = jObj.getString("pm10Value"); // 미세먼지
                    String pm25Value    = jObj.getString("pm25Value"); // 초미세먼지
                    String no2Value     = jObj.getString("no2Value");  // 이산화질소
                    String o3Value      = jObj.getString("o3Value");   // 오존
                    String coValue      = jObj.getString("coValue");   // 일산화탄소
                    String so2Value     = jObj.getString("so2Value");  // 아황산가스

                    // DUST 등급
                    String pm10Grade    = jObj.getString("pm10Grade"); // 미세먼지
                    String pm25Grade    = jObj.getString("pm25Grade"); // 초미세먼지
                    String no2Grade     = jObj.getString("no2Grade");  // 이산화질소
                    String o3Grade      = jObj.getString("o3Grade");   // 오존
                    String coGrade      = jObj.getString("coGrade");   // 일산화탄소
                    String so2Grade     = jObj.getString("so2Grade");  // 아황산가스

                    Log.d("mylog", "시 : " + stationName + " (" + siNameAll + " " + my_gu + ")");
                    Log.d("mylog", "측정시간 : " + dataTime);
                    Log.d("mylog", "미세먼지 농도 : " + pm10Value);
                    Log.d("mylog", "초미세먼지 농도 : " + pm25Value);
                    Log.d("mylog", "이산화질소 농도 : " + no2Value);
                    Log.d("mylog", "오존 농도 : " + o3Value);
                    Log.d("mylog", "일산화탄소 농도 : " + coValue);
                    Log.d("mylog", "아황산가스 농도 : " + so2Value);
                    Log.d("mylog", "미세먼지 등급 : " + pm10Grade);
                    Log.d("mylog", "초미세먼지 등급 : " + pm25Grade);
                    Log.d("mylog", "이산화질소 등급 : " + no2Grade);
                    Log.d("mylog", "오존 등급 : " + o3Grade);
                    Log.d("mylog", "일산화탄소 등급 : " + coGrade);
                    Log.d("mylog", "아황산가스 등급 : " + so2Grade);

                    // bundle에 put
                    bundle.putString("stationName", siNameAll + " " + my_gu);
                    bundle.putString("dataTime", dataTime);
                    bundle.putString("pm10Value", pm10Value);
                    bundle.putString("pm25Value", pm25Value);
                    bundle.putString("no2Value", no2Value);
                    bundle.putString("o3Value", o3Value);
                    bundle.putString("coValue", coValue);
                    bundle.putString("so2Value", so2Value);
                    bundle.putString("pm10Grade", pm10Grade);
                    bundle.putString("pm25Grade", pm25Grade);
                    bundle.putString("no2Grade", no2Grade);
                    bundle.putString("o3Grade", o3Grade);
                    bundle.putString("coGrade", coGrade);
                    bundle.putString("so2Grade", so2Grade);

                    // 번들 객체에 담음
                    intent.putExtras( bundle );

                    setResult(10, intent);
                    new ForeTask().execute();   // 파라미터 필요없음
                    //finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    /* ------------------------------------------------------------------------------------------ */

    class ForeTask extends AsyncTask<String, Void, String> {
        String serverUrl    = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMinuDustFrcstDspth";          // 요청 URL
        String serviceKey   = "rPNdTrRFHsOppCMZeNj4v%2FCCUmfOOpErbdS%2BzMjQxju%2F5SKwK%2FmhMKpsu9jmx8I2vfEfnXx4WZgX3o4jH9W83A%3D%3D";       //  인증 key
        String serviceKey2   = "UX8hoQIEIB%2FrUlJ2X%2FLe%2B4u16k0yK6Wcz1PCqipW2jDdf3Uj5hN9Dn9MWsRNKGM2jPeX%2BB4oerQH0JmNI5bLlQ%3D%3D";       //  인증 key2

        String sendMsg, receiveMsg; // 서버로 전달할 스트림, 받을 스트림

        @Override
        protected String doInBackground( String... si ) {
            String buf = ""; // 요청에 따른 결과 값을 읽을 버퍼

            // 통신관련 변수들
            URL url                 = null;
            HttpURLConnection conn  = null;
            OutputStreamWriter osw  = null;
            InputStreamReader tmp   = null;
            BufferedReader reader   = null;

            try {

                while ( true ) {
                    url = new URL(serverUrl);

                    // 서버 연결
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.connect();

                    // 서버로 전달할 스트림
                    osw = new OutputStreamWriter(conn.getOutputStream());

                    // 오늘 날짜 구해옴
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd"); // 년월일
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH");         // 시간

                    Calendar calendar = Calendar.getInstance();

                    String date = sdfDate.format(calendar.getTime());                   // 오늘 날짜
                    String time = sdfTime.format(calendar.getTime());

                    Log.d("mylog", "현재날짜는? : " + date);
                    Log.d("mylog", "현재시간은? : " + time);
                    Log.d("mylog", "현재시간 숫자로 형변환한 값 : " + Integer.parseInt( time ));

                    // api 첫 갱신시간인 오전5시 전이면 하루 전날 데이터를 보여준다 (자정 ~ 오전5시)
                    if ( Integer.parseInt( time ) < 5 ) {
                        calendar.add(Calendar.DATE, -1); // 오늘날짜로부터 -1 (어제)
                        date = sdfDate.format(calendar.getTime()); // -1일 된 날짜
                        Log.d("mylog", "하루 뺀 날짜 : " + date);
                    }

                    sendMsg = "ServiceKey=" + serviceKey2 +
                              "&searchDate=" + date +
                              "&_returnType=json";

                    // Log.d("mylog", "sendMsg = " + sendMsg);

                    // 서버로 파라미터들 전송
                    osw.write(sendMsg);
                    osw.flush();
                    osw.close();

                    int code = conn.getResponseCode();

                    if (code == conn.HTTP_OK) {
                        Log.d("mylog", "ForeTask, if() 문 들어옴");

                        tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();

                        // 버퍼에 한줄씩 추가 (무한루프, 알수없는 오류때문에 넣음)
                        try {
                            while ((buf = reader.readLine()) != null) {
                                buffer.append(buf);
                            }
                        } catch (Exception e) {
                            Log.d("mylog", "ForeTask, readLine() 오류, continue...");
                            tmp.close();
                            reader.close();
                            conn.disconnect();
                            continue;
                        }

                        tmp.close();
                        reader.close();

                        // 버퍼에 담긴 값을 receiveMsg에 저장
                        receiveMsg = buffer.toString();

                        // 사용한 버퍼를 비운다
                        buffer.setLength(0);

                        Log.d("mylog", "ForeTask, receiveMsg : " + receiveMsg);

                        // 받아온 메세지를 JSONObject로 만듬
                        JSONObject jObj = new JSONObject(receiveMsg);

                        JSONArray jsonArray = jObj.getJSONArray("list"); // 필요한 값들이 저장되어있는 'list' 배열만 추출

                        // Log.d("mylog", "jsonArray 전체 : " + jsonArray.toString());

                        // 배열에 값이 없으면 빈 문자열 반환
                        if ( jsonArray.length() == 0 ) {
                            Log.d("mylog", "ForeTask, JsonArray 길이가 0 이거나 결과 못찾음");
                            return "";
                        }

                        // 정상적으로 값이 구해졌으면 이름이 'list' 인 JSONArray 반환 (오늘, 내일 예보 들어있음)
                        return jsonArray.toString();

                    } // if문 끝
                }

            } // try 끝

            catch (Exception e) {
                Log.d("mylog", "ForeTask, doInBackground(), 오류 메세지 : " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (tmp != null) tmp.close();
                    if (reader != null) reader.close();
                    if (osw != null) osw.close();
                } catch (IOException ioe) {}
            }

            return "";
        }


        @Override
        protected void onPostExecute( String foreCast ) {
            Log.d("mylog","ForeTask : onPostExecute 들어옴");

            try {

                // 넘어올 때마다 (검색할 때마다) 번들, 인텐트 객체 생성
                //Bundle bundle = new Bundle();
                //intent = new Intent();

                if ( foreCast == null || "".equals( foreCast ) ) {
                    Toast.makeText(LocationSearch.this, "실시간 예보 정보를 가져올 수 없습니다!", Toast.LENGTH_SHORT).show();

                } else {
                    JSONArray jArr = new JSONArray( foreCast );

                    /*
                    Log.d("mylog", "ForeTask : 발표시간 : " + jArr.getJSONObject(0).getString("dataTime"));
                    Log.d("mylog", "ForeTask : 발생원인 : " + jArr.getJSONObject(0).getString("informCause"));
                    Log.d("mylog", "ForeTask : 예보등급 : " + jArr.getJSONObject(0).getString("informGrade"));
                    Log.d("mylog", "ForeTask : 예보개황 : " + jArr.getJSONObject(0).getString("informOverall"));
                    Log.d("mylog", "ForeTask : 언제날짜예보? : " + jArr.getJSONObject(0).getString("informData"));

                    Log.d("mylog", "ForeTask : 발표시간 : " + jArr.getJSONObject(1).getString("dataTime"));
                    Log.d("mylog", "ForeTask : 발생원인 : " + jArr.getJSONObject(1).getString("informCause"));
                    Log.d("mylog", "ForeTask : 예보등급 : " + jArr.getJSONObject(1).getString("informGrade"));
                    Log.d("mylog", "ForeTask : 예보개황 : " + jArr.getJSONObject(1).getString("informOverall"));
                    Log.d("mylog", "ForeTask : 언제날짜예보? : " + jArr.getJSONObject(1).getString("informData"));
                    */

                    String today_dataTime = jArr.getJSONObject(0).getString("dataTime");
                    String today_informCause = jArr.getJSONObject(0).getString("informCause");
                    String today_informGrade = jArr.getJSONObject(0).getString("informGrade");
                    String today_informOverall = jArr.getJSONObject(0).getString("informOverall");

                    String tom_dataTime = jArr.getJSONObject(1).getString("dataTime");
                    String tom_informCause = jArr.getJSONObject(1).getString("informCause");
                    String tom_informGrade = jArr.getJSONObject(1).getString("informGrade");
                    String tom_informOverall = jArr.getJSONObject(1).getString("informOverall");

                    bundle.putString("today_dataTime", today_dataTime);
                    bundle.putString("today_informCause", today_informCause);
                    bundle.putString("today_informOverall", today_informOverall);
                    bundle.putString("today_informGrade", today_informGrade);

                    bundle.putString("tom_dataTime", tom_dataTime);
                    bundle.putString("tom_informCause", tom_informCause);
                    bundle.putString("tom_informOverall", tom_informOverall);
                    bundle.putString("tom_informGrade", tom_informGrade);

                    intent.putExtras( bundle );

                    setResult(10, intent);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    // '시' 이름 가공해줄 함수
    private String strProcessing ( String siName ) {
        // 가공된 '시' 명을 반환할 변수
        String res = "";

        switch ( siName ) {
            // 앞 2글자만 필요한 시, 도
            case "서울특별시" :
            case "부산광역시" :
            case "대구광역시" :
            case "인천광역시" :
            case "광주광역시" :
            case "대전광역시" :
            case "울산광역시" :
            case "제주특별자치도" :
            case "경기도" :
            case "강원도" :
                res = siName.substring(0, 2);
                break;

            // 1,3번째 글자가 필요한 애들
            case "충청북도" :
            case "충청남도" :
            case "전라북도" :
            case "전라남도" :
            case "경상북도" :
            case "경상남도" :
                res = siName.substring(0,1) + siName.substring(2, 3);
                break;
        }

        return res;
    }


}

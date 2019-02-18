package com.koreais.a5_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dust_Info extends AppCompatActivity {

    TextView tv_cai_text, tv_pm10_text ,tv_pm_text, tv_o3_text, tv_so2_text, tv_co_text, tv_no2_text;
    Button btn_esc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust__infor);
        Log.d("mylog", "Dust_Info 들어옴");

        /**---- 닫기 button -----*/
        btn_esc         = findViewById(R.id.btn_esc);

        /**---- textview ----*/
        tv_cai_text     = findViewById(R.id.tv_cai_text);
        tv_pm_text      = findViewById(R.id.tv_pm_text);
        tv_o3_text      = findViewById(R.id.tv_o3_text);
        tv_so2_text     = findViewById(R.id.tv_so2_text);
        tv_co_text      = findViewById(R.id.tv_co_text);
        tv_no2_text     = findViewById(R.id.tv_no2_text);



        tv_cai_text.setText("통합대기환경지수(CAI, Comprehensive air-quality index)는 대기오염도 측정치를 국민이 쉽게 알수 있도록 하고 " +
                            "대기오염으로부터 피해를 예방하기 위한 행동지침을 국민에게 제시하기 위하여 대기오염도에 따른 인체 영향 및 체감오염도를" +
                            " 고려하여 개발된 대기오염도 표현방식");

        tv_pm_text.setText("미세먼지는 직경에 따라 PM10과 PM2.5등으로 구분하며,PM10은 1000분의 10mm보다 작은 먼지이며, PM2.5는 1000분의 2.5mm보다" +
                    "작은 먼지로, 머리카락 직경(약 60um)의 1/20~1/30 크기보다 작은 입지이다." +
                    " 미세먼지는 공기 중 고체상태와 액체상태의 입자의 혼합물로 배출되며 화학반응 또는 자연적으로 생성된다. 사업장 연소, 자동차 연로연소," +
                    " 생물성 연소 과정등 특정 배출원으로부터 직접 발생한다. PM2.5의 경우 상당량이 황산화물(SOx), 질소산화물(NOx),암모니아(NH3)," +
                    " 휘발성 유기화학물(VOCs)등의 전구물질이 대기 중의 특정 조건에서 반응하여 2차 생성된다. 자연적으로 존재하는 입자로서 광물 입자(예 : 황사)," +
                    " 소금입자, 생물성 입자(예:꽃가루, 미생물)등이 있다. 미세먼지 조성은 매우 다양하나, 주로 탄소성분(유기탄소, 원소탄소), 이온성분(황산염," +
                    " 질산염, 암모늄), 광물성분 등으로 구성되어 있다.\n" +
                    "미세먼지는 천식과 같은 호흡기계 질병을 악화시키고, 폐 기능의 저하를 초래한다. PM2.5는 입자가 미세하여 코 점막을 통해 걸러지지 않고 흡입시" +
                    " 폐포까지 직접 침투하여  천식이나 폐질환의 유병률과 조기사망률을 증가시킨다. 또한 미세먼지는 시정을 악화시키고, 식물의 잎 표면에 침적되어 신진대사를 방해하며, " +
                    " 건축물이나 유적물 및 동상 등에 퇴적되어 부식을 일으킨다.");

        tv_o3_text.setText("오존은 대기 중에 배출된 NOx와 휘발성유기화합물(VOCs) 등이 자외선과 광화학 반을을 일으켜 생성된 PAN, 알데하이드, Acrolein 등의 광화학 옥시단트의 " +
                         " 일종으로 2차 오염물질에 속한다. 전구물질인 휘발성 유기화합물은 자동차, 화학공장, 정유공장과 같은 산업시설과 자연적 생성 등 다양한 배출원에서 발생한다." +
                         "오존에 반복 노출시에는 폐에 피해를 줄 수 있는데, 가슴의 통증, 기침, 메스꺼움, 목 자극, 소화 등에 영향을 미치며, 기관지염, 심장질환, 폐기종 및 천식을" +
                         " 약화시키고, 폐활량을 감소 시킬 수 있다. 특히 기관지 천식 환자나 호흡기 질환자, 어린이, 노약자 등에게는 많은 영향을 미치므로 주의해야할 필요가 있다." +
                         " 또한 농작물과 식물에 직접적으로 영향을 미쳐 수확량이 감소되기도 하며 잎이 말라 죽기도 한다.");

        tv_so2_text.setText("황산화물의 일종으로 물에 잘 녹는 무색의 자극적인 냄새가 나는 불연성가스이다. 천연으로는 화산, 온천 등에 존재하며 황화수소와 반응하여 황을 생성한다." +
                        " 황을 함유하는 석탄, 석유 등의 화석연로가 연소될 때 인위적으로 배출되며, 주요 배출원은 발전소, 난방장치, 금속 제련공장, 정유공장 및 기타 산업공정 등에서 발생한다." +
                        "고농도의 아황산가스는 옥외 활동이 많고 천식에 걸린 어른과 어린이에게 일시적으로 호흡장애를 일으킬 수 있으며, 고농도에 폭로될 경우 호흡기계 질환을 일으키고 심장혈관" +
                        " 질환을 악화시키는 것으로 알려져 있다. 질소산화물과 함께 산성비의 주요원인 물질로 토양등의 산성화에 영향을 미치고 바람에 의해 장거리 수송되어 다른 지역에" +
                        " 영향을 주며 식물의 잎맥손상 등을 일으키고 시정장애를 일으키며 각종 구조물의 부식을 촉진시킨다.");

        tv_co_text.setText("일산화탄소는 무색, 무취의 유독성 가스로서 연료속의 탄소성분이 불완전 연소되었을 때 발생한다. 배출원은 주로 수송부문이 차지아며, 산업공정과" +
                        " 비수송부문의 연료연소 그리고 산불과 같은 자연발생원 및 주방, 담배연기, 지역난방과 같은 실내 발생원이 있다." +
                        "일산화단소의 인체 영향을 살펴보면, 혈액순환 중에서 산소운반 역할을 하는 헤모그로빈을 카르복실헤모글로빈(COHb)으로 변성시켜 산소의 운반기능을" +
                        " 저하시키며, 고농도의 일산화탄소는 유독성이 있어 건강한 사람에게도 치명적인 해를 입힌다.");

        tv_no2_text.setText("이산화질소는 적갈색의 반응성이 큰 기체로서, 대기 중에서 일산화질소의 산화에 의해서 발행하며, 대기 중에서 휘발성유기화합물과 반응하여 오존을 생성하는" +
                         " 전구밀질(precursor)의 역할을 한다. 주요 배출원은 자동차, 발전소와 같은 고온 연소공정과 화학물질 제조공정 등이 있으며, 토양중의 세균에 의해 생성되는 자연적 현상 등이 있다." +
                         "질소산화물의 인체영향을 살펴보면, 일산화질소(NO)보다는 이산화질소(NO2)가 인체에 더욱 큰 피해를 주는 것으로 알려져 있다. 고농도에 노출되면 눈, 코등의 점막에서" +
                         " 만성 기관지염, 폐렴, 폐출혈, 폐수종의 발병으로까지 발전할 수 있는 것으로 보고되고 있으며, 식물에 대한 피해로는 식물세포를 파괴하여 꽃식물의 잎에 갈색이나 흑살색의 반점이 생기게 한다.");

        btn_esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

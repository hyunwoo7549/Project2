package com.koreais.a5_project;

public class Address {
    // 시, 도
    final private static String[] sido       = {"시 선택", "서울특별시", "인천광역시", "부산광역시", "울산광역시", "대구광역시", "대전광역시", "광주광역시", "충청북도", "충청남도", "경상북도", "경상남도", "전라북도", "전라남도", "경기도", "강원도", "제주특별자치도"};

    // 군, 구, 동
    final private static String[] seoul      = {"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
    final private static String[] jeju       = {"이도동", "연동", "동홍동", "성산읍", "대정읍", "고산리"};
    final private static String[] gyunggi    = {"신풍동", "인계동", "광교동", "영통동", "천천동", "경수대로(동수원)", "고색동", "대왕판교로(백현동)", "단대동", "정자동", "수내동", "성남대로(모란역)", "복정동", "운중동", "상대원동", "의정부동", "의정부1동", "안양6동", "부림동", "호계동", "안양2동", "철산동", "소하동", "고잔동", "원시동", "본오동", "원곡동", "부곡동1", "호수동", "중앙대로(고잔로)", "별양동", "과천동", "교문동", "동구동", "부곡3동", "고천동", "정왕동", "시화산단", "대야동", "금곡동", "오남읍", "별내동", "화도읍", "비전동", "안중", "평택항", "송북동", "금촌동", "운정", "행신동", "식사동", "백마로(마두역)", "신원동", "주엽동", "경안동", "김량장동", "수지", "기흥", "중부대로(구갈동)", "설성면", "창전동", "관인면", "선단동", "일동면", "사우동", "고촌읍", "통진읍", "당동", "산본동", "오산동", "신장읍", "남양읍", "향남읍", "동탄", "우정읍", "청계동", "백석읍", "고읍", "보산동", "봉산동", "여주", "연천", "가평", "양평", "소사본동", "내동", "중2동", "오정동", "송내대로(중동)"};
    final private static String[] gangwon    = {"중앙로", "석사동", "방산면", "양구읍", "중앙동(원주)", "명륜동", "문막읍", "옥천동", "천곡동", "남양동1", "평창읍", "북평면", "정선읍", "간성읍", "치악산", "횡성군", "상리", "양양군", "속초시(금호동)", "철원군", "영월읍", "(평창)수도권본부 이동차", "(보광)경기도이동차"};
    final private static String[] chungbuk   = {"송정동(봉명동)", "사천동", "문화동", "용암동", "복대동", "호암동", "칠금동", "장락동", "오창읍", "매포읍", "청천면", "진천읍", "금왕", "영동읍", "증평읍", "보은읍", "옥천읍"};
    final private static String[] chungnam   = {"성황동", "백석동", "성성동", "성거읍", "사곡면", "공주", "부여읍", "독곶리", "동문동", "대산리", "송산면", "당진시청사", "모종동", "배방읍", "도고면", "둔포면", "인주면", "논산", "파도리", "이원면", "태안읍", "예산군", "대천2동", "주교면", "홍성읍", "금산읍", "청양읍", "엄사면", "서천읍", "서면"};
    final private static String[] daegu      = {"수창동", "지산동", "서호동", "이현동", "평리동", "대명동", "노원동", "신암동", "태전동", "만촌동", "호림동", "현풍읍", "이곡동", "시지동", "진천동"};
    final private static String[] daejeon    = {"읍내동", "문평동", "문창동", "구성동", "노은동", "상대동(대전)", "대흥동1", "성남동1", "대성동", "정림동", "둔산동", "월평동"};
    final private static String[] gwangju    = {"서석동", "농성동", "치평동", "두암동", "운암동", "건국동", "송정1동", "오선동", "주월동"};
    final private static String[] gyeongbuk  = {"장흥동", "장량동", "대도동", "대송면", "3공단", "성건동", "문당동", "명륜동", "공단동", "원평동", "형곡동", "4공단", "휴천동", "중방동", "상주시", "칠곡군", "지품면", "화북면", "안계면", "태하리"};
    final private static String[] gyeongnam  = {"회원동", "봉암동", "상봉동", "대안동", "상대동", "명서동", "웅남동", "가음정동", "용지동", "반송로", "사파동", "경화동", "하동읍", "동상동", "삼방동", "장유동", "저구리", "아주동", "사천읍", "대산면", "북부동", "웅상읍", "내일동", "무전동", "고성읍", "거창읍", "가야읍", "함양읍", "남해읍", "남상면"};
    final private static String[] incheon    = {"신흥", "송림", "구월동", "숭의", "석바위", "부평역", "부평", "연희", "검단", "계산", "고잔", "석남", "송해", "동춘", "운서", "송현", "논현", "청라", "송도", "원당", "석모리", "덕적도", "백령도"};
    final private static String[] jeonbuk    = {"중앙동(전주)", "삼천동", "팔복동", "송천동", "금암동", "신풍동(군산)", "소룡동", "개정동", "남중동", "팔봉동", "모현동", "연지동", "신태인", "죽항동", "고창읍", "심원면", "부안읍", "새만금", "요촌동", "고산면", "진안읍", "운암면", "임실읍", "무주읍", "순창읍", "장수읍"};
    final private static String[] jeonnam    = {"연향동", "순천만", "호두리", "신대", "빛가람동", "담양읍", "장성읍", "화양면", "율촌면", "삼일동", "중동", "태인동", "진상면", "광양읍", "해남읍", "나불리", "송단리", "영광읍", "장흥읍"};
    final private static String[] ulsan      = {"대송동", "성남동", "부곡동(울산)", "여천동(울산)", "야음동", "삼산동", "도로변", "신정동", "덕신리", "무거동", "효문동", "화산리", "상남리", "농소동", "삼남면", "약사동", "전하동"};
    final private static String[] busan      = {"광복동", "초량동", "태종대", "전포동", "온천동", "명장동", "대연동", "학장동", "덕천동", "청룡동", "좌동", "장림동", "대저동", "녹산동", "연산동", "기장읍", "용수리", "수정동", "부곡동", "광안동", "대신동", "덕포동", "부산북항", "부산신항"};

    public static String[] getSido() {
        return sido;
    }

    public static String[] getSeoul() {
        return seoul;
    }

    public static String[] getIncheon() {
        return incheon;
    }

    public static String[] getJeju() {
        return jeju;
    }

    public static String[] getGyunggi() {
        return gyunggi;
    }

    public static String[] getGangwon() {
        return gangwon;
    }

    public static String[] getBusan() {
        return busan;
    }

    public static String[] getChungbuk() {
        return chungbuk;
    }

    public static String[] getChungnam() {
        return chungnam;
    }

    public static String[] getDaegu() {
        return daegu;
    }

    public static String[] getDaejeon() {
        return daejeon;
    }

    public static String[] getGwangju() {
        return gwangju;
    }

    public static String[] getGyeongbuk() {
        return gyeongbuk;
    }

    public static String[] getGyeongnam() {
        return gyeongnam;
    }

    public static String[] getJeonbuk() {
        return jeonbuk;
    }

    public static String[] getJeonnam() {
        return jeonnam;
    }

    public static String[] getUlsan() {
        return ulsan;
    }
}

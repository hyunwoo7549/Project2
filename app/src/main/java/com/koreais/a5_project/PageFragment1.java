package com.koreais.a5_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageFragment1 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment 생성시, 미리 그려놓은 xml을 객체화하여, 반환해준다. (반환 타입 = View)

        Log.d("mylog", "PageFragment1, onCreateView()");

        // 객체화 하기
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.activity_page_fragment1, container, false);


        // 만들어진 뷰 반환해주기
        return layout;

    } // onCreateView()

    // onCreateView()는 페이지 만들어질 때 최초 1회만 실행되고,
    // 페이지가 유지되는 상태라면 페이지가 반복적으로 전환될떄 호출되지 않는다.


    @Override
    public void setUserVisibleHint( boolean isVisibleToUser ) {
        // 변화가 있을때 호출되는 메서드
        // 최초 1회 비활성화 값 (false)로 무조건 수행된다. (생성될 때)

        // Fragment 간에 값을 전달하려면 MainActivity를 거쳐야한다.
        // 만들어진 객체끼리 연관이 없다.

        MainActivity main = (MainActivity)getActivity();
        // MainActivity의 onCreate() 과정 중, 이 Fragment가 생성되기 때문에..
        // 최초 1회 main이 아직 안 만들어진 상태에서 호출되는 것을 예외처리
        if( main == null ){
            return;
        }

        if( isVisibleToUser ){
            // 내 페이지가 활성화됐다!
            Log.d("mylog","1페이지 활성화, count=" + main.count);
        }
        else {
            // 비활성화 됐다!
            Log.d("mylog","1페이지 비활성화, count=" + main.count);
        }

        super.setUserVisibleHint( isVisibleToUser );
    }



}

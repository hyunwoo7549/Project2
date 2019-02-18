package com.koreais.a5_project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // 페이지가 전환됐을 때 (드래그 또는 버튼 클릭)
    // 해당 뷰를 만들어준다.
    @Override
    public Fragment getItem( int i ) {
        // getItem() --> 몇 번쨰 페이지 뷰를 요청

        switch ( i ){
            case PageInfo.PAGE1 : // 첫 페이지가 0부터 시작
                return new PageFragment1(); // Fragment 객체 생성하여 반환 (반환 타입 : Fragment)

            //case PageInfo.PAGE2 :
                //return new PageFragment2(); // Fragment 객체 생성하여 반환 (반환 타입 : Fragment)
        }

        return null;
    }

    @Override
    public int getCount() {
        return PageInfo.PAGES; // 전체 페이지 수 반환
    }
}

package lee.andrew.cerner.facial_recognition_search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


public class MainScreenActivity extends FragmentActivity {
    ScrollerAdapter mScrollerAdapter;
    ViewPager mViewPager;
    String facebookPassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        mScrollerAdapter = new ScrollerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mScrollerAdapter);
    }

    public class ScrollerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;

        public ScrollerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment=null;
            if(i==0)
                fragment = new CameraFragment();
            else if(i==1)
                fragment = new AboutFragment();        
            Bundle args = new Bundle();
            args.putInt("current_page", i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return "Camera";
            case 1:
                return "About";
            case 2:
                return "About";
            }
            return null;
        }
    }


}

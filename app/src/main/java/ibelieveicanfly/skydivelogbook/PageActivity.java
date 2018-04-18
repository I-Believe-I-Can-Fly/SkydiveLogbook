package ibelieveicanfly.skydivelogbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class PageActivity extends AppCompatActivity implements PageFragment.OnFragmentInteractionListener {

    private CustomPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String USER;
    private Integer JUMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        mSectionsPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.logPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //get extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USER = extras.getString("user");
            JUMP = extras.getInt("jump");
        }

    }


    public class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PageFragment pageFragment = new PageFragment().newInstance(USER, position);
            return pageFragment;
        }

        @Override
        public int getCount() {
            // Show 100 total pages.
            return 100;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

}

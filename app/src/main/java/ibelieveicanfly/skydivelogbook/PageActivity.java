package ibelieveicanfly.skydivelogbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PageActivity extends AppCompatActivity {

    private CustomPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String userID;
    private Integer JUMP = 0;
    private Integer ITEMS = 0;

    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        //get extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            JUMP = extras.getInt("jump");
        }

        auth = FirebaseAuth.getInstance();
        //retrieve userid
        userID = auth.getCurrentUser().getUid();

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference(userID);

        loadPages();


        mSectionsPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.logPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(JUMP-1);
            }
        });
    }


    private void loadPages() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ITEMS++;

                    //update number of pages in pagerAdapter
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // rip
            }
        });
    }


    public class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: Send actual jumpNr instead of position, will not work unless the user logs every jump from jump 1.
            PageFragment pageFragment = new PageFragment().newInstance(userID, position);
            return pageFragment;
        }

        @Override
        public int getCount() {
            // Show x pages
            return ITEMS;
        }
    }

}

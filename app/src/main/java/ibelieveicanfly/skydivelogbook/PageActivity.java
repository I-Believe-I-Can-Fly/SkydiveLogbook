package ibelieveicanfly.skydivelogbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PageActivity extends AppCompatActivity implements PageFragment.OnFragmentInteractionListener {

    private CustomPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String USER;
    private Integer JUMP;
    private Integer ITEMS = 0;

    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        auth = FirebaseAuth.getInstance();
        //retrieve userid
        userID = auth.getCurrentUser().getUid();

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference(userID);

        loadPages();


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
            PageFragment pageFragment = new PageFragment().newInstance(USER, position);
            return pageFragment;
        }

        @Override
        public int getCount() {
            // Show 100 total pages.
            return ITEMS;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

}

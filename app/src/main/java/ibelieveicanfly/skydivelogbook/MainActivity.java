package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2;


    private ArrayList<LogbookPage> jumpList = new ArrayList<>();

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        // auth.signOut();
        userStatus(auth.getCurrentUser());

        this.recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreatePageActivity.class);
                startActivity(intent);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

            }
        });

        refreshListAdapter();
    }

    // userStatus checks if user is signed in or not
    private void userStatus(FirebaseUser currentUser) {
        if (currentUser != null) {
            // User is signed in
            //retrieve userid

            userID = auth.getCurrentUser().getUid();
            Log.d(TAG, userID);
            this.mDatabase = FirebaseDatabase.getInstance();
            this.myRef = mDatabase.getReference("Logs");

            getLogs(userID);
        } else {
            // User is not signed in

            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    }

    /**
     * refreshListAdapter
     * updates the recyclerView with new content and scrolls to bottom
     */
    public void refreshListAdapter() {
        //We set the array to the adapter
        adapter.setListContent(jumpList);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
        //Scroll to bottom :)
        recyclerView.scrollToPosition(jumpList.size() - 1);
    }

    /**
     * getLogs
     * retrieves log instances related to userID from database
     * adds instances to list
     */
    private void getLogs(String uID) {
        // Read from the database
        myRef.child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jumpList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LogbookPage logbookPage = child.getValue(LogbookPage.class);
                    jumpList.add(logbookPage);

                    refreshListAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // rip
            }
        });
    }

    /**
     * Adds button to action bar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_profile, menu);
        return true;
    }

    /**
     * Adds event to button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                //rip
                return super.onOptionsItemSelected(item);

        }
    }
}

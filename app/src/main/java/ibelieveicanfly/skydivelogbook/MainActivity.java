package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

        //Floating action button for adding new pages to the logbook
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_white_48dp); //adds the plus icon to the button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatePageActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatePageActivity.class);
                startActivity(intent);
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

    public void toUserProfile(View view) {
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

}

package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    RecyclerView recyclerView;
    CustomAdapter adapter;

    private ArrayList<LogbookPage> jumpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    String helloText = this.getResources().getString(R.string.hello);

        auth = FirebaseAuth.getInstance();
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

        TEST();
        refreshListAdapter();
    }

    // userStatus checks if user is signed in or not
    private void userStatus(FirebaseUser currentUser) {


        if (currentUser != null) {
            // User is signed in

            // auth.signOut();
            // Code here ...
        } else {
            // User is not signed in

            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    }

    public void refreshListAdapter() {
        //We set the array to the adapter
        adapter.setListContent(jumpList);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
        //Scroll to bottom :)
        recyclerView.scrollToPosition(jumpList.size() - 1);
    }

    public void TEST() {
        //fill with empty data
        for (int i = 0; i < 100; i++) {
            LogbookPage page = new LogbookPage(i + 1, "a", "a", "a", "a", "a", "a", "a", "a", true);
            jumpList.add(page);
        }
    }
}

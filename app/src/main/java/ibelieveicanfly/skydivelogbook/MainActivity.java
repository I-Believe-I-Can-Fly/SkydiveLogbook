package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    private FirebaseAuth auth;
    private ArrayList<LogbookPage> jumpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        userStatus(auth.getCurrentUser());

        this.recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this);

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
            LogbookPage page = new LogbookPage(i + 1, "a", "a", "a", "a", "a", "a", "a", "a", "a", true);
            jumpList.add(page);
        }
    }
}

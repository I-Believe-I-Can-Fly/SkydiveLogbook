package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    String helloText = this.getResources().getString(R.string.hello);

        auth = FirebaseAuth.getInstance();
        userStatus(auth.getCurrentUser());
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
}

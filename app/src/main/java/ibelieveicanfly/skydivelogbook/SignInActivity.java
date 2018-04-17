package ibelieveicanfly.skydivelogbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private EditText txt_email;
    private EditText txt_password;
    private String email;
    private String password;
    private FirebaseAuth auth;
    private String authError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_email = findViewById(R.id.edit_email);
        txt_password = findViewById(R.id.edit_password);
        auth = FirebaseAuth.getInstance();
        authError = this.getResources().getString(R.string.authError);
    }

    @Override
    public void onBackPressed(){
        // TODO : code here ...
    }

    // Create new user
    public void onRegisterClick(View view) {
        // E-Mail
        // Name
        // Date of birth
        // Password
        if (inputOK()) {
            /*
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, authError, Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
                    */
        }
    }

    // Sign in existing user
    public void onSignInClick(View view) {
        // Email
        // Password
        if (inputOK()) {
            /*
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, authError, Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
                    */
        }
    }

    // Check if user input is ok
    private boolean inputOK() {
        // Get values
        String emailError, passwordError;
        emailError = this.getResources().getString(R.string.emailEmpty);
        passwordError = this.getResources().getString(R.string.passwordEmpty);
        email = txt_email.getText().toString();
        password = txt_password.getText().toString();

        // Check values
        if (email.isEmpty()) {
            Toast.makeText(SignInActivity.this, emailError, Toast.LENGTH_LONG).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(SignInActivity.this, passwordError, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

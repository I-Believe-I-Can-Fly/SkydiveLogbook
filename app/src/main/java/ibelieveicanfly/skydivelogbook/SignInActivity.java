package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private EditText edit_firstName;
    private EditText edit_lastName;
    private EditText edit_email;
    private EditText edit_password;
    private EditText edit_confirmPass;
    private EditText edit_emailLogIn;
    private EditText edit_passwordLogIn;
    private TextView txt_register;
    private TextView txt_forgotten;
    private FirebaseAuth auth;
    private String firsNameEmpty;
    private String lastNameEmpty;
    private String emailEmpty;
    private String passwordEmpty;
    private String confirmPassEmpty;
    private String passwordDontMatch;
    private String email;
    private String password;
    private String newUser;
    private String oldUser;
    private String boldRegister;
    private String boldLogin;
    private String forgottenPass;
    private RelativeLayout layout_signIn;
    private RelativeLayout layout_register;
    private String authError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getValues();

        txt_register.setText(Html.fromHtml(newUser + boldRegister));
        txt_forgotten.setText(forgottenPass);

        onRegisterClick();
        onForgottenClick();
    }

    @Override
    public void onBackPressed() {
        // TODO : code here ...
    }

    private void getValues() {
        // Get components
        edit_emailLogIn = findViewById(R.id.edit_emailLogIn);
        edit_passwordLogIn = findViewById(R.id.edit_passwordLogIn);
        edit_firstName = findViewById(R.id.edit_firstName);
        edit_lastName = findViewById(R.id.edit_lastName);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_confirmPass = findViewById(R.id.edit_confirmPass);
        txt_register = findViewById(R.id.txt_register);
        txt_forgotten = findViewById(R.id.txt_forgotten);
        layout_signIn = findViewById(R.id.layout_LogIn);
        layout_register = findViewById(R.id.layout_register);

        // Get instance from Firebase
        auth = FirebaseAuth.getInstance();

        // Get string resources
        firsNameEmpty = this.getResources().getString(R.string.firstNameEmpty);
        lastNameEmpty = this.getResources().getString(R.string.lastNameEmpty);
        confirmPassEmpty = this.getResources().getString(R.string.confirmPassEpty);
        passwordDontMatch = this.getResources().getString(R.string.passDontMatch);
        emailEmpty = this.getResources().getString(R.string.emailEmpty);
        passwordEmpty = this.getResources().getString(R.string.passwordEmpty);
        authError = this.getResources().getString(R.string.authError);
        newUser = this.getResources().getString(R.string.newUser);
        oldUser = this.getResources().getString(R.string.existingUser);
        forgottenPass = this.getResources().getString(R.string.forgotPassword);
        boldRegister = " <b>" + this.getResources().getString(R.string.boldRegister) + "</b>";
        boldLogin = " <b>" + this.getResources().getString(R.string.boldLogin) + "</b>";
    }

    private void onRegisterClick() {
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_register.getText().toString().startsWith(newUser)) {
                    // If user want's to register
                    layout_signIn.setVisibility(View.GONE);
                    layout_register.setVisibility(View.VISIBLE);
                    txt_register.setText(Html.fromHtml(oldUser + boldLogin));


                } else if (txt_register.getText().toString().startsWith(oldUser)) {
                    // If user wants to log in
                    layout_register.setVisibility(View.GONE);
                    layout_signIn.setVisibility(View.VISIBLE);
                    txt_register.setText(Html.fromHtml(newUser + boldRegister));
                }
            }
        });
    }

    private void onForgottenClick() {
        txt_forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "In progress!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Create new user
    public void onRegisterClick(View view) {

        // Get input
        String firstName = edit_firstName.getText().toString();
        String lastName = edit_lastName.getText().toString();
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();
        String confirmPassword = edit_confirmPass.getText().toString();

        if (firstName.isEmpty()) {
            Toast.makeText(SignInActivity.this, firsNameEmpty, Toast.LENGTH_SHORT).show();

        } else if (lastName.isEmpty()) {
            Toast.makeText(SignInActivity.this, lastNameEmpty, Toast.LENGTH_SHORT).show();

        } else if (email.isEmpty()) {
            Toast.makeText(SignInActivity.this, emailEmpty, Toast.LENGTH_SHORT).show();

        } else if (password.isEmpty()) {
            Toast.makeText(SignInActivity.this, passwordEmpty, Toast.LENGTH_SHORT).show();

        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(SignInActivity.this, confirmPassEmpty, Toast.LENGTH_SHORT).show();

        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(SignInActivity.this, passwordDontMatch, Toast.LENGTH_SHORT).show();
            edit_password.setText("");
            edit_confirmPass.setText("");

        } else {

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                Intent main = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(main);
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                if (task.getException() != null) {
                                    Toast.makeText(SignInActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignInActivity.this, authError, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
        }
    }

    // Log in existing user
    public void onLogInClick(View view) {

        // Get input
        email = edit_emailLogIn.getText().toString();
        password = edit_passwordLogIn.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(SignInActivity.this, emailEmpty, Toast.LENGTH_SHORT).show();

        } else if (password.isEmpty()) {
            Toast.makeText(SignInActivity.this, passwordEmpty, Toast.LENGTH_SHORT).show();

        } else {

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                Intent main = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(main);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                if (task.getException() != null) {
                                    String error = task.getException().getLocalizedMessage();
                                    Log.d(TAG, "HELLO " + error);
                                    Toast.makeText(SignInActivity.this, error, Toast.LENGTH_LONG).show();

                                    if(error.contains("The password is invalid")){
                                        // If password is invalid
                                        Log.d(TAG, "HELOOOO");
                                        txt_forgotten.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(SignInActivity.this, authError, Toast.LENGTH_SHORT).show();
                                    txt_forgotten.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        }
    }
}

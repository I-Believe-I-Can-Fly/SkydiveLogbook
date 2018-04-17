package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btn_logIn;
    private Button btn_register;
    private TextView txt_register;
    private FirebaseAuth auth;
    private String authError;
    private String firsNameEmpty;
    private String lastNameEmpty;
    private String emailEmpty;
    private String passwordEmpty;
    private String confirmPassEmpty;
    private String passwordDontMatch;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String newUser;
    private String oldUser;
    private String boldRegister;
    private String boldLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Get values
        edit_emailLogIn = findViewById(R.id.edit_emailLogIn);
        edit_passwordLogIn = findViewById(R.id.edit_passwordLogIn);
        edit_firstName = findViewById(R.id.edit_firstName);
        edit_lastName = findViewById(R.id.edit_lastName);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_confirmPass = findViewById(R.id.edit_confirmPass);
        txt_register = findViewById(R.id.txt_register);
        btn_logIn = findViewById(R.id.btn_logIn);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        firsNameEmpty = this.getResources().getString(R.string.firstNameEmpty);
        lastNameEmpty = this.getResources().getString(R.string.lastNameEmpty);
        confirmPassEmpty = this.getResources().getString(R.string.confirmPassEpty);
        passwordDontMatch = this.getResources().getString(R.string.passDontMatch);
        emailEmpty = this.getResources().getString(R.string.emailEmpty);
        passwordEmpty = this.getResources().getString(R.string.passwordEmpty);
        authError = this.getResources().getString(R.string.authError);
        newUser = this.getResources().getString(R.string.newUser);
        oldUser = this.getResources().getString(R.string.existingUser);
        boldRegister = " <b>" + this.getResources().getString(R.string.boldRegister) + "</b>";
        boldLogin = " <b>" + this.getResources().getString(R.string.boldLogin) + "</b>";

        txt_register.setText(Html.fromHtml(newUser + boldRegister));
        onRegisterClick();
    }

    @Override
    public void onBackPressed() {
        // TODO : code here ...
    }

    private void onRegisterClick() {


        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_register.getText().toString().startsWith(newUser)) {
                    // If user want's to register
                    edit_emailLogIn.setVisibility(View.GONE);
                    edit_passwordLogIn.setVisibility(View.GONE);
                    btn_logIn.setVisibility(View.GONE);
                    edit_email.setVisibility(View.VISIBLE);
                    edit_password.setVisibility(View.VISIBLE);
                    edit_firstName.setVisibility(View.VISIBLE);
                    edit_lastName.setVisibility(View.VISIBLE);
                    edit_confirmPass.setVisibility(View.VISIBLE);
                    btn_register.setVisibility(View.VISIBLE);
                    txt_register.setText(Html.fromHtml(oldUser + boldLogin));

                } else if (txt_register.getText().toString().startsWith(oldUser)) {
                    // If user wants to log in
                    edit_emailLogIn.setVisibility(View.VISIBLE);
                    edit_passwordLogIn.setVisibility(View.VISIBLE);
                    btn_logIn.setVisibility(View.VISIBLE);
                    edit_email.setVisibility(View.GONE);
                    edit_password.setVisibility(View.GONE);
                    edit_firstName.setVisibility(View.GONE);
                    edit_lastName.setVisibility(View.GONE);
                    edit_confirmPass.setVisibility(View.GONE);
                    btn_register.setVisibility(View.GONE);
                    txt_register.setText(Html.fromHtml(newUser + boldRegister));
                }
            }
        });
    }

    // Create new user
    public void onRegisterClick(View view) {

        // Get input
        firstName = edit_firstName.getText().toString();
        lastName = edit_lastName.getText().toString();
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();
        confirmPassword = edit_confirmPass.getText().toString();

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
                                Toast.makeText(SignInActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }

                            // ...
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
                                Toast.makeText(SignInActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });
        }
    }
}

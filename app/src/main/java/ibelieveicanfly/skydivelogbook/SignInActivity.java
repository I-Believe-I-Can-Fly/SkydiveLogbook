package ibelieveicanfly.skydivelogbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    private EditText txt_email;
    private EditText txt_password;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_email = findViewById(R.id.edit_email);
        txt_password = findViewById(R.id.edit_password);
    }

    public void onRegisterClick(View view) {
        email = txt_email.getText().toString();
        password = txt_password.getText().toString();

        // TODO : register user here...

    }

    public void onSignInClick(View view) {
        email = txt_email.getText().toString();
        password = txt_password.getText().toString();

        // TODO : sign in user here...

    }
}

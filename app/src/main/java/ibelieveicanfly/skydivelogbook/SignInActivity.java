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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private EditText edit_firstName;
    private EditText edit_lastName;
    private EditText edit_email;
    private EditText edit_password;
    private EditText edit_confirmPass;
    private EditText edit_emailLogIn;
    private EditText edit_passwordLogIn;
    private EditText edit_emailReset;
    private EditText edit_certificate;
    private EditText edit_license;
    private EditText edit_DOB;
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
    private String certificateEmpty;
    private String licenseEmpty;
    private String dobEmpty;
    private String oldUser;
    private String boldRegister;
    private String boldLogin;
    private String forgottenPass;
    private String userNotExists;
    private RelativeLayout layout_signIn;
    private RelativeLayout layout_register;
    private RelativeLayout layout_resetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getValues();
        // TODO : The phone has to be at least 5' 1080x1920 420dpi For the whole register form to show...Fix this!
        // TODO : first name - last name and certificate - license is not to the middle of the screen

        txt_register.setText(Html.fromHtml(newUser + boldRegister));
        txt_forgotten.setText(forgottenPass);

        layout_register.setVisibility(View.GONE);
        layout_resetPass.setVisibility(View.GONE);
        layout_signIn.setVisibility(View.VISIBLE);

        onRegisterClick();
        onForgottenClick();
    }

    @Override
    public void onBackPressed() {
        if (layout_signIn.getVisibility() == View.VISIBLE) {
            moveTaskToBack(true);

        } else if (layout_register.getVisibility() == View.VISIBLE) {
            layout_register.setVisibility(View.GONE);
            layout_signIn.setVisibility(View.VISIBLE);

        } else if (layout_resetPass.getVisibility() == View.VISIBLE) {
            layout_resetPass.setVisibility(View.GONE);
            layout_signIn.setVisibility(View.VISIBLE);

        }
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
        edit_emailReset = findViewById(R.id.edit_emailReset);
        edit_certificate = findViewById(R.id.edit_certificate);
        edit_license = findViewById(R.id.edit_license);
        edit_DOB = findViewById(R.id.edit_DOB);
        txt_register = findViewById(R.id.txt_register);
        txt_forgotten = findViewById(R.id.txt_forgotten);
        layout_signIn = findViewById(R.id.layout_LogIn);
        layout_register = findViewById(R.id.layout_register);
        layout_resetPass = findViewById(R.id.layout_resetPassword);

        // Get instance from Firebase
        auth = FirebaseAuth.getInstance();

        // Get string resources
        userNotExists = this.getResources().getString(R.string.userNotExists);
        firsNameEmpty = this.getResources().getString(R.string.firstNameEmpty);
        lastNameEmpty = this.getResources().getString(R.string.lastNameEmpty);
        confirmPassEmpty = this.getResources().getString(R.string.confirmPassEpty);
        licenseEmpty = this.getResources().getString(R.string.licenseEmpty);
        certificateEmpty = this.getResources().getString(R.string.certificateEmpty);
        dobEmpty = this.getResources().getString(R.string.dobEmpty);
        passwordDontMatch = this.getResources().getString(R.string.passDontMatch);
        emailEmpty = this.getResources().getString(R.string.emailEmpty);
        passwordEmpty = this.getResources().getString(R.string.passwordEmpty);
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
                if (layout_signIn.getVisibility() == View.VISIBLE) {
                    // If user want's to register
                    layout_signIn.setVisibility(View.GONE);
                    layout_register.setVisibility(View.VISIBLE);
                    txt_register.setText(Html.fromHtml(oldUser + boldLogin));


                } else if (layout_register.getVisibility() == View.VISIBLE) {
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
                layout_signIn.setVisibility(View.GONE);
                txt_register.setVisibility(View.GONE);
                layout_resetPass.setVisibility(View.VISIBLE);
            }
        });
    }

    // Send email to user about resetting the password
    public void onResetClick(View view) {
        email = edit_emailReset.getText().toString();
        final String emailSent = this.getResources().getString(R.string.emailSent);
        if (email.isEmpty()) {
            Toast.makeText(SignInActivity.this, emailEmpty, Toast.LENGTH_SHORT).show();
        } else {
            auth.useAppLanguage();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, emailSent, Toast.LENGTH_SHORT).show();
                                layout_resetPass.setVisibility(View.GONE);
                                layout_signIn.setVisibility(View.VISIBLE);
                                txt_register.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(SignInActivity.this, userNotExists, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    // Create new user
    public void onRegisterClick(View view) {

        // Get input
        final String firstName = edit_firstName.getText().toString();
        final String lastName = edit_lastName.getText().toString();
        final String certificate = edit_certificate.getText().toString();
        final String license = edit_license.getText().toString();
        final String dateOfBirth = edit_DOB.getText().toString();
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();
        String confirmPassword = edit_confirmPass.getText().toString();

        if (firstName.isEmpty()) {
            Toast.makeText(SignInActivity.this, firsNameEmpty, Toast.LENGTH_SHORT).show();

        } else if (lastName.isEmpty()) {
            Toast.makeText(SignInActivity.this, lastNameEmpty, Toast.LENGTH_SHORT).show();

        } else if (certificate.isEmpty()) {
            Toast.makeText(SignInActivity.this, certificateEmpty, Toast.LENGTH_SHORT).show();

        } else if (license.isEmpty()) {
            Toast.makeText(SignInActivity.this, licenseEmpty, Toast.LENGTH_SHORT).show();

        } else if (dateOfBirth.isEmpty()) {
            Toast.makeText(SignInActivity.this, dobEmpty, Toast.LENGTH_SHORT).show();

        } else if (email.isEmpty()) {
            Toast.makeText(SignInActivity.this, emailEmpty, Toast.LENGTH_SHORT).show();

        } else if (password.isEmpty()) {
            Toast.makeText(SignInActivity.this, passwordEmpty, Toast.LENGTH_SHORT).show();

        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(SignInActivity.this, confirmPassEmpty, Toast.LENGTH_SHORT).show();

        } else if (!dateOfBirth.matches("^[0-9][1-9]/[0-9][1-9]/[1-9]{4}")) {
            Toast.makeText(SignInActivity.this, "not a valid date", Toast.LENGTH_SHORT).show();

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

                                // Set the name as Display name
                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firstName + ' ' + lastName).build();

                                auth.getCurrentUser().updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Displayname is set");

                                            // Add new user to database and go to MainActivity
                                            User user = new User(firstName, lastName, dateOfBirth, license, certificate, email);
                                            addUserToDB(user, auth.getCurrentUser().getUid());

                                            Intent main = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(main);
                                        }
                                    }
                                });

                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                if (giveExceptionError(task.getException()) == 3) {
                                    edit_email.setText("");
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

                                if (giveExceptionError(task.getException()) == 2) {
                                    // If password is invalid
                                    txt_forgotten.setVisibility(View.VISIBLE);
                                } else {
                                    txt_forgotten.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        }
    }

    private int giveExceptionError(Exception exception) {
        final String weakPassword = this.getResources().getString(R.string.weakPassword);
        final String invalidEmail = this.getResources().getString(R.string.invalidEmail);
        final String emailInUse = this.getResources().getString(R.string.emailInUse);
        final String invalidCred = this.getResources().getString(R.string.invalidCred);
        final String tooManyReq = this.getResources().getString(R.string.tooManyReq);

        if (exception != null) {
            try {
                throw exception;
            } catch (FirebaseAuthWeakPasswordException e) {
                // Weak password, less than 6 chars
                Toast.makeText(SignInActivity.this, weakPassword, Toast.LENGTH_LONG).show();
                return 0;

            } catch (FirebaseAuthInvalidCredentialsException e) {

                if (e.getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                    // Invalid email
                    Toast.makeText(SignInActivity.this, invalidEmail, Toast.LENGTH_LONG).show();
                    return 1;
                } else if (e.getErrorCode().equals("ERROR_WRONG_PASSWORD")) {
                    // Wrong password
                    Toast.makeText(SignInActivity.this, invalidCred, Toast.LENGTH_LONG).show();
                    return 2;
                }

            } catch (FirebaseAuthInvalidUserException e) {
                // User doesn't exist
                Toast.makeText(SignInActivity.this, userNotExists, Toast.LENGTH_LONG).show();
                return 3;

            } catch (FirebaseAuthUserCollisionException e) {
                // E-mail already in use
                Toast.makeText(SignInActivity.this, emailInUse, Toast.LENGTH_LONG).show();
                return 4;

            } catch (FirebaseTooManyRequestsException e) {
                Toast.makeText(SignInActivity.this, tooManyReq, Toast.LENGTH_LONG).show();
                return 5;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return -1;
    }

    // Adds user to database
    private void addUserToDB(User user, String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        // With the firebaseUser Uid as key
        reference.child(userId).setValue(user);
    }
}

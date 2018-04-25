package ibelieveicanfly.skydivelogbook;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView name;
    private TextView age;
    private TextView yis;
    private TextView jumps;
    private TextView certificate;
    private TextView dropzone;

    private ImageButton cancelEdit;
    private ImageButton confirmEdit;
    private ImageButton edit;
    private EditText ageEdit;
    private EditText yisEdit;
    private EditText jumpsEdit;
    private Spinner certificateEdit;
    private EditText dropzoneEdit;

    private User tempUser;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        yis = findViewById(R.id.yis);
        jumps = findViewById(R.id.jumps);
        certificate = findViewById(R.id.certificate);
        dropzone = findViewById(R.id.dropzone);

        cancelEdit = findViewById(R.id.cancelEdit);
        confirmEdit = findViewById(R.id.confirmEdit);
        edit = findViewById(R.id.editButton);
        ageEdit = findViewById(R.id.ageEdit);
        yisEdit = findViewById(R.id.yisEdit);
        jumpsEdit = findViewById(R.id.jumpsEdit);
        certificateEdit = findViewById(R.id.certificateEdit);
        dropzoneEdit = findViewById(R.id.dropzoneEdit);

        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        ageEdit.setVisibility(View.GONE);
        yisEdit.setVisibility(View.GONE);
        jumpsEdit.setVisibility(View.GONE);
        certificateEdit.setVisibility(View.GONE);
        dropzoneEdit.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.certificateEditString, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        certificateEdit.setAdapter(adapter);

/*
        ImageView icon = findViewById(R.id.profilePicture);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.common_google_signin_btn_icon_dark);

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable.setCircular(true);
        icon.setImageDrawable(drawable);
*/
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        getFromDB();

    }

    public void onStart() {
        super.onStart();
        ageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
    }

    private void getFromDB() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference ref = usersRef.child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to get current user
            }
        });
    }

    private void getUser(User user) {
        tempUser = user;

        name.setText(user.firstName + " " + user.lastName);
        age.setText(calculateAge(user) + " (" + user.dateOfBirth +")");
        if (!user.yearsInSport.equals("")) yis.setText(user.yearsInSport);
        if (!user.totalJumps.equals("")) jumps.setText(user.totalJumps);
        certificate.setText(user.certificate);
        if (!user.primaryDropzone.equals("")) dropzone.setText((user.primaryDropzone));

        if (user.userID.equals(auth.getCurrentUser().getUid())) {
            edit.setVisibility(View.VISIBLE);
        }
        else {
            edit.setVisibility(View.GONE);
        }
    }

    public void editClick(View view) {
        ageEdit.setText(tempUser.dateOfBirth);
        yisEdit.setText(tempUser.yearsInSport);
        jumpsEdit.setText(tempUser.totalJumps);
        if (tempUser.certificate.equals("Elev")) certificateEdit.setSelection(0);
        else if (tempUser.certificate.equals("A")) certificateEdit.setSelection(1);
        else if (tempUser.certificate.equals("B")) certificateEdit.setSelection(2);
        else if (tempUser.certificate.equals("C")) certificateEdit.setSelection(3);
        else if (tempUser.certificate.equals("D")) certificateEdit.setSelection(4);
        dropzoneEdit.setText(tempUser.primaryDropzone);

        cancelEdit.setVisibility(View.VISIBLE);
        confirmEdit.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);

        changeVisibilityAll();
    }

    public void confirmClick(View view) {

        tempUser.dateOfBirth = ageEdit.getText().toString();
        tempUser.yearsInSport = yisEdit.getText().toString();
        tempUser.totalJumps = jumpsEdit.getText().toString();
        tempUser.certificate = certificateEdit.getSelectedItem().toString();
        tempUser.primaryDropzone = dropzoneEdit.getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(uid).setValue(tempUser);

        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        changeVisibilityAll();

        getFromDB();

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
    }

    public void cancelClick(View view) {
        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        changeVisibilityAll();
    }

    private void changeVisibility(TextView text, EditText edit) {
        if (text.getVisibility() == View.VISIBLE) {
            text.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            edit.setEnabled(true);
        }
        else {
            text.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            edit.setEnabled(false);
        }
    }

    private void changeVisibilityAll() {
        changeVisibility(age, ageEdit);
        changeVisibility(yis, yisEdit);
        changeVisibility(jumps, jumpsEdit);
        if (certificate.getVisibility() == View.VISIBLE) {
            certificate.setVisibility(View.GONE);
            certificateEdit.setVisibility(View.VISIBLE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        else {
            certificate.setVisibility(View.VISIBLE);
            certificateEdit.setVisibility(View.GONE);
        }
        changeVisibility(dropzone, dropzoneEdit);
    }

    private String calculateAge(User user) {
        String s = "";
        String temp = user.dateOfBirth;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dob = format.parse(temp);
            Date date = new Date();

            long diff = (date.getTime() - dob.getTime());
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long years = days / 365;
            s = String.valueOf(years);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }
}



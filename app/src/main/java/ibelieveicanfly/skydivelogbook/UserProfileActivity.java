package ibelieveicanfly.skydivelogbook;

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
import android.widget.ImageButton;
import android.widget.ImageView;
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

    //String uid = auth.getCurrentUser().getUid();

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

        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);


/*
        ImageView icon = findViewById(R.id.profilePicture);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.common_google_signin_btn_icon_dark);

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable.setCircular(true);
        icon.setImageDrawable(drawable);
*/
        auth = FirebaseAuth.getInstance();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference ref = usersRef.child(auth.getCurrentUser().getUid());

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
        name.setText(user.firstName + " " + user.lastName);

        String temp = user.dateOfBirth;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dob = format.parse(temp);
            Date date = new Date();

            long diff = (date.getTime()  - dob.getTime());
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long years = days / 365;
            String s = String.valueOf(years);
            age.setText(s + " (" + temp +")");
        }
        catch(ParseException e) {
            e.printStackTrace();
        }

        certificate.setText(user.certificate);
    }

}

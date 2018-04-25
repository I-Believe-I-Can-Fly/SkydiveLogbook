package ibelieveicanfly.skydivelogbook;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePageActivity extends AppCompatActivity {

    private EditText mJumpNr;
    private EditText mDate;
    private EditText mDz;
    private EditText mPlane;
    private EditText mEquipment;
    private EditText mExit;
    private EditText mFreefall;
    private EditText mCanopy;
    private EditText mComments;
    private TextView mSignature;
    private String mSignatureUserID;
    private boolean mApproved = false;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private FirebaseAuth auth;
    private String userID;
    private String mUsernameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page);

        mJumpNr = (EditText) findViewById(R.id.page_jumpNr);
        mDate = (EditText) findViewById(R.id.page_date);
        mDz = (EditText) findViewById(R.id.page_DZ);
        mPlane = (EditText) findViewById(R.id.page_plane);
        mEquipment = (EditText) findViewById(R.id.page_equipment);
        mExit = (EditText) findViewById(R.id.page_exitAlt);
        mFreefall = (EditText) findViewById(R.id.page_freefallTime);
        mCanopy = (EditText) findViewById(R.id.page_canopyAlt);
        mComments = (EditText) findViewById(R.id.page_comments);
        mSignature = (TextView) findViewById(R.id.page_signed);

        auth = FirebaseAuth.getInstance();

        //retrieve userid
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("Logs");
        this.myRef2 = mDatabase.getReference("Requests");

        getUser();
    }

    public void onStart() {
        super.onStart();
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v, "-1/-1/-1");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
    }

    /**
     * Adds button to action bar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_btn, menu);
        return true;
    }

    /**
     * Adds event to button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:

                LogbookPage logbookPage = getDataFromLogbook();
                if (logbookPage != null) {
                    savePageToFirebase(logbookPage);
                    createSignatureRequest(userID, mSignatureUserID, mJumpNr.getText().toString());

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void SignRequest(View v) {
        Intent intent = new Intent(this, SignatureRequest.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Get text from A3
                String SignatureUserID = data.getStringExtra("SignUser_id");
                String SignatureUserTxt = data.getStringExtra("SignUser_text");

                this.mSignatureUserID = SignatureUserID;
                this.mUsernameTxt = SignatureUserTxt;

                mSignature.setText(SignatureUserTxt);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //rip?
            }
        }
    }

    private LogbookPage getDataFromLogbook() {
        // TODO: Make it more useful
        //'atomic'-wannabe variable to make sure fields are filled
        boolean filled = true;

        if (TextUtils.isEmpty(mJumpNr.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mDate.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mDz.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mPlane.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mEquipment.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mExit.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mFreefall.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mCanopy.getText())) {
            filled = false;
        }
        if (TextUtils.isEmpty(mSignatureUserID)) {
            filled = false;
        }
        if (TextUtils.isEmpty(mSignature.getText())){
            filled = false;
        }
        if (!mDate.getText().toString().matches("^[0-9][1-9]/[0-9][1-9]/[1-2][0-9]{3}$")) {
            filled = false;
        }

        if (filled) {
            return new LogbookPage(
                    Integer.parseInt(mJumpNr.getText().toString()),
                    mDate.getText().toString(),
                    mDz.getText().toString(),
                    mPlane.getText().toString(),
                    mEquipment.getText().toString(),
                    mExit.getText().toString(),
                    mFreefall.getText().toString(),
                    mCanopy.getText().toString(),
                    mComments.getText().toString(),
                    mSignature.getText().toString(),
                    mApproved
            );
        } else {
            Toast.makeText(getApplicationContext(), "Required fields not filled", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void getUser() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference ref = usersRef.child(auth.getCurrentUser().getUid());


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot users) {
                autoComplete(users.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to get current user
            }
        });
    }

    private void autoComplete(User user) {
        // TODO: Auto complete jumpNr
        // TODO: Get info about last log

        // Get date
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();

        // Set date
        mDate.setText(sdf.format(date));

        // mJumpNr.setText();

        if (user != null) {
            if (user.dropZone != null) {
                mDz.setText(user.dropZone);
            }
            if (user.plane != null) {
                mPlane.setText(user.plane);
            }
            if (user.equipment != null) {
                mEquipment.setText(user.equipment);
            }
            if (user.exitAlt != null) {
                mExit.setText(user.exitAlt);
            }
            if (user.canopyAlt != null) {
                mCanopy.setText(user.canopyAlt);
            }
            if (user.freefall != null) {
                mFreefall.setText(user.freefall);
            }
        }
    }

    private void savePageToFirebase(LogbookPage logbookPage) {

        String key = myRef.push().getKey();
        myRef.child(userID).child(key).setValue(logbookPage);
    }

    private void createSignatureRequest(String User, String Signer, String JumpNr){
        Request request = new Request(User, Signer, JumpNr, mUsernameTxt);

        String key = myRef2.push().getKey();
        myRef2.child(key).setValue(request);
    }
}

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
    private DatabaseReference myRef3;
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
        this.myRef3 = mDatabase.getReference("Users");

        getLogbook();

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

                mSignature.setText(SignatureUserTxt);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //rip?
            }
        }
    }

    private LogbookPage getDataFromLogbook() {
        // TODO: Make it more useful
        String emptyjumpNr = this.getResources().getString(R.string.emptyJumpNr);
        String emptyDate = this.getResources().getString(R.string.emptyDate);
        String emptyDz = this.getResources().getString(R.string.emptyDz);
        String emptyPlane = this.getResources().getString(R.string.emptyPlane);
        String emptyEquipment = this.getResources().getString(R.string.emptyEquipment);
        String emptyExit= this.getResources().getString(R.string.emptyExit);
        String emptyFreefall = this.getResources().getString(R.string.emptyFreefall);
        String emptyCanopy = this.getResources().getString(R.string.emptyCanopy);
        String emptySignatureID = this.getResources().getString(R.string.emptySignatureUserID);
        String emptySignature = this.getResources().getString(R.string.emptySignature);
        String invalidDate = this.getResources().getString(R.string.notValidDate);


        if (TextUtils.isEmpty(mJumpNr.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyjumpNr, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mDate.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyDate, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mDz.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyDz, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mPlane.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyPlane, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mEquipment.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyEquipment, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mExit.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyExit, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mFreefall.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyFreefall, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mCanopy.getText())) {
            Toast.makeText(CreatePageActivity.this, emptyCanopy, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mSignatureUserID)) {
            Toast.makeText(CreatePageActivity.this, emptySignatureID, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(mSignature.getText())) {
            Toast.makeText(CreatePageActivity.this, emptySignature, Toast.LENGTH_SHORT).show();

        } else if (!mDate.getText().toString().matches("^[0-9]{1,2}/[0-9]{1,2}/[1-2][0-9]{3}$")) {
            Toast.makeText(CreatePageActivity.this, invalidDate, Toast.LENGTH_SHORT).show();

        } else {
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
        }
        return null;
    }

    /**
     * getUser:
     * Function get's the user that is creating the page, and saves the full name
     */
    private void getUser() {
        DatabaseReference reference = mDatabase.getReference("Users").child(auth.getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot users) {
                User user = users.getValue(User.class);
                mUsernameTxt = user.getFirstName() + " " + user.getLastName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

    private void getLogbook() {

        DatabaseReference reference = mDatabase.getReference("Logs").child(auth.getCurrentUser().getUid());

        reference.orderByChild("jumpNr").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // I had to have a for loop here, even though I'm just getting one logbookPage object :)))))
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LogbookPage logbookPage = child.getValue(LogbookPage.class);
                    autoComplete(logbookPage);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    private void autoComplete(LogbookPage logbookPage) {

        // Get date
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();

        // Set date
        mDate.setText(sdf.format(date));


        if (logbookPage.getJumpNr() != null) {
            String newJump = (logbookPage.getJumpNr() + 1) + "";
            mJumpNr.setText(newJump);
        }
        if (logbookPage.getDz() != null) {
            mDz.setText(logbookPage.getDz());
        }
        if (logbookPage.getPlane() != null) {
            mPlane.setText(logbookPage.getPlane());
        }
        if (logbookPage.getEquipment() != null) {
            mEquipment.setText(logbookPage.getEquipment());
        }
        if (logbookPage.getExit() != null) {
            mExit.setText(logbookPage.getExit());
        }
        if (logbookPage.getCanopy() != null) {
            mCanopy.setText(logbookPage.getCanopy());
        }
        if (logbookPage.getFreefall() != null) {
            mFreefall.setText(logbookPage.getFreefall());
        }

    }

    private void savePageToFirebase(LogbookPage logbookPage) {

        String key = myRef.push().getKey();
        myRef.child(userID).child(key).setValue(logbookPage);
    }

    private void createSignatureRequest(String User, String Signer, String JumpNr) {
        String key = myRef2.push().getKey();
        Request request = new Request(User, Signer, JumpNr, mUsernameTxt, key);
        myRef2.child(key).setValue(request);
    }
}

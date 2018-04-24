package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private boolean mApproved = false;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private String userID;

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

        // TODO: Auto complete most of the inputs


        auth = FirebaseAuth.getInstance();
        //retrieve userid
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }


        this.mDatabase = FirebaseDatabase.getInstance();
        //    this.myRef = mDatabase.getReference(userID);
        this.myRef = mDatabase.getReference("Logs");
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

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }

                Intent intent = new Intent(this, CreatePageActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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
                    mApproved
            );
        } else {
            Toast.makeText(getApplicationContext(), "Required fields not filled", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void savePageToFirebase(LogbookPage logbookPage) {

        String key = myRef.push().getKey();
        myRef.child(userID).child(key).setValue(logbookPage);
    }
}

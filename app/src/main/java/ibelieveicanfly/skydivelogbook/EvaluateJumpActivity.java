package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EvaluateJumpActivity extends AppCompatActivity {

    private TextView mJumpNr;
    private TextView mDate;
    private TextView mDz;
    private TextView mPlane;
    private TextView mEquipment;
    private TextView mExit;
    private TextView mFreefall;
    private TextView mCanopy;
    private EditText mComments;

    private String JumpNr;
    private Integer JumpInt;
    private String UserID;
    private String SignerID;
    private String RequestID;
    private String jumpKey;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;

    private LogbookPage page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_jump);

        mJumpNr = (TextView) findViewById(R.id.page_jumpNr);
        mDate = (TextView) findViewById(R.id.page_date);
        mDz = (TextView) findViewById(R.id.page_DZ);
        mPlane = (TextView) findViewById(R.id.page_plane);
        mEquipment = (TextView) findViewById(R.id.page_equipment);
        mExit = (TextView) findViewById(R.id.page_exitAlt);
        mFreefall = (TextView) findViewById(R.id.page_freefallTime);
        mCanopy = (TextView) findViewById(R.id.page_canopyAlt);
        mComments = (EditText) findViewById(R.id.page_comments);

        //get extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            JumpNr = extras.getString("JumpNr", JumpNr);
            UserID = extras.getString("UserID", UserID);
            SignerID = extras.getString("SignerID", SignerID);
            RequestID = extras.getString("RequestID", RequestID);
        }

        JumpInt = Integer.parseInt(JumpNr);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("Logs").child(UserID);
        this.myRef2 = mDatabase.getReference("Requests").child(RequestID);

        // Read from the database
        myRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LogbookPage logbookPage = child.getValue(LogbookPage.class);
                    if (logbookPage.getJumpNr().toString().equals(JumpNr)) {
                        page = logbookPage;
                        jumpKey = child.getKey();
                        updateInfo();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //rip
            }
        });

    }

    private void updateInfo(){
        mJumpNr.setText(page.getJumpNr().toString());
        mDate.setText(page.getDate());
        mDz.setText(page.getDz());
        mPlane.setText(page.getPlane());
        mEquipment.setText(page.getEquipment());
        mExit.setText(page.getExit());
        mFreefall.setText(page.getFreefall());
        mCanopy.setText(page.getCanopy());
        mComments.setText(page.getComments());
    }

    public void SignJump(View view){
        page.setComments(mComments.getText().toString());
        page.setApproved(true);

        myRef.child(jumpKey).setValue(page);
        myRef2.setValue(null);

        Intent intent = new Intent(this, EvaluateRequestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, EvaluateRequestActivity.class);
        startActivity(intent);
    }
}

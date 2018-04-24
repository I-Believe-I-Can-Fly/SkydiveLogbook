package ibelieveicanfly.skydivelogbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private static final String ARG_PARAM2 = "jump";

    private String userID;
    private Integer JUMP;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private LogbookPage page;

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


    public PageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageFragment newInstance(String param1, Integer param2) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM1);
            JUMP = getArguments().getInt(ARG_PARAM2);
        }
        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("Logs").child(userID);

        page = new LogbookPage();

        pageInfo();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        mJumpNr = (EditText) view.findViewById(R.id.page_jumpNr);
        mDate = (EditText) view.findViewById(R.id.page_date);
        mDz = (EditText) view.findViewById(R.id.page_DZ);
        mPlane = (EditText) view.findViewById(R.id.page_plane);
        mEquipment = (EditText) view.findViewById(R.id.page_equipment);
        mExit = (EditText) view.findViewById(R.id.page_exitAlt);
        mFreefall = (EditText) view.findViewById(R.id.page_freefallTime);
        mCanopy = (EditText) view.findViewById(R.id.page_canopyAlt);
        mComments = (EditText) view.findViewById(R.id.page_comments);
        mSignature = (TextView) view.findViewById(R.id.page_signed);

        return view;
    }


    private void fillPageData() {
        mJumpNr.setText(page.getJumpNr().toString());
        mDate.setText(page.getDate());
        mDz.setText(page.getDz());
        mPlane.setText(page.getPlane());
        mEquipment.setText(page.getEquipment());
        mExit.setText(page.getExit());
        mFreefall.setText(page.getFreefall());
        mCanopy.setText(page.getCanopy());
        mComments.setText(page.getComments());
        mSignature.setText("Brede er mæd loser");
    }

    private void pageInfo() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LogbookPage logbookPage = child.getValue(LogbookPage.class);
                    if (logbookPage.getJumpNr().equals(JUMP + 1)) {
                        page = logbookPage;
                        fillPageData();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // rip
            }
        });
    }
}

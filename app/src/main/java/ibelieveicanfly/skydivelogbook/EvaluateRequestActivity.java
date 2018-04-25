package ibelieveicanfly.skydivelogbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EvaluateRequestActivity extends AppCompatActivity {

    private ArrayList<Request> requests;
    private RequestAdapter adapter;

    private RecyclerView recyclerView;

    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_request);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("Requests");
        this.recyclerView = (RecyclerView)findViewById(R.id.EvaluateRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requests = new ArrayList<>();

        // Read from the database
        myRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    Request request = child.getValue(Request.class);
                    if(request.getSigner().equals(uid)){
                        requests.add(request);
                        refreshListAdapter();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //rip
            }
        });

        adapter = new RequestAdapter(this);
    }

    public void refreshListAdapter(){
        //We set the array to the adapter
        adapter.setListContent(requests);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }


    public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

        Context context;
        private ArrayList<Request> requests=new ArrayList<>();
        private final LayoutInflater inflater;

        public RequestAdapter(Context context) {
            this.context = context;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public RequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.activity_evaluate_request_item, parent, false);
            return new RequestAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RequestAdapter.MyViewHolder holder, int position) {
            Request request = requests.get(position);
            String text1 = request.getUserName();
            String text2 = request.getJumpNr();
            holder.nameTxt.setText(text1);
            holder.jumpNr.setText(text2);
        }

        @Override
        public int getItemCount() {
            return requests.size();
        }

        //Setting the arraylist
        public void setListContent(ArrayList<Request> requests){
            this.requests=requests;
            notifyItemRangeChanged(0, requests.size());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView nameTxt, jumpNr;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                nameTxt = (TextView) itemView.findViewById(R.id.EvaluateName);
                jumpNr = (TextView) itemView.findViewById(R.id.EvaluateJumpNr);
            }
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(getApplicationContext(), CreatePageActivity.class);

                intent.putExtra("SignUser_id", SignatureUserID); // Dette burde vært userID'en til brukeren
                intent.putExtra("SignUser_text", nameTxt.getText().toString()+" "+licenseTxt.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();*/
            }

        }
    }
}

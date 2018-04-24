package ibelieveicanfly.skydivelogbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignatureRequest extends AppCompatActivity {

    private ArrayList<User> users;
    private UserAdapter adapter;

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_request);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("Users");
        this.recyclerView = (RecyclerView)findViewById(R.id.signRequestUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();

        // Read from the database
        myRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                refreshListAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adapter = new UserAdapter(this);
    }

    public void refreshListAdapter(){
        //We set the array to the adapter
        adapter.setListContent(users);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

        Context context;
        private ArrayList<User> users=new ArrayList<>();
        private final LayoutInflater inflater;

        public UserAdapter(Context context) {
            this.context = context;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.activity_signature_request_items, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            User user=users.get(position);
            String text1 = user.getFirstName()+" "+user.getLastName();
            String text2 = user.getCertificate()+"#"+user.getLicence();
            holder.nameTxt.setText(text1);
            holder.licenseTxt.setText(text2);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        //Setting the arraylist
        public void setListContent(ArrayList<User> users){
            this.users=users;
            notifyItemRangeChanged(0, users.size());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView nameTxt, licenseTxt;
            String SignatureUserID;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                nameTxt = (TextView) itemView.findViewById(R.id.FirstLastName);
                licenseTxt = (TextView) itemView.findViewById(R.id.certLicense);
                SignatureUserID = "kurwa";
            }
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePageActivity.class);
                // TODO: endre 'extraen' under til userID
                intent.putExtra("SignUser_id", nameTxt.getText().toString()); // Dette burde vært userID'en til brukeren
                intent.putExtra("SignUser_text", nameTxt.getText().toString()+" "+licenseTxt.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }
    }
}

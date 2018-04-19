package ibelieveicanfly.skydivelogbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<LogbookPage> list_members = new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public CustomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.logbook_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        LogbookPage list_items = list_members.get(position);

        holder.jumpNr.setText(list_items.getJumpNr().toString());
        holder.jumpDz.setText(list_items.getDz());
        holder.date.setText(list_items.getDate());
        if (list_items.isApproved()) {
            String approvedTxt = context.getResources().getString(R.string.approved);
            holder.approved.setText(approvedTxt);
        } else {
            String approvedTxt = context.getResources().getString(R.string.notApproved);
            holder.approved.setText(approvedTxt);
            holder.approved.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    //Setting the arraylist
    public void setListContent(ArrayList<LogbookPage> list_members) {
        this.list_members = list_members;
        notifyItemRangeChanged(0, list_members.size());
    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView jumpNr, jumpDz, approved, date;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            jumpNr = (TextView) itemView.findViewById(R.id.item_jumpNr);
            jumpDz = (TextView) itemView.findViewById(R.id.item_DZ);
            approved = (TextView) itemView.findViewById(R.id.item_signed);
            date = (TextView) itemView.findViewById(R.id.item_date);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PageActivity.class);
            intent.putExtra("user", "johan");
            intent.putExtra("jump", Integer.parseInt(jumpNr.getText().toString()));
            context.startActivity(intent);
        }
    }

    public void removeAt(int position) {
        list_members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, list_members.size());
    }

}


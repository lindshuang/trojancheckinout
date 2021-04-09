package cs310.trojancheckinout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OccupantsAdapter extends RecyclerView.Adapter<OccupantsAdapter.MyViewHolder>{

    private ArrayList<String> nameList = new ArrayList<String>();
    private Context ctx;
    private RecyclerViewClickListener listener;

    public OccupantsAdapter(ArrayList<String> nameList, Context ctx, RecyclerViewClickListener listener) {
        this.nameList = nameList;
        this.ctx = ctx;
        this.listener = listener;
    }


    @NonNull
    @Override
    public OccupantsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx.getApplicationContext()).inflate(R.layout.activity_single_occ,parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccupantsAdapter.MyViewHolder holder, int position) {
        holder.name.setText(nameList.get(position));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }


    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.single_occupant);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
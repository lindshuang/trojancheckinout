package cs310.trojancheckinout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;

import cs310.trojancheckinout.models.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ProjectViewHolder> {

    //private History[] histories; //thing that we are adapting
    private ArrayList<History> histories;

    //constructor
    public HistoryAdapter(ArrayList<History> histories) {
        this.histories = histories;
    }

    @Override //called first to determine how many items our adapter is dealing with
    public int getItemCount() { //return an item for every one of our projects
        return histories.size();
    }

    @NonNull
    @Override //manually inflate our layout file (builds it into code)
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //setContentView functionality
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false); //inflate layout file
        return new ProjectViewHolder(view); //returns View
    }

    @Override //as each item comes in and out of focus, this method is called
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.bind(histories.get(position)); //pass in an individual project
    }


    static class ProjectViewHolder extends RecyclerView.ViewHolder{ //set individual item layout

//        private ImageView appImage;
//        private TextView appTitle;
//        private TextView appDescription;

        private TextView appTimeIn;
        private TextView appTimeOut;
        private TextView appTotalTime;
        private TextView appBuildingName;
        private TextView appDate;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            appTimeIn = itemView.findViewById(R.id.text_view_time_in);
            appDate = itemView.findViewById(R.id.text_view_date);
            appTimeOut = itemView.findViewById(R.id.text_view_time_out);
            appTotalTime = itemView.findViewById(R.id.text_view_total_time);
            appBuildingName = itemView.findViewById(R.id.text_view_building_name);

        }
        //holds all views, as items come in and out viewHodlder has previous projects and can swap out values

        public void bind(History History){

            String timeInString = History.getTimeInTime();
            String timeOutString = History.getTimeOutTime();

            double time_elapsed = History.getTotalTime();
            //convert to hrs and mins
            double m = time_elapsed % 60;
            String mins = Double.toString(m);
            double h = Math.floor(time_elapsed/60.0);
            String hour = Double.toString(h);
            String totalTimeString = hour + " hr " + mins + " min";

            //String totalTimeString = String.valueOf(History.getTotalTime());

            appTimeIn.setText("Time in: " + timeInString);
            appTimeOut.setText("Time out: " + timeOutString);
            appTotalTime.setText("Total time: " + totalTimeString);
            appDate.setText("Date: " + History.getTimeInDate());
            appBuildingName.setText(History.getBuildingName());

        }
    }
}

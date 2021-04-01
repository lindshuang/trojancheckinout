package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class displaySearchResults extends AppCompatActivity {

    private CustomAdapter adapter_c;
    private RecyclerView mFireStoreList_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);

        mFireStoreList_s = findViewById(R.id.firestore_list_s);
       // String[] names  = {"Anya","Gauri","Shania","Ashna","Anya","Gauri","Shania","Ashna","Anya","Gauri","Shania","Ashna"};
       // ArrayList<String> names= new ArrayList<String>(Arrays.asList("Anya","Gauri","Shania","Ashna","Anya","Gauri","Shania","Ashna","Anya","Gauri","Shania","Ashna"));
        Intent i = getIntent();
        ArrayList<String> names = i.getStringArrayListExtra("names_list");
        adapter_c = new CustomAdapter(names);

        mFireStoreList_s.setHasFixedSize(true);
        mFireStoreList_s.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList_s.setAdapter(adapter_c);

    }


    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private ArrayList<String> localDataSet;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                textView = (TextView) view.findViewById(R.id.textView);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public CustomAdapter(ArrayList<String> dataSet) {
            localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.text_row_item, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextView().setText(localDataSet.get(position));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return (int)localDataSet.size();
        }
    }


}
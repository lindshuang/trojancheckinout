package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import cs310.trojancheckinout.models.SearchUser;

public class displaySearchResults extends AppCompatActivity implements CustomAdapter.ListItemClickListener {

    private CustomAdapter adapter_c;
    private RecyclerView mFireStoreList_s;
    ArrayList<SearchUser> search_results = new ArrayList<SearchUser>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);

        mFireStoreList_s = findViewById(R.id.firestore_list_s);

        Intent i = getIntent();

        search_results = (ArrayList<SearchUser>) i.getSerializableExtra("names_list");
        ArrayList<String> names = new ArrayList<String>();
        for(int a=0;a<search_results.size();a++){
            names.add(search_results.get(a).getLastFirstName());
        }
        if(names.size()==0){
            names.add("No students found");
        }

        adapter_c = new CustomAdapter(names, this);

        mFireStoreList_s.setHasFixedSize(true);
        mFireStoreList_s.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList_s.setAdapter(adapter_c);

    }

    @Override
    public void onListItemClick(int position) {
        //Toast.makeText(this, names[position].getName(), Toast.LENGTH_SHORT).show();
        Intent displayIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        displayIntent.putExtra("Source","Occupants");
        displayIntent.putExtra("email",search_results.get(position).getEmail());
        startActivity(displayIntent);
    }


}
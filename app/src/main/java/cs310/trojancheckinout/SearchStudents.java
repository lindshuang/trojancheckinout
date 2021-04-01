package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cs310.trojancheckinout.models.User;

public class SearchStudents extends AppCompatActivity {

    private EditText first_name_search_terms;
    private EditText last_name_search_terms;

    //Database Reading -- Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentSnapshot userDoc;

    private Button search_button;
    private ArrayList<String> fnameresult_names = new ArrayList<String>();
    private ArrayList<String> lnameresult_names = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_students);

        search_button = (Button) findViewById(R.id.searchButton_main);

//        //goal 1: search students by first name

        first_name_search_terms = (EditText) findViewById(R.id.first_search_edit);
        last_name_search_terms = (EditText) findViewById(R.id.last_search_edit);

        //ArrayList<String> names= new ArrayList<String>(Arrays.asList("Anya","Kenna","Shania","Karen","Ryan","Gauri","Tina","Ashna","Anya","Leah","Shania","Gina"));

        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fnameresult_names.clear();
                lnameresult_names.clear();

                String firstName_search = (first_name_search_terms.getText().toString()).toLowerCase();
                String lastName_search = (last_name_search_terms.getText().toString()).toLowerCase();

                Log.d("fi",firstName_search+"fsearch");
                Log.d("li",lastName_search+"lsearch");

                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    if(firstName_search != null|| firstName_search !=""){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("firstName").toLowerCase().contains(firstName_search) == true)
                                            {
                                                Log.d("tring","2"+document.getString("firstName"));
                                                Log.d("tring","1" +document.getString("lastName"));
                                                fnameresult_names.add(document.getString("lastName")+", "+document.getString("firstName"));
                                                Log.d("found", "found and display student" +document.getString("firstName"));
                                            }
                                            Log.d("success", document.getId());
                                        }
                                    }
                                    if(lastName_search != null || lastName_search != ""){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("lastName").toLowerCase().contains(lastName_search) == true)
                                            {
                                                Log.d("tring","2"+document.getString("firstName"));
                                                Log.d("tring","1" +document.getString("lastName"));
                                                lnameresult_names.add(document.getString("lastName")+", "+document.getString("firstName"));
                                                Log.d("found", "found and display student" +document.getString("firstName"));
                                            }
                                            Log.d("success", document.getId());
                                        }
                                    }
                                    //fname should be size 3
                                    //lname should be size 1
                                    Log.d("tag","beforef size "+fnameresult_names.size());
                                    Log.d("tag","beforel size "+lnameresult_names.size());
                                    fnameresult_names.retainAll(lnameresult_names);
                                    Log.d("tag","afterf size "+fnameresult_names.size());
                                    Log.d("tag","afterl size "+lnameresult_names.size());
                                    Intent searchActivityIntent = new Intent(SearchStudents.this, displaySearchResults.class);
                                    Collections.sort(fnameresult_names,String.CASE_INSENSITIVE_ORDER);
                                    searchActivityIntent.putStringArrayListExtra("names_list",fnameresult_names);
                                    startActivityForResult(searchActivityIntent, 0);

                                } else {
                                    Log.d("bad", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });




    }


}
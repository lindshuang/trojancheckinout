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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import cs310.trojancheckinout.models.NameSorter;
import cs310.trojancheckinout.models.SearchUser;
import cs310.trojancheckinout.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class SearchStudents extends AppCompatActivity {

    private EditText first_name_search_terms;
    private EditText last_name_search_terms;
    private Spinner major_search_terms;
    private Spinner building_search_terms;
    private EditText date_search_terms;
    private EditText start_time_search_terms;
    private EditText end_time_search_terms;
    private EditText student_id_search_terms;

    //Database Reading -- Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentSnapshot userDoc;

    private Button search_button;
    private ArrayList<String> fnameresult_emails = new ArrayList<String>();
    private ArrayList<String> lnameresult_emails = new ArrayList<String>();
    private ArrayList<String> majorresult_emails = new ArrayList<String>();
    private ArrayList<String> buildingresult_emails = new ArrayList<String>();
    private ArrayList<String> dateresult_emails = new ArrayList<String>();
    private ArrayList<String> timeresult_emails = new ArrayList<String>();
    private ArrayList<String> studentID_emails = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<SearchUser> all_results = new ArrayList<SearchUser>();
    private ArrayList<ArrayList<String>> all_arrays = new ArrayList<ArrayList<String>>();

    private int anchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_students);

        search_button = (Button) findViewById(R.id.searchButton_main);

//        //goal 1: search students by first name

        first_name_search_terms = (EditText) findViewById(R.id.first_search_edit);
        last_name_search_terms = (EditText) findViewById(R.id.last_search_edit);
        major_search_terms = (Spinner) findViewById(R.id.major_search_spinner);
        building_search_terms =  (Spinner) findViewById(R.id.building_search_spinner);
        date_search_terms = (EditText) findViewById(R.id.date_search_edit);
        start_time_search_terms = (EditText) findViewById(R.id.hours_start_search_edit);
        end_time_search_terms = (EditText) findViewById(R.id.hours_end_search_edit);
        student_id_search_terms = (EditText) findViewById(R.id.student_id_search_edit);


        //ArrayList<String> names= new ArrayList<String>(Arrays.asList("Anya","Kenna","Shania","Karen","Ryan","Gauri","Tina","Ashna","Anya","Leah","Shania","Gina"));

        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("document", "SEARCH STARTS HERE");
                fnameresult_emails.clear();
                lnameresult_emails.clear();
                majorresult_emails.clear();
                buildingresult_emails.clear();
                dateresult_emails.clear();
                timeresult_emails.clear();
                studentID_emails.clear();
                all_arrays.clear();
                all_results.clear();

                anchor =0;

                String firstName_search = (first_name_search_terms.getText().toString()).toLowerCase();
                String lastName_search = (last_name_search_terms.getText().toString()).toLowerCase();
                String major_search = major_search_terms.getSelectedItem().toString();
                String building_search = building_search_terms.getSelectedItem().toString();
                String date_search = (date_search_terms.getText().toString());
                String hour_start_search = start_time_search_terms.getText().toString();
                String hour_end_search = end_time_search_terms.getText().toString();
                String student_id_search = student_id_search_terms.getText().toString();


                Log.d("fi",firstName_search+"fsearch");
                Log.d("li",lastName_search+"lsearch");
                Log.d("mi",major_search+"major_search");
                Log.d("bi",building_search+"building search");
                Log.d("di",date_search+"building search");
                Log.d("di",hour_start_search+ "start hrsearch");
                Log.d("di",hour_end_search+"end hr search");
                Log.d("di",student_id_search+"student_id_search");

                if(building_search.length() !=0 || !(building_search.matches(""))) {
                    Character[] numbers = {'0','1','2','3','4','5','6','7','8','9'};
                    db.collection("history")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        ArrayList<String> temp_emails_building = new ArrayList<String>();

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String b_name = document.getString("buildingName");
                                            if(b_name.compareTo(building_search)==0){
                                                temp_emails_building.add(document.getId());
                                            }

                                        }
                                        Log.d("succes","SIZE"+ temp_emails_building.size());
                                        for(int b=0;b<temp_emails_building.size();b++)
                                        {
                                            int index=0;
                                            boolean number_found = false;
                                            for(int h=0;h< temp_emails_building.get(b).length();h++) {
                                                String tmp = temp_emails_building.get(b);
                                                number_found = false;
                                                for (int c = 0; c < numbers.length; c++) {
                                                    if (tmp.charAt(h) ==numbers[c])
                                                    {
                                                        ++index;
                                                        number_found = true;
                                                        break;

                                                    }
                                                }
                                                if(number_found==false){
                                                    break;
                                                }


                                            }
                                            buildingresult_emails.add(temp_emails_building.get(b).substring(index));
                                        }

                                        Log.d("T","size of"+ buildingresult_emails.size());

                                        all_arrays.add(buildingresult_emails);


                                    } else {
                                        Log.d("bad", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
//                    DocumentReference docIdRef2 = db.collection("buildings").document(building_search);
//                    docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                List<String> occupants_results_array = (List<String>) document.get("occupants");
//                                Log.d("occuapnt","getting occupants array");
//                                Log.d("tag","size"+occupants_results_array.size());
//
//                                if (!document.exists()) {
//                                    Log.d("document", "Document does not exist! in check in function");
//
//                                }
//                                else {
//                                    Log.d("Document", "Document exists! in check in function");
//                                    for(int h=0;h<occupants_results_array.size();h++){
//
//                                        //only want to add real occupants "not the 0s"
//                                        if(occupants_results_array.get(h).compareTo("0") != 0){
//                                            buildingresult_emails.add(occupants_results_array.get(h));
//                                        }
//                                    }
//                                    all_arrays.add(buildingresult_emails);
//
//
//                                }
//                            } else {
//                                Log.d("document", "Failed with: ", task.getException());
//                            }
//                        }
//                    });

                }
                if(date_search.length() !=0 || !(date_search.matches(""))) {
                    Character[] numbers = {'0','1','2','3','4','5','6','7','8','9'};


                    db.collection("history")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            int index =0;
                                            boolean number_found = false;
                                            if(document.getString("timeInDate").compareTo(date_search)==0){
                                               String date_email = document.getId();
                                               for(int b=0;b<date_email.length();b++)
                                               {
                                                       number_found = false;
                                                       for(int c=0;c<numbers.length;c++){
                                                           if(date_email.charAt(b) ==numbers[c]){
                                                               number_found = true;
                                                               ++index;
                                                               break;

                                                           }
                                                       }
                                                       if(number_found==false){
                                                           break;
                                                       }
                                               }
                                                String official_email = date_email.substring(index);
                                               Log.d("official email","off      "+ official_email);
                                                dateresult_emails.add(official_email);
                                            }


                                            Log.d("success", document.getId());
                                        }

                                        all_arrays.add(dateresult_emails);


                                    } else {
                                        Log.d("bad", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }
                if(hour_start_search.length() !=0 || !(hour_start_search.matches(""))
                        ||hour_end_search.length() !=0 || !(hour_end_search.matches(""))) {

                    Character[] numbers = {'0','1','2','3','4','5','6','7','8','9'};
                    ArrayList<String> temp_emails_hour = new ArrayList<String>();

                    db.collection("history")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

//
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            String startTime = document.getString("timeInTime");
                                            String endTime = document.getString("timeOutTime");
                                            if(endTime == null) {
                                                Date date = new Date();
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(date);
                                                SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
                                                String timeInDate = SDF.format(date);

                                                Formatter timeInT = new Formatter();
                                                Calendar gfg_calender = Calendar.getInstance(TimeZone.getTimeZone("GMT-7"));
                                                timeInT.format("%tH:%tM", gfg_calender, gfg_calender);
                                                endTime = String.valueOf(timeInT);
                                            }

                                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                            Date hour_start_user_input =  new Date();
                                            Date hour_end_user_input = new Date();
                                            Date startTime_database = new Date();
                                            Date endTime_database =new Date();
                                            try {
                                                 hour_start_user_input = format.parse(hour_start_search);
                                                 hour_end_user_input = format.parse(hour_end_search);
                                                 startTime_database = format.parse(startTime);
                                                 endTime_database = format.parse(endTime);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            long hour_start = hour_start_user_input.getTime();
                                            long hour_end = hour_end_user_input.getTime();
                                            long hour_start_db = startTime_database.getTime();
                                            long hour_end_db =   endTime_database.getTime();

//                                            Log.d("hour start", "H "+hour_start_user_input);
//                                            Log.d("hour end", "H "+hour_end_user_input);
//                                            Log.d("hour start db","H "+startTime_database );
//                                            Log.d("hour end db", "H "+endTime_database);

//                                            Case 1: someone checks in after the start time and before the checkout time

                                            //this means that the start time from the databse happens AFTER the user's inputted start time
                                            //we also want to check if start time from the databse happens BEFORE the end time from the user input
                                            if( (startTime_database.compareTo(hour_start_user_input)>0) && (startTime_database.compareTo(hour_end_user_input)<0)){
                                                temp_emails_hour.add(document.getId());
                                                Log.d("case 1 ", document.getId());

                                            }

//                                        Case 2: someone checks out after the start time and before the checkout time

//                                            this means that the check out time in the database is AFTER the start time inputted by the user
//                                            we also want to check if the check out time from the databse happens BEFORE the end time from the user input
                                            else if((endTime_database.compareTo(hour_start_user_input)>0) && (endTime_database.compareTo(hour_end_user_input)<0)){
                                                temp_emails_hour.add(document.getId());
                                                Log.d("case 2 ", document.getId());

                                            }
//                                            //Case 3: someone checks in before the start time & checks out after the end time
                                            else if((startTime_database.compareTo(hour_start_user_input)<0) && (endTime_database.compareTo(hour_end_user_input)>0)){
                                                temp_emails_hour.add(document.getId());
                                                Log.d("case 3 ", document.getId());

                                            }
                                            Log.d("success", document.getId());
                                        }
                                        Log.d("succes","SIZE"+ temp_emails_hour.size());
                                        for(int b=0;b<temp_emails_hour.size();b++)
                                        {
                                            int index=0;
                                            boolean number_found = false;
                                            for(int h=0;h< temp_emails_hour.get(b).length();h++) {
                                                String tmp = temp_emails_hour.get(b);
                                                number_found = false;
                                                    for (int c = 0; c < numbers.length; c++) {
                                                        if (tmp.charAt(h) ==numbers[c])
                                                        {
                                                            ++index;
                                                            number_found = true;
                                                            break;

                                                        }
                                                    }
                                                    if(number_found==false){
                                                        break;
                                                    }


                                            }
                                            timeresult_emails.add(temp_emails_hour.get(b).substring(index));
                                        }

                                        Log.d("T","size of"+ timeresult_emails.size());

                                        all_arrays.add(timeresult_emails);


                                    } else {
                                        Log.d("bad", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }


                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("fi",firstName_search+"1fsearch");
                                    Log.d("li",lastName_search+"2lsearch");
                                    if(firstName_search.length()!=0 || !(firstName_search.matches(""))){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("firstName").toLowerCase().contains(firstName_search) == true)
                                            {
                                                Log.d("tring","2"+document.getString("firstName"));
                                                Log.d("tring","1" +document.getString("lastName"));
                                                fnameresult_emails.add(document.getId());
                                                Log.d("found", "found and display student" +document.getString("firstName"));
                                            }
                                            Log.d("success", document.getId());
                                        }
                                        all_arrays.add(fnameresult_emails);
                                    }
                                    if(lastName_search.length() !=0 || !(lastName_search.matches(""))){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("lastName").toLowerCase().contains(lastName_search) == true)
                                            {
                                                Log.d("tring","2"+document.getString("firstName"));
                                                Log.d("tring","1" +document.getString("lastName"));
                                                lnameresult_emails.add(document.getId());
                                                Log.d("found", "found and display student" +document.getString("firstName"));
                                            }
                                            Log.d("success", document.getId());
                                        }
                                        all_arrays.add(lnameresult_emails);
                                    }

                                    if(major_search.length() !=0 || !(major_search.matches(""))){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("occupation").contains(major_search) == true)
                                            {
                                                Log.d("tring","2occ"+document.getString("occupation"));
                                                Log.d("tring","1" +document.getString("lastName"));
                                                majorresult_emails.add(document.getId());
                                                Log.d("found", "found and display student21231" +document.getString("firstName"));
                                            }
                                            Log.d("success", document.getId());
                                        }
                                        all_arrays.add(majorresult_emails);
                                    }
                                    if(student_id_search.length() !=0 || !(student_id_search.matches(""))){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("studentID").compareTo(student_id_search) == 0)
                                            {
                                                Log.d("tring","2occ"+document.getString("studentID"));
                                                studentID_emails.add(document.getId());
                                                Log.d("found", "found and display student21231" +document.getString("firstName"));
                                            }
                                            Log.d("success", document.getId());
                                        }
                                        all_arrays.add(studentID_emails);
                                    }

                                while(all_arrays.size()<1){
                                    Log.d("tag","cat");

                                }
                               // if(all_arrays.size() >1){
                                        search();
                             //   }


                                } else {
                                    Log.d("bad", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });




    }


    public void search(){

        Log.d("tag a","beforef size "+fnameresult_emails.size());
        Log.d("tag a","beforel size "+lnameresult_emails.size());
        Log.d("tag a","beforemajor size "+majorresult_emails.size());
        Log.d("tag a","beforedate size "+dateresult_emails.size());
        Log.d("tag a","beforebuilding size "+buildingresult_emails.size());
        Log.d("tag a","all arrays  size "+all_arrays.size());
        //retain all

        //Array1: Building which is empty
        //Array 2: Date search which has 2 people
        if(all_arrays.size()>1){
            for(int r=0;r<all_arrays.size();r++){
                //anchor
                //if(all_arrays.get(r).size() != 0){
                    anchor = r;
                    Log.d("anchor","anchor check"+ anchor);
                    for(int u=r+1;u<all_arrays.size();u++){
                       // if(all_arrays.get(u).size() != 0){
                            all_arrays.get(anchor).retainAll(all_arrays.get(u));
                      //  }

                    }
                    break;
                //}
            }
        }

        //fnameresult_emails.retainAll(lnameresult_emails);
        Log.d("tag b","afterf size "+fnameresult_emails.size());
        Log.d("tag b","afterl size "+lnameresult_emails.size());
        Log.d("tag b","beforedate size "+dateresult_emails.size());
        Log.d("tag b","beforebuilding size "+buildingresult_emails.size());
        Log.d("acnhr b","ANCHOR _" + anchor);
        Log.d("tag b","all arrays at anchor size "+all_arrays.get(anchor).size());

        //Edit all arrays.get (anchor) to remove duplicates
        LinkedHashSet<String> set = new LinkedHashSet<String>();

        for (int i = 0; i < all_arrays.get(anchor).size(); i++){
            set.add(all_arrays.get(anchor).get(i));
        }
        all_arrays.get(anchor).clear();
        for (String temp : set) {
            all_arrays.get(anchor).add(temp);
        }
        set.clear();


        //get last name, first name combo
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for(int y=0;y<all_arrays.get(anchor).size();y++){
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    if(document.getId().compareTo(all_arrays.get(anchor).get(y)) == 0)
                                    {
                                        String l_f_name = document.getString("lastName")+", "+document.getString("firstName");
                                        SearchUser s_user = new SearchUser(l_f_name,all_arrays.get(anchor).get(y));
                                        all_results.add(s_user);
                                        Log.d("tag","ALL" + all_results.size());
                                    }

                                    Log.d("success", document.getId());
                                }

                            }

                            all_results.sort(new NameSorter());
//                                                        Log.d("sort","sorting check "+all_results.get(0).getLastFirstName());
//                                                        Log.d("sort","sorting check2 "+all_results.get(1).getLastFirstName());
//                                                        Log.d("sort","sorting check3 "+all_results.get(2).getLastFirstName());
                            Intent searchActivityIntent = new Intent(SearchStudents.this, displaySearchResults.class);
                            Log.d("tag","ALL" + all_results.size());
                            //Collections.sort(fnameresult_names,String.CASE_INSENSITIVE_ORDER);
                            searchActivityIntent.putExtra("names_list",all_results);
                            startActivityForResult(searchActivityIntent, 0);

                        } else {
                            Log.d("bad", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}
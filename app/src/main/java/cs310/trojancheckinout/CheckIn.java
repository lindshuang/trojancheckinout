package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import cs310.trojancheckinout.models.History;
import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.TempUser;
import cs310.trojancheckinout.models.User;
import java.util.List;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Scanner;
import java.util.TimeZone;

import com.budiyev.android.codescanner.CodeScannerView;
import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import java.lang.Runnable;


import java.util.ArrayList;


public class CheckIn extends AppCompatActivity {

    // Define the pic id
    private static final int pic_id = 123;

    Button checkIn_open_id;
    ImageView click_image_id;
    TextView name_id;
    TextView location_id;
    TextView time_id;
    String QR_code="";
    TextView status_id;
    Button home_button;

    //Pop-Up
    TextView confirm_pop_up_id;
    LinearLayout pop_up_id;
    LinearLayout invalid_pop_up_id;
    Button confirmCheck_b_id;
    Button cancel_b_id;
    Button ok_b_id;


    CodeScannerView scannerView;
    FrameLayout frameLayout;

    //Database Reading -- Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private LocalDateTime now = LocalDateTime.now();
    private LocalDateTime future = LocalDateTime.now();
    private List<History> student_history = null;
    //private User current_student = null;
    private User current_student;
    private Building current_building=null; //set in scanQR function
    String last_building = "";
    Double time_elapsed;
    String last_timeOut;
    String last_timeIn;
    String last_DateOut;
    String last_DateIn;
    private boolean checked_in;
    String timeIn_db = "";


    // TO DO: get user info from login/signup page
    private Bundle bundle;
    private String currEmail = "";
    private DocumentSnapshot userDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        Log.d("printing","went into on create");

//        // By ID we can get each component which id is assigned in XML file
        checkIn_open_id = (Button) findViewById(R.id.checkIn_button);
        name_id = (TextView) findViewById(R.id.name);
        location_id = (TextView) findViewById(R.id.lastLocation);
        time_id = (TextView) findViewById(R.id.time);
        status_id = (TextView) findViewById(R.id.status);
        home_button = (Button) findViewById(R.id.home_button);

        currEmail = sharedData.getCurr_email();
        Log.d("Email", "currEmail: " + currEmail);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //start realtime update
        Button ok_id = (Button) findViewById(R.id.okButtonCheckIn);
        //Button pop_up_profile = findViewById(R.id.pop_up_kickProfile);
        LinearLayout pop_up_profile = (LinearLayout) findViewById(R.id.pop_up_kickCheckIn);
        final DocumentReference docRef = db.collection("users").document(sharedData.getCurr_email());
        docRef.addSnapshotListener((snapshot, e) -> {
            Log.d("Doc", "inside listener");
            if (e != null) {
                Log.d("Doc", "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("Doc", "Current data: " + snapshot.getData());
                TempUser tempUser = snapshot.toObject(TempUser.class);
                if (tempUser.isKicked_out()){
                    Log.d("kick", "inside temp user kicked out");
                    pop_up_profile.setVisibility(View.VISIBLE);
                    ok_id.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("kick", "kick out Clicked notification");
                            pop_up_profile.setVisibility(View.INVISIBLE);
                            //set kicked out to false
                            DocumentReference checkOutRef = db.collection("users").document(sharedData.getCurr_email());
                            checkOutRef
                                    .update("kicked_out", false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("updating kicked out", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("error updating time out date", "Error updating document", e);
                                        }
                                    });
                            Intent intent = new Intent(CheckIn.this, CheckIn.class);
                            startActivityForResult(intent, 0);
                        }
                    });
                }else{
                    pop_up_profile.setVisibility(View.INVISIBLE);
                }
            } else {
                Log.d("Doc", "Current data: null");
            }
        }); //end kick out

        DocumentReference docIdRef2 = db.collection("users").document(currEmail);

        docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userDoc = task.getResult();
                if (task.isSuccessful()) {
                    //User(String firstName, String lastName, String email, String lindspassword, boolean checked_in, String occupation, String studentID, String profilePicture
                    current_student = new User(
                            userDoc.getString("firstName"),
                            userDoc.getString("lastName"),
                            userDoc.getString("email"),
                            userDoc.getString("password"),
                            userDoc.getBoolean("checked_in"),
                            userDoc.getString("occupation"),
                            userDoc.getString("studentID"),
                            userDoc.getString("profilePicture"),
                            userDoc.getString("current_qr"));

                    //paste code

                    String name = current_student.getFirstName() + " " + current_student.getLastName();
                    name_id.setText("Hi "+ name + "!");

                    //TO DO: Get name of the last building that the student was in from database
                    String email = current_student.getEmail();
                    DocumentReference docIdRef = db.collection("users").document(email);
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                List<String> histories = (List<String>) document.get("histories");
                                if (!document.exists()) {
                                    Log.d("document", "Document does not exist!");

                                }
                                else {
                                    Log.d("Document", "Document exists!");
                                    //look at last object in list of histories
                                    int history_size = histories.size();
                                    if(history_size > 0 && histories != null) {
                                        String last_history = histories.get(histories.size()-1); //1anyanutakki@usc.edu
                                        Log.d("Last history", "Last History is " + last_history);
                                        //check histories collection by passing in concatenated email
                                        DocumentReference docIdRef = db.collection("history").document(last_history);
                                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    last_building = document.getString("buildingName");
                                                    time_elapsed = document.getDouble("totalTime");
                                                    //convert to hrs and mins
                                                    double m = time_elapsed % 60;
                                                    String mins = Double.toString(m);
                                                    double h = Math.floor(time_elapsed/60.0);
                                                    String hour = Double.toString(h);
                                                    String totalTime = hour + " hr " + mins + " min";
                                                    last_timeIn = document.getString("timeInTime");
                                                    last_timeOut = document.getString("timeOutTime");
                                                    last_DateIn = document.getString("timeInDate");
                                                    last_DateOut = document.getString("timeOutDate");
                                                    Log.d("time in", "last_timeIn is " + last_timeIn);
                                                    Log.d("time out", "last_timeOut is " + last_timeOut);
                                                    Log.d("time in", "last_timeIn is " + last_DateIn);
                                                    Log.d("time out", "last_timeOut is " + last_DateOut);


                                                    //UPDATE UI
                                                    if(last_timeOut == "" || last_timeOut == null) {
                                                        checked_in = true;
                                                        Log.d("printing","checked out ui updating");
                                                        //checkIn_open_id.setText("Check Out");
                                                        status_id.setText("You are checked in.");
                                                        //checkIn_open_id.setBackgroundColor(Color.RED);
                                                        location_id.setText(last_building);
                                                        location_id.setTextColor(Color.GREEN);
                                                        time_id.setText("Checked in at "+ last_DateIn + " " + last_timeIn);
                                                    }
                                                    else {
                                                        checked_in = false;
                                                        Log.d("printing","checked in ui updating");
                                                        //checkIn_open_id.setText("Check In");
                                                        status_id.setText("You are checked out.");
                                                        //checkIn_open_id.setBackgroundColor(Color.GREEN);
                                                        location_id.setText("Last Location: "+last_building);
                                                        location_id.setTextColor(Color.RED);
                                                        time_id.setText(totalTime +" | Left at "+ last_DateOut + " " + last_timeOut);
                                                    }

                                                    if (!document.exists()) {
                                                        Log.d("document", "Document does not exist!");
                                                    }
                                                    else {
                                                        Log.d("Document", "Document exists!");
                                                        Log.d("Building name", "Last building name is " + last_building);
                                                        location_id.setText(last_building);
                                                    }
                                                } else {
                                                    Log.d("document", "Failed with: ", task.getException());
                                                }
                                            }
                                        });
                                    }

                                }
                            } else {
                                Log.d("document", "Failed with: ", task.getException());
                            }
                        }
                    });
                }
            }
        });

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("on home", "home button clicked");
                Intent intent = new Intent(CheckIn.this, NavActivity.class);
                intent.putExtra("email", currEmail);
                startActivityForResult(intent, 0);
            }
        });

        //QR Scanner
        Log.d("on click", "before scanner code");
        checkIn_open_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("on click", "check in button clicked");
                Intent intent = new Intent(CheckIn.this, QRScanner.class);
                intent.putExtra("email", currEmail);

                startActivityForResult(intent, 0);
            }
        });

    }


}

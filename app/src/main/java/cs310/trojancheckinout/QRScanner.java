package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import java.lang.Runnable;

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


import java.util.ArrayList;

import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.History;
import cs310.trojancheckinout.models.User;

public class QRScanner extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private Button qrCodeFoundButton;
    private String qrCode;

    //Pop-Up
    TextView confirm_pop_up_id;
    LinearLayout pop_up_id;
    Button confirmCheck_b_id;
    Button cancel_b_id;
    Button ok_b_id;
    LinearLayout invalid_pop_up_id;
    TextView invalid_text_id;

    //Database Reading -- Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //set to checked in or out
    String result = "";

    //user variables
    private User current_student = new User();
    private Building current_building=null; //set in scanQR function
    private String timeIn_db = "";
    private boolean checkedIn;
    private boolean validCode = false;

    double current_cap;
    double max_cap;
    List<String> occupants = new ArrayList<String>();
    List<String> u_histories = new ArrayList<String>();
    String buildingName = "";
    int size_u_histories = 0;
    String h_id ="";
    double timeElapsed=0.0;

    // TO DO: get user info from login/signup page
    private Bundle bundle;
    private String currEmail = "";
    private DocumentSnapshot userDoc;

    //test
    MyApplication test = (MyApplication) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("qr scanner", "in the qr scanner page");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);

        // USER INFO
        // USER INFO
        bundle = getIntent().getExtras();
        if(bundle !=null) {
            currEmail = sharedData.getCurr_email(); //uncomment when you pass in bundle
            if(currEmail != null || currEmail.length() > 0) {
                DocumentReference docIdRef2 = db.collection("users").document(currEmail);

                docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        userDoc = task.getResult();
                        if (task.isSuccessful()) {
                            //User(String firstName, String lastName, String email, String password, boolean checked_in, String occupation, String studentID, String profilePicture
                            current_student = new User(
                                    userDoc.getString("firstName"),
                                    userDoc.getString("lastName"),
                                    userDoc.getString("email"),
                                    userDoc.getString("password"),
                                    userDoc.getBoolean("checked_in"),
                                    userDoc.getString("occupation"),
                                    userDoc.getString("studentID"),
                                    userDoc.getString("profilePicture"),
                                    "");
                        }
                    }
                });
            }
        }


        //pop up
        pop_up_id = (LinearLayout) findViewById(R.id.pop_up);
        confirm_pop_up_id = (TextView) findViewById(R.id.confirm_text);
        confirmCheck_b_id = (Button) findViewById(R.id.confirmCheckButton);
        cancel_b_id =(Button) findViewById(R.id.cancelButton);
        ok_b_id = (Button) findViewById(R.id.okButton);
        invalid_pop_up_id = (LinearLayout) findViewById(R.id.pop_up_invalid);
        invalid_text_id = (TextView) findViewById(R.id.invalid_text);

        Log.d("qr scanner", "qr scanner 1");
        previewView = findViewById(R.id.activity_main_previewView);
        Log.d("qr scanner", "qr scanner 2");
        qrCodeFoundButton = findViewById(R.id.activity_main_qrCodeFoundButton);
        qrCodeFoundButton.setVisibility(View.INVISIBLE);
        Log.d("qr scanner", "qr scanner 3");
        qrCodeFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qr scanner", "on click qr scanner");

                Toast.makeText(getApplicationContext(), qrCode, Toast.LENGTH_SHORT).show();
                Log.d("qr scanner", "qr scanner 4");
                //Log.i(MainActivity.class.getSimpleName(), "QR Code Found: " + qrCode);
            }
        });

        Log.d("qr scanner", "before 69");
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        Log.d("qr scanner", "after 69");
        requestCamera();
        Log.d("qr scanner", "qr scanner 5");
        // ((MyApplication) this.getApplication()).setSomeVariable(qrCode);


    }

    private void requestCamera() {
        Log.d("qr scanner", "camera requested");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d("start", "camera started");
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(QRScanner.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        Log.d("qr scanner", "start camera");


        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    QRScanner.this.bindCameraPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(QRScanner.this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, ContextCompat.getMainExecutor(this));

    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Log.d("qr scanner", "bind camera");
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCode = _qrCode;
                Log.d("qrcode read", "QR code reads: " + qrCode);

                //set current QR
                current_student.setCurrent_qr(qrCode);
                qrCodeFoundButton.setVisibility(View.VISIBLE);

                Log.d("validate", "validating building");

                db.collection("buildings")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // List<Entry> list = new ArrayList<>();
                                    Log.d("valid", "qr code - validate building - " + qrCode);

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getId().compareTo(qrCode) == 0)
                                        {
                                            validCode = true;
                                            Log.d("valid", "valid code is being set to true");
                                        }
                                        Log.d("success", document.getId());
                                    }
                                    Log.d("valid","VALID CHECK2"+validCode);
                                    if(validCode==false){
                                        Log.d("valid","VALID CHECK"+validCode);

                                        invalid_pop_up_id.setVisibility(View.VISIBLE);
                                        invalid_text_id.setText("Building Not Found.");
                                        ok_b_id.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                Intent intent = new Intent(QRScanner.this, CheckIn.class);
                                                startActivityForResult(intent, 0);

                                            }
                                        });
                                    }
                                } else {
                                    Log.d("bad", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                Log.d("valid", "about to return valid code. value is " + validCode);


                //Confirm Pop-Up goes to visible
                pop_up_id.setVisibility(View.VISIBLE);
                Log.d("pop up", "setting pop up to visible");

                //Change text for Confirm Check In
                String check = checkStatus();
                Log.d("check", "check is " + check);
                if (check.compareTo("checked in") == 0)
                {
                    checkedIn = true;
                    confirm_pop_up_id.setText("You are already checked in. Please confirm check out.");
                }
                else if (check.compareTo("checked out") == 0)
                {
                    checkedIn = false;
                    confirm_pop_up_id.setText("Please confirm check in.");
                }
                else {
                    confirm_pop_up_id.setText("Waiting...");
                }


                //on clicking confirm
                confirmCheck_b_id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        pop_up_id.setVisibility(View.INVISIBLE);

                        //check in or check out accordingly
                        if(checkedIn == true) {
                            Log.d("confirm", "about to check out");
                            checkOut();

                        }
                        else{
                            Log.d("confirm", "about to check in");
                            checkIn();
                        }
                        //navigate back to check in page
                        Log.d("on click", "confirm pop up - leaving qr scanner");
                        //thread sleep
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(QRScanner.this, CheckIn.class);
                        startActivityForResult(intent, 0);
                    }
                });

                //on clicking cancel
                cancel_b_id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        pop_up_id.setVisibility(View.INVISIBLE);
                        Log.d("on click", "cancel pop up - leaving qr scanner");
                        Intent intent = new Intent(QRScanner.this, CheckIn.class);
                        startActivityForResult(intent, 0);
                    }
                });



            }

            @Override
            public void qrCodeNotFound() {
                qrCodeFoundButton.setVisibility(View.INVISIBLE);
            }
        }));

        ((MyApplication) this.getApplication()).setSomeVariable(qrCode);

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageAnalysis, preview);
    }

    public String checkStatus() {
        String email = current_student.getEmail();
        DocumentReference docIdRef = db.collection("users").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean status = document.getBoolean("checked_in");
                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");

                    }
                    else {
                        Log.d("Document", "Document exists! Status is " + status);
                        if(status == true) {
                            result = "checked in";
                        }
                        else if(status == false) {
                            result = "checked out";
                        }
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
        return result;
    }

    //CHECK IN
    public void checkIn(){
        Log.d("in check in", "in check in");
        //get building current and max cap
        DocumentReference docIdRef = db.collection("buildings").document(qrCode);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String cap = document.getString("currCapacity");
                    Log.d("cap check", cap);
                    String max = document.getString("maxCapacity");
                    Log.d("mcap check", max);
                    String buildingN= document.getString("buildingName");
                    List<String> occ = (List<String>) document.get("occupants");

//                    Log.d("occupants list", occ.get(0));

                    if (!document.exists()) {
                        Log.d("document", "Document does not exist! in check in function");

                    }
                    else {
                        Log.d("Document", "Document exists! in check in function");
                        current_cap = Double.parseDouble(cap);
                        max_cap = Double.parseDouble(max);
                        occupants = occ;
                        buildingName = buildingN;
                    }
                    Log.d("in check in", "cap+ "+current_cap);
                    Log.d("in check in", "max cap "+ max_cap);
                    Log.d("in check in", "in building in "+ buildingName);
                    checkInPart2();
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });



    }

    public void checkInPart2(){

//                //there is space
        //if(current_cap+1<=max_cap){
        if(current_cap+1<=max_cap) {
            ++current_cap;

            //Figure out date and time of check in
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
            String timeInDate = SDF.format(date);

            Formatter timeInT = new Formatter();
            Calendar gfg_calender = Calendar.getInstance(TimeZone.getTimeZone("GMT-7"));
            timeInT.format("%tH:%tM", gfg_calender, gfg_calender);
            String timeInTime = String.valueOf(timeInT);
            Log.d("times",timeInTime+timeInDate);

            //get student history for the user
            DocumentReference docIdRef2 = db.collection("users").document(current_student.getEmail());
            docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<String> user_history_array = (List<String>) document.get("histories");

                        //Log.d("occupants list", user_history_array.get(0));
                        //Log.d("occupants list size", "occ size" +user_history_array.size());

                        if (!document.exists()) {
                            Log.d("document", "Document does not exist! in check in function");

                        }
                        else {
                            Log.d("Document", "Document exists! in check in function");
                            u_histories = user_history_array;
                            size_u_histories = user_history_array.size();
                            Log.d(" list size", "u_histories size" +u_histories.size());
                            addHistory(timeInDate, timeInTime, u_histories);

                        }
                    } else {
                        Log.d("document", "Failed with: ", task.getException());
                    }
                }
            });

            //update checkIn status
            DocumentReference statusRef = db.collection("users").document(current_student.getEmail());
            statusRef
                    .update("checked_in", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("updating check in status", "DocumentSnapshot successfully updated! checkin status");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error updating check in status", "Error updating document", e);
                        }
                    });
            statusRef
                    .update("current_qr", current_student.getCurrent_qr())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("updating check in status", "DocumentSnapshot successfully updated! checkin status");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error updating check in status", "Error updating document", e);
                        }
                    });


            //increase current capacity in database by 1
            DocumentReference buildingRef = db.collection("buildings").document(qrCode);
            buildingRef
                    .update("currCapacity", Double.toString(current_cap))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("updating capacity", "DocumentSnapshot successfully updated! currcap");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error updating capacity", "Error updating document", e);
                        }
                    });

            //add user as an occupant of the building in the database
            occupants.add(current_student.getEmail());
            DocumentReference OccupantsRef = db.collection("buildings").document(qrCode);
            OccupantsRef
                    .update("occupants", occupants)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("updating occupants", "DocumentSnapshot successfully updated occupants!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error updating occupants", "Error updating document", e);
                        }
                    });

        }
        //if building is already full
        else{
//            Log.d("TAG","went in ELSEE");
            invalid_pop_up_id.setVisibility(View.VISIBLE);
//            //on clicking OK
            ok_b_id.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //navigate to check in page
                    Intent intent = new Intent(QRScanner.this, CheckIn.class);
                    startActivityForResult(intent, 0);
                    //return;
                }
            });
        }
    }

    public void addHistory(String timeInDate, String timeInTime, List<String> temp){
        //send history to database
        Log.d("TEMP","temp size beginning"+temp.size());
        History new_history = new History(timeInDate, timeInTime, null, null, 0.0, buildingName);
        String history_id ="";
        Log.d("size of size u histories","UHIST SIZEEE"+ size_u_histories);
        if(size_u_histories==0){
            history_id = "0" + current_student.getEmail();
            Log.d("went into 0","if statement");
            Log.d("PRINTING HIST ID", "history id if" + history_id);
        }
        else{
            history_id += size_u_histories;
            history_id += current_student.getEmail();
            Log.d("PRINTING HIST ID", "history id else" + history_id);
            //List<String> temp = new ArrayList<String>();
        }

        // String history_id = "4nutakki@usc.edu";
        db.collection("history").document(history_id).set(new_history);
        temp.add(history_id);
        Log.d("TEMP","temp size"+temp.size());
        Log.d("", "post history");

        //add new history string to histories array for hte user
        DocumentReference historiesRef = db.collection("users").document(current_student.getEmail());
        historiesRef
                .update("histories", temp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating occupants", "DocumentSnapshot successfully updated occupants!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating occupants", "Error updating document", e);
                    }
                });
    }


    //CHECK OUT

    public void checkOut() {
        Log.d("in check out", "in check out");

        //Set current QR to empty string

        //get building information
        DocumentReference docIdRef = db.collection("buildings").document(qrCode);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String cap = document.getString("currCapacity");
                    Log.d("cap check", "cc"+cap);
                    String max = document.getString("maxCapacity");
                    Log.d("mcap check", "mcap"+max);
                    String buildingN= document.getString("buildingName");
                    List<String> occ = (List<String>) document.get("occupants");

                    //Log.d("occupants list", occ.get(0));
                    Log.d("occupants list","occ"+occ.size());

                    if (!document.exists()) {
                        Log.d("document", "Document does not exist! in check in function");
                        invalid_pop_up_id.setVisibility(View.VISIBLE);
                        invalid_text_id.setText("Building Not Found.");
                        ok_b_id.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(QRScanner.this, CheckIn.class);
                                startActivityForResult(intent, 0);

                            }
                        });

                    }
                    else {
                        Log.d("Document", "Document exists! in check in function");
                        current_cap = Double.parseDouble(cap);
                        max_cap = Double.parseDouble(max);
                        occupants = occ;
                        buildingName = buildingN;
                    }
                    Log.d("in check out", "cap+ "+current_cap);
                    Log.d("in check out", "max cap "+ max_cap);
                    Log.d("in check out", "in building in "+ buildingName);
                    //checkOutPart2();
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
        //  fnding hte most recent history id in the users' history array
        DocumentReference docIdRef2 = db.collection("users").document(current_student.getEmail());
        docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<String> user_history_array = (List<String>) document.get("histories");

//                    Log.d("history array for user list", user_history_array.get(0));
                    Log.d("history array list size", "history size" +user_history_array.size());

                    if (!document.exists()) {
                        Log.d("document", "Document does not exist! in check in function");

                    }
                    else {
                        Log.d("Document", "Document exists! in check in function");
                        u_histories = user_history_array;
                        if(user_history_array.size()>=1){
                            h_id = user_history_array.get(user_history_array.size()-1);
                            Log.d("printing out h id", "h_id "+ h_id);
                        }
                        size_u_histories = user_history_array.size();
                        Log.d(" list size", "u_histories size" +u_histories.size());
                        findTimeIn(h_id);

                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
        Log.d("printing out h id", "h_id2 "+ h_id);
    }

    public void findTimeIn(String hist_id){


        //READ CHECK IN TIME FROM MOST RECENT HISTORY
        DocumentReference h_refID = db.collection("history").document(hist_id);
        h_refID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String timeIn_db2 = document.getString("timeInTime");
                    Log.d("time in from database","timeIndb"+timeIn_db2);

                    if (!document.exists()) {
                        Log.d("document", "Document does not exist! in check in function");

                    }
                    else {
                        Log.d("Document", "Document exists! in check in function");
                        timeIn_db = timeIn_db2;
                        Log.d("time", "timeInDB"+ timeIn_db);
                        //Figure out date and time of check out
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
                        String timeOutDate = SDF.format(date);
                        Log.d("time out date", "time Out Date "+ timeOutDate);

                        Formatter timeOutT = new Formatter();
                        Calendar gfg_calender = Calendar.getInstance(TimeZone.getTimeZone("GMT-7"));
                        timeOutT.format("%tH:%tM", gfg_calender, gfg_calender);
                        String timeOutTime = String.valueOf(timeOutT);
                        Log.d("time out date", "time Out Time "+ timeOutTime);

                        //calculate time elapsed
                        try {
                            Log.d("try", "went into try");
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            Date date1 = format.parse(timeIn_db);
                            Log.d("date1","date1"+date1);
                            Date date2 = format.parse(timeOutTime);
                            Log.d("date1","date2"+date2);
                            long difference = date2.getTime() - date1.getTime();
                            Log.d("date1","diff");
                            timeElapsed = (double)difference/60000;
                            Log.d("time", "timeElapsed"+ timeElapsed);
                            Log.d("date1","updating back");
                        }
                        catch (Exception e) {
                            System.out.print(e);
                            Log.d("try", "EXCEPTION");
                        }
                        finally{
                            updateAll(timeOutDate,timeOutTime,timeElapsed);
                        }

                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });

    }

    public void updateAll(String timeOutD ,String timeOutT,double elapsed){

        Log.d("update","went into update ALL");
        //        update database with check out date and time
        DocumentReference checkOutRef = db.collection("history").document(h_id);
        checkOutRef
                .update("timeOutDate", timeOutD)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating time out date", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating time out date", "Error updating document", e);
                    }
                });
        checkOutRef
                .update("timeOutTime", timeOutT)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating time out time", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating time out time", "Error updating document", e);
                    }
                });
        checkOutRef
                .update("totalTime", elapsed)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating time elapsed", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating time elapsed", "Error updating document", e);
                    }
                });
        Log.d("madeIt", "made it past history update");

        //update user checkedIn status
        DocumentReference statusRef = db.collection("users").document(current_student.getEmail());
        statusRef
                .update("checked_in", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating check in status", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating check in status", "Error updating document", e);
                    }
                });
        statusRef
                .update("current_qr", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating check in status", "DocumentSnapshot successfully updated! checkin status");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating check in status", "Error updating document", e);
                    }
                });

        Log.d("madeIt", "made it past boolean update");
        //Update current cap
//        decrease current capacity in database by 1
        DocumentReference buildingRef = db.collection("buildings").document(qrCode);
        buildingRef
                .update("currCapacity", Double.toString((current_cap-1)))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating capacity", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating capacity", "Error updating document", e);
                    }
                });

        Log.d("madeIt", "made it past current cap update");
        //Remove user from current list of occupants, at the end of the list
        Log.d("occuptan","occup check size"+ occupants.size());
        for(int i = 0; i < occupants.size(); i++) {
            Log.d("ocu list","printing "+occupants.get(i));
            if(occupants.get(i).compareTo(current_student.getEmail()) == 0) {
                occupants.set(i, "0");
            }
            else {
                Log.d("error", "can not find occupant in the list");
            }
        }
        DocumentReference OccupantsRef = db.collection("buildings").document(qrCode);
        Log.d("occ", "occupants size in 785"+occupants.size());
        OccupantsRef
                .update("occupants", occupants)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updating occupants", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error updating occupants", "Error updating document", e);
                    }
                });

        Log.d("madeIt", "made it past occupants update");

    }




}

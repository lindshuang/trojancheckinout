package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.User;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView profilePicView;
    private FirebaseFirestore db;
    private User currUser;
    private AlertDialog.Builder builder;
    private Bundle bundle;
    private String currEmail;
    private String picLink;
    private DocumentSnapshot userDoc;
    public static String QR_test;
    public static final int GET_FROM_GALLERY = 3;
    ///private EditText picEditText;

    //user variables
    //private User current_student = new User("Anya","Nutakki","nutakki@usc.edu","password",false);
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

    //private String qrCode = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get db and bundle and current user
        db = FirebaseFirestore.getInstance();
        bundle = getIntent().getExtras();
        //currEmail = bundle.getString("email"); //uncomment when you pass in bundle
        currEmail = sharedData.getCurr_email();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        qrCode = sharedData.getData();
//        Log.d("QR: ", "qrcode: " + qrCode);

        //create user object since we need almost all the info
        //currEmail = "nutakki@usc.edu"; //temporary, just for testing

        DocumentReference docIdRef = db.collection("users").document(currEmail);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userDoc = task.getResult();
                if (task.isSuccessful()) {
                    //User(String firstName, String lastName, String email, String password, boolean checked_in, String occupation, String studentID, String profilePicture
                    currUser = new User(
                            userDoc.getString("firstName"),
                            userDoc.getString("lastName"),
                            userDoc.getString("email"),
                            userDoc.getString("password"),
                            userDoc.getBoolean("checked_in"),
                            userDoc.getString("occupation"),
                            userDoc.getString("studentID"),
                            userDoc.getString("profilePicture"),
                            userDoc.getString("current_qr"));

                    //Buttons
                    Button viewHistory = findViewById(R.id.button_view_history);
                    Button editProfileButton = findViewById(R.id.button_edit_pic);
                    Button logoutButton = findViewById(R.id.button_logout);
                    Button deleteAccount = findViewById(R.id.button_delete_account);
                    Button checkoutButton = findViewById(R.id.button_checkout_profile);
                    Button editProfileGalleryButton = findViewById(R.id.button_edit_pic_gallery);

                    if (currUser.isChecked_in()){
                        checkoutButton.setVisibility(View.VISIBLE);
                    }

                    //Text Views
                    TextView nameView = findViewById(R.id.text_view_name);
                    TextView emailView = findViewById(R.id.text_view_email);
                    TextView studentIDView = findViewById(R.id.text_view_id);
                    TextView majorView = findViewById(R.id.text_view_major);
                    TextView occupationView = findViewById(R.id.text_view_project_role);
                    TextView checkedIn = findViewById(R.id.text_view_checkin);
                    profilePicView = findViewById(R.id.image_view_pic);

                    //Display Data
                    String fullName = currUser.getFirstName() + " " + currUser.getLastName();
                    nameView.setText(fullName);
                    emailView.setText("Email: " + currUser.getEmail());
                    studentIDView.setText("Student ID: " + currUser.getStudentID());

                    //set as invisible if manager
                    //majorView.setText("Major: " + "Computer Science");
                    occupationView.setText(currUser.getOccupation());
                    final String profilePic = currUser.getProfilePicture();
                    Picasso.get().load(profilePic).into(profilePicView);
                    //qrCode = currUser.getCurrent_qr();

                    //Get Current Checked in Building for User and display
                    DocumentReference docIdRef = db.collection("users").document(currEmail);
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
                                                    String last_building = document.getString("buildingName");

                                                    //UPDATE UI
                                                    if (currUser.isChecked_in()){
                                                        checkedIn.setText("Currently Checked Into " + last_building);
                                                    }else{
                                                        checkedIn.setText("Currently Checked Out");
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

                    //Dialog for Picture Link
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this)
                            .setPositiveButton("OK", null);
                    final EditText input = new EditText(ProfileActivity.this);
                    builder.setTitle("Enter Link to Profile Picture");
                    builder.setView(input);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog editLinkDialog = builder.create();

                    //Override so we don't submit on invalid input
                    editLinkDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {

                            Button button = ((AlertDialog) editLinkDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            button.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    final String oldPicLink = profilePic;
                                    picLink = input.getText().toString();
                                    picLink = picLink.replaceAll("\\s+","");
                                    Log.d("document", "Link:(" + picLink + ")");
                                    if(TextUtils.isEmpty(picLink)){
                                        input.setError("No Link Provided");
                                    }else{
                                        Log.d("test", picLink);

                                        Picasso.get().load(picLink).into(profilePicView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                DocumentReference currDoc = db.collection("users").document(currEmail);
                                                currDoc.update("profilePicture", picLink)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("Doc", "DocumentSnapshot successfully written!");
                                                                profilePicView.setContentDescription(picLink);
                                                                Log.d("Doc", "Description: " + profilePicView.getContentDescription());
                                                                editLinkDialog.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("Doc", "Error writing document", e);
                                                            }
                                                        });
                                            }
                                            @Override
                                            public void onError(Exception e) {
                                                input.setError("Invalid Link");
                                                Picasso.get().load(oldPicLink).into(profilePicView);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });

                    //Click Edit Profile Button
                    editProfileButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Profile", "button Clicked");
                            //editProfilePic(picEditText)
                            editLinkDialog.show();
                        }
                    });

                    //Click Log Out Button
                    logoutButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logout();
                        }
                    });

                    //Click CSV BUTTON(TEMP)
//                    csvButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent profileActivityIntent = new Intent(ProfileActivity.this, CsvActivity.class);
//                            startActivity(profileActivityIntent);
//                        }
//                    });

                    //Click Delete Account Button
                    deleteAccount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteAccount();
                        }
                    });

                    //Click View History Button
                    viewHistory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileActivityIntent = new Intent(ProfileActivity.this, HistoryActivity.class);
                            profileActivityIntent.putExtra("email", currEmail);
                            startActivity(profileActivityIntent);
                        }
                    });

                    //Click Edit Profile Pic from Gallery
                    editProfileGalleryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                        }
                    });

                    //Click check out button
                    checkoutButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkOut();
                            Intent checkOutIntent = new Intent(ProfileActivity.this, CheckIn.class);
                            startActivity(checkOutIntent);
                        }

                    });
                }
            }
        });
    }

    //Edit Profile Pic from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                profilePicView.setImageBitmap(bitmap);
                uploadPic(); //call upload pic function to upload to firebase storage
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    protected void uploadPic(){
        profilePicView.setDrawingCacheEnabled(true);
        profilePicView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profilePicView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //create upload task
        String path = "profilepics/" + UUID.randomUUID() + ".png";
        Log.d("Doc", "storage path: " + path);
        StorageReference profilePicRef = storage.getReference(path);
        UploadTask uploadTask = profilePicRef.putBytes(data);

        uploadTask.addOnSuccessListener(ProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        String urlString = downloadUrl.toString();
                        Log.d("Doc", "download URL: " + urlString);

                        //upload into firebase Storage
                        DocumentReference currDoc = db.collection("users").document(currEmail);
                        currDoc.update("profilePicture", urlString)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Doc", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Doc", "Error writing document", e);
                                }
                            });
                    }
                });
            }
        });
    }

    //Log Out Button Function
    protected void logout(){
        Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
        intent.putExtra("email", "");
        startActivity(intent);
    }

    //Delete Account Function
    protected void deleteAccount(){
        db.collection("users").document(currUser.getEmail())//temp, ask Ashna about this
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("test", "DocumentSnapshot successfully deleted!");
                        //redirect to the home screen after logging out
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("test", "Error deleting document", e);
                    }
                });

        Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
        intent.putExtra("email", "");
        startActivity(intent);
    }

    //Edit Profile Picture Function
    protected void editProfilePic(){

        Log.d("test", picLink);
        Picasso.get().load(picLink).into(profilePicView, new Callback() {
            @Override
            public void onSuccess() {
                DocumentReference currDoc = db.collection("users").document(currEmail);
                currDoc.update("profilePicture", picLink)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Doc", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Doc", "Error writing document", e);
                            }
                        });
                //edit profile pic link in D
            }
            @Override
            public void onError(Exception e) {
                Picasso.get().load("https://raw.githubusercontent.com/lindshuang/image-store/main/error_profile_pic.png").into(profilePicView);
            }
        });

        //https://ctcusc.com/images/headshots/ctc-lindsay.jpg
    }

    //CHECK OUT

    public void checkOut() {
        Log.d("in check out", "in check out" + currUser.getCurrent_qr());

        //get building information
        DocumentReference docIdRef = db.collection("buildings").document(currUser.getCurrent_qr());
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
        DocumentReference docIdRef2 = db.collection("users").document(currUser.getEmail());
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
                        Calendar gfg_calender = Calendar.getInstance(TimeZone.getTimeZone("PST"));
                        timeOutT.format("%tl:%tM", gfg_calender, gfg_calender);
                        String timeOutTime = String.valueOf(timeOutT);
                        Log.d("time out date", "time Out Time "+ timeOutTime);

                        //calculate time elapsed
                        try {
                            Log.d("try", "went into try");
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            Date date1 = format.parse(timeIn_db);
                            Log.d("date1","date1");
                            Date date2 = format.parse(timeOutTime);
                            Log.d("date1","date2");
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
        DocumentReference statusRef = db.collection("users").document(currUser.getEmail());
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
        DocumentReference buildingRef = db.collection("buildings").document(currUser.getCurrent_qr());
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
            if(occupants.get(i).compareTo(currUser.getEmail()) == 0) {
                occupants.set(i, "0");
            }
            else {
                Log.d("error", "can not find occupant in the list");
            }
        }
        DocumentReference OccupantsRef = db.collection("buildings").document(currUser.getCurrent_qr());
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


    //Edit Profile Pic Pop-up
//    public AlertDialog onCreateDialog() {
//        builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater(); // Get the layout inflater
//        final View view = inflater.inflate(R.layout.dialog_edit_pic, null);
//        final EditText picEditText = view.findViewById(R.id.img_link);
//        final TextView error_text = view.findViewById(R.id.error_text);
//
//                // Inflate and set the layout for the dialog, pass null as the parent view because its going in the dialog layout
//        builder.setView(view)
//                // Add action buttons
//                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        picLink = picEditText.getText().toString();
//                        if(picLink.length() <= 0){
//                            error_text.setVisibility(View.VISIBLE);
//                            error_text.setText("Email Address not found");
//                            Log.d("document", "no link!");
//                        }else{
//                            editProfilePic(picLink);
//                        }
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        return builder.create();
//    }
}
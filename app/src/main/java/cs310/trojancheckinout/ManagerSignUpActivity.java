package cs310.trojancheckinout;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import androidx.annotation.NonNull;

import cs310.trojancheckinout.models.User;

public class ManagerSignUpActivity  extends AppCompatActivity {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button submitBtn;
    private TextView errorTexts;

    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String password = "";

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_signup);

        firstNameEdit = findViewById(R.id.firstname_edit);
        lastNameEdit = findViewById(R.id.lastname_edit);
        emailEdit = findViewById(R.id.email_address_edit);
        passwordEdit = findViewById(R.id.password_edit);
        submitBtn = findViewById(R.id.submitButton);
        errorTexts = findViewById(R.id.error_email);

        db = FirebaseFirestore.getInstance();

        firstNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                firstName = firstNameEdit.getText().toString();
                if(firstName.length() <= 0){
                    firstNameEdit.setError("Enter FirstName");
                }
                return true;
            }

        });

        lastNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                lastName = lastNameEdit.getText().toString();
                if(lastName.length() <= 0){
                    lastNameEdit.setError("Enter LastName");
                }
                return true;
            }
        });

        emailEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                email = emailEdit.getText().toString();
                //  boolean checkDupe = callData(email);
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.d("success", document.getId() + " => " + document.getData());
                                        //all_users.add(document.getId());
                                        if(document.getId().compareTo(email)==0){
                                            Log.d("set","SETTING");
                                            emailEdit.setError("Duplicate email");
                                            errorTexts.setText("Duplicate email");
                                        }
                                    }
                                    Log.d("success2", "success2");


                                } else {
                                    Log.d("bad", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                if(email.length() <= 1){
                    emailEdit.setError("Enter Email Address");
                }
                if(!email.contains("@usc.edu")){
                    emailEdit.setError("Enter USC Email Address");
                }
                return true;
            }
        });

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                password = passwordEdit.getText().toString();
                if(password.length() <= 0){
                    passwordEdit.setError("Enter Password");
                }
                else if(password.length() < 4) {
                    passwordEdit.setError("Password needs to be at least 4 characters long");
                }
                return true;
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = firstNameEdit.getText().toString();
                lastName = lastNameEdit.getText().toString();
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                User user = new User(firstName, lastName, email, password);
                // Log.d("TESTT",  firstName + lastName + email + password);
                db.collection("users").document(email).set(user);

                if(firstName.length() <= 1){
                    firstNameEdit.setError("Enter FirstName");
                }
                if(email.length() <= 1){
                    emailEdit.setError("Enter Email Address");
                }
                if(!email.contains("@usc.edu")){
                    emailEdit.setError("Enter USC Email Address");
                }
                if(lastName.length() <= 1){
                    lastNameEdit.setError("Enter LastName");
                }
                if(password.length() <= 1){
                    passwordEdit.setError("Enter Password");
                }
                if(password.length() < 4) {
                    passwordEdit.setError("Password needs to be at least 4 characters long");
                }

                if(firstName.length() > 1 && email.length() > 1 && email.contains("usc.edu") && lastName.length() > 1 && password.length() >= 4) {
                    Intent intent = new Intent(ManagerSignUpActivity.this, LogInActivity.class);
                    startActivityForResult(intent, 0);
                }


            }
        });


    }
}
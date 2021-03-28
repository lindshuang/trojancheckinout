package cs310.trojancheckinout;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import cs310.trojancheckinout.models.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StudentSignUpActivity  extends AppCompatActivity {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText studentIDEdit;
    private EditText majorEdit;
    private Button submitBtn;
    private TextView errorTexts;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String studentID;
    private String major;

    List<String> all_users = new ArrayList<>();
    boolean checkDuplicate = false;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_signup);

        firstNameEdit = findViewById(R.id.firstname_edit);
        lastNameEdit = findViewById(R.id.lastname_edit);
        emailEdit = findViewById(R.id.email_address_edit);
        passwordEdit = findViewById(R.id.password_edit);
        studentIDEdit = findViewById(R.id.student_id_edit);
        majorEdit = findViewById(R.id.major_edit);
        submitBtn = findViewById(R.id.submitButton);

        errorTexts = (TextView) findViewById(R.id.errorSignUp);

        db = FirebaseFirestore.getInstance();


//        Log.d("tag","made it out");

        firstNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                firstName = firstNameEdit.getText().toString();
                if(firstName.length() <= 1){
                    firstNameEdit.setError("Enter FirstName");
                }
                return true;
            }
        });

        lastNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                lastName = lastNameEdit.getText().toString();
                if(lastName.length() <= 1){
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
                errorTexts.setText("");
                password = passwordEdit.getText().toString();
                if(password.length() <= 1){
                    passwordEdit.setError("Enter Password");
                }
                return true;
            }
        });

        studentIDEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                studentID = studentIDEdit.getText().toString();
                if(studentID.length() <= 9){
                    studentIDEdit.setError("Enter StudentID");
                }
                return true;
            }
        });

        majorEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                major = majorEdit.getText().toString();
                if(major.length() <= 1){
                    majorEdit.setError("Enter Major");
                }
                return true;
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("taggg","got in here the onclicl");
                firstName = firstNameEdit.getText().toString();
                lastName = lastNameEdit.getText().toString();
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                major = majorEdit.getText().toString();
                studentID = studentIDEdit.getText().toString();

                User user = new User(firstName, lastName, email, password, studentID, major);
                db.collection("users").document(email).set(user);

                Intent intent = new Intent(StudentSignUpActivity.this, LogInActivity.class);
                startActivityForResult(intent, 0);


            }
        });


    }

}
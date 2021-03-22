package cs310.trojancheckinout;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;

import cs310.trojancheckinout.models.User;
import androidx.appcompat.app.AppCompatActivity;

public class StudentSignUpActivity  extends AppCompatActivity {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText studentIDEdit;
    private EditText majorEdit;
    private Button submitBtn;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String studentID;
    private String major;

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
                if(email.length() <= 0){
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
                return true;
            }
        });

        studentIDEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                studentID = studentIDEdit.getText().toString();
                if(studentID.length() <= 0){
                    studentIDEdit.setError("Enter StudentID");
                }
                return true;
            }
        });

        majorEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                major = majorEdit.getText().toString();
                if(major.length() <= 0){
                    majorEdit.setError("Enter Major");
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
                major = majorEdit.getText().toString();
                studentID = studentIDEdit.getText().toString();

                User user = new User(firstName, lastName, email, password, studentID, major);
                db.collection("users").document(email).set(user);


                Intent intent = new Intent(StudentSignUpActivity.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}
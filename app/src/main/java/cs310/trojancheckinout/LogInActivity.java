package cs310.trojancheckinout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import android.util.Log;
import javax.security.auth.login.LoginException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity  extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private Button createAccountButton;

    private TextView error_text;

    private String email;
    private String password;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEdit = findViewById(R.id.email_address_edit);
        passwordEdit = findViewById(R.id.password_edit);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        error_text = findViewById(R.id.error_text);
        error_text.setVisibility(View.INVISIBLE);

        db = FirebaseFirestore.getInstance();


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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEdit.getText().toString();
                sharedData.setCurr_email(email);
                password = passwordEdit.getText().toString();
                DocumentReference docIdRef = db.collection("users").document(email);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (!document.exists()) {
                                error_text.setVisibility(View.VISIBLE);
                                error_text.setText("Email Address not found");
                                Log.d("document", "Document does not exist!");

                            }
                            else{
                                String pswd = document.getString("password");
                                boolean is_deleted = document.getBoolean("is_deleted");

                                if(!pswd.equals(password)){
                                    error_text.setVisibility(View.VISIBLE);
                                    error_text.setText("Password does not match");
                                    Log.d("document", "password is wrong");
                                }
                                else if(is_deleted){
                                    error_text.setVisibility(View.VISIBLE);
                                    error_text.setText("Account has been deleted");
                                    Log.d("document", "account is deleted");
                                }
                                else {
                                    Intent intent = new Intent(LogInActivity.this, NavActivity.class);
                                    //intent.putExtra("email", email);
                                    startActivityForResult(intent, 0);
                                    Log.d("Document", "Document exists!");
                                }
                            }

                        } else {
                            Log.d("document", "Failed with: ", task.getException());
                        }
                    }
                });

            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}

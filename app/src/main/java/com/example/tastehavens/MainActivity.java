package com.example.tastehavens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText SignUpNames, userName, SignUpEmail, SignUpPassword;
    private Button buttonSign;
    private String currentUserId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      
        db = FirebaseFirestore.getInstance();

        SignUpEmail = findViewById(R.id.signup_email);
        userName = findViewById(R.id.signup_username);
        SignUpNames = findViewById(R.id.signup_name);
        SignUpPassword = findViewById(R.id.signup_password);
        buttonSign = findViewById(R.id.signup_button);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        buttonSign.setOnClickListener(v -> {
            String email = SignUpEmail.getText().toString().trim();
            String user = userName.getText().toString().trim();
            String password = SignUpPassword.getText().toString().trim();
            String name = SignUpNames.getText().toString().trim();

            if (validateInputs(email, user, password, name)) {
                showSecurityQuestionsBottomSheet(name, user, email, password);
            }
        });
    }

    private boolean validateInputs(String email, String user, String password, String name) {
        boolean isValid = true;

        if (name.isEmpty()) {
            SignUpNames.setError("Name is required");
            isValid = false;
        }

        if (user.isEmpty()) {
            userName.setError("Username is required");
            isValid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignUpEmail.setError("Valid email is required");
            isValid = false;
        }

        if (password.isEmpty() || password.length() < 6) {
            SignUpPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    @SuppressLint("NewApi")
    private void showSecurityQuestionsBottomSheet(String name, String user, String email, String password) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.register, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        Spinner Q1 = bottomSheetView.findViewById(R.id.security_question_1);
        Spinner Q2 = bottomSheetView.findViewById(R.id.security_question_2);
        Spinner Q3 = bottomSheetView.findViewById(R.id.security_question_3);
        Button finishSignUp = bottomSheetView.findViewById(R.id.btn_save_questions);
        EditText A1 = bottomSheetView.findViewById(R.id.security_answer_1);
        EditText A2 = bottomSheetView.findViewById(R.id.security_answer_2);
        EditText A3 = bottomSheetView.findViewById(R.id.security_answer_3);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.secutity,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Q1.setAdapter(adapter);
        Q2.setAdapter(adapter);
        Q3.setAdapter(adapter);

        finishSignUp.setOnClickListener(v -> {
            String answer1 = A1.getText().toString().trim();
            String answer2 = A2.getText().toString().trim();
            String answer3 = A3.getText().toString().trim();
            String question1 = Q1.getSelectedItem().toString();
            String question2 = Q2.getSelectedItem().toString();
            String question3 = Q3.getSelectedItem().toString();


            if (answer1.isEmpty()) {
                A1.setError("Answer required");
                return;
            }
            if (answer2.isEmpty()) {
                A2.setError("Answer required");
                return;
            }
            if (answer3.isEmpty()) {
                A3.setError("Answer required");
                return;
            }
            if (question1.equals(question2) || question1.equals(question3) || question2.equals(question3)) {
                Toast.makeText(this, "Please select different questions", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();
            completeRegistration(name, user, email, password, question1, answer1, question2, answer2, question3, answer3);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void completeRegistration(String name, String username, String email, String password,
                                      String question1, String answer1, String question2, String answer2,
                                      String question3, String answer3) {
        // Add validation for questions
        if (question1.equals(question2) || question1.equals(question3) || question2.equals(question3)) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please select different security questions", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            currentUserId = user.getUid();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            saveUserData(name, username, email,
                                                    question1.trim(), answer1.trim(),
                                                    question2.trim(), answer2.trim(),
                                                    question3.trim(), answer3.trim());
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void saveUserData(String name, String username, String email,
                              String question1, String answer1, String question2, String answer2,
                              String question3, String answer3) {

        Helper user = new Helper(
                name, username, email, "", "customer",
                0, "N/A", 0, "N/A"
        );


        Secutity_questions questions = new Secutity_questions(
                question1, answer1,
                question2, answer2,
                question3, answer3
        );


        db.collection("Users").document(currentUserId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    db.collection("Users").document(currentUserId)
                            .collection("SecurityQuestions")
                            .document("questions")
                            .set(questions)
                            .addOnSuccessListener(aVoid1 -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, Login.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Failed to save security questions", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                });
    }
    public void clicked(View view) {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
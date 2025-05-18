package com.example.tastehavens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText, forgot_Password;
    FirebaseAuth mAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        loginEmail = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        forgot_Password = findViewById(R.id.forgot_Password);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        TextView signupText = findViewById(R.id.signupRedirectText);
        String fullText = "Don't have an account yet? Sign up";
        SpannableString spannable = new SpannableString(fullText);


        int start = fullText.indexOf("Sign up");
        int end = start + "Sign up".length();
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#f1b123")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        signupText.setText(spannable);

        loginButton.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (email.isEmpty()) {
                loginEmail.setError("Email cannot be empty");
                return;
            }
            if (password.isEmpty()) {
                loginPassword.setError("Password cannot be empty");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getUid());
                            }
                        } else {
                            Toast.makeText(Login.this,
                                    "Login failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        signupRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, MainActivity.class));
        });

        forgot_Password.setOnClickListener(v ->showVerificationBottomSheet() );
    }

    private void checkUserRole(String userId) {
        database.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        if (role != null) {
                            navigateBasedOnRole(role);
                        } else {
                            Toast.makeText(this, "Role not assigned", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Failed to fetch user data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });

        FirebaseMessaging.getInstance().subscribeToTopic("AllOrders")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed to AllOrders topic";
                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Log.d("FCM", msg);
                });

    }

    private void navigateBasedOnRole(String role) {
        Intent intent;
        switch (role.toLowerCase()) {
            case "chef":
                intent = new Intent(this, Incoming_orders.class);
                break;
            case "customer":
                intent = new Intent(this, Menu.class);
                break;
            case "admin":
                intent = new Intent(this, Admin_Portal.class);
                break;
            case "waiter":
                intent = new Intent(this, Waiter_Menu.class);
                break;
            default:
                Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }

    public void showVerificationBottomSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.forgot_password, null);
        bottomSheet.setContentView(bottomSheetView);

        Spinner spinner1 = bottomSheetView.findViewById(R.id.security_question_1);
        Spinner spinner2 = bottomSheetView.findViewById(R.id.security_question_2);
        Spinner spinner3 = bottomSheetView.findViewById(R.id.security_question_3);
        EditText emailInput = bottomSheetView.findViewById(R.id.Email_text);
        EditText answer1 = bottomSheetView.findViewById(R.id.security_answer_1);
        EditText answer2 = bottomSheetView.findViewById(R.id.security_answer_2);
        EditText answer3 = bottomSheetView.findViewById(R.id.security_answer_3);
        Button verifyButton = bottomSheetView.findViewById(R.id.button2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.secutity,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");
        progressDialog.setCancelable(false);

        verifyButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String q1 = spinner1.getSelectedItem().toString();
            String q2 = spinner2.getSelectedItem().toString();
            String q3 = spinner3.getSelectedItem().toString();
            String a1 = answer1.getText().toString().trim();
            String a2 = answer2.getText().toString().trim();
            String a3 = answer3.getText().toString().trim();


//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("New Password");
//            builder.setMessage("this is your new password ");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();

            if (email.isEmpty()) {
                emailInput.setError("Email required");
                return;
            }
            if (a1.isEmpty() || a2.isEmpty() || a3.isEmpty()) {
                Toast.makeText(this, "Please answer all security questions", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            db.collection("Users")
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(query -> {
                        if (query.isEmpty()) {
                            progressDialog.dismiss();
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DocumentSnapshot userDoc = query.getDocuments().get(0);
                        String userId = userDoc.getId();

                        db.collection("Users")
                                .document(userId)
                                .collection("SecurityQuestions")
                                .document("questions")
                                .get()
                                .addOnSuccessListener(securityDoc -> {
                                    progressDialog.dismiss();
                                    if (!securityDoc.exists()) {
                                        Toast.makeText(this, "Security questions not set", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    String dbQ1 = securityDoc.getString("question1");
                                    String dbQ2 = securityDoc.getString("question2");
                                    String dbQ3 = securityDoc.getString("question3");
                                    String dbA1 = securityDoc.getString("answer1");
                                    String dbA2 = securityDoc.getString("answer2");
                                    String dbA3 = securityDoc.getString("answer3");

                                    boolean questionsMatch = false;
                                    boolean answersMatch = true;

                                    if (q1.equals(dbQ1) || q1.equals(dbQ2) || q1.equals(dbQ3)) {
                                        questionsMatch = true;
                                        if (q1.equals(dbQ1)) answersMatch &= a1.equalsIgnoreCase(dbA1);
                                        else if (q1.equals(dbQ2)) answersMatch &= a1.equalsIgnoreCase(dbA2);
                                        else if (q1.equals(dbQ3)) answersMatch &= a1.equalsIgnoreCase(dbA3);
                                    }

                                    if (q2.equals(dbQ1) || q2.equals(dbQ2) || q2.equals(dbQ3)) {
                                        questionsMatch = true;
                                        if (q2.equals(dbQ1)) answersMatch &= a2.equalsIgnoreCase(dbA1);
                                        else if (q2.equals(dbQ2)) answersMatch &= a2.equalsIgnoreCase(dbA2);
                                        else if (q2.equals(dbQ3)) answersMatch &= a2.equalsIgnoreCase(dbA3);
                                    }

                                    if (q3.equals(dbQ1) || q3.equals(dbQ2) || q3.equals(dbQ3)) {
                                        questionsMatch = true;
                                        if (q3.equals(dbQ1)) answersMatch &= a3.equalsIgnoreCase(dbA1);
                                        else if (q3.equals(dbQ2)) answersMatch &= a3.equalsIgnoreCase(dbA2);
                                        else if (q3.equals(dbQ3)) answersMatch &= a3.equalsIgnoreCase(dbA3);
                                    }

                                    if (questionsMatch && answersMatch) {
                                        AlertDialog.Builder passwordDialog = new AlertDialog.Builder(this);
                                        passwordDialog.setTitle("Reset Password");

                                        View viewInflated = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
                                        final EditText input = viewInflated.findViewById(R.id.new_password_input);
                                        passwordDialog.setView(viewInflated);

                                        passwordDialog.setPositiveButton("Reset", (dialog, which) -> {
                                            String newPassword = input.getText().toString().trim();
                                            if (newPassword.length() < 6) {
                                                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, "current_known_password_or_temp")
                                                    .addOnSuccessListener(authResult -> {
                                                        FirebaseUser user = authResult.getUser();
                                                        if (user != null) {
                                                            user.updatePassword(newPassword)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        Toast.makeText(this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(this, "Reauthentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });

                                        }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                                        passwordDialog.show();


                                    } else {
                                        Toast.makeText(this, "Verification failed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Error finding user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        bottomSheet.show();
    }


}





package com.example.tastehavens;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_Users extends AppCompatActivity {
    Button adduser;
    FirebaseFirestore database;
    FirebaseUser user;
    FirebaseAuth auth;
    EditText name, email, password, phone_Number, salary, employee_Number, username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users);

        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        phone_Number = findViewById(R.id.phoneNumberEditText);
        salary = findViewById(R.id.salaryEditText);
        employee_Number = findViewById(R.id.employeeNumberEditText);
        username = findViewById(R.id.UserEditText);
        adduser = findViewById(R.id.addUserButton);


        Spinner spinner = findViewById(R.id.role);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        adduser.setOnClickListener(v -> {
            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();
            String Name = name.getText().toString().trim();
            String PhoneNumber = phone_Number.getText().toString().trim();
            String Pay = salary.getText().toString().trim();
            String EmpNumber = employee_Number.getText().toString().trim();
            String Username = username.getText().toString().trim();
            String Role = spinner.getSelectedItem().toString();

            int Salary = Integer.valueOf(Pay);

            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(Add_Users.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();

                            Helper details = new Helper(Name, Username, Email, Password,Role, 0, EmpNumber, Salary, PhoneNumber);

                            database.collection("Users")
                                    .document(userId)
                                    .set(details)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(Add_Users.this, "User has been added", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Add_Users.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(Add_Users.this, "Failed to create Auth user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }
}
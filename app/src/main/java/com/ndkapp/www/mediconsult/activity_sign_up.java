package com.ndkapp.www.mediconsult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ndkapp.www.mediconsult.model.UserModel;

import org.jetbrains.annotations.NotNull;

public class activity_sign_up extends AppCompatActivity {

    CheckBox checkBox;
    Button signUp;
   EditText email,password,password_conf;
   TextView signIn;
   FirebaseAuth auth;
   FirebaseDatabase database;

   ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        checkBox = findViewById(R.id.checkBox);
        signUp = findViewById(R.id.reg_btn);
       // name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        password_conf = findViewById(R.id.password_confirm);
        signIn = findViewById(R.id.sign_in);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(activity_sign_up.this, activity_sign_in.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valeur = checkBox.isChecked();
                if(valeur == false){
                 checkBox.setError("please check the box");
                    Toast.makeText(activity_sign_up.this, "please check the box !", Toast.LENGTH_SHORT).show();
                    return;
                }
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }

            private void createUser() {

                //String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String Password_c = password_conf.getText().toString();

               /* if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegistrationActivity.this, "Name is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(activity_sign_up.this,"Email is Empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(userPassword)){
                    Toast.makeText(activity_sign_up.this,"Password is Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password_c)){
                    Toast.makeText(activity_sign_up.this,"Confirm Password is Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Password_c.equals(userPassword)){
                    Toast.makeText(activity_sign_up.this,"the password confirmation does not match ",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userPassword.length() < 6){
                    Toast.makeText(activity_sign_up.this, "Password Length must be grater then 6 letter", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Create User
                auth.createUserWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    UserModel userModel = new UserModel(userEmail,userPassword,Password_c);
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(userModel);
                                    progressBar.setVisibility(View.GONE);


                                    Toast.makeText(activity_sign_up.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(activity_sign_up.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }
}
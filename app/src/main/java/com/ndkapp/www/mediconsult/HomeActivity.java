package com.ndkapp.www.mediconsult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    LinearLayout L1,L2;
    Button bt1,bt2;
    TextView  textview1,textview2;
    EditText editText1,editText2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        L1=findViewById(R.id.L1);
        L2=findViewById(R.id.L2);
        bt1=findViewById(R.id.bt1);
        bt2=findViewById(R.id.bt2);
        textview2=findViewById(R.id.textview2);
        editText1=findViewById(R.id.editText1);
        editText2=findViewById(R.id.editText2);
        textview1=findViewById(R.id.textview1);

        L1.setVisibility(View.VISIBLE);
        L2.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        pd=new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(HomeActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String verificationId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent: "+verificationId);

                mVerificationId=verificationId;
                forceResendingToken=token;
                pd.dismiss();

                L1.setVisibility(View.GONE);
                L2.setVisibility(View.VISIBLE);

                Toast.makeText(HomeActivity.this,"Verification code sent...",Toast.LENGTH_SHORT).show();

                textview1.setText("Please type the verification code we sent \nto"+editText1.getText().toString().trim());
            }
        };
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = editText1.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(HomeActivity.this,"Please enter phone number...",Toast.LENGTH_SHORT).show();
                }else{
                    startPhoneNimberVerification(phone);
                }
            }
        });
        textview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = editText1.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(HomeActivity.this,"Please enter phone number...",Toast.LENGTH_SHORT).show();
                }else{
                    resendVerificationCode(phone,forceResendingToken);
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText2.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(HomeActivity.this,"Please enter verification code...",Toast.LENGTH_SHORT).show();
                }else{
                    verifyPhoneNumberWithCode(mVerificationId,code);
                }
            }
        });
    }

    private void startPhoneNimberVerification(String phone) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendVerificationCode(String phone,PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending Code");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        pd.setMessage("Verifying Code");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(HomeActivity.this,"Logged In as"+phone,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this,Doctors.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(HomeActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
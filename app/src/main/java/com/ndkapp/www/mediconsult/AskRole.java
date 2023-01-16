package com.ndkapp.www.mediconsult;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AskRole extends AppCompatActivity {

    private ImageView doctorButton, patientButton;
    private TextView doctorview, patientview;
    public int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_role);
        doctorButton = findViewById(R.id.doctorLogin);
        patientButton = findViewById(R.id.patientLogin);
        doctorview = findViewById(R.id.doctortextView);
        patientview = findViewById(R.id.PatienttextView2);

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, HomeActivity.class));
                //checkDoctorSession();
            }

        });

        doctorview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, HomeActivity.class));
                // checkDoctorSession();
            }
        });

        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, activity_sign_in.class));
                //checkPatientSession();
            }
        });

        patientview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, activity_sign_in.class));
                //checkPatientSession();
            }
        });
        TextView changeLang = findViewById(R.id.changMyLang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

    }
    private void showChangeLanguageDialog() {
        final String[] listItems = {"French","English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AskRole.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    n=0;
                    setLocale("fr");
                    recreate();
                }else if(i==1){
                    n=1;
                    setLocale("en");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public int langue(){
        return n;
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }


}
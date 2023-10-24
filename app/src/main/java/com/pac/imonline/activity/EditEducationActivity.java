package com.pac.imonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pac.imonline.R;


public class EditEducationActivity extends AppCompatActivity {

    public static void start(Context context, EducationAdapter educationAdapter) {
        Intent intent = new Intent(context, EditEducationActivity.class);
        context.startActivity(intent);
    }

     EditText editTextUni_name;
     EditText editTextDate;
     EditText editTextGraduation;
     EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_education);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        this.editTextUni_name = findViewById(R.id.editTextUni_name);
        this.editTextDate = findViewById(R.id.editTextDate);
        this.editTextGraduation = findViewById(R.id.editTextGraduation);
        this.editTextDescription = findViewById(R.id.editTextDescription);
    }

    public void editEducation(View view){
        String uniName = this.editTextUni_name.getText().toString();
        String date = this.editTextDate.getText().toString();
        String graduation = this.editTextGraduation.getText().toString();
        String description = this.editTextDescription.getText().toString();

        EducationActivity newEducation = new EducationActivity(0,uniName, date, graduation, description);


        AppDatabase.getInstance(this).getEducationDAO().insert(newEducation);
        setResult(RESULT_OK);
        finish();
    }
}
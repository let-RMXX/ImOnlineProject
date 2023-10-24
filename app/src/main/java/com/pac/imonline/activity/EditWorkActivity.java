package com.pac.imonline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

public class EditWorkActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, EditWorkActivity.class);
        context.startActivity(intent);
    }

    private EditText editTextCompanyName;
    private EditText editTextWorkDate;
    private EditText editTextRole;
    private EditText editTextWorkDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_work);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        this.editTextCompanyName = findViewById(R.id.editTextCompanyName);
        this.editTextWorkDate = findViewById(R.id.editTextWorkDate);
        this.editTextRole = findViewById(R.id.editTextRole);
        this.editTextWorkDescription = findViewById(R.id.editTextWorkDescription);
    }

    public void editWork(View view){
        String compName = this.editTextCompanyName.getText().toString();
        String workDate = this.editTextWorkDate.getText().toString();
        String role = this.editTextRole.getText().toString();
        String WorkDescription = this.editTextWorkDescription.getText().toString();

        WorkActivity newWork = new WorkActivity(0, compName, workDate, role, WorkDescription);


        AppDatabase.getInstance(this).getWorkDAO().insert(newWork);
        finish();
    }
}

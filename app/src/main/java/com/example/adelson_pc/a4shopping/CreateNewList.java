package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateNewList extends AppCompatActivity {

    Date date = new Date();

    private AutoCompleteTextView listName;
    FloatingActionButton goTo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);

        listName = (AutoCompleteTextView) findViewById(R.id.list_name);
        goTo2 = (FloatingActionButton) findViewById(R.id.goTo2);

        goTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewList.this, Form2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
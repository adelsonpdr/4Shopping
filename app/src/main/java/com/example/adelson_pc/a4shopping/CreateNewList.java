package com.example.adelson_pc.a4shopping;

import android.content.Intent;
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
    private Button createList;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);

        listName = (AutoCompleteTextView) findViewById(R.id.list_name);
        createList = (Button) findViewById(R.id.list_creation);

        createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ShopList shopList = new ShopList();
                    shopList.setId(UUID.randomUUID().toString());
                    shopList.setName(listName.getText().toString());
                    shopList.setDate(date.toString());
                    shopList.setAdmin(firebaseAuth.getInstance().getCurrentUser().getUid().toString());

                    // Members members = new Members();
                    // shopList.setMember("ioUYvIJWreUWzt2CDppgMwHlzyn1");
                    //shopList.setMember("asasasasasasasasdfsdfaad");

                    // shopList.setMembers(members);

                    if (!shopList.getName().isEmpty()) {
                        databaseReference.child("Shopping_Lists").child(shopList.getId()).
                                setValue(shopList).isSuccessful();

//                        boolean members1 = databaseReference.child("Members").child("ioUYvIJWreUWzt2CDppgMwHlzyn1").
                        //                              updateChildren(members.getMember()).isSuccessful();

                        Intent intent = new Intent(CreateNewList.this, Lists.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                    alert("Não foi possível criar lista!");
                }
            }
        });

        initializeFirebase();
    }

    private void alert(String msg) {
        Toast.makeText(CreateNewList.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(CreateNewList.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
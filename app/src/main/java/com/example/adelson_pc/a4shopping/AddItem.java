package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.UUID;

public class AddItem extends AppCompatActivity {

    private AutoCompleteTextView itemName;
    private AutoCompleteTextView itemDescription;
    private Button saveItem;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        Gson gson = new Gson();
        String json = pref.getString("List", "");
        final ShopList shopList = gson.fromJson(json, ShopList.class);

        itemName = (AutoCompleteTextView) findViewById(R.id.item_name);
        itemDescription = (AutoCompleteTextView) findViewById(R.id.item_description);
        saveItem = (Button) findViewById(R.id.save_item);

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!itemName.getText().toString().isEmpty()) {

                    Item item = new Item();
                    item.setId(UUID.randomUUID().toString());
                    item.setMemberId(firebaseAuth.getInstance().getCurrentUser().getUid());
                    item.setName(itemName.getText().toString());
                    item.setDescription(itemDescription.getText().toString());

                    shopList.setItem(item.getId(), item);

                    try {
                        databaseReference.child("Shopping_Lists").child(shopList.getId()).
                                setValue(shopList).isSuccessful();

//                        boolean members1 = databaseReference.child("Members").child("ioUYvIJWreUWzt2CDppgMwHlzyn1").
                        //                              updateChildren(members.getMember()).isSuccessful();

                        SharedPreferences pref = getApplicationContext()
                                .getSharedPreferences("Chosen List", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        Gson gson = new Gson();
                        String json = gson.toJson(shopList);
                        editor.putString("List", json);
                        editor.commit();

                        Intent intent = new Intent(AddItem.this, ListItems.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        alert("Não foi possível criar lista!");
                    }
                } else {
                    alert("Nome necessário");
                }
            }
        });

        initializeFirebase();
    }

    private void alert(String msg) {
        Toast.makeText(AddItem.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(AddItem.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}

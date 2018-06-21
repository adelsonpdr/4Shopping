package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.UUID;

public class EditItem extends AppCompatActivity {

    private AutoCompleteTextView itemName;
    private AutoCompleteTextView itemDescription;
    private Button editItem;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        final Bundle extras = getIntent().getExtras();

        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        Gson gson = new Gson();
        String json = pref.getString("List", "");
        final ShopList shopList = gson.fromJson(json, ShopList.class);

        itemName = (AutoCompleteTextView) findViewById(R.id.name);
        itemDescription = (AutoCompleteTextView) findViewById(R.id.description);
        editItem = (Button) findViewById(R.id.edit_item);

        itemName.setText(extras.getString("nameItem"));
        itemDescription.setText(extras.getString("descriptionItem"));

        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!itemName.getText().toString().isEmpty()) {

                    Item item = new Item();
                    item.setId(extras.getString("itemId"));
                    item.setMemberId(extras.getString("itemMemberId"));
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

                        Intent intent = new Intent(EditItem.this, ListItems.class);
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
        Toast.makeText(EditItem.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(EditItem.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}

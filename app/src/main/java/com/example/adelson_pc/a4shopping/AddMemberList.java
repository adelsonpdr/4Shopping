package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class AddMemberList extends AppCompatActivity {

    private AutoCompleteTextView memberEmail;
    private Button addMember;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_list);

        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        Gson gson = new Gson();
        String json = pref.getString("List", "");
        final ShopList shopList = gson.fromJson(json, ShopList.class);

        memberEmail = (AutoCompleteTextView) findViewById(R.id.member_email);
        addMember = (Button) findViewById(R.id.add_member);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println(1);
                    if (memberEmail.getText().toString().equalsIgnoreCase("")) {
                        alert("Entrada inválida");
                    } else if (memberEmail.getText().toString().equalsIgnoreCase(
                            firebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        alert("Você é o admin da lista");
                    } else if (shopList.getMembers() != null &&
                            shopList.getMembers().containsValue(memberEmail.getText().toString())) {
                        alert("Membro já adicionado");
                    } else {
                        System.out.println(2);
                        final boolean[] flag = new boolean[1];
                        System.out.println(3);
                        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = new User();
                                System.out.println(4);
                                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                                    System.out.println(5);
                                    user = objSnapshot.getValue(User.class);

                                    System.out.println(6);
                                    if (user.getEmail().equalsIgnoreCase(memberEmail.getText().toString())) {

                                        System.out.println(7);
                                        flag[0] = true;

                                        System.out.println(8);
                                        break;
                                    }
                                }

                                if (flag[0]) {
                                    shopList.setMember(user.getUid(), user);
                                    databaseReference.child("Shopping_Lists").child(shopList.getId()).
                                            setValue(shopList).isSuccessful();

                                    SharedPreferences pref = getApplicationContext()
                                            .getSharedPreferences("Chosen List", 0);
                                    SharedPreferences.Editor editor = pref.edit();

                                    Gson gson = new Gson();
                                    String json = gson.toJson(shopList);
                                    editor.putString("List", json);
                                    editor.commit();

                                    alert("Membro adicionado com sucesso");
                                    Intent intent = new Intent(AddMemberList.this, ListItems.class);
                                    startActivity(intent);
                                } else {
                                    alert("Usuário não cadastrado");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

/*
                        FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();
                        mActivityHelper.showLoadingDialog(R.string.progress_dialog_loading);
                        if (memberEmail.getText().toString() != null && !memberEmail.getText().toString().isEmpty()) {
                            firebaseAuth.fetchProvidersForEmail(memberEmail.getText().toString()).
                                    addOnFailureListener(new TaskFailureLogger(TAG, "Error fetching providers for email")).
                                    addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {

                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        startEmailHandler(memberEmail.getText().toString(), task.getResult().getProviders());
                                    } else {
                                        mActivityHelper.dismissDialog();
                                    }
                                }
                            });
                            alert("User Adicionado");
                        }
                        else alert("User Nao encontrado");

                        System.out.println("FirebaseAuth.fetchProvidersForEmail Depois");



                        */
                    }

                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });

        initializeFirebase();

    }

    private void alert(String msg) {
        Toast.makeText(AddMemberList.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(AddMemberList.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}

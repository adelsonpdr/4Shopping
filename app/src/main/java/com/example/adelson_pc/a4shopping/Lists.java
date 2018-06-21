package com.example.adelson_pc.a4shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lists extends AppCompatActivity implements Serializable {

    private FloatingActionButton createGroup;
    ListView listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        createGroup = (FloatingActionButton) findViewById(R.id.createGroup);
        listData = (ListView) findViewById(R.id.lists);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Lists.this, CreateNewList.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        switch (item.getItemId()) {
            case 1:
                confirmationDialog(position);
        }
        return super.onContextItemSelected(item);
    }

    private void confirmationDialog(final int position) {

    }

   /* private boolean checkDatabaseEvent(final String listID){
        final boolean[] exist = new boolean[1];
        databaseReference.child("Members/members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopList    .clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Members members = new Members();
                    members.setMember((Map<String, Object>) objSnapshot.getValue(Members.class));


                    System.out.println(members.getMember().toString());


                    System.out.println("List ID: " + listID);

                    if(members.getMember().containsKey(listID)){
                        exist[0] = true;
                        //|| sL.getMembers().containsValue(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(exist[0] == true){
            return true;
        } else {
            return false;
        }
    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            // shopList.clear();
            Connection.logOut();
            cleanPreferences();
            Intent intent = new Intent(Lists.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            Toast.makeText(Lists.this, "Não foi possível sair!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void cleanPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Save user credentials", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("email");
        editor.remove("password");
        editor.commit();
        editor.clear();
        editor.commit();
    }
}
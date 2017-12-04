package com.example.adelson_pc.a4shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListMembers extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    private ShopList sL = new ShopList();
    private ArrayAdapter<User> arrayAdapterMember;
    private List<User> members = new ArrayList<User>();
    ListView listMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_members);

        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        Gson gson = new Gson();
        String json = pref.getString("List", "");
        final ShopList shopList = gson.fromJson(json, ShopList.class);
        sL = shopList;

        initializeFirebase();

        listMembers = (ListView) findViewById(R.id.members);

        fillList();

        listMembers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                listMembers.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                                    ContextMenu.ContextMenuInfo contextMenuInfo) {
                        if (shopList.getAdmin().equalsIgnoreCase(firebaseAuth.
                                getInstance().getCurrentUser().getUid())) {
                            contextMenu.add(Menu.NONE, 1, Menu.NONE, "deletar");
                        }
                    }
                });
                return false;
            }
        });
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(ListMembers.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
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
        new AlertDialog.Builder(this)
                .setTitle(members.get(position).getName())
                .setMessage("Deseja realmente deletar este usuário?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deletar(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (firebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(sL.getAdmin())) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.add_member, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_member_button) {
            Intent intent = new Intent(ListMembers.this, AddMemberList.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void deletar(int position) {

        sL.getMembers().remove(members.get(position).getUid());
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(sL);
        editor.putString("List", json);
        editor.commit();

        databaseReference.child("Shopping_Lists").child(sL.getId()).child("members").child(members.
                get(position).getUid()).setValue(null);


        fillList();

        // databaseReference.child("Shopping_List").
    }

    public void fillList() {
        if (sL.getMembers() != null) {
            members = new ArrayList<User>(sL.getMembers().values());
            arrayAdapterMember = new ArrayAdapter<User>(this,
                    android.R.layout.simple_list_item_1, members);
            listMembers.setAdapter(arrayAdapterMember);
        }
    }

}
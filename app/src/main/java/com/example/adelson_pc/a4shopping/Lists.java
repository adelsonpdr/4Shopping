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

    private List<ShopList> shopList = new ArrayList<ShopList>();
    //private List<Members> membersList = new ArrayList<Members>();
    private ArrayAdapter<ShopList> arrayAdapterShopList;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

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
        initializeFirebase();
        databaseEvent();

        fillList();

        System.out.println("Antes de Clicar");
        try {
            listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Depois de Clicar");

                    SharedPreferences pref = getApplicationContext()
                            .getSharedPreferences("Chosen List", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    Gson gson = new Gson();
                    String json = gson.toJson(shopList.get(position));
                    editor.putString("List", json);
                    editor.commit();

                    Intent intent = new Intent(Lists.this, ListItems.class);
                    startActivity(intent);
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }

        try {
            listData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    listData.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
                            if (shopList.get(position).getAdmin().equalsIgnoreCase(firebaseAuth.
                                    getInstance().getCurrentUser().getUid())) {
                                contextMenu.add(Menu.NONE, 1, Menu.NONE, "deletar");
                            }
                        }
                    });
                    return false;
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
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
                .setTitle(shopList.get(position).getName())
                .setMessage("Deseja realmente deletar esta lista?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deletar(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deletar(int position) {
        databaseReference.child("Shopping_Lists").child(shopList.get(position).getId()).setValue(null);
        databaseEvent();
        fillList();
    }

    private void fillList() {
        listData.setAdapter(null);
        ArrayAdapter<ShopList> adapter = new ArrayAdapter<ShopList>(this,
                android.R.layout.simple_list_item_1, shopList);
        adapter.notifyDataSetChanged();
        listData.setAdapter(adapter);
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(Lists.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void databaseEvent() {
        System.out.println("Teste 1");
        shopList.clear();
        databaseReference.child("Shopping_Lists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    ShopList sL = objSnapshot.getValue(ShopList.class);
                    System.out.println("Admin: " + sL.getAdmin().toString());

                    if (sL.getAdmin().equalsIgnoreCase(firebaseAuth.getInstance().getCurrentUser().getUid()) ||
                            (sL.getMembers() != null && sL.getMembers().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                        //|| sL.getMembers().containsValue(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        shopList.add(sL);
                    }

                }

                Collections.sort(shopList, new Comparator<ShopList>() {
                    public int compare(ShopList list1, ShopList list2) {
                        return list2.getDate().compareToIgnoreCase(list1.getDate());
                    }
                });

                arrayAdapterShopList = new ArrayAdapter<ShopList>(Lists.this,
                        android.R.layout.simple_list_item_1, shopList);
                if (!arrayAdapterShopList.isEmpty()) {
                    listData.setAdapter(arrayAdapterShopList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


/*
        databaseReference.child("Members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersLists    .clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    if(objSnapshot.getValue(Members.class).getMembers().
                            containsValue(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        ShopList sL = objSnapshot.getValue(ShopList.class);
                        shopList.add(sL);
                    }

                }

                Collections.sort(shopList, new Comparator<ShopList>(){
                    public int compare(ShopList list1, ShopList list2) {
                        return list2.getDate().compareToIgnoreCase(list1.getDate());
                    }
                });

                arrayAdapterShopList = new ArrayAdapter<ShopList>(Lists.this,
                        android.R.layout.simple_list_item_1, shopList);
                if(!arrayAdapterShopList.isEmpty()) {
                    listData.setAdapter(arrayAdapterShopList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
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
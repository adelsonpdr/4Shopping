package com.example.adelson_pc.a4shopping;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

public class ListItems extends AppCompatActivity {

    private FloatingActionButton addItem;
    ListView listItem;

    private ArrayAdapter<Item> arrayAdapterItem;
    private ShopList sL = new ShopList();
    private List<Item> items = new ArrayList<Item>();

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        Gson gson = new Gson();
        String json = pref.getString("List", "");
        final ShopList shopList = gson.fromJson(json, ShopList.class);
        sL = shopList;

        addItem = (FloatingActionButton) findViewById(R.id.add_item);
        listItem = (ListView) findViewById(R.id.items);


        initializeFirebase();

        fillList();
        try {
            listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListItems.this, ItemDetails.class);
                    intent.putExtra("nameItem", shopList.getItems().get(items.get(position).getId()).getName());
                    intent.putExtra("descriptionItem", shopList.getItems().get(items.get(position).getId()).getDescription());
                    startActivity(intent);
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }

        try {
            listItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    listItem.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
                            if (shopList.getItems().get(items.get(position).getId()).getMemberId().equalsIgnoreCase(firebaseAuth.
                                    getInstance().getCurrentUser().getUid()) || shopList.getAdmin().equalsIgnoreCase(firebaseAuth.
                                    getInstance().getCurrentUser().getUid())) {
                                contextMenu.add(Menu.NONE, 1, Menu.NONE, "editar");
                                contextMenu.add(Menu.NONE, 2, Menu.NONE, "deletar");
                            }
                        }
                    });
                    return false;
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
/*
        String items1[] = new String[shopList.getItems().size()];
        items1[0] = "a";

        for(Map<String, Item> i: shopList.getItems()){

        }
        List<Item> listItem = (List<Item>) shopList.getItems();

        ArrayAdapter<String> arrayAdapterItem = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItem.get(0).getName());

        setAdapter(arrayAdapterItem);
*/
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListItems.this, AddItem.class);
                startActivity(intent);
            }
        });
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(ListItems.this);
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
                Intent intent = new Intent(ListItems.this, EditItem.class);
                intent.putExtra("nameItem", sL.getItems().get(items.get(position).getId()).getName());
                intent.putExtra("descriptionItem", sL.getItems().get(items.get(position).getId()).getDescription());
                intent.putExtra("itemId", sL.getItems().get(items.get(position).getId()).getId());
                intent.putExtra("itemMemberId", sL.getItems().get(items.get(position).getId()).getMemberId());
                startActivity(intent);
                break;
            case 2:
                confirmationDialog(position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void confirmationDialog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle(items.get(position).getName())
                .setMessage("Deseja realmente deletar este item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deletar(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    private void deletar(int position) {

        sL.getItems().remove(items.get(position).getId());
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Chosen List", 0);
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(sL);
        editor.putString("List", json);
        editor.commit();

        databaseReference.child("Shopping_Lists").child(sL.getId()).child("items").child(items.
                get(position).getId()).setValue(null);

        fillList();

        // databaseReference.child("Shopping_List").
    }

    public void fillList() {
        if (sL.getItems() != null) {
            items = new ArrayList<Item>(sL.getItems().values());
            arrayAdapterItem = new ArrayAdapter<Item>(this,
                    android.R.layout.simple_list_item_1, items);
            listItem.setAdapter(arrayAdapterItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.see_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.see_members_button) {
            Gson gson = new Gson();
            String shopListObjectAsAString = gson.toJson(sL);
            Intent intent;
            intent = new Intent(ListItems.this, ListMembers.class);
            intent.putExtra("Member", shopListObjectAsAString);
            startActivity(intent);
            return true;
        }
        return false;
    }

}

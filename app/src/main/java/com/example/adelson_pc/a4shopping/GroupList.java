package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GroupList extends AppCompatActivity {

    private FloatingActionButton createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        createGroup = (FloatingActionButton) findViewById(R.id.createGroup);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Trocar por tela de criar Grupo
                Intent s = new Intent(GroupList.this, LoginActivity.class);
                startActivity(s);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Connection.logOut();
            cleanPreferences();
            Intent intent = new Intent(GroupList.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            Toast.makeText(GroupList.this, "Não foi possível sair!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void cleanPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit(); // commit changes
    }
}

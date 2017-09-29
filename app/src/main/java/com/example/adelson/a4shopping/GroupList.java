package com.example.adelson.a4shopping;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
}

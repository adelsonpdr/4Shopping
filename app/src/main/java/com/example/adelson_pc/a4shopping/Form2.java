package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Form2 extends AppCompatActivity {

    TextView e, obs2, eExclusiva;

    EditText obsBox2;

    RadioButton diesel, eletrica, sim2, nao2;

    RadioGroup radioEletrica;

    FloatingActionButton goTo3, backTo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);

        radioEletrica = (RadioGroup) findViewById(R.id.radioEletrica);

        e = (TextView) findViewById(R.id.e);
        obs2 = (TextView) findViewById(R.id.obs2);
        obsBox2 = (EditText) findViewById(R.id.obsBox2);
        eExclusiva = (TextView) findViewById(R.id.eExclusiva);

        diesel = (RadioButton) findViewById(R.id.diesel);
        eletrica = (RadioButton) findViewById(R.id.eletrica);
        sim2 = (RadioButton) findViewById(R.id.sim2);
        nao2 = (RadioButton) findViewById(R.id.nao2);

        goTo3 = (FloatingActionButton) findViewById(R.id.goTo2);
        backTo2 = (FloatingActionButton) findViewById(R.id.backTo2);

        diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eExclusiva.setVisibility(View.INVISIBLE);
                radioEletrica.setVisibility(View.INVISIBLE);
                obs2.setVisibility(View.INVISIBLE);
                obsBox2.setVisibility(View.INVISIBLE);
            }
        });

        eletrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eExclusiva.setVisibility(View.VISIBLE);
                radioEletrica.setVisibility(View.VISIBLE);
            }
        });

        sim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obs2.setVisibility(View.INVISIBLE);
                obsBox2.setVisibility(View.INVISIBLE);
            }
        });

        nao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obs2.setVisibility(View.VISIBLE);
                obsBox2.setVisibility(View.VISIBLE);
            }
        });

        backTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Form2.this, CreateNewList.class);
                startActivity(intent);
                finish();
            }
        });

        goTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Form2.this, Form3.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

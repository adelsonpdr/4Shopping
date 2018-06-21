package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Form3 extends AppCompatActivity {

    TextView obs3;

    RadioButton sim3, nao3;

    EditText obsBox3, lPlanilha, lHorimetro, data3;

    FloatingActionButton goTo3, backTo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);

        obs3 = (TextView) findViewById(R.id.obs3);
        obsBox3 = (EditText) findViewById(R.id.obsBox3);
        lPlanilha = (EditText) findViewById(R.id.lPlanilha);
        lHorimetro = (EditText) findViewById(R.id.lHorimetro);
        data3 = (EditText) findViewById(R.id.data3);

        sim3 = (RadioButton) findViewById(R.id.sim3);
        nao3 = (RadioButton) findViewById(R.id.nao3);

        goTo3 = (FloatingActionButton) findViewById(R.id.goTo2);
        backTo2 = (FloatingActionButton) findViewById(R.id.backTo2);

        sim3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lPlanilha.setVisibility(View.VISIBLE);
                lHorimetro.setVisibility(View.VISIBLE);
                data3.setVisibility(View.VISIBLE);
                obs3.setVisibility(View.INVISIBLE);
                obsBox3.setVisibility(View.INVISIBLE);
            }
        });

        nao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lPlanilha.setVisibility(View.INVISIBLE);
                lHorimetro.setVisibility(View.INVISIBLE);
                data3.setVisibility(View.INVISIBLE);
                obs3.setVisibility(View.VISIBLE);
                obsBox3.setVisibility(View.VISIBLE);
            }
        });

        backTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Form3.this, Form2.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

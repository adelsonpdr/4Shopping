package com.example.adelson_pc.a4shopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

public class ItemDetails extends AppCompatActivity {

    TextView itemName;
    TextView descriptionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Bundle extras = getIntent().getExtras();

        itemName = (TextView) findViewById(R.id.name_item);
        descriptionName = (TextView) findViewById(R.id.description_item);

        descriptionName.setMovementMethod(new ScrollingMovementMethod());

        itemName.setText(extras.getString("nameItem"));
        descriptionName.setText(extras.getString("descriptionItem"));

    }
}

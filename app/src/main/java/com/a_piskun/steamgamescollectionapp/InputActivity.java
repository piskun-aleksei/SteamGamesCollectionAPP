package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.a_piskun.steamgamescollectionapp.MESSAGE";
    Button confirm_id_button;
    EditText id_input_field;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        confirm_id_button = (Button) findViewById(R.id.confirm_id_button);

        id_input_field = (EditText) findViewById(R.id.id_input_field);

        confirm_id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose_intent = new Intent(v.getContext(), ChooseNextStepActivity.class);
                String message = id_input_field.getText().toString();
                choose_intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(choose_intent);
            }
        });
    }
}

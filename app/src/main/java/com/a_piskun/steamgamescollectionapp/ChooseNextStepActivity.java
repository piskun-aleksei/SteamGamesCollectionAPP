package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChooseNextStepActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    public final static String NAME_MESSAGE = "NAME";
    public final static String VIEW_MESSAGE = "VIEW";
    int GAMES_CONST = 1;
    int FRIENDS_CONST = 2;
    int RECENT_CONST = 3;

    Button games_collection_button, recent_games_button,
            compare_button, friend_list_button;
    String profile_id, profile_name;
    TextView name_message;
    Intent parent_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_next_step);

        initialize_fields();
        initialize_buttons();
    }

    private void initialize_fields(){
        parent_intent = getIntent();

        profile_id = parent_intent.getStringExtra(InputActivity.EXTRA_MESSAGE);
        profile_name = parent_intent.getStringExtra(InputActivity.NAME_MESSAGE);

        name_message = (TextView) findViewById(R.id.choose_name_message);
        name_message.setText("Hello, " + profile_name);

        games_collection_button = (Button) findViewById(R.id.games_collection_button);
        recent_games_button = (Button) findViewById(R.id.recent_games_button);
        compare_button = (Button) findViewById(R.id.compare_button);
        friend_list_button = (Button) findViewById(R.id.friend_list_button);
    }

    private void initialize_buttons(){
        games_collection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowChosenListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                show_intent.putExtra("View", 1);
                startActivity(show_intent);
            }
        });

        recent_games_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowChosenListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                show_intent.putExtra("View", 3);
                startActivity(show_intent);
            }
        });

        friend_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowChosenListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                show_intent.putExtra("View", 2);
                startActivity(show_intent);
            }
        });

        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowChosenListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                startActivity(show_intent);
            }
        });
    }
}

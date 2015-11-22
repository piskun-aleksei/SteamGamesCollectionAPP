package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class ChooseNextStepActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.a_piskun.steamgamescollectionapp.MESSAGE";
    public final static String NAME_MESSAGE = "com.a_piskun.steamgamescollectionapp.NAME";
    Button back_button, games_collection_button, recent_games_button,
            compare_button, friend_list_button;
    String profile_id, profile_name;
    TextView name_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_next_step);

        Intent parent_intent = getIntent();

        profile_id = parent_intent.getStringExtra(InputActivity.EXTRA_MESSAGE);
        profile_name = parent_intent.getStringExtra(InputActivity.NAME_MESSAGE);

        name_message = (TextView) findViewById(R.id.choose_name_message);
        name_message.setText(profile_name);

        back_button = (Button) findViewById(R.id.back_choose_button);
        games_collection_button = (Button) findViewById(R.id.games_collection_button);
        recent_games_button = (Button) findViewById(R.id.recent_games_button);
        compare_button = (Button) findViewById(R.id.compare_button);
        friend_list_button = (Button) findViewById(R.id.friend_list_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        games_collection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                startActivity(show_intent);
            }
        });

        recent_games_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                startActivity(show_intent);
            }
        });

        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                startActivity(show_intent);
            }
        });

        friend_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                show_intent.putExtra(EXTRA_MESSAGE, profile_id);
                show_intent.putExtra(NAME_MESSAGE, profile_name);
                startActivity(show_intent);
            }
        });
    }
}

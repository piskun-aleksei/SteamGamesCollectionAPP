package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class ChooseNextStepActivity extends AppCompatActivity {
    Button back_button, games_collection_button, recent_games_button,
            compare_button, friend_list_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_next_step);

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
                startActivity(show_intent);
            }
        });

        recent_games_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                startActivity(show_intent);
            }
        });

        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                startActivity(show_intent);
            }
        });

        friend_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_intent = new Intent(v.getContext(), ShowFriendListActivity.class);
                startActivity(show_intent);
            }
        });
    }
}

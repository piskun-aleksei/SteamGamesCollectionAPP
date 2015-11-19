package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class InputActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.a_piskun.steamgamescollectionapp.MESSAGE";
    Button confirm_id_button, get_info_button;
    EditText id_input_field;
    TextView json_message;
    File steam_api;
    String profile_url;
    StringBuilder profile_url_builder;
    String BASE_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=6FA27B723BB28AFB78D820A2C4C6DBD6&steamids=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        confirm_id_button = (Button) findViewById(R.id.confirm_id_button);
        get_info_button = (Button) findViewById(R.id.get_info_button);

        id_input_field = (EditText) findViewById(R.id.id_input_field);

        json_message = (TextView) findViewById(R.id.json_message);

        profile_url_builder = new StringBuilder("");

        profile_url_builder.append(BASE_URL);

        confirm_id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose_intent = new Intent(v.getContext(), ChooseNextStepActivity.class);
                String message = id_input_field.getText().toString();
                choose_intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(choose_intent);
            }
        });

        get_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_url_builder.append(id_input_field.getText().toString());
                profile_url = profile_url_builder.toString();
                ArrayList<String> strings = new ArrayList<String>();
                get_json_file("test");
              //  json_message.setText(strings.get(0));
                //id_input_field.setText(profile_url);
            }
        });
    }

    private void get_json_file(String json_url){
        //ArrayList<String> listItems = new ArrayList<String>();
        try {
            URL steam_api = new URL(
                    "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=6FA27B723BB28AFB78D820A2C4C6DBD6&steamids=76561197986648105.json");
            URLConnection tc = steam_api.openConnection();
            String test = tc.getInputStream().toString();

           /* String line;
            while ((line = in.readLine()) != null) {
                JSONArray ja = new JSONArray(line);

                //for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(0);
                    listItems.add(jo.getString("response"));
                //}
            }*/
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } /*catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        //return listItems;
    }

}

package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InputActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.a_piskun.steamgamescollectionapp.MESSAGE";
    public final static String NAME_MESSAGE = "com.a_piskun.steamgamescollectionapp.NAME";
    Button confirm_id_button, get_info_button;
    EditText id_input_field;
    TextView json_message;
    File steam_api;
    String profile_url, profile_name;

    String BASE_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?" +
            "key=6FA27B723BB28AFB78D820A2C4C6DBD6&steamids=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        confirm_id_button = (Button) findViewById(R.id.confirm_id_button);
        get_info_button = (Button) findViewById(R.id.get_info_button);

        id_input_field = (EditText) findViewById(R.id.id_input_field);

        json_message = (TextView) findViewById(R.id.json_message);


        confirm_id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose_intent = new Intent(v.getContext(), ChooseNextStepActivity.class);
                String message = id_input_field.getText().toString();
                choose_intent.putExtra(EXTRA_MESSAGE, message);
                choose_intent.putExtra(NAME_MESSAGE, profile_name);
                startActivity(choose_intent);
            }
        });

        get_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder profile_url_builder = new StringBuilder("");
                profile_url_builder.append(BASE_URL);
                profile_url_builder.append(id_input_field.getText().toString());
                profile_url = profile_url_builder.toString();

                get_json_file(profile_url);
                confirm_id_button.setVisibility(View.VISIBLE);

            }
        });
    }

    private void get_json_file(String json_url){
        new JSONTask().execute(json_url);
    }

    public class JSONTask extends AsyncTask<String , String, String > {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection url_connection = null;
            BufferedReader reader = null;
            try {
                URL steam_api = new URL(params[0]);
                url_connection = (HttpURLConnection) steam_api.openConnection();
                url_connection.connect();

                InputStream stream = url_connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String json_info = buffer.toString();

                JSONObject json_object = new JSONObject(json_info);
                JSONObject json_response = json_object.getJSONObject("response");
                JSONArray json_players = json_response.getJSONArray("players");
                JSONObject json_final_object = json_players.getJSONObject(0);

                String profile_name = json_final_object.getString("personaname");

                return profile_name;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(url_connection != null){
                    url_connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }

                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            profile_name = result;
            json_message.setText(profile_name);
        }
    }
}





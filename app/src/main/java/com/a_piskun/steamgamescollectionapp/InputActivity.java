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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InputActivity extends AppCompatActivity {

    Button confirm_id_button, get_info_button;
    EditText id_input_field;
    TextView json_message;
    String profile_url, profile_name;
    JSONObject recieved_json_object;

    String BASE_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?" +
            "key=6FA27B723BB28AFB78D820A2C4C6DBD6&steamids=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        initialize_fields();
        initialize_buttons();
    }

    private void initialize_fields(){

        confirm_id_button = (Button) findViewById(R.id.confirm_id_button);
        get_info_button = (Button) findViewById(R.id.get_info_button);

        id_input_field = (EditText) findViewById(R.id.id_input_field);

        json_message = (TextView) findViewById(R.id.json_message);
    }

    private void initialize_buttons(){
        confirm_id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose_intent = new Intent(v.getContext(), ChooseNextStepActivity.class);
                String message = id_input_field.getText().toString();
                choose_intent.putExtra("ID", message);
                choose_intent.putExtra("NAME", profile_name);
                confirm_id_button.setVisibility(View.INVISIBLE);
                startActivity(choose_intent);
            }
        });

        get_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirm_id_button.getVisibility() == View.VISIBLE) {
                    confirm_id_button.setVisibility(View.INVISIBLE);
                }

                StringBuilder profile_url_builder = new StringBuilder("");
                profile_url_builder.append(BASE_URL);
                profile_url_builder.append(id_input_field.getText().toString());
                profile_url = profile_url_builder.toString();
                get_json_file(profile_url);


            }
        });
    }

    private void get_json_file(String json_url){
        new JSONTask().execute(json_url);
    }

    private void parse_json() throws JSONException {
        if(recieved_json_object == null) {
            json_message.setText("Invalid URL or Bad internet connection");
            return;
        }
        JSONObject json_response = recieved_json_object.getJSONObject("response");
        JSONArray json_players = json_response.getJSONArray("players");
        JSONObject json_final_object = json_players.getJSONObject(0);
        profile_name = json_final_object.getString("personaname");

        if(profile_name != null) {
            json_message.setText(profile_name);
            confirm_id_button.setVisibility(View.VISIBLE);
        }
        else{
            json_message.setText("Invalid URL or Bad internet connection");
        }
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
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    String json_info = buffer.toString();

                    recieved_json_object = new JSONObject(json_info);

                return null;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (url_connection != null) {
                        url_connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                parse_json();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}





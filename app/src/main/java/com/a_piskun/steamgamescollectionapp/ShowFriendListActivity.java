package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
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

public class ShowFriendListActivity extends AppCompatActivity {
    Button back_button;
    TextView friend_first_message, friend_second_message, friend_third_message,
            friend_fourth_message, friend_fifth_message, name_message;
    String profile_id, profile_url, profile_name;
    String BASE_URL = "http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" +
            "6FA27B723BB28AFB78D820A2C4C6DBD6&steamid=";
    String URL_ENDING = "&relationship=friend";
    String BASE_SECONDARY_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?" +
            "key=6FA27B723BB28AFB78D820A2C4C6DBD6&steamids=";
    int current_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend_list);

        Intent parent_intent = getIntent();

        profile_id = parent_intent.getStringExtra(ChooseNextStepActivity.EXTRA_MESSAGE);
        profile_name = parent_intent.getStringExtra(ChooseNextStepActivity.NAME_MESSAGE);

        back_button = (Button) findViewById(R.id.back_friends_button);

        friend_first_message = (TextView) findViewById(R.id.friend_first_message);
        friend_second_message = (TextView) findViewById(R.id.friend_second_message);
        friend_third_message = (TextView) findViewById(R.id.friend_third_message);
        friend_fourth_message = (TextView) findViewById(R.id.friend_fourth_message);
        friend_fifth_message = (TextView) findViewById(R.id.friend_fifth_message);
        name_message = (TextView) findViewById(R.id.friends_name_message);

        name_message.setText(profile_name);

        StringBuilder profile_url_builder = new StringBuilder("");
        profile_url_builder.append(BASE_URL);
        profile_url_builder.append(profile_id);
        profile_url_builder.append(URL_ENDING);
        profile_url = profile_url_builder.toString();

        //for(int i = 0; i < 5; i ++){
        current_friend = 4;
        get_json_file(profile_url);

        //}

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String json_info = buffer.toString();
                JSONObject json_object = new JSONObject(json_info);
                JSONObject json_friendslist = json_object.getJSONObject("friendslist");
                JSONArray json_friends = json_friendslist.getJSONArray("friends");
                JSONObject json_final_object = json_friends.getJSONObject(current_friend);

                String friend_id = json_final_object.getString("steamid");

                StringBuilder profile_url_builder = new StringBuilder("");
                profile_url_builder.append(BASE_SECONDARY_URL);
                profile_url_builder.append(friend_id);

                return profile_url_builder.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            new JSONGetFriendName().execute(result);
        }
    }

    public class JSONGetFriendName extends AsyncTask<String , String, String > {

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
                JSONObject json_object = new JSONObject(json_info);
                JSONObject json_response = json_object.getJSONObject("response");
                JSONArray json_players = json_response.getJSONArray("players");
                JSONObject json_final_object = json_players.getJSONObject(0);

                String profile_name = json_final_object.getString("personaname");

                return profile_name;

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            if(current_friend == 0){
                friend_first_message.setText(result);
            }
            if(current_friend == 1){
                friend_second_message.setText(result);
            }
            if(current_friend == 2){
                friend_third_message.setText(result);
            }
            if(current_friend == 3){
                friend_fourth_message.setText(result);
            }
            if(current_friend == 4){
                friend_fifth_message.setText(result);
            }
            if(current_friend != 0){
                current_friend--;
                get_json_file(profile_url);
            }
        }
    }
}

package com.a_piskun.steamgamescollectionapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Arrays;

public class ShowChosenListActivity extends AppCompatActivity {
    TextView name_message, percentage_message, chosen_list_message, total_count_message,
            total_message;
    Integer list_mode;
    String profile_id, profile_url, profile_name, friend_id;
    String BASE_URL, SECONDARY_URL;
    Intent parent_intent;
    JSONArray recieved_json_array;

    ArrayList<String> list_of_friends;

    int [] games_hours;
    ListView chosen_list;
    ArrayAdapter<String> adapter;
    ProgressBar loading_bar;
    Integer current_list_line, list_length, percent;
    Boolean start_flag = true, clear_flag = true, no_recent_games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chosen_list);

        initialize_fields();
        initialize_strings();

       // Toast.makeText(this, parent_intent.getStringExtra(ChooseNextStepActivity.EXTRA_MESSAGE), Toast.LENGTH_SHORT).show();

        fill_view();
        build_url(1);

        get_json_file(profile_url);
    }

    private void initialize_fields(){

        parent_intent = getIntent();

        profile_id = parent_intent.getStringExtra(ChooseNextStepActivity.EXTRA_MESSAGE);
        profile_name = parent_intent.getStringExtra(ChooseNextStepActivity.NAME_MESSAGE);

        name_message = (TextView) findViewById(R.id.name_message);
        percentage_message = (TextView) findViewById(R.id.percentage_message);
        chosen_list_message = (TextView) findViewById(R.id.choosen_list_message);
        total_count_message = (TextView) findViewById(R.id.total_count_message);
        total_message = (TextView) findViewById(R.id.total_message);

        chosen_list = (ListView) findViewById(R.id.choosen_list);

        loading_bar = (ProgressBar) findViewById(R.id.loading_bar);

        list_of_friends = new ArrayList<String>(Arrays.asList("Wait for list to load"));

        adapter = new ArrayAdapter<String>
                (this, R.layout.text_view_layout,android.R.id.text1, list_of_friends);

        list_mode = parent_intent.getIntExtra("View", 0);

        if(list_mode != 2) {
            chosen_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), adapter.getItem(position) + " playtime: "
                            + Integer.toString(games_hours[position] / 60) + " hours", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initialize_strings(){
        switch(list_mode){
            case 1:
                BASE_URL = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?" +
                        "key=6FA27B723BB28AFB78D820A2C4C6DBD6&include_appinfo=1&&steamid=";
                break;
            case 2:
                BASE_URL = "http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" +
                        "6FA27B723BB28AFB78D820A2C4C6DBD6&relationship=friend&steamid=";
                SECONDARY_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?" +
                        "key=6FA27B723BB28AFB78D820A2C4C6DBD6&steamids=";
                break;
            case 3:
                BASE_URL = "http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?" +
                        "key=6FA27B723BB28AFB78D820A2C4C6DBD6&count=10&steamid=";
        }
    }

    private void fill_view(){
        chosen_list.setAdapter(adapter);

        name_message.setText("Profile: " + profile_name);
        switch (list_mode){
            case 1:
                chosen_list_message.setText("Games list:");
                break;
            case 2:
                chosen_list_message.setText("Friend list:");
                break;
            case 3:
                chosen_list_message.setText("Recent games list:");
                break;
        }

    }

    private void build_url(int mode){
        StringBuilder profile_url_builder = new StringBuilder("");
        if(mode == 1) {
            profile_url_builder.append(BASE_URL);
            profile_url_builder.append(profile_id);
        }
        if(mode == 2) {
            profile_url_builder.append(SECONDARY_URL);
            profile_url_builder.append(friend_id);
        }
        profile_url = profile_url_builder.toString();
    }

    private void get_json_file(String json_url){
        new JSONTask().execute(json_url);
    }

    private void parse_json() throws JSONException {
        switch (list_mode){
            case 1:
                adapter.clear();
                for(int i = 0; i < list_length; i ++) {
                    JSONObject json_games_final_object = recieved_json_array.getJSONObject(i);
                    games_hours[i] = json_games_final_object.getInt("playtime_forever");
                    adapter.add(json_games_final_object.getString("name"));
                }
                total_message.setVisibility(View.VISIBLE);
                total_count_message.setVisibility(View.VISIBLE);
                total_count_message.setText(list_length.toString());
                break;
            case 2:
                current_list_line = 0;
                StringBuilder profile_url_builder = new StringBuilder("");
                profile_url_builder.append(SECONDARY_URL);
                for(int i = 0; i < list_length; i++){
                    profile_url_builder.append(recieved_json_array.getJSONObject(i).getString("steamid"));
                    if(i != list_length - 1)
                        profile_url_builder.append(",");
                }
                new JSONGetFriendName().execute(profile_url_builder.toString());
                break;
            case 3:
                adapter.clear();
                if(no_recent_games){
                    adapter.add("No recently played games");
                    break;
                }
                for(int i = 0; i < list_length; i ++) {
                    JSONObject json_games_final_object = recieved_json_array.getJSONObject(i);
                    games_hours[i] = json_games_final_object.getInt("playtime_forever");
                    adapter.add(json_games_final_object.getString("name"));
                }
                total_message.setVisibility(View.VISIBLE);
                total_count_message.setVisibility(View.VISIBLE);
                total_count_message.setText(list_length.toString());
                break;
        }
    }

    private void parse_friends() throws JSONException {
        adapter.clear();
        total_message.setVisibility(View.VISIBLE);
        total_count_message.setVisibility(View.VISIBLE);
        total_count_message.setText(list_length.toString());
        for(int i = 0; i < list_length; i ++){
            adapter.add(recieved_json_array.getJSONObject(i).getString("personaname"));
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
                switch(list_mode) {
                    case 1:
                        String json_games_info = buffer.toString();
                        JSONObject json_games_object = new JSONObject(json_games_info);
                        JSONObject json_games_list = json_games_object.getJSONObject("response");
                        JSONArray json_games = json_games_list.getJSONArray("games");
                        recieved_json_array = json_games;
                        list_length = json_games.length();
                        games_hours = new int[list_length];
                        return null;
                    case 2:
                        String json_friends_info = buffer.toString();
                        JSONObject json_friends_object = new JSONObject(json_friends_info);
                        JSONObject json_friends_list = json_friends_object.getJSONObject("friendslist");
                        JSONArray json_friends = json_friends_list.getJSONArray("friends");
                        recieved_json_array = json_friends;
                        list_length = json_friends.length();
                        games_hours = new int[list_length];
                        return null;
                    case 3:
                        String json_recent_info = buffer.toString();
                        JSONObject json_recent_object = new JSONObject(json_recent_info);
                        JSONObject json_recent_list = json_recent_object.getJSONObject("response");
                        if(json_recent_list.getInt("total_count") != 0) {
                            JSONArray json_recent_games = json_recent_list.getJSONArray("games");
                            recieved_json_array = json_recent_games;
                            list_length = json_recent_games.length();
                            games_hours = new int[list_length];
                            no_recent_games = false;
                            return null;
                        }
                        else{
                            no_recent_games = true;
                            return null;
                        }
                }
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                parse_json();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

                recieved_json_array = json_players;
                list_length = json_players.length();
                return null;

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
            try {
                parse_friends();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

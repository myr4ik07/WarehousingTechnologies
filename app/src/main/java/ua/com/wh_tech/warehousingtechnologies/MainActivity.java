package ua.com.wh_tech.warehousingtechnologies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String urlAddress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        urlAddress = geturlAddress();
        MyDownloadTask myDownloadTask = new MyDownloadTask();
        myDownloadTask.execute(urlAddress);

    }

    String geturlAddress() {
        SharedPreferences settings;
        settings = getSharedPreferences("URL_Address_Connect", MODE_PRIVATE);
        String url_address = settings.getString("URL_Address_Connect", "");

        return url_address;
    }

    public void In(View view) {

    }

    public void preferences(View view) {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }

    void print(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private class MyDownloadTask extends AsyncTask<String, String, String> {

        public ArrayList userList = new ArrayList<>();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, userList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = (Spinner) findViewById(R.id.usr_list);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }

            });

        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(strings[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                if (connection.getResponseCode() != 200) {
                    Log.d("ERROR", connection.getRequestMethod());
                    return null;
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                JSONArray mJsonArray = new JSONArray(sb.toString());
                for (int x = 0; x < mJsonArray.length(); x++) {
                    JSONObject jsonObject = mJsonArray.getJSONObject(x);
                    userList.add(jsonObject.getString("login"));
                }

                br.close();
                connection.disconnect();

                return null;

            } catch (Exception e) {
                return e.toString();
            }

        }

    }

}

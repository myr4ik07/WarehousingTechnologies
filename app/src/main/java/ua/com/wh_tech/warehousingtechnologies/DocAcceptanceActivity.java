package ua.com.wh_tech.warehousingtechnologies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DocAcceptanceActivity extends AppCompatActivity {

    String urlAddress;
    String pathAddress = "/docacceptance/get/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_acceptance);

        urlAddress = geturlAddress();
        GetUserList getUserList = new GetUserList();
        getUserList.execute(urlAddress+pathAddress);
    }

    private String geturlAddress() {
        SharedPreferences settings;
        settings = getSharedPreferences("URL_Address_Connect", MODE_PRIVATE);
        String url_address = settings.getString("URL_Address_Connect", "");

        return url_address;
    }

    private class GetUserList extends AsyncTask<String, String, String> {

        public ArrayList arrayList = new ArrayList<>();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DocAcceptanceActivity.this, android.R.layout.select_dialog_item, arrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ListView listView = (ListView) findViewById(R.id.list_doc);
            listView.setAdapter(adapter);

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
                    arrayList.add("№ "+jsonObject.getString("numberDoc") +" от "+jsonObject.getString("dateDoc"));
                }

                br.close();
                connection.disconnect();

                return null;

            } catch (Exception e) {
                return e.toString();
            }

        }

    }

    public void rowClick(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}

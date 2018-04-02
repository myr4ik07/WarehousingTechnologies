package ua.com.wh_tech.warehousingtechnologies.Logic;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyDownloadTask extends AsyncTask<String, String, String> {

    public ArrayList userList = new ArrayList<>();

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
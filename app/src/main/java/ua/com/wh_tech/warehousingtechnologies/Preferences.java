package ua.com.wh_tech.warehousingtechnologies;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Preferences extends AppCompatActivity {

    SharedPreferences settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        backupPreferences();
    }

    void backupPreferences() {
        settings = getSharedPreferences("URL_Address_Connect", MODE_PRIVATE);
        EditText url = (EditText) findViewById(R.id.url);
        String url_address = settings.getString("URL_Address_Connect", "");
        url.setText(url_address);
    }

    public void savePreferences(View view) {
        EditText url = (EditText) findViewById(R.id.url);
        String url_address = url.getText().toString();

        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString("URL_Address_Connect", url_address);
        prefEditor.apply();
    }

}

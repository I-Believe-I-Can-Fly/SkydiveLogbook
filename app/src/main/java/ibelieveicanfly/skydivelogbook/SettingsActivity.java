package ibelieveicanfly.skydivelogbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_CODE = "countryCode";
    private static final String KEY_N = "isCheked";
    private static final String KEY_L = "languageKey";
    private Switch switch_Notification;
    private Spinner spinner_Languages;
    private Button btn_Languages;
    private boolean notificationOn;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String[] languages = {"English", "Norwegian"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        switch_Notification = findViewById(R.id.switch_Notifications);
        spinner_Languages = findViewById(R.id.spinner_Languages);
        btn_Languages = findViewById(R.id.btn_Languages);
        preferences = getPreferences(0);

        spinner_Languages.setAdapter(adapter);

        setNotificationChoice();
        getNotificationChoice();

        setLanguage();
        getLanguage();
    }

    private void setNotificationChoice() {
        notificationOn = preferences.getBoolean(KEY_N, true);

        if (notificationOn) {
            switch_Notification.setChecked(true);
        } else {
            switch_Notification.setChecked(false);
        }
    }

    private void getNotificationChoice() {

        switch_Notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (switch_Notification.isChecked()) {
                    notificationOn = true;
                } else {
                    notificationOn = false;
                }

                editor = getPreferences(0).edit();
                editor.putBoolean(KEY_N, notificationOn);
                editor.apply();
            }
        });
    }

    private void setLanguage() {
        int position = preferences.getInt(KEY_L, 0);

        spinner_Languages.setSelection(position);
    }

    private void getLanguage() {

        btn_Languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = spinner_Languages.getSelectedItemPosition();
                String selection = spinner_Languages.getSelectedItem().toString();

                editor = getPreferences(0).edit();
                editor.putInt(KEY_L, position);
                editor.apply();

                switchLanguage(selection);
            }
        });
    }

    // https://stackoverflow.com/questions/12908289/how-to-change-language-of-app-when-user-selects-language
    private void switchLanguage(String language) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        String countryCode;

        // Get language code and save it
        switch (language) {
            case "English":
                countryCode = "en";
                break;
            case "Norwegian":
                countryCode = "nb";
                break;
            default:
                countryCode = "en";
                break;
        }

        // Save language TODO : Remember what language is set, and change to that language
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editorToMain = preferences.edit();

        editorToMain.putString(KEY_CODE, countryCode);
        editorToMain.apply();

        // Set language
        configuration.locale = new Locale(countryCode);
        getResources().updateConfiguration(configuration, metrics);

        // Refresh activity
        Intent refresh = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(refresh);
        finish();
    }

    // Use this from other activities to check if true or false
    public boolean getNotification() {
        return switch_Notification.isChecked();
    }
}

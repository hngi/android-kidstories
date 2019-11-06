package com.project.android_kidstories.ui.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.receivers.AlarmReceiver;
import com.project.android_kidstories.ui.base.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsActivity extends BaseActivity {

    private static final String ALARM_TIME = "ALARM_TIME";

    SharePref sharePref;

    TextView timeTextview;

    //    SharePref sharePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Set up toolbar
        Toolbar settingsToolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharePref = getSharePref();

        Switch nightSwitch = findViewById(R.id.night_switch);
        Log.d("XXX night", String.valueOf(getSharePref().getNightMode()));

        if (sharePref.getNightMode()) {
            nightSwitch.setChecked(true);
        }

//        sharePref = SharePref.getINSTANCE(SettingsActivity.this);
        timeTextview = findViewById(R.id.timetext_settings);

        String timeStr = getSharePref().getString(ALARM_TIME);
        if (TextUtils.isEmpty(timeStr)) {
            timeStr = "8:00 PM";
        }
        timeTextview.setText(timeStr);


        nightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            sharePref.setNightMode(b);
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

    }

    public void reminderClicked(View view) {
        String timeText = timeTextview.getText().toString();
        String[] timeSplit = timeText.replace("PM", "")
                .replace("AM", "")
                .trim()
                .split(":");

        String hourString = timeSplit[0];
        String minuteString = timeSplit[1];

        int hour = Integer.valueOf(hourString);
        int min = Integer.valueOf(minuteString);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                Date d = new Date(calendar.getTimeInMillis());
                DateFormat format = new SimpleDateFormat("h:mm a", Locale.US);
                timeTextview.setText(
                        format.format(d)
                );

                getSharePref().setString(ALARM_TIME, format.format(d));

                setAlarm(i, i1);
            }
        }, hour, min, false);
        timePickerDialog.setTitle("Daily Reminder Time");
        timePickerDialog.show();
    }

    private void setAlarm(int hour, int minute) {
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        try {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("Alarm", "Saved Alarm");
        } catch (NullPointerException npe) {
            Toast.makeText(this, "Could not create reminder", Toast.LENGTH_SHORT).show();
            Log.d("Alarm", "Could not save Alarm");
        }

    }

    // Method to display the privacy policy
    public void openPrivacyPolicyPage(View view) {
        String policyUrl = "http://kidstories.app//privacy-policy";
        String packageName = "com.android.chrome";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);

        customTabsIntent.launchUrl(SettingsActivity.this, Uri.parse(policyUrl));
    }
}

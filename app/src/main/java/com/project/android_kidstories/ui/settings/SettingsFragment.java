package com.project.android_kidstories.ui.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.source.local.preferences.PreferenceKeys;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.receivers.AlarmReceiver;
import com.project.android_kidstories.ui.KidstoriesApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharePref sharePref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.kidstories_preference, rootKey);

        sharePref = SharePref.getInstance(requireActivity().getApplication());

        SwitchPreferenceCompat nightModeSwitch = findPreference("night_mode");
        if (nightModeSwitch != null) {
            if (sharePref.getNightMode()) nightModeSwitch.setChecked(true);
            nightModeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isNightMode = (boolean) newValue;
                Log.d("GLOBAL_SCOPE", String.valueOf(isNightMode));
                sharePref.setNightMode(isNightMode);
                KidstoriesApplication.changeMode(isNightMode);
                return true;
            });
        }

        Preference ppolicy = findPreference("privacy_policy");
        if (ppolicy != null) {
            ppolicy.setOnPreferenceClickListener(preference -> {
                openPrivacyPolicyPage();
                return true;
            });
        }

        Preference reminderTime = findPreference("reminder_time");
        if (reminderTime != null) {
            reminderTime.setOnPreferenceClickListener(preference -> {
                reminderClicked(reminderTime.getSummary().toString());
                return true;
            });
        }
    }


    // Method to display the privacy policy
    private void openPrivacyPolicyPage() {
        String policyUrl = "http://kidstories.app//privacy-policy";
        String packageName = "com.android.chrome";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);

        customTabsIntent.launchUrl(requireContext(), Uri.parse(policyUrl));
    }

    private void reminderClicked(String summary) {
        String newTime = summary;
        String[] timeSplit = summary.replace("PM", "")
                .replace("AM", "")
                .trim()
                .split(":");

        String hourString = timeSplit[0];
        String minuteString = timeSplit[1];

        int hour = Integer.valueOf(hourString);
        int min = Integer.valueOf(minuteString);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                Date d = new Date(calendar.getTimeInMillis());
                DateFormat format = new SimpleDateFormat("h:mm a", Locale.US);
                //newTime = format.format(d);

                sharePref.setString(PreferenceKeys.ALARM_TIME, format.format(d));
                setAlarm(i, i1);
            }
        }, hour, min, false);
        timePickerDialog.setTitle("Daily Reminder Time");
        timePickerDialog.show();
    }

    private void setAlarm(int hour, int minute) {
        AlarmManager am = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        try {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("Alarm", "Saved Alarm");
        } catch (NullPointerException npe) {
            Toast.makeText(getContext(), "Could not create reminder", Toast.LENGTH_SHORT).show();
            Log.d("Alarm", "Could not save Alarm");
        }

    }

}

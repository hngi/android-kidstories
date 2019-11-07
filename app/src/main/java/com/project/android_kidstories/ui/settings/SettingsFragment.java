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
import android.util.Pair;
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

    private static final String DEFAULT_ALARM_TIME = "8:00PM";
    private SharePref sharePref;

    private Preference reminderTime;

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

        reminderTime = findPreference("reminder_time");
        if (reminderTime != null) {
            reminderTime.setOnPreferenceClickListener(preference -> {
                reminderClicked(reminderTime.getSummary().toString());
                return true;
            });
        }

        String chosenTime = sharePref.getString(PreferenceKeys.ALARM_TIME);
        if (TextUtils.isEmpty(chosenTime)) chosenTime = DEFAULT_ALARM_TIME;
        reminderTime.setSummary(chosenTime);

        SwitchPreferenceCompat reminderOn = findPreference("reminder_on");
        if (reminderOn != null && reminderTime != null) {
            reminderOn.setOnPreferenceChangeListener(((preference, newValue) -> {
                boolean isOn = (boolean) newValue;
                if (isOn) {
                    Pair<Integer, Integer> timePair = splitTime(reminderTime.getSummary().toString());
                    setAlarm(timePair.first, timePair.second);
                } else {
                    unRegisterAlarm();
                }
                return true;
            }));
        }

    }

    private Pair<Integer, Integer> splitTime(String timeStr) {
        String[] timeSplit = reminderTime.getSummary().toString()
                .replace("PM", "")
                .replace("AM", "")
                .trim()
                .split(":");
        String hourString = timeSplit[0];
        String minuteString = timeSplit[1];

        return new Pair<>(Integer.valueOf(hourString), Integer.valueOf(minuteString));
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

    private PendingIntent getAlarmIntent() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        return PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void unRegisterAlarm() {
        AlarmManager am = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (am != null) am.cancel(getAlarmIntent());
    }

    private void reminderClicked(String summary) {
        Pair<Integer, Integer> timePair = splitTime(summary);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i, i1) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, i1);
            Date d = new Date(calendar.getTimeInMillis());
            DateFormat format = new SimpleDateFormat("h:mm a", Locale.US);

            reminderTime.setSummary(format.format(d));
            sharePref.setString(PreferenceKeys.ALARM_TIME, format.format(d));

            setAlarm(i, i1);
        }, timePair.first, timePair.second, false);

        timePickerDialog.setTitle("Daily Reminder Time");
        timePickerDialog.show();
    }

    private void setAlarm(int hour, int minute) {
        AlarmManager am = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        try {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getAlarmIntent());
            Log.d("Alarm", "Saved Alarm");
        } catch (NullPointerException npe) {
            Toast.makeText(getContext(), "Could not create reminder", Toast.LENGTH_SHORT).show();
            Log.d("Alarm", "Could not save Alarm");
        }

    }

}

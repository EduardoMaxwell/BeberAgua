package br.com.eduardomaxwell.beberagua;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import br.com.eduardomaxwell.beberagua.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int hour;
    private int minute;
    private int interval;
    private boolean activated;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot().getRootView());
        setHourTo24h();
        getPreferences();
        setListeners();

    }


    private void setListeners() {
        binding.btnNotify.setOnClickListener(view -> this.setupNotify());
    }

    private void setupNotify() {
        String sInterval = binding.edtNumberInterval.getText().toString();

        if (sInterval.isEmpty()) {
            Snackbar.make(binding.getRoot(), R.string.error_empty_field, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.design_default_color_error))
                    .show();
            return;
        }

        hour = binding.timePicker.getCurrentHour();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            minute = binding.timePicker.getMinute();
        interval = Integer.parseInt(sInterval);

        if (!activated) {
            binding.btnNotify.setText(R.string.btn_pause);
            binding.btnNotify.setBackgroundColor(getResources().getColor(android.R.color.black));
            this.saveSharedPreferences();
            activated = true;
        } else {
            binding.btnNotify.setText(R.string.notify);
            binding.btnNotify.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.deleteSharedPreferences();
            activated = false;
        }

        Snackbar.make(binding.getRoot(), "Hora: " + hour + " Minute: " + minute + " Interval: " + interval,
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(android.R.color.holo_green_light))
                .show();

    }

    private void setHourTo24h() {
        binding.timePicker.setIs24HourView(true);
    }

    private void getPreferences() {
        preferences = getSharedPreferences("database", Context.MODE_PRIVATE);
        activated = preferences.getBoolean("activated", false);

        if (this.activated) {
            binding.btnNotify.setText(R.string.btn_pause);
            binding.btnNotify.setBackgroundColor(getResources().getColor(android.R.color.black));

            int interval = preferences.getInt("interval", 0);
            int hour = preferences.getInt("hour", binding.timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", binding.timePicker.getCurrentMinute());

            binding.edtNumberInterval.setText(String.valueOf(interval));
            binding.timePicker.setCurrentHour(hour);
            binding.timePicker.setCurrentMinute(minute);
        }
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activated", true);
        editor.putInt("interval", interval);
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.apply();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION, "Hora de beber água.");

        PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + (interval * 60 * 1000L);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval * 60 * 1000L, broadcast);
    }

    private void deleteSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activated", false);
        editor.remove("interval");
        editor.remove("hour");
        editor.remove("minute");
        editor.apply();

        Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(broadcast);

    }
}
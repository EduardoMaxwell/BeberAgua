package br.com.eduardomaxwell.beberagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

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
            activated = true;
            this.saveSharedPreferences();
        } else {
            binding.btnNotify.setText(R.string.notify);
            binding.btnNotify.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            activated = false;
            this.deleteSharedPreferences();
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
    }

    private void deleteSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activated", false);
        editor.remove("interval");
        editor.remove("hour");
        editor.remove("minute");
        editor.apply();
    }
}
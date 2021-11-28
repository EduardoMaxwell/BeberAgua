package br.com.eduardomaxwell.beberagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import br.com.eduardomaxwell.beberagua.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int hour;
    private int minute;
    private int interval;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot().getRootView());
        setHourTo24h();
//        setListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setListeners() {

        binding.notify.setOnClickListener(view -> {
            String sInterval = binding.edtNumberInterval.getText().toString();
            hour = binding.timePicker.getCurrentHour();
            minute = binding.timePicker.getMinute();
            interval = Integer.parseInt(sInterval);

            Log.d("TAG", "Hora: " + hour + " Minute: " + minute + " Interval: " + interval);
            Snackbar.make(view, "Hora: " + hour + " Minute: " + minute + " Interval: " + interval, Snackbar.LENGTH_LONG).show();
        });

        setHourTo24h();
    }

    public void notify(View view) {
        String sInterval = binding.edtNumberInterval.getText().toString();
        hour = binding.timePicker.getCurrentHour();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            minute = binding.timePicker.getMinute();

        interval = Integer.parseInt(sInterval);

        Log.d("TAG", "Hora: " + hour + " Minute: " + minute + " Interval: " + interval);
        Snackbar.make(view, "Hora: " + hour + " Minute: " + minute + " Interval: " + interval, Snackbar.LENGTH_LONG).show();

    }

    private void setHourTo24h() {
        binding.timePicker.setIs24HourView(true);
    }


}
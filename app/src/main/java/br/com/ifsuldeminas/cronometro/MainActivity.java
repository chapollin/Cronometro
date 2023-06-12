package br.com.ifsuldeminas.cronometro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvElapsedTime;
    private Button btnStartPause;
    private Button btnReset;
    private boolean isRunning;
    private long startTime;
    private long elapsedTime;
    private long pausedTime;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvElapsedTime = findViewById(R.id.tvElapsedTime);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnReset = findViewById(R.id.btnReset);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateElapsedTime();
                handler.postDelayed(this, 100);
            }
        };

        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
            btnStartPause.setText("Pausar");
            btnReset.setVisibility(View.INVISIBLE);
            handler.postDelayed(runnable, 0);
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            isRunning = false;
            btnStartPause.setText("Continuar");
            btnReset.setVisibility(View.VISIBLE);
            pausedTime = elapsedTime; // Armazena o tempo decorrido atual
            handler.removeCallbacks(runnable);
        }
    }

    private void resetTimer() {
        isRunning = false;
        elapsedTime = 0;
        pausedTime = 0;
        updateElapsedTime();
        btnReset.setVisibility(View.INVISIBLE);
    }

    private void updateElapsedTime() {
        if (isRunning) {
            elapsedTime = System.currentTimeMillis() - startTime + pausedTime;
        }
        int minutes = (int) (elapsedTime / 1000) / 60;
        int seconds = (int) (elapsedTime / 1000) % 60;
        int milliseconds = (int) (elapsedTime % 1000);
        String formattedTime = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
        tvElapsedTime.setText(formattedTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}

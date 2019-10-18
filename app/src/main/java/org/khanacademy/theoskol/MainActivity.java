package org.khanacademy.theoskol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Screen.setScreenDims(getWindowManager());
        GameCode.init();
        gameView = findViewById(R.id.gameView);
        startMainThread();
    }
    private void startMainThread() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    long previousMillis = System.currentTimeMillis();
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameView.draw();
                            }
                        });
                        Thread.sleep(Math.abs(previousMillis - System.currentTimeMillis() + 50 / 3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
}

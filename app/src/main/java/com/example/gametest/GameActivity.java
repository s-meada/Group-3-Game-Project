package com.example.gametest;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // give the point object the size of the screen in the x and y coordinates
        Point point  = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        // give the gameView the size of the screen
        gameView = new GameView(this, point.x, point.y);
        setContentView(gameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // pause the game
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // resume the game
        gameView.resume();
    }


}

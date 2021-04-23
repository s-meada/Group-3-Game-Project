package com.example.gametest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private GameActivity activity;
    private SharedPreferences prefs;
    private int screenX;
    private int screenY;
    private Background background1, background2;
    Paint paint;
    Car car;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        this.screenX = screenX;
        this.screenY = screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        car = new Car(200, 300, R.drawable.carblue, getResources());

        background2.y = -screenY;

        // paint object that will draw everything onto the canvas
        paint = new Paint();
    }


    @Override
    public void run() {

        while(isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        // background moves
        background1.y += 10;
        background2.y += 10;

        // if one of the backgrounds moves completely off the screen, put it on the other side of the other background
        if (background1.y > background1.background.getHeight()) {
            background1.y = -screenY;
        }

        if (background2.y > background2.background.getHeight()) {
            background2.y = -screenY;
        }

    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            canvas.drawBitmap(car.car, car.x, car.y, paint);

            // post canvas on the screen
            getHolder().unlockCanvasAndPost(canvas);
        }
    }


    // thread methods

    private void sleep () {
        try {
            // 1000 ms / 17 ms = 60  using this sleep() will give us 60 fps
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            // stops the thread
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

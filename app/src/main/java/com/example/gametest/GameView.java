package com.example.gametest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private GameActivity activity;
    private SharedPreferences prefs;
    private int screenX;
    private int screenY;
    private Background background1, background2;
    private Obstacle[] obstacles;
    private Random random;
    private SoundPool soundPool;
    private int sound;
    private Paint paint;
    private long t0=0;
    private long score;
    private Car car;
    private MediaPlayer mediaPlayer;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        isPlaying = true;
        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        this.screenX = screenX;
        this.screenY = screenY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        car = new Car(400, 700, R.drawable.carblue, getResources());
        background2.y = -screenY;

        // paint object that will draw everything onto the canvas
        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        // number of obstacles on screen
        obstacles = new Obstacle[4];

        // assign each index of the obstacles array to an obstacle
        random = new Random();

        for (int i = 0; i < obstacles.length; i++) {
            // can be edited with a random image
            obstacles[i] = new Obstacle(R.drawable.log, getResources());
            obstacles[i].x = random.nextInt(screenX - obstacles[i].width);
            // obstacles start off of the screen and will get to the screen at a random time
            obstacles[i].y = -(random.nextInt(screenY) + obstacles[i].height);
            // System.out.println("OBSTACLE: " + obstacles[i].x + ", " + obstacles[i].y);
        }
        t0 = System.nanoTime();
        // sets up short sounds to be played, instantiating soundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        // sets the boom sound ready
        sound = soundPool.load(activity, R.raw.boom, 1);
        // instantiates and plays bgm
        mediaPlayer = MediaPlayer.create(activity, R.raw.bgm);
        mediaPlayer.start();

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
        long t1 = System.nanoTime();
        score = (long) ((t1-t0) * 0.00000001);
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

        // updating every obstacle
        for (Obstacle obstacle : obstacles) {

            // for obstacles that move off the screen
            if(obstacle.y > screenY) {
                // for obstacles that move off the screen
            if(obstacle.y > screenY) {
                // make a new obstacle on the screen
                //obstacle.y = -(random.nextInt(50) + obstacle.height);
                obstacle.y = -(random.nextInt(screenY));
                //obstacle.x = random.nextInt(screenY - obstacle.height);
                obstacle.x = random.nextInt(screenX - obstacle.width);
            }
            }

            // update movement of obstacle (same speed as car)
            obstacle.y += 10;

            // check for collisions between car and obstacles
            if(Rect.intersects(car.getCollisionShape(), obstacle.getCollisionShape())) {
                // stops bgm
                mediaPlayer.stop();
                // play boom sound
                soundPool.play(sound, 100, 100, 1, 0, 1);
                // end the game if the car collides with an obstacle
                isGameOver = true;
                break;
            }

        }

    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            // draw objects on the screen

            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(car.car, car.x, car.y, paint);

            // draw obstacles
            for(Obstacle obstacle : obstacles) {
                if(obstacle.y >= 0) {
                    canvas.drawBitmap(obstacle.image, obstacle.x, obstacle.y, paint);
                }
            }
            canvas.drawText(String.valueOf(score),300,200, paint);

            // when the game is over
            if(isGameOver) {
                isPlaying = false;
                // [death animation]
                canvas.drawBitmap(background1.background, 0, 0, paint);
                canvas.drawText("Game Over", screenX/2 - 250, screenY/2, paint);
                paint.setTextSize(75);
                canvas.drawText("Score: " + score, screenX/2 - 175, screenY/2 + 100, paint);
                System.out.println("GAME OVER!");
                waitBeforeExiting();
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }
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

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
//            activity.startActivity(new Intent(activity, MainActivity.class));
            // Finish the game activity
//            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    // method gets called whenever the screen is touched
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // press down
            case MotionEvent.ACTION_DOWN:
                if (event.getX() >= car.x && event.getX() <= car.x + car.width) {
                    car.setActionDown(true);
                }
                break;
            // finger moved along screen
            case MotionEvent.ACTION_MOVE:
                if(car.getActionDown()) {
                    car.setPosition(event.getX(), car.y);
                }
                break;
            // finger released
            case MotionEvent.ACTION_UP:
                car.setActionDown(false);
                break;
        }
        return true;
    }

}

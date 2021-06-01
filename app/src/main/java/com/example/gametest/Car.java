package com.example.gametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Car {
    float x, y, width, height;
    private boolean actionDown = false;
    Bitmap car;


    /**
     *
     * @param x x coordinate on the screen
     * @param y y coordinate on the screen
     * @param id The image "path" (e.g. R.drawable.car)
     * @param res resource
     */
    Car(int x, int y, int id, Resources res) {
        this.x = x;
        this.y = y;

        car = BitmapFactory.decodeResource(res, id);

        width = car.getWidth();
        height = car.getHeight();
        // reduce the car's size
        width /= 4;
        height /= 4;

        car = Bitmap.createScaledBitmap(car, (int) width, (int) height, false);
    }
    public void setActionDown(boolean actionDown) {
        this.actionDown = actionDown;
    }
    Rect getCollisionShape () {
        return new Rect((int)x, (int)y + 42, (int)x + (int) width - 30, (int)y + (int)height - 10);
    }

    public boolean getActionDown() {
        return actionDown;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

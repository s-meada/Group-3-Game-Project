package com.example.gametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Car {
    int x, y, width, height;

    Bitmap car;


    /**
     *
     * @param x x coordinate on the screen
     * @param y y coordinate on the screen
     * @param id The image "path" (e.g. R.drawable.car)
     * @param res
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

        car = Bitmap.createScaledBitmap(car, width, height, false);
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}

package com.example.gametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Obstacle {

    public int x, y, width, height;
    Bitmap image;


    /**
     *
     * @param x x coordinate on the screen
     * @param y y coordinate on the screen
     * @param id The image "path" (e.g. R.drawable.obstacle)
     * @param res
     */
    public Obstacle (int x, int y, int id, Resources res) {
        this(id, res);

        this.x = x;
        this.y = y;
    }

    // Only use if you intend to assign the other values later
    public Obstacle (int id, Resources res) {
        image = BitmapFactory.decodeResource(res, id);

        width = image.getWidth();
        height = image.getHeight();
        // reduce the obstacle's size
        width /= 4;
        height /= 6;

        image = Bitmap.createScaledBitmap(image, width, height, false);

    }

    public Rect getCollisionShape () {
        return new Rect(x + 30, y + 20, x + width - 50, y + height - 50);
    }

}

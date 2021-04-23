package com.example.gametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Obstacle {

    public int x, y;
    Bitmap image;

    /**
     *
     * @param x x coordinate on the screen
     * @param y y coordinate on the screen
     * @param id The image "path" (e.g. R.drawable.obstacle)
     * @param res
     */
    Obstacle (int x, int y, int width, int height, int id, Resources res) {
        image = BitmapFactory.decodeResource(res, id);

    }
}

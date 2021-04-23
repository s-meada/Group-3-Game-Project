package com.example.gametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0, y = 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res) {
        // TODO: Add a background image named background in the drawable folder
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        // scale background to the screen size
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        System.out.println(screenX + ", " + screenY);
    }
}

package com.example.nathan.tinkercontroller;

import android.graphics.Color;

public class Utility {
        public static int getComplimentColor(int color) {
            // get existing colors
            int alpha = Color.alpha(color);
            int red = Color.red(color);
            int blue = Color.blue(color);
            int green = Color.green(color);

            // find compliments
            red = (~red) & 0xff;
            blue = (~blue) & 0xff;
            green = (~green) & 0xff;

            return Color.argb(alpha, red, green, blue);
        }

    public static float mapFloat(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}

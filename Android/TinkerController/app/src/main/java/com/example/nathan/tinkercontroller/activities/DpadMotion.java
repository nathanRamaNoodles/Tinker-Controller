package com.example.nathan.tinkercontroller.activities;

import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class DpadMotion {
final static int CENTER = KeyEvent.KEYCODE_DPAD_CENTER;
    final static int LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
    final static int UP = KeyEvent.KEYCODE_DPAD_UP;
    final static int RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
    final static int DOWN = KeyEvent.KEYCODE_DPAD_DOWN;

    int directionPressed = -1; // initialized to -1

    public int getDirectionPressed(InputEvent event) {

        // If the input event is a MotionEvent, check its hat axis values.
        if (event instanceof MotionEvent) {

            // Use the hat axis value to find the D-pad direction
            MotionEvent motionEvent = (MotionEvent) event;
            float xaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
            float yaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);

            // Check if the AXIS_HAT_X value is -1 or 1, and set the D-pad
            // LEFT and RIGHT direction accordingly.
            if (Float.compare(xaxis, -1.0f) == 0) {
                directionPressed = LEFT;
            } else if (Float.compare(xaxis, 1.0f) == 0) {
                directionPressed = RIGHT;
            }
            // Check if the AXIS_HAT_Y value is -1 or 1, and set the D-pad
            // UP and DOWN direction accordingly.
            else if (Float.compare(yaxis, -1.0f) == 0) {
                directionPressed = UP;
            } else if (Float.compare(yaxis, 1.0f) == 0) {
                directionPressed = DOWN;
            } else if ((Float.compare(xaxis, 0.0f) == 0)
                    && (Float.compare(yaxis, 0.0f) == 0)) {
                directionPressed = CENTER;
            }
        }
        return directionPressed;
    }
}
package com.example.nathan.tinkercontroller.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nathan.tinkercontroller.R;
import com.example.nathan.tinkercontroller.Utility;
import com.example.nathan.tinkercontroller.activities.GamepadActivity;
import com.example.nathan.tinkercontroller.ClientStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.nathan.tinkercontroller.BuildConfig.DEBUG;


public class Gamepad_Joystick_Fragment extends CyaneaFragment implements GamepadActivity.JoystickListener, Esp8266ClientAsyncFragment.esp8266ClientListener {
    //Tag for Logging
    private final String TAG = "myTag";

    private final static String ssidName = "ESPTest";
    private final static String IP_ADDRESS = "192.168.4.1";
    private final static int PORT = 4210;
    private final static String TAG_TASK_FRAGMENT = "task_fragment";

    private Toast mToast;
    private ClientStatus mClientStatus;
    private Esp8266ClientAsyncFragment mAsyncFragment;

    private Unbinder unbinder;

    //Connect to WiFi button
    @BindView(R.id.wifi_connect_fab)
    FloatingActionButton mConnectFab;
    //Toggle button for people who don't have a controller, but just want to test out the app
    @BindView(R.id.toggle_led)
    ImageView mToggleBtn;
    //Used to show if we are connected to esp8266
    @BindView(R.id.Toggle_background)
    ImageView mStatusWiFi;

    //right joystick
    @BindView(R.id.right_outer_edge_joystick)
    ImageView mJoystickRight;
    @BindView(R.id.right_inner_edge_joystick)
    ImageView mJoystickRight_inner;
    @BindView(R.id.right_outline_joystick)
    ImageView mJoystickRight_outline;

    //Left joystick
    @BindView(R.id.left_outer_edge_joystick)
    ImageView mJoystickLeft;
    @BindView(R.id.left_inner_edge_joystick)
    ImageView mJoystickLeft_inner;
    @BindView(R.id.left_outline_joystick)
    ImageView mJoystickLeft_outline;

    //Back Button
    @BindView(R.id.button_back)
    ImageView mBackButton;

    //Select/Menu Button
    @BindView(R.id.button_menu)
    ImageView mMenuButton;

    //X Button
    @BindView(R.id.button_left)
    ImageView mButtonX;
    //Y Button
    @BindView(R.id.button_up)
    ImageView mButtonY;
    //B Button
    @BindView(R.id.button_right)
    ImageView mButtonB;
    //A Button
    @BindView(R.id.button_down)
    ImageView mButtonA;

    //D-Pad left
    @BindView(R.id.dPad_left)
    ImageView mDpadLeft;
    //D-Pad Up
    @BindView(R.id.dPad_up)
    ImageView mDpadUp;
    //D-Pad Right
    @BindView(R.id.dPad_right)
    ImageView mDpadRight;
    //D-Pad Down
    @BindView(R.id.dPad_down)
    ImageView mDpadDown;

    //Left Trigger
    @BindView(R.id.trigger_left)
    ImageView mTriggerLeft;
    //Right Trigger
    @BindView(R.id.trigger_right)
    ImageView mTriggerRight;
    //Analog Left trigger
    @BindView(R.id.analog_trigger_left)
    ImageView mAnalogTriggerLeft;
    //Analog Right trigger
    @BindView(R.id.analog_trigger_right)
    ImageView mAnalogTriggerRight;

    private int[] joystickLeft_CenterLocation = new int[6];
    private int[] joystickRight_CenterLocation = new int[6];
    private int dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_CENTER;
    private int brightStandOutColor = Utility.getComplimentColor(getCyanea().getAccent());
    private final static String CONNECTED_TO_WIFI = "current_wifi_status";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mClientStatus = (ClientStatus) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MyInterface ");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_gamepad_joystick, container, false);
        unbinder = ButterKnife.bind(this, v);
        v.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        //Fetch our initial center coordinates for our joysticks
                        joystickLeft_CenterLocation[0] = mJoystickLeft.getLeft();
                        joystickLeft_CenterLocation[1] = mJoystickLeft.getTop();
                        joystickLeft_CenterLocation[2] = mJoystickLeft_outline.getLeft();
                        joystickLeft_CenterLocation[3] = mJoystickLeft_outline.getTop();
                        joystickLeft_CenterLocation[4] = mJoystickLeft_inner.getLeft();
                        joystickLeft_CenterLocation[5] = mJoystickLeft_inner.getTop();

                        joystickRight_CenterLocation[0] = mJoystickRight.getLeft();
                        joystickRight_CenterLocation[1] = mJoystickRight.getTop();
                        joystickRight_CenterLocation[2] = mJoystickRight_outline.getLeft();
                        joystickRight_CenterLocation[3] = mJoystickRight_outline.getTop();
                        joystickRight_CenterLocation[4] = mJoystickRight_inner.getLeft();
                        joystickRight_CenterLocation[5] = mJoystickRight_inner.getTop();

                        // Don't forget to remove your listener when you are done with it.
                        if (Build.VERSION.SDK_INT < 16) {
                            v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        //Setup up our interface listeners
        ((GamepadActivity) getActivity()).setJoystickListener(this);


        mConnectFab.setOnClickListener(v1 -> {
            toggleWifi();
        });

        mToggleBtn.setOnTouchListener((v12, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mToggleBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_radio_button_checked_black_24dp));
                if (mAsyncFragment != null) {
                    mAsyncFragment.sendMessage("a=1", 0);
                }
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mToggleBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_album_black_24dp));
                if (mAsyncFragment != null) {
                    mAsyncFragment.sendMessage("a=0", 0);
                }
                return true;
            }
            return false;
        });


        // Restore saved state.
        if (savedInstanceState != null) {
            boolean connectedToWiFi = savedInstanceState.getBoolean(CONNECTED_TO_WIFI, false);
            if (connectedToWiFi) {
                onConnectedListener(false);
            } else {
                onDisconnectListener(false);
            }
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mAsyncFragment = (Esp8266ClientAsyncFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If we haven't retained the worker fragment, then create it
        // and set this UIFragment as the TaskFragment's target fragment.
        if (mAsyncFragment == null) {
            Log.d(TAG, "New async created");
            mAsyncFragment = new Esp8266ClientAsyncFragment();
            mAsyncFragment.setTargetFragment(this, 0);
            mClientStatus.onConnecting();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAsyncFragment.start(IP_ADDRESS, PORT, ssidName);
                    mConnectFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_wifi_black_24dp));
                }
            }, 500);
            //Connect to Esp8266 using async
            fm.beginTransaction().add(mAsyncFragment, TAG_TASK_FRAGMENT).commit();
        } else {
            Log.d(TAG, "Using old Async");
        }

        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (DEBUG) Log.i(TAG, "onSaveInstanceState(Bundle)");
        super.onSaveInstanceState(outState);
        outState.putBoolean(CONNECTED_TO_WIFI, mAsyncFragment.isConnected());
    }

    private void toggleWifi() {
        if (!mAsyncFragment.isRunning()) {
            mAsyncFragment.start(IP_ADDRESS, PORT, ssidName);
            mConnectFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_wifi_black_24dp));
        } else if (mAsyncFragment.isConnected()) {
            mAsyncFragment.disconnect();
            mConnectFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_signal_wifi_off_black_24dp));
        }
    }


    private void sayToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onJoystick(float[] joystickData) {
        float translationX = Utility.mapFloat(joystickData[0], -1, 1, -40, 40);
        float translationY = Utility.mapFloat(joystickData[1], -1, 1, -40, 40);
        mJoystickLeft.setX(translationX + joystickLeft_CenterLocation[0]);
        mJoystickLeft_outline.setX(translationX + joystickLeft_CenterLocation[2]);
        mJoystickLeft_inner.setX(translationX + joystickLeft_CenterLocation[4]);

        mJoystickLeft.setY(translationY + joystickLeft_CenterLocation[1]);
        mJoystickLeft_outline.setY(translationY + joystickLeft_CenterLocation[3]);
        mJoystickLeft_inner.setY(translationY + joystickLeft_CenterLocation[5]);

        translationX = Utility.mapFloat(joystickData[2], -1, 1, -40, 40);
        translationY = Utility.mapFloat(joystickData[3], -1, 1, -40, 40);
        mJoystickRight.setX(translationX + joystickRight_CenterLocation[0]);
        mJoystickRight_outline.setX(translationX + joystickRight_CenterLocation[2]);
        mJoystickRight_inner.setX(translationX + joystickRight_CenterLocation[4]);

        mJoystickRight.setY(translationY + joystickRight_CenterLocation[1]);
        mJoystickRight_outline.setY(translationY + joystickRight_CenterLocation[3]);
        mJoystickRight_inner.setY(translationY + joystickRight_CenterLocation[5]);

        animateAnalogTriggers(mAnalogTriggerLeft, joystickData[4]);
        animateAnalogTriggers(mAnalogTriggerRight, joystickData[5]);

        int k = (int) Utility.mapFloat(joystickData[0], -1, 1, 100, 355);  //I put in terms of 3 digits because the UDP packets must have the same number of digits to be run smoothly
        int l = (int) Utility.mapFloat(joystickData[1], -1, 1, 100, 355);
        int z = (int) Utility.mapFloat(joystickData[2], -1, 1, 100, 355);
        int x = (int) Utility.mapFloat(joystickData[3], -1, 1, 100, 355);

        int q = (int) Utility.mapFloat(joystickData[4], -1, 1, 100, 355);
        int r = (int) Utility.mapFloat(joystickData[5], -1, 1, 100, 355);

        if (mAsyncFragment != null) {
            //either we can send all the data in one packet....
//            mAsyncFragment.sendMessage("k" + k + ",l" + l + ",z" + z + ",x" + x + ",q" + q + ",r" + r, 1); //I'm gunna have to clean this up later

            //Or we can send each packet individually on different channels
            mAsyncFragment.sendMessage("k" + k, 1);
            mAsyncFragment.sendMessage("l" + l, 2);
            mAsyncFragment.sendMessage("z" + z, 3);
            mAsyncFragment.sendMessage("x" + x, 4);
            mAsyncFragment.sendMessage("q" + q, 5);
            mAsyncFragment.sendMessage("r" + r, 6);
        }


    }


    @Override
    public boolean onButton(int buttonPress, boolean isPressed) {
        boolean handled = true;
        switch (buttonPress) {
            case KeyEvent.KEYCODE_BACK:
//                sayToast("Back Button");
                changeImageViewColor(mBackButton, isPressed);
                sendToClient((isPressed) ? "y1" : "y0");
                break;
            case KeyEvent.KEYCODE_BUTTON_START:
//                sayToast("Start/Menu Button");
                changeImageViewColor(mMenuButton, isPressed);
                sendToClient((isPressed) ? "u1" : "u0");
                break;
            case KeyEvent.KEYCODE_BUTTON_X:
//                sayToast("X Button");
                changeImageViewColor(mButtonX, isPressed);
                sendToClient((isPressed) ? "i1" : "i0");
                break;
            case KeyEvent.KEYCODE_BUTTON_Y:
//                sayToast("Y Button");
                changeImageViewColor(mButtonY, isPressed);
                sendToClient((isPressed) ? "o1" : "o0");
                break;
            case KeyEvent.KEYCODE_BUTTON_B:
//                sayToast("B Button");
                changeImageViewColor(mButtonB, isPressed);
                sendToClient((isPressed) ? "p1" : "p0");
                break;
            case KeyEvent.KEYCODE_BUTTON_A:
//                sayToast("A Button");
                changeImageViewColor(mButtonA, isPressed);
                sendToClient((isPressed) ? "a1" : "a0");
                break;


            case KeyEvent.KEYCODE_BUTTON_THUMBL:
//                sayToast("Left Joystick Button");
                changeImageViewColor(mJoystickLeft, isPressed);
                changeImageViewColor(mJoystickLeft_inner, isPressed);
                sendToClient((isPressed) ? "t1" : "t0");
                break;
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
//                sayToast("Right Joystick Button");
                changeImageViewColor(mJoystickRight, isPressed);
                changeImageViewColor(mJoystickRight_inner, isPressed);
                sendToClient((isPressed) ? "j1" : "j0");
                break;


            case KeyEvent.KEYCODE_BUTTON_L1:
//                sayToast("Left Trigger");
                changeImageViewColor(mTriggerLeft, isPressed);
                sendToClient((isPressed) ? "w1" : "w0");

                break;
            case KeyEvent.KEYCODE_BUTTON_R1:
//                sayToast("Right Trigger");
                changeImageViewColor(mTriggerRight, isPressed);
                sendToClient((isPressed) ? "e1" : "e0");
                break;
            case KeyEvent.KEYCODE_BUTTON_L2:
//                sayToast("2nd Left Trigger");
                changeImageViewColor(mAnalogTriggerLeft, isPressed);
                sendToClient((isPressed) ? "q255" : "q0");

                break;
            case KeyEvent.KEYCODE_BUTTON_R2:
//                sayToast("2nd Right Trigger");
                changeImageViewColor(mAnalogTriggerRight, isPressed);
                sendToClient((isPressed) ? "r255" : "r0");
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
//                sayToast("D-pad Center");
                clearPreviousDpad();
                dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_CENTER;
                dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_CENTER;
                sendToClient("d0");
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                clearPreviousDpad();
                changeImageViewColor(mDpadLeft, isPressed);
                dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_LEFT;
                sendToClient("d1");
//                sayToast("D-pad Left");
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                clearPreviousDpad();
                changeImageViewColor(mDpadUp, isPressed);
                dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_UP;
                sendToClient("d2");
//                sayToast("D-pad Up");
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                clearPreviousDpad();
                changeImageViewColor(mDpadRight, isPressed);
                dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_RIGHT;
                sendToClient("d3");
//                sayToast("D-pad Right");
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                clearPreviousDpad();
                changeImageViewColor(mDpadDown, isPressed);
                dPadCurrentLocation = KeyEvent.KEYCODE_DPAD_DOWN;
                sendToClient("d4");
//                sayToast("D-pad Down");
                break;
            default:
                handled = false;
        }
        return handled;
    }

    private void sendToClient(String message) {
        if (mAsyncFragment != null) {
            mAsyncFragment.sendMessage(message, 0);  //buttons are sent on channel 0 because android receives them in order.
        }
    }

    private void clearPreviousDpad() {
        switch (dPadCurrentLocation) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                changeImageViewColor(mDpadLeft, false);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                changeImageViewColor(mDpadUp, false);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                changeImageViewColor(mDpadRight, false);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                changeImageViewColor(mDpadDown, false);
                break;
        }
    }

    private void animateAnalogTriggers(final ImageView img, float ratio) {
        int colorTo = brightStandOutColor;
        int colorFrom = getCyanea().getPrimaryDark();

        int red = (int) Math.abs((ratio * Color.red(colorTo)) + ((1 - ratio) * Color.red(colorFrom)));
        int green = (int) Math.abs((ratio * Color.green(colorTo)) + ((1 - ratio) * Color.green(colorFrom)));
        int blue = (int) Math.abs((ratio * Color.blue(colorTo)) + ((1 - ratio) * Color.blue(colorFrom)));


        int transparentColor = Color.rgb(red, green, blue);
        ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(transparentColor));

    }

    public void changeImageViewColor(ImageView img, boolean isPressed) {
        ImageViewCompat.setImageTintList(img, ColorStateList.valueOf((isPressed) ? brightStandOutColor : getCyanea().getPrimaryDark()));
    }

    /**
     * Esp8266 Client Callbacks
     **/

    @Override
    public void onReceiveListener(String message) {

    }

    @Override
    public void onDisconnectListener(boolean toast) {
        if (toast) sayToast("Disconnected.");
        mConnectFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_signal_wifi_off_black_24dp));
        ImageViewCompat.setImageTintList(mConnectFab, ColorStateList.valueOf(getCyanea().getPrimaryDark()));


        ImageViewCompat.setImageTintList(mStatusWiFi, ColorStateList.valueOf(getCyanea().getPrimaryDark()));
        mClientStatus.onDisconnected();
    }

    @Override
    public void onConnectedListener(boolean toast) {
        if (toast) sayToast("Connected!");
        mConnectFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_wifi_black_24dp));
        ImageViewCompat.setImageTintList(mConnectFab, ColorStateList.valueOf(Utility.getComplimentColor(getCyanea().getAccent())));

        ImageViewCompat.setImageTintList(mStatusWiFi, ColorStateList.valueOf(Utility.getComplimentColor(getCyanea().getAccent())));
        mClientStatus.onConnected();
    }

    @Override
    public void onConnectingListener(boolean toast) {
        mClientStatus.onConnecting();
    }

    public void forceDisconnect() {
        if (mAsyncFragment != null)
            mAsyncFragment.disconnect();
    }

}

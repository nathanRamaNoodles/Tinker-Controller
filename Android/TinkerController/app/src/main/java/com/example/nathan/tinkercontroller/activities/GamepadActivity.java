package com.example.nathan.tinkercontroller.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.example.nathan.tinkercontroller.R;
import com.example.nathan.tinkercontroller.ClientStatus;
import com.example.nathan.tinkercontroller.fragments.Gamepad_Joystick_Fragment;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class GamepadActivity extends CyaneaAppCompatActivity implements ClientStatus {

    private Toolbar toolbar;
    private ProgressBar mProgressbar;
    //fragments, manager
    FragmentTransaction ft;
    private Gamepad_Joystick_Fragment mJoystickFragment = new Gamepad_Joystick_Fragment();

    DpadMotion mDpadMotion = new DpadMotion();

    Boolean isJoyStick = false, isGamePad = false;
    private JoystickListener mJoystickListener;
    int previousDpadButton = KeyEvent.KEYCODE_DPAD_CENTER;
    int previousButton = KeyEvent.KEYCODE_DPAD_CENTER;
    float[] previousJoystick = {0, 0, 0, 0, 0, 0};

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("myTag", "Found device");
                //Device found
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
                Log.d("myTag", "device connected");
                onResume();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
                Log.d("myTag", "Done Searching");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
                Log.d("myTag", "Device is GOing to disconnect");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                Log.d("myTag", "Device disconnected");
                isGamePad = isJoyStick = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepad);
        toolbar = findViewById(R.id.toolbar);
        mProgressbar = findViewById(R.id.connection_progress_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        if (null == savedInstanceState) {
            // set your initial fragment object
            loadFragment(mJoystickFragment);
        } else {  //On Orientation change, this saves the fragment's state and scroll position
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container); //get old fragments on rotation

            if (frag.getClass().equals(mJoystickFragment.getClass())) {
                mJoystickFragment = (Gamepad_Joystick_Fragment) frag;
            }
// else if (frag.getClass().equals(mAboutFragment.getClass())) {
//                mAboutFragment = (About_Fragment) frag;
//            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        getGameControllerIds();
    }


    //Fragment loader/animator
    private boolean loadFragment(Fragment currentFragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.fragment_container, currentFragment);
        ft.commit();
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(android.view.MotionEvent motionEvent) {
        boolean handled = true;

        int press = mDpadMotion.getDirectionPressed(motionEvent);

        if (isGamePad && (previousDpadButton != press)) {   //(prevent repetition in data) for example, if the d-pad was centered, then don't call this method again until a different d-pad button is pressed.
            previousDpadButton = press;

            mJoystickListener.onButton(press, true);
        }

        if (isJoyStick) {
            // Process all historical movement samples in the batch
            final int historySize = motionEvent.getHistorySize();

            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(motionEvent, i);
            }

            // Process the current movement sample in the batch (position -1)
            if (motionEvent.getDevice() != null)
                processJoystickInput(motionEvent, -1);
            handled = true;
        }
        return handled;
    }

    @Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {  //Button pressed down

                if (previousButton != event.getKeyCode() && !mJoystickListener.onButton(event.getKeyCode(), true)) {
                    Log.d("myTag", "Unhandled KeyCode: " + event.getKeyCode());
                    return false;
                } else {
                    previousButton = event.getKeyCode();
                    return true;
                }
            } else if (event.getAction() == KeyEvent.ACTION_UP) {   //button released
                if (!mJoystickListener.onButton(event.getKeyCode(), false)) {
                    Log.d("myTag", "Unhandled KeyCode: " + event.getKeyCode());
                    return false;
                } else {
                    previousButton = -1;
                    return true;
                }
            }
        } else {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {  //if the back press is from the phone then go back
                onBackPressed();
                return true;
            }
            Log.d("myTag", "Not a Joystick, KeyCode: " + event.getKeyCode());
        }
        return false;
    }

    //From Google's page on controller-input
    public ArrayList getGameControllerIds() {
        ArrayList gameControllerDeviceIds = new ArrayList();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
                //possible both maybe true.
                if ((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    isGamePad = true;
                if ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)
                    isJoyStick = true;
            }

        }
        return gameControllerDeviceIds;
    }

    private static float getCenteredAxis(MotionEvent event,
                                         InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis) :
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    private void processJoystickInput(MotionEvent event,
                                      int historyPos) {

        InputDevice mInputDevice = event.getDevice();

        float[] newJoystickValues = {
                getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_X, historyPos),
                getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Y, historyPos),
                getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Z, historyPos),
                getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_RZ, historyPos),
                getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_BRAKE, historyPos),
                getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_GAS, historyPos)
        };

        boolean isDifferent = false;
        for (int i = 0; i < previousJoystick.length; i++) {
            if (Math.abs(previousJoystick[i] - newJoystickValues[i]) > 0.01) {  //check if values are different;
                isDifferent = true;
                break;
            }
        }
        if (isDifferent) {   //If the joysticks are in different positions, then continue sending data.
            for (int i = 0; i < previousJoystick.length; i++) {
                previousJoystick[i] = newJoystickValues[i];  //update our new values
            }

            //This is where we can do what we want with our efficient , accurate joystick data :)

            mJoystickListener.onJoystick(previousJoystick); //here, we pass our data to our listening fragments

//            Log.d("myTag", "JoyStick: X " + previousJoystick[0] + " , Y " + previousJoystick[1] +
//                    " , Z " + previousJoystick[2] + " , RZ " + previousJoystick[3] +
//                    " , ALT " + previousJoystick[4] + " , ART " + previousJoystick[5]);
        }
    }

    /**
     * These listeners are used to toggle the progressbar's connection state
     */
    @Override
    public void onDisconnected() {
        mProgressbar.setIndeterminate(false);
        mProgressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnected() {
        mProgressbar.setIndeterminate(false);
        mProgressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnecting() {
        mProgressbar.setIndeterminate(true);
        mProgressbar.setVisibility(View.VISIBLE);
    }

    public interface GamepadListener {  //All controllers have a button(Dpads are also buttons)
        boolean onButton(int buttonPress, boolean isPressed);
    }

    public interface JoystickListener extends GamepadListener {  //Joysticks have a joystick and some buttons
        void onJoystick(float[] joystickData);
    }

    public void setJoystickListener(JoystickListener interFace) {
        mJoystickListener = interFace;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (frag.getClass().equals(mJoystickFragment.getClass())) {
            mJoystickFragment.forceDisconnect();
            super.onBackPressed();
        } else {
            loadFragment(mJoystickFragment);
        }
    }
}

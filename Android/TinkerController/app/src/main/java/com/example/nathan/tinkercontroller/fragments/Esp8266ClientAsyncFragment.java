package com.example.nathan.tinkercontroller.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.nathan.tinkercontroller.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.WIFI_SERVICE;
import static com.example.nathan.tinkercontroller.BuildConfig.DEBUG;

public class Esp8266ClientAsyncFragment extends Fragment {

    /**
     * AsyncTask that connects to a remote host over WiFi and reads/writes the connection
     * using a socket. The read loop of the AsyncTask happens in a separate thread, so the
     * main UI thread is not blocked. However, the AsyncTask has a way of sending data back
     * to the UI thread. Under the hood, it is using Threads and Handlers.
     */
// Tag for logging
    private final String TAG = "myTag";
    private boolean mRunning;

    private esp8266ClientListener mClient;
    private esp8266Async mClientAsync;
    // Location of the remote host
    String address;
    int port;
    private String ssidName = "";

    // Special messages denoting connection status
    private static final String CONNECTING_MSG = "CONNECTING_MSG";
    private static final String CONNECTED_MSG = "CONNECTED_MSG";
    private static final String DISCONNECTED_MSG = "DISCONNECTED_MSG";

    // Signal to disconnect from the socket
    private boolean disconnectSignal;
    private boolean[] sendSignal = new boolean[7];
    private String[] dataToSend = new String[7];
    private int channel = 0;

    // interval to turn off same repeated messages
//    private int interval = 10;  //keeping this small is essential, because the esp8266 can crash with too much data to read
    @Override
    public void onAttach(Context context) {
        if (DEBUG) Log.i(TAG, "onAttach(Activity)");
        super.onAttach(context);
        if (!(getTargetFragment() instanceof esp8266ClientListener)) {
            throw new IllegalStateException("Target fragment must implement the Esp8266ClientAsync interface.");
        }

        // Hold a reference to the target fragment so we can report back the task's
        // current progress and results.
        mClient = (esp8266ClientListener) getTargetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        disconnect();
    }

    /**
     * Start the background task.
     */
    public void start(String address, int port, String ssidName) {
        if (!mRunning) {
            this.address = address;
            this.port = port;
            this.ssidName = ssidName;
            disconnectSignal = false;
            mClientAsync = new esp8266Async();
            mClientAsync.execute();
            mRunning = true;
        }
    }

    public boolean isRunning() {
        return mRunning;
    }

    public boolean isConnected() {
        return !disconnectSignal;
    }

    private class esp8266Async extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            // Proxy the call to the Activity.
            mRunning = true;
        }

        @Override
        protected Void doInBackground(Void... arg) {

            try {
                /**
                 * Remember in the AsyncTask, everywhere before you call getActivity(),
                 * you'd better to check isAdded() again, because user may exit the
                 * activity at anytime during the AsyncTask is executing.
                 */
                if (!isAdded())
                    return null;  //connectToWiFi requires getActivity(), so we need to check if fragment was added.
                connectToWIFI();
                //After we connect, let's try to send a test packet to ensure this is a UDP device.
                InetAddress addr = InetAddress.getByName(address);
                DatagramSocket ds = new DatagramSocket();
                ds.setReuseAddress(true);
                ds.setBroadcast(true);
                ds.connect(addr, port);
                for (int i = 0; i < 3; i++) {
                    String testPacket = "m=0";
                    DatagramPacket datagramPacket = new DatagramPacket(testPacket.getBytes(), testPacket.length(), addr, port);
                    ds.send(datagramPacket);
                }

                Log.d(TAG, "Connected Successfully!");
                publishProgress(CONNECTED_MSG);


                DatagramPacket[] dp = new DatagramPacket[7];


                // Read messages in a loop until disconnected
                while (!disconnectSignal) {

                    //Channel 0 is for handling button presses since Android receives button inputs one at a time.
                    if (sendSignal[0]) {
                        sendSignal[0] = false;
                        dp[0] = new DatagramPacket(dataToSend[0].getBytes(), dataToSend[0].length(), addr, port);
                        ds.send(dp[0]);
                    }

                    //However, not all actions happen one at a time(like analog joysticks), so we need to force them in order by running on the main thread.
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            for (int i = 1; i < sendSignal.length; i++) {
                                if (sendSignal[i]) {
                                    sendSignal[i] = false;
                                    dp[i] = new DatagramPacket(dataToSend[i].getBytes(), dataToSend[i].length(), addr, port);
                                    try {
                                        ds.send(dp[i]);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    };

                    thread.start();
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "Unknown Error: " + e.toString());
                publishProgress(DISCONNECTED_MSG);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d(TAG, "IOException: " + e.toString());
                e.printStackTrace();
                publishProgress(DISCONNECTED_MSG);
            }

            // Send a disconnect message
            publishProgress(DISCONNECTED_MSG);
            Log.d(TAG, "Successfully Disconnected...Ending Session");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            String msg = values[0];
            if (msg == null) return;

            // Handle meta-messages
            if (msg.equals(CONNECTED_MSG)) {
                mRunning = true;
                mClient.onConnectedListener(true);
            } else if (msg.equals(DISCONNECTED_MSG)) {
                mRunning = false;
                mClient.onDisconnectListener(true);
            } else if (msg.equals(CONNECTING_MSG)) {
                mRunning = true;
                mClient.onConnectingListener(true);
            } else
                mClient.onReceiveListener(msg);  //our main messages arrive here, then we send them to whoever is listening to them
        }

        private boolean connectToWIFI() {
            publishProgress(CONNECTING_MSG);
            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
            boolean wifiWasOn = true;
            assert wifiManager != null;
            while (!wifiManager.isWifiEnabled()) {
                wifiWasOn = false;
                wifiManager.setWifiEnabled(true); //turn on wifi if it isn't already;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.d(TAG, "Checking if already connected to Esp8266");
            if (!connectToEsp(wifiInfo, wifiWasOn)) {  //if false, then search for it and connect
                Log.d(TAG, "Not connected to Esp8266...");
                String passwordEsp8266 = "";
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", ssidName);
                wifiConfig.preSharedKey = String.format("\"%s\"", passwordEsp8266);

                //remember id
                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
                Log.d(TAG, "auto-connecting");
                wifiInfo = wifiManager.getConnectionInfo();
                return !connectToEsp(wifiInfo, false);
            }
            return true;
        }

        private boolean connectToEsp(WifiInfo wifiInfo, boolean wifiWasOn) {
            publishProgress(CONNECTING_MSG);
            long start = System.currentTimeMillis();
            boolean scanComplete = true;
            while (wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
                long now = System.currentTimeMillis();
                if (now - start > 5E3) {
                    scanComplete = false;
                    publishProgress(DISCONNECTED_MSG);
                    break;
                }
            }

            if (scanComplete) {
                Log.d(TAG, "Yay, found a device...");
                String requiredSSID = String.format("\"%s\"", ssidName);
                start = System.currentTimeMillis();
                while (!wifiInfo.getSSID().equalsIgnoreCase(requiredSSID)) {
                    long now = System.currentTimeMillis();
                    if (now - start > 12E3) {
//                        if (!wifiWasOn) {
//                            Log.d(TAG, "Retrying...");
//                            return connectToWIFI();
//                        }
                        Log.d(TAG, "Unknown device: " + wifiInfo.getSSID());
                        publishProgress(DISCONNECTED_MSG);
                        scanComplete = false;
                        break;
                    }
                }
            }
            return scanComplete;
        }

        @Override
        protected void onCancelled() {
            // Proxy the call to the Activity.
//            if (mClientAsync != null)
//                mClientAsync.onCancelled();
            mRunning = false;
        }

        @Override
        protected void onPostExecute(Void ignore) {
            // Proxy the call to the Activity.
            mRunning = false;
        }
    }


    /**
     * This function runs in the UI thread but receives data from the
     * doInBackground() function running in a separate thread when
     * publishProgress() is called.
     */

    /**
     * Write a message to the connection. Runs in UI thread.
     */
    public void sendMessage(String dataToSend, int channel) {
//        Log.d(TAG, dataToSend);
        this.dataToSend[channel] = dataToSend;
        this.channel = channel;
        sendSignal[channel] = true;
    }


    /**
     * Set a flag to disconnect from the socket.
     */
    public void disconnect() {
        if (mRunning) {
            mClientAsync.cancel(false);
            mClient.onDisconnectListener(true);
            disconnectSignal = true;
            mClientAsync = null;
            mRunning = false;
        }
    }


    public interface esp8266ClientListener {
        void onReceiveListener(String message);

        void onDisconnectListener(boolean toast);

        void onConnectedListener(boolean toast);

        void onConnectingListener(boolean toast);
    }
}

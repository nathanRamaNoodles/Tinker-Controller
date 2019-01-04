package com.example.nathan.tinkercontroller;

public interface ClientStatus {
    void onDisconnected();

    void onConnected();

    void onConnecting();
}

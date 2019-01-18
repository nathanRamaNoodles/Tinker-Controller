////////////////////////////--------------------Libraries-------------------------------------------/////////////////////////////////////
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <Wire.h>
#include "WEMOS_Motor.h"
////////////////////////////--------------------Variables-------------------------------------------/////////////////////////////////////
WiFiUDP Udp;
const int LED = LED_BUILTIN;
const int deadZone = 5;  //deadZone for joystick input
//Motor shield default I2C Address: 0x30
//PWM frequency: 1000Hz(1kHz)
Motor M1(0x30, _MOTOR_A, 1000); //Motor A
Motor M2(0x30, _MOTOR_B, 1000); //Motor B
////////////////////////////--------------------Don't change these Settings-------------------------------------------/////////////////////////////////////
/* Don't change this stuff(because the phone will be looking for us based on SSID name and port) */ //Note, we are not connecting to WiFi, because we are looking to optimize speed.  You can connect to WiFi if you want
const char *ssid = "ESPTest";
const char *password = "";
unsigned int localUdpPort = 4210;  // local port to listen on

////////////////////////////--------------------SETUP-------------------------------------------/////////////////////////////////////

void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.print("Configuring access point...");
  /* You can remove the password parameter if you want the AP to be open. */
  WiFi.softAP(ssid, password);
  IPAddress myIP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(myIP);
  Serial.print("Port: ");
  Serial.println(localUdpPort);

  Udp.begin(localUdpPort);

  pinMode(LED, OUTPUT);
  digitalWrite(LED, HIGH);
}

////////////////////////////--------------------LOOP-------------------------------------------/////////////////////////////////////

void loop() {
  // put your main code here, to run repeatedly:
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    char InputType, InputValue;
    //When we get our message, we need to evaluate the message type(Is it a button? Is it a joystick?), then we need to get its value.
    while (!isAlpha(InputType = Udp.read()));//Keep skipping until we get a letter.
    while (!isdigit(InputValue = Udp.read())); //Keep skipping until we get a integer.

    int num = InputValue - '0';
    while (isdigit(InputValue = Udp.read()))
    {
      num = (num * 10) + (InputValue - '0');  //some magic
    }
    useGameData(InputType, num);
  }
}
////////////////////////////--------------------Process Controller data-------------------------------------------/////////////////////////////////////

void useGameData(char type, int value) {
  yield();

  int num;
  switch (type) {
    case 'a':
      switch (value) {
        case 1:
          digitalWrite(LED, LOW);
          break;
        case 0:
          digitalWrite(LED, HIGH);
          break;
      }
      break;

    //left joystick Y direction
    case 'l':
      num = value - 100; //this will bring it down to a range 0-255;
      if ((127 - deadZone) < num && num < (127 + deadZone)) {
        //do nothing
        M1.setmotor(_STOP);
      }
      else {
        int DIRECTION;
        if (num < 127) {
          DIRECTION = _CW;
        }
        else {
          DIRECTION = _CCW;
        }
        int SPEED = abs(127 - num); //this will make the range from 0-127 ignorning the negative.
        SPEED = map(SPEED, 0, 127, 0, 100); //For the Wemos motor shield, the range is 0-100
        M1.setmotor(DIRECTION, SPEED);
      }
      break;


    //Right joystick X direction
    case 'z':
      num = value - 100;
      if ((127 - deadZone) < num && num < (127 + deadZone)) {
        //do nothing
        M2.setmotor(_STOP);
      }
      else {
        int DIRECTION;
        if (num < 127) {
          DIRECTION = _CCW;
        }
        else {
          DIRECTION = _CW;
        }
        int SPEED = abs(127 - num); //this will make the range from 0-127 ignorning the negative.
        SPEED = map(SPEED, 0, 127, 0, 100); //For the Wemos motor shield, the range is 0-100
        M2.setmotor(DIRECTION, SPEED);
      }
      break;
  }
  yield();
}

/**
   Author: Nathan Ramanathan
   Project link: https://github.com/nathanRamaNoodles/Tinker-Controller

   Directions: Hook up an LED to pin D7 and D8.  Next connect the controller to the app, and move the left joystick.  You should
   notice that the LEDs will fade according to the position of your controller's joystick.
**/
//Note: I or someone will make a library out of this because it looks complex right now, unless you actually do understand it, then good job :)

/////////////--------------------------------Libraries------------------------------------------------------------///////////////
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <SensorToButton.h>   //https://github.com/nathanRamaNoodles/SensorToButton

/////////////--------------------------------Pins--and---Settings------------------------------------------------------------/////////////////////
#define LED D8
#define LED_2 D7
//#define STREAMING
/*Uncomment this define statement if you just want to read all data. For gaming mode(default), comment it*/
WiFiUDP Udp;
/////////////--------------------------------Don't change These Settings------------------------------------------------------------/////////////////////

/* Don't change this stuff(because the phone will be looking for us based on SSID name and port) */ //Note, we are not connecting to WiFi, because we are looking to optimize speed.  You can connect to WiFi if you want
const char *ssid = "ESPTest";
const char *password = "";
unsigned int localUdpPort = 4210;  // local port to listen on

/////////////--------------------------------Beginning of Program------------------------------------------------------------/////////////////////

void setup()
{
  //fix for the "race of the auto connect" issue found on some smart Phones
  ESP.eraseConfig();
  if (WiFi.status() == WL_CONNECTED) WiFi.disconnect();
  /////

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
}

/////////////--------------------------------Loop------------------------------------------------------------/////////////////////

void loop()
{
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
#if defined(STREAMING)
    for (int i = 0; i < 5; i++) {  //usually the max number of chars sent from the phone is 4-5.  If its more, then we might crash ESP8266
      Serial.print((char)Udp.read());
    }
    Serial.println();
#else
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
#endif
  }
}

/////////////--------------------------------Process Game Controller Data------------------------------------------------------------/////////////////////
/*I know the naming of chars are weird, but I made them up based on the QWERTY system on my keyboard, So i looked at my controller and typed
  Right to left and then top to bottom*/

void useGameData(char type, int value) {
  yield();
  switch (type) {
    //A Button
    case 'a':
      switch (value) {  //use switch statements if you aren't printing to Serial.
        case 1:
          Serial.println("A on");
          digitalWrite(LED, LOW);
          break;
        case 0:
          Serial.println("A off");
          digitalWrite(LED, HIGH);
          break;
      }
      break;
    //B Button
    case 'p':
     switch (value) {  //use switch statements if you aren't printing to Serial.
        case 1:
          Serial.println("B on");
          break;
        case 0:
          Serial.println("B off");
          break;
      }
      break;
    //Y button
    case 'o':
      switch (value) {  //use switch statements if you aren't printing to Serial.
        case 1:
          Serial.println("Y on");
          break;
        case 0:
          Serial.println("Y off");
          break;
      }
      break;

    //You can do this for the other buttons...but I don't have to because its boring :)

    //X Button
    case 'i':
     switch (value) {  //use switch statements if you aren't printing to Serial.
        case 1:
          Serial.println("X on");
          break;
        case 0:
          Serial.println("X off");
          break;
      }
      break;

    //-------------Dpad Buttons------------//
    case 'd':
      switch (value) {
        //Dpad Center Button
        case 0:
          Serial.println("D-pad Center");
          break;
        //Dpad Left Button
        case 1:
          Serial.println("D-pad Left");
          break;
        //Dpad Up Button
        case 2:
          Serial.println("D-pad Up");
          break;
        //Dpad Right Button
        case 3:
          Serial.println("D-pad Right");
          break;
        //Dpad Down Button
        case 4:
          Serial.println("D-pad Down");
          break;
      }
      break;


    //-------------Function Buttons------------//
    //Back Button
    case 'y':
      break;
    //Start Button
    case 'u':
      break;

    //-------------Joystick Buttons------------//
    //Left Joystick Button
    case 't':
      break;
    //Right Joystick Button
    case 'j':
      break;

    //-------------Trigger Buttons------------//
    //Top left trigger Button
    case 'w':
      break;
    //Top right trigger Button
    case 'e':
      break;
    //Bottom left Trigger(Some controllers have this as analog so 355 means on)
    case 'q':
      switch (value) {
        //        case 355:
        //          digitalWrite(LED, LOW);
        //          break;
        //        case 0:
        //          digitalWrite(LED, HIGH);
        //          break;
        default:
          //analog stuff handled here
          break;
      }
      break;
    //Bottom Right Trigger(Some controllers have this as analog so 355 means on)
    case 'r':
      switch (value) {
        //        case 355:
        //          digitalWrite(LED, LOW);
        //          break;
        //        case 0:
        //          digitalWrite(LED, HIGH);
        //          break;
        default:
          //analog stuff handled here
          break;
      }
      break;


    //-------------Analog------------//
    //Analog values(Joysticks) are under construction.  There's an annoying method i have to deal with in Android
    //(Every joystick movement is sent out to the esp8266 which causes too many packets to reset the esp).
    /*The range for these values is 100-355.  I chose these ranges because Android can't sent UDP packets of different length. I
      wanted a range of 0-255, so I simply bumped it up by adding 100. And it works great and smoothly for Android*/

    //left joystick X direction
    case 'k':
      analogWrite(LED, value - 100);  /*Subtract by 100 since the incoming data has a range from 100-355, and Arduino's analogWrite() only accepts 0-255*/
      break;
    //left joystick Y direction
    case 'l':
      analogWrite(LED_2, value - 100);
      break;
    //Right joystick X direction
    case 'z':
      break;
    //Right joystick Y direction
    case 'x':
      break;
  }
  yield();
}

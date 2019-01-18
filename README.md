
# Tinker-Controller
An open-source Game Controller App that allows you to use an Xbox/Ps4/Joycon or any controller that works with your phone to effortlessly control most micro-controllers such as Arduino/Esp8266/Raspberry Pi over WiFi, bluetooth, or USB.

<p align="center">
<img src="https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/Art/ic_launcher_round.png"></img>
</p>
<h1 align="center">Tinker Controller</h1>


<p align="center">
	<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=R6GFJS92FX7J2"><img src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif"></a>
</p>


|Slick and fast UI| Dozens of Themes| Quick Reactions using UDP protocol|
|:--:|:---:|:------:|
| <a width="100" href="https://imgflip.com/gif/2qc7es"><img src="https://i.imgflip.com/2qc7es.gif" title="made at imgflip.com"/></a> | <a width="100" href="https://imgflip.com/gif/2qc7jh"> <img src="https://i.imgflip.com/2qc7jh.gif" title="made at imgflip.com"/></a> |  <a width="100" href="https://imgflip.com/gif/2qcbkg"><img src="https://i.imgflip.com/2qcbkg.gif" title="made at imgflip.com"/></a>|

<h2>Table Of Contents</h2>

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Tinker-Controller](#tinker-controller)
	- [Videos](#videos)
	- [Getting Started](#getting-started)
	- [Thank you](#thank-you)
	- [A Letter](#a-letter)
- [Questions](#questions)
	- [Controller Support?](#controller-support)
	- [Contribute?](#contribute)
	- [App Store?](#app-store)
	- [How does it Work?](#how-does-it-work)
	- [Submit an Issue/Bug?](#submit-an-issuebug)
- [Updates](#updates)
	- [Agenda](#agenda)
	- [Features](#features)
	- [Bugs To Fix](#bugs-to-fix)
	- [License](#license)

<!-- /TOC -->


## Videos
This new video below demonstrates and explains how it works:
<p align="center">
	<a href="https://www.youtube.com/watch?v=Gns7bLHp2VQ"><img src="https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/Art/xbox_esp8266_android_photo.jpg" width="500" height="300"></a>
</p>

And these are my teaser videos from [Reddit](https://www.reddit.com/r/arduino/comments/aap154/i_connected_my_xbox_controller_to_my_esp8266_with/) and [Youtube](https://www.youtube.com/watch?v=hzFY1dZjo34)

----------------
## Getting Started
  1. Download Android Studio 3.2.0 or higher and then use it to build the project files into an .apk file
     + If you are on your Android phone, then you can [download the demo.apk file here](https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/demo.apk?raw=true)
  2. Next upload the Tinker_Controller.ino file to the esp8266
  3. Launch app, and click on "Gamepad".  
  4. You should notice the app will automatically connect to the esp8266, but if it doesn't you can tap on the button that has the WiFi symbol
     + If that doesn't work, then go to your WiFi settings, and connect to the "ESPTest" network.
  5. When connected successfully, the WiFi button and toggle button should turn blue.
  6. Now, grab a game controller supported by Android.
     + If you don't have a controller, then you can tap on the toggle button(located at bottom left) to toggle esp8266's built-in LED
  7. You should notice that the game controller on the screen is showing the live actions you are doing on your controller
     + If nothing happens, try to close app, and try again.  If it still fails, then your controller is not supported by Android(which is sad)
  8. Press the A button(down button) on your controller, and you should notice that you are controlling the Esp8266's Built-in Led.
  9. If you open the Serial monitor, and press the other buttons like the dpad, they should say something
     + Note: I didn't code the Serial output for the other triggers because I was bored, but you can do that. :D
  9. To disconnect, you can leave the gamepad mode or press the WiFi button.
  10. Have fun, and join me/donate if you want this project to continue :)

## Thank you
+ People who fixed this project's bugs:
	+ [reddit user u/cointoss3](https://www.reddit.com/r/arduino/comments/acign8/this_is_how_i_connected_my_xbox_controller_to_my/ed96ko2) fixed WDT resets
+ Guys at Espressif for the amazing and fast library and plugin for esp8266
+ Guys who post good code with examples on stack overflow
+ [JareDrummler](https://github.com/jaredrummler/Cyanea) for the amazing Theme Engine
+ [Jake Whorton](https://github.com/JakeWharton/butterknife), for Butterknife library
+ AndroidX libraries and other libraries cause I don't know where I'd be without you.

## A Letter

  Dear Tinkers,

  **Sorry it took a few days to upload all this stuff to github.  Apparently, TCP protocol is pretty bad for streaming, so I decided to use UDP.  
  I'm just learning this stuff straight out of college so..yeah.** Also, regarding respect to co-workers, I realized using Linus Torvalds as a role model sounds kind of bad. XD

  -Thanks,
    Nathan Ramanathan

  **P.S.**

  **College DOES NOT TEACH YOU, only StackOverflow, Google, other people's code-on-Github are your teachers**

  ------


# Questions
------
## Controller Support?
Works with any controller that can connect to your phone.  They are called HID/Bluetooth controller devices.

**Note**: For Wii U/Switch, you must use the [Mayflash Adapter](https://www.amazon.com/Mayflash-Magic-NS-Wireless-Controller-Nintendo/dp/B079B5KHWQ)(Because Nintendo hates open-source)

## Contribute?
Yes, please contribute. It will make the community a much better and active environment.  Also, running away with the source code without
recognizing us and knowing how it works it just plain rude. :\

## App Store?
Yes, it will be there once I add "Terminal mode" and "Phone as a Gamepad" mode.  Also, Google Play has been removing more and more
apps lately, so I'm worried that my app may be removed. But at least it's open-source :)

## How does it Work?
 The Phone is the most powerful device that we all carry in our pockets, It can perform stuff on its screen while
doing some magic in the background. So how does it work?
It's quite simple, really :D

  For my app, the game controller connects via bluetooth or USB to your phone.  
The Android IDE(aka Android Studio) has built-in functions to handle keyEvents like a button press or joystick
movement. It can even detect keyboard presses and mouse clicks.  

  Well, my app takes these controller inputs and sends them to the ESP8266 (aka best WiFi microcontroller) by
the network SSID: "ESPTest". The way I programmed the Esp8266 is based on [my MusicWithoutDelay library](https://github.com/nathanRamaNoodles/MusicWithoutDelay-LIbrary).
This means we will use the Char-by-Char or Streaming method to evaluate each byte in the order it came. For example,
the phone sends a UDP packet containing two values that I made up.  So, if I press the `A` button on my Xbox controller,
my phone will send `a1` to the esp8266.  So `a1` means the A button was pressed, and `a0` vias versa.  I'm pretty sure this
techinque is used a lot in the industries.

**The Esp8266 acts as the server, while my phone is the client.**
Fortunately, you don't have to connect to this server manually. My app immediately starts searching for
WiFi devices with the name "ESPTest" within your area.  I tried to mimic Google Chromecast's method,
but its hard to decipher :/

   Next, after the phone connects to your esp8266, it will enable streaming mode.  This mode allows you to send UDP
packets to your esp8266 quickly.  Of course, there are many problems that arose.  Because UDP is so fast, it
caused my esp8266 to Crash XD.

   The solution I found was to pause the UDP stream if the game controller sent the same stuff before.  Essentially,
I'm muting extra repeating data. This idea/method is very similar with [my SensorToButton library](https://github.com/nathanRamaNoodles/SensorToButton).
You can check that out, as it's very simple to use.  

   The only downside is that some packets can be lost, but since this is game data, we don't care because the Human
button press only takes around 10-20 ms which is hardly noticeable when streaming :)  But you could try and clicking
faster and see a few hiccups, **but let's just be honest** that this looks cool; it's just so satisfying to see an LED fade when its being controlled by a Xbox controller's joystick.


## Submit an Issue/Bug?
Submit an [Issue](https://github.com/nathanRamaNoodles/Tinker-Controller/issues) and we'll fix it. :)

# Updates
---------
## Agenda

Things I plan to do:
- [x] ~~Fix Esp8266 from WDT resets, which is caused by doing an action too fast(such as printing to Serial or analogWrite())~~
- [x] ~~Create a Table of Contents~~ :D
- [ ] Create TinkerController Arduino Library
- [x] ~~Add WiFi support~~
- [ ] Add Bluetooth support(If its fast enough to handle gamecontroller input)
- [ ] Add USB support
- [ ] Allow a Keyboard/Mouse to act as a game controller
- [ ] Add MQTT/CloudMQTT support (This allows playing over the internet anywhere in the world for free)
- [ ] Make the Phone act as the server instead of the Esp8266
    + This will allow us to connect as many WiFi devices to the phone
    + You can add multiple controllers to the Phone thus creating a game server :D
- [ ] Create a page to access and change Esp8266 password and other stuff
    + Essentially, I'm trying to mimic Google Chromecast's Setup procedure
- [ ] Make a Terminal for communicating via TCP/UDP.
- [ ] Make the Phone as a Gamepad, because some people don't have controllers
- [ ] Conquer my fear: Dagger 2  :'(
- [x] Have Fun :D

## Features
I decided to use UDP because it's WAYYYYYYY faster than TCP. I didn't know if esp8266 had a UDP library, but
low and behold it did :)

## Bugs To Fix
1. Arduino Side:
   + ~~Esp8266 WDT reset happens when you move the joystick for a long time.~~
	   + Solved by reddit User: [u/cointoss3](https://www.reddit.com/r/arduino/comments/acign8/this_is_how_i_connected_my_xbox_controller_to_my/ed96ko2) who said to use `yield()` when using the controller data.
2. Android Side:
	+ The WiFi struggles to connect to esp8266 when the Phone's autoconnect is turned on.
	+ Lag develops after exiting gamepad mode

## License

```
GNU GENERAL PUBLIC LICENSE
   Version 3, 29 June 2007

Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
Everyone is permitted to copy and distribute verbatim copies
of this license document, but changing it is not allowed.

Rest of License found here: https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/LICENSE
```

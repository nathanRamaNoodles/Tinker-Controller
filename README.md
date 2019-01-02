# Tinker-Controller
An open-source Game Controller App that allows you to use an Xbox/Ps4/Joycon or any controller that works with your phone to effortlessly control most micro-controllers such as Arduino/Esp8266/Raspberry Pi over WiFi, bluetooth, or USB.
[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=R6GFJS92FX7J2)

## Video
My Teaser Video from [Reddit](https://www.reddit.com/r/arduino/comments/aap154/i_connected_my_xbox_controller_to_my_esp8266_with/) and [Youtube](https://www.youtube.com/watch?v=hzFY1dZjo34)


## Thank you
  + Guys at Espressif for the amazing and fast library and plugin for esp8266
  + Guys who post good code with examples on stack overflow
  + [JareDrummler](https://github.com/jaredrummler/Cyanea) for the amazing Theme Engine
  + [Jake Whorton](https://github.com/JakeWharton/butterknife), for Butterknife library
  + AndroidX libraries and other libraries cause I don't know where I'd be without you.

## A Letter

Dear Tinkers,
    
  **Sorry it took a few days to upload all this stuff to github.  Apparently, TCP protocol is pretty bad for streaming, so I decided to use UDP.  
I'm just learning this stuff straight out of college so..yeah.** Also, I realized using Linus Torvalds as a role model sounds kinda bad. XD

-Thanks,
  Nathan Ramanathan
  
**P.S.** 

**College DOES NOT TEACH YOU, only StackOverflow, Google, other people's code-on-Github are your teachers**

------

## Agenda
Things I plan to do:
- [ ] Create a Table of Contents :D
- [ ] Create TinkerController Arduino Library
- [x] Add WiFi support
- [ ] Add Bluetooth support(If its fast enough to handle gamecontroller input)
- [ ] Add USB support
- [ ] Allow a Keyboard/Mouse to act as a game controller
- [ ] Add MQTT/CloudMQTT support (This allows playing over the internet anywhere in the world for free)
- [ ] Make the Phone act as the server instead of the Esp8266
    + This will allow us to connect as many WiFi devices to the phone
    + You can add multiple controllers to the Phone thus creating a game server :D
- [ ] Create a page to access and change Esp8266 password and other stuff
    + Essentially, I'm trying to mimick Google Chromecast's Setup procedure
- [ ] Make a Terminal for communicating via TCP/UDP.
- [ ] Make the Phone as a Gamepad, because some people don't have controllers
- [ ] Conquer my fear: Dagger 2  :'(
- [x] Have Fun :D

## Controller Support
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
my phone will send `a1` to the esp8266.  So `a1` means the A button was pressed, and 'a0' vias versa.  I'm pretty sure this 
techinque is used a lot in the industries.

**The Esp8266 acts as the server, while my phone is the client.**
Fortunately, you don't have to connect to this server manually. My app immediately starts searching for 
WiFi devices with the name "ESPTest" within your area.  I tried to copy Google Chromecast's method, 
but its hard to decipher :/

   Next, after the phone connects to your esp8266, it will enable streaming mode.  This mode allows you to send UDP 
packets to your esp8266 quickly.  Of course, there are many problems that arose.  Because UDP is so fast, it 
caused my esp8266 to Crash XD. 
 
   The solution I found was to pause the UDP stream if the game controller sent the same stuff before.  Essentially, 
I'm muting extra repeating data. This idea/method is very similar with [my SensorToButton library](https://github.com/nathanRamaNoodles/SensorToButton).
You can check that out, as it's very simple to use.  
   
   The only downside is that some packets can be lost, but since this is game data, we don't care because the Human 
button press only takes around 10-20 ms which is hardly noticeable when streaming :)  But you could try and clicking 
faster and see a few hiccups, **but let's just be honest** that this whole project is pretty useful and awesome.  The 
other stuff/material design is just basic Android stuff.

## Questions?
Submit an [Issue](https://github.com/nathanRamaNoodles/Tinker-Controller/issues) and I'll explain there. :)

## Updates
I decided to use UDP because it's WAYYYYYYY faster than TCP. I didn't know if esp8266 had a UDP library, but 
low and behold it did :)

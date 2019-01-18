
# Tinker-Car
A Car I made that can be controlled over WiFi using my Tinker Controller app.  You can use any controller that works with your phone; I used an Xbox controller.

<h1 align= "center">Tinker Car</h1>
<p align="center">
	<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=R6GFJS92FX7J2"><img src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif"></a>
</p>

| Slick Controls | Smooth and fast| Quick Reactions using UDP protocol|
|:----:|:---:|:------:|
| <a href="https://imgflip.com/gif/2rgzoe"><img src="https://i.imgflip.com/2rgzoe.gif" title="made at imgflip.com"/></a>| <a href="https://imgflip.com/gif/2rh0mf"><img src="https://i.imgflip.com/2rh0mf.gif" title="made at imgflip.com"/></a> |  <img src="https://i.imgflip.com/2qcbkg.gif"/>|


<h2>Table Of Contents</h2>
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Tinker-Car](#tinker-car)
	- [Videos](#videos)
	- [Pictures](#pictures)
	- [Getting Started](#getting-started)
		- [Materials](#materials)
		- [Procedure](#procedure)
	- [Thank you](#thank-you)
	- [Submit an Issue/Bug?](#submit-an-issuebug)
	- [License](#license)

<!-- /TOC -->



## Videos
Video Demonstration:
 [Youtube](https://www.youtube.com/watch?v=CuxZIBogDuw)

## Pictures

<p align="center">
<img src="https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/Art/Motor_Shield.jpg"/>
</p>

<p align="center">
<img src="https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/Art/Tinker_Car_side.jpg"/>
</p>

<p align="center">
<img src="https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/Art/Tinker_Car_top.jpg"/>
</p>

----------------
## Getting Started

### Materials
+ Wemos D1 Mini
+ Wemos Motor Shield(v1.0.0)
+ A controller that can connect to an Android phone
+ An Android phone
+ A Toy Car

### Procedure
  1. [Download my latest release of my Tinker controller app from here](https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/demo.apk?raw=true)
  2. Next upload the Tinker_Motor_Car_Wemos.ino file to the Wemos D1 Mini
	   + Also, download the [motor shield library](https://github.com/thomasfredericks/wemos_motor_shield) for the Wemos Motor Shield(v1.0.0).
		   + Note: The motor shield has buggy firmware, so follow the directions in the link.
  3. Launch app, and click on "Gamepad".  
  4. You should notice the app will automatically connect to the esp8266, but if it doesn't you can tap on the button that has the WiFi symbol
     + If that doesn't work, then go to your WiFi settings, and connect to the "ESPTest" network.
  5. When connected successfully, the WiFi button and toggle button should turn blue.
  6. Now, grab a game controller supported by Android.
  7. You should notice that the game controller on the screen is showing the live actions you are doing on your controller
     + If nothing happens, try to close app, and try again.  If it still fails, then your controller is not supported by Android(which is sad)
  8. Press the A button(down button) on your controller, and you should notice that you are controlling the Esp8266's Built-in Led.
  9. You can move the left joystick to move the car forward/backward, and steer using the right joystick.

## Thank you
+ Thomas Fredrick for the [Wemos Motor Shield library](https://github.com/thomasfredericks/wemos_motor_shield).
+ Guys at Espressif for the amazing and fast library and plugin for esp8266
+ Guys who post good code with examples on stack overflow


## Submit an Issue/Bug?
Submit an [Issue](https://github.com/nathanRamaNoodles/Tinker-Controller/issues) and we'll fix it. :)

## License

```
GNU GENERAL PUBLIC LICENSE
   Version 3, 29 June 2007

Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
Everyone is permitted to copy and distribute verbatim copies
of this license document, but changing it is not allowed.

Rest of License found here: https://github.com/nathanRamaNoodles/Tinker-Controller/blob/master/LICENSE
```

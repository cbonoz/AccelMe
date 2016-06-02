AccelMe Android Wearable Application
===================================

Sample Android Project demonstrating how to create a basic Android Wearable/Mobile Integrated application. This demo was made for a short wearable introduction presentation at UCLA on 4/8/2016.

This app creates a transparent activity that runs in the background on the wearable, while information is still being delivered to the phone. This could also be extended on the phone to make post requests to your remote server. A common use case is to have the wearable sensors collect data in the background and run transparently (once launched) on the wearable itself.


Below is a screenshot of what the app looks like while running on your mobile device. When the sensors are on, the shown value and background color will change depending on the intensity of the acceleration movement of the wearable.

<img src="http://s21.postimg.org/ikiw1bod3/accelme1.png" alt="AccelMe App" width="300"/>

Select the app icon on both the mobile and wearable to start.


Pre-requisites
--------------

App requires an up to date version of the Android build tools and the Android support repository.
Android Wearable Device (Smartwatch)
Android Mobile Device


Getting Started
---------------

This demo project uses the Gradle build system.

First download the demo by cloning this repository

In Android Studio, use the "Import non-Android Studio project" or 
"Import Project" option. Next select one of the sample directories that you downloaded from this
repository.
If prompted for a gradle configuration accept the default settings. 

Alternatively use the "gradlew build" command to build the project directly.


License
-------

MIT
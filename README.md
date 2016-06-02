AccelMe Android Wearable Application
===================================

Samples demonstrating how to create a basic Android Wearable/Mobile Integrated application. This demo was made for a short introduction presentation at UCLA.

This app creates a transparent activity that runs in the background on the watch, while information is still being delivered to the phone. This could also be extended on the phone to make post requests to your remote server. A common use case for wearables is having wearable sensors collect data in the background, without any display on the watch itself.

This app listens to the accelerometer on the watch and displays a green intensity value on the phone between 00-ff depending on the intensity of the watch acceleration.


<!-- include image extension in url -->
<!-- ![Tooltip Light](http://s21.postimg.org/ikiw1bod3/accelme1.png) -->
<img src="http://s21.postimg.org/ikiw1bod3/accelme1.png" alt="AccelMe App" style="max-width: 250px; margin: 0 auto;"/>



Pre-requisites
--------------

App requires an up to date version of the Android build tools and the Android support repository.
Android Wearable Device
Android Mobile Device


Getting Started
---------------

This demo uses the Gradle build system.

First download the demo by cloning this repository

In Android Studio, use the "Import non-Android Studio project" or 
"Import Project" option. Next select one of the sample directories that you downloaded from this
repository.
If prompted for a gradle configuration accept the default settings. 

Alternatively use the "gradlew build" command to build the project directly.


License
-------

MIT
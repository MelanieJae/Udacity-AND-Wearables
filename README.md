This project is an enhancement of an existing project that displays a 7-day weather forecast for your location to support Android Wear devices. The base project can be found here

https://github.com/udacity/ud851-Sunshine

# Build Notes
This project is built using the Gradle Build System.

# Development Notes
An OpenWeatherMap API key is required to obtain accurate dynamic weather data and can be obtained here:

https://openweathermap.org/appid

This project utilizes a data layer and Google API Client to send dynamic accurate weather information from a mobile device to a wearable device paired with the mobile device.

You can run this application by:

1. Ensuring your mobile device and Android Wear Device are paired. If you are unsure you may go here: https://support.google.com/androidwear/answer/6056630?hl=en

2. Cloning or downloading the project
3. Building an APK in Android Studio to run the project on your device by clicking the Play symbol on either the mobile or wearable module. This is because this project does not have an available release APK at this time that would provide for automatic installation on your Wear device. Selecting the wearable module will run the wearable version on your watch or the emulator in Android Studio.


Sources:

1. LoadBitmapAsyncTask: all code courtesy of: https://github.com/googlesamples/android-DataLayer/
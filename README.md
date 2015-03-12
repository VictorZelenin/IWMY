##IWMY Speed Dating

The app for the social service [want2meetu.com](http://want2meetu.com)

### Back End

Uses Google App Engine.

####Running back end locally

You need Java 1.7+ and Gradle 2.2. Get this repo and run

    cd IWMY-Speed-Dating
	gradlew appengineRun

####Deploying the application to Google App Engine

After specifying application ID in `backend/src/main/webapp/WEB-INF/appengine-web.xml`, run

    gradlew appengineUpdate

### The Android app

[UI design prototype](https://moqups.com/arhangel-studio.com.ua/chL2PsyU)

To debug the app through ADB, run

    gradlew installDebug
# IWMY Speed Dating

This app for the social service [want2meetu.com](http://want2meetu.com) is currently released [at Google Play](https://play.google.com/store/apps/details?id=com.oleksiykovtun.iwmy.speeddating).


## For Developers

The project includes the following modules:

`app` - the Android application. Its design is currently based on the UI design prototype *(link temporarily unavailable)*. This is a reference app. The apps for other platforms are created based on this one. If you develop an app for the other platform, you can use the code of this app for help.

`backend` - the Google App Engine backend which provides API for the Android app (and other apps which can be developed). Its API documentation for app developers is *to be released*.

`shared` - the code used both in the Android app and the Google App Engine backend.



## Building and Running the App

The app includes 2 flavors: Developer (for debugging) and General (for public use). They are supposed to use separate backend instances, and they are installed on device separately.

The following instructions require Java 1.7+, Gradle 2.2+ and Android SDK installed.

Get (clone or download ZIP) this repo and navigate to its root folder.

    cd IWMY-Speed-Dating

The paths in these instructions are relative to the repo root folder path.

### Building and running the Developer version

First, the Google App Engine application should be run locally as the backend. Then, the Developer flavor of the Android app is used with the locally running backend.

#### Running backend locally

Run this command to build and run the backend locally:

	gradlew appengineRun

As an alternative, on Windows, for automatic keeping the database changes between runs, you can use the following command:

    developer-backend-run

and for stopping the backend:

    developer-backend-stop

If you want to manage the database manually, its file path is `backend/build/exploded-app/WEB-INF/appengine-generated/local_db.bin`. It can be created and updated by the backend after a delay from the actual operations.

#### Running the Developer flavor of the Android app

Android SDK must be installed. Connect your Android device to ADB and run the command:

    gradlew installDeveloperDebug

The app will be installed on your device. You can run it.

### Building and running the General version

First, the Google App Engine application should be run at Google App Engine as the backend. Then, the General flavor of the Android app is used with the backend which runs at Google App Engine.

#### Running backend at Google App Engine

You need to have an application ID registered at Google App Engine.

Check if your application ID is set properly within the `application` tag in the XML file `backend/src/main/webapp/WEB-INF/appengine-web.xml`. Then run the command:

	gradlew appengineUpdate

At first run you will be prompted for authentication with your Google account.

#### Running the General flavor of the Android app

The release certificate is used for signing this flavor of the app. You should put your keystore file as `app/IWMY-release.keystore.jks`.

***Note.*** In these instructions the keystore and key passwords are supposed to be the same. If yours are not, you should remove the function `gradle.taskGraph.whenReady` in the file `app/build.gradle` and put your keys as values of the `storePassword` and `keyPassword` fields. Other solutions can be used as well. Please keep your keystore and keys secure.

For building the signed app, run the command:

    gradlew installGeneralRelease

You will be prompted for your keystore/key password.

The app will be built as `app/build/outputs/apk/IWMY-Speed-Dating-version.apk` where `version` is the build date and time. You can install it manually and then run it.

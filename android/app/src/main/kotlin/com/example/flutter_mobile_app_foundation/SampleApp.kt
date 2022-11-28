package com.example.flutter_mobile_app_foundation

import io.flutter.app.FlutterApplication

class SampleApp : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Banuba VE UI SDK
        BanubaVideoEditorSDK().initialize(this@SampleApp)
    }
}
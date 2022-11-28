package com.example.flutter_mobile_app_foundation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.content.FileProvider
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.ve.flow.VideoCreationActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import java.io.File

class MainActivity : FlutterActivity() {
    companion object {
        private val BanubaSDKHelper = BanubaSDKHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appFlutterEngine = requireNotNull(flutterEngine)

        GeneratedPluginRegistrant.registerWith(appFlutterEngine)
        MethodChannel(
            appFlutterEngine.dartExecutor.binaryMessenger,
            BanubaSDKHelper.companion.CHANNEL
        ).setMethodCallHandler { call, result ->
            BanubaSDKHelper.companion.exportVideoChanelResult = result

            when (call.method) {
                BanubaSDKHelper.companion.METHOD_START_VIDEO_EDITOR ->
                    startActivityForResult(BanubaSDKHelper.startFromCamera(this, null), BanubaSDKHelper.companion.VIDEO_EDITOR_REQUEST_CODE)

                BanubaSDKHelper.companion.METHOD_START_VIDEO_EDITOR_PIP -> {
                    val videoFilePath = call.arguments as? String
                    val pipVideoUri = videoFilePath?.let { Uri.fromFile(File(it)) }
                    if (pipVideoUri == null) {
                        Log.w(
                            BanubaSDKHelper.companion.TAG,
                            "Missing or invalid video file path = [$videoFilePath] to start video editor in PIP mode."
                        )
                        BanubaSDKHelper.companion.exportVideoChanelResult.error(
                            BanubaSDKHelper.companion.ERR_START_PIP_MISSING_VIDEO,
                            "Missing video to start video editor in PIP mode",
                            null
                        )
                    } else {
                        startActivityForResult(BanubaSDKHelper.startFromCamera(this, pipVideoUri), BanubaSDKHelper.companion.VIDEO_EDITOR_REQUEST_CODE)}
                }

                else -> result.notImplemented()
            }
        }
    }

    // Observe export video results
    override fun onActivityResult(requestCode: Int, result: Int, intent: Intent?) {
        super.onActivityResult(requestCode, result, intent)

        if (requestCode == BanubaSDKHelper.companion.VIDEO_EDITOR_REQUEST_CODE) {
            if (result == Activity.RESULT_OK) {
                val exportResult =
                    intent?.getParcelableExtra(EXTRA_EXPORTED_SUCCESS) as? ExportResult.Success
                if (exportResult == null) {
                    BanubaSDKHelper.companion.exportVideoChanelResult.error(
                       BanubaSDKHelper.companion.ERR_MISSING_EXPORT_RESULT,
                        "Export finished with no result!",
                        null
                    )
                } else {
                    val data = BanubaSDKHelper.prepareExportData(exportResult)
                    BanubaSDKHelper.companion.exportVideoChanelResult.success(data)
                }
            }
        }
    }
}
package com.example.flutter_mobile_app_foundation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.ve.flow.VideoCreationActivity
import io.flutter.plugin.common.MethodChannel
import java.io.File

class BanubaSDKHelper {
     companion object {
         const val VIDEO_EDITOR_REQUEST_CODE = 7788
         const val TAG = "FlutterVE"

         const val METHOD_START_VIDEO_EDITOR = "StartBanubaVideoEditor"
         const val METHOD_START_VIDEO_EDITOR_PIP = "StartBanubaVideoEditorPIP"
         const val METHOD_DEMO_PLAY_EXPORTED_VIDEO = "PlayExportedVideo"

         const val ERR_MISSING_EXPORT_RESULT = "ERR_MISSING_EXPORT_RESULT"
         const val ERR_START_PIP_MISSING_VIDEO = "ERR_START_PIP_MISSING_VIDEO"
         const val ERR_EXPORT_PLAY_MISSING_VIDEO = "ERR_EXPORT_PLAY_MISSING_VIDEO"

         const val ARG_EXPORTED_VIDEO_FILE = "exportedVideoFilePath"
         const val CHANNEL = "startActivity/VideoEditorChannel"

         lateinit var exportVideoChanelResult: MethodChannel.Result
    }

    var companion = Companion

    fun startFromCamera(context: Context, pipVideo: Uri?) = run {
        VideoCreationActivity.startFromCamera(
            context = context,
            // setup data that will be acceptable during export flow
            additionalExportData = null,
            // set TrackData object if you open VideoCreationActivity with preselected music track
            audioTrackData = null,
            // set PiP video configuration
            pictureInPictureConfig = PipConfig(
                video = pipVideo ?: Uri.EMPTY,
                openPipSettings = false
            )
        )
    }

    // Customize export data results to meet your requirements.
    // You can use Map or JSON to pass custom data for your app.
    public fun prepareExportData(result: ExportResult.Success): Map<String, Any?> {
        // First exported video file path is used to play video in this sample to demonstrate
        // the result of video export.
        // You can provide your custom logic.
        val firstVideoFilePath = result.videoList[0].sourceUri.toString()
        return mapOf(
            ARG_EXPORTED_VIDEO_FILE to firstVideoFilePath
        )
    }
}
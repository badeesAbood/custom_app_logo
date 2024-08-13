package com.logo_changer.custom_app_logo
import ChangeLogoService
import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** CustomAppLogoPlugin */
class CustomAppLogoPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var context: Context
    private lateinit var channel: MethodChannel
    private lateinit var changeAppLogoService: ChangeLogoService
    private lateinit var activityRetrieverService: ActivityRetrieverService

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, "my_plugin")
        channel.setMethodCallHandler(this)
        context = binding.applicationContext
        changeAppLogoService = ChangeLogoService()
        activityRetrieverService = ActivityRetrieverService()
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        try {
            when (call.method) {
                "getActivityInfo" -> {
                    result.success(activityRetrieverService.getActivityInfo(context))
                }

                "changeAppLogo" -> {
                    changeAppLogoService.changeAppLogo(call.argument("activityName")!!, context);
                }

                else -> {
                    result.notImplemented()
                }
            }
        } catch (e: Exception) {
            println(e.message + " - here is the exception")
            result.error("ERROR", e.message, null)
        }
    }

}
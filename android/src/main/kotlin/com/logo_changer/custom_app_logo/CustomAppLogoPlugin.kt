package com.logo_changer.custom_app_logo

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.util.Base64
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.ByteArrayOutputStream
import android.content.SharedPreferences


/** CustomAppLogoPlugin */
class CustomAppLogoPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var context: Context
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, "my_plugin")
        channel.setMethodCallHandler(this)
        context = binding.applicationContext
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        try {
            when (call.method) {
                "getActivityInfo" -> {
                    result.success(getActivityInfo())
                }
                "changeAppLogo" -> {
                    changeAppLogo(call.argument("activityName")!!);
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

    private fun getActivityInfo(): Map<String, Any> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myAct", Context.MODE_PRIVATE)
        try {
            val pm: PackageManager = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            for (activityInfo in packageInfo.activities) {
                if(!sharedPreferences.contains(activityInfo.name)){
                    with(sharedPreferences.edit()) {
                        putString(activityInfo.name ,Base64.encodeToString(getActivityLogos(activityInfo), Base64.DEFAULT))
                        apply()
                    }
                }
            }
//            if (sharedPreferences.all.isEmpty()){
//                for (activityInfo in packageInfo.activities) {
//                    with(sharedPreferences.edit()) {
//                        putString(activityInfo.name ,Base64.encodeToString(getActivityLogos(activityInfo), Base64.DEFAULT))
//                        apply()
//                    }
//                }
//            }

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return mapOf(
            "activityLogos" to sharedPreferences.all
        )
    }

    private fun getActivityLogos(activityInfo: ActivityInfo): ByteArray {
        val pm = context.packageManager
        val logoResId = activityInfo.icon
        if (logoResId != 0) {
            val drawable = pm.getDrawable(activityInfo.packageName, logoResId, activityInfo.applicationInfo)
            if (drawable != null) {
                return drawableToByteArray(drawable)
            }
        }
        return ByteArray(0)
    }

    private fun drawableToByteArray(drawable: Drawable): ByteArray {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 250 // or any default size
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 250

        val bitmap = Bitmap.createBitmap(
            width,
            height,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun changeAppLogo(activityToEnable:  String) {
        val enabledActivities = getEnabledActivities() ;
        enabledActivities.forEach {
            val christmasComponent = ComponentName(context, it)
            context.packageManager.setComponentEnabledSetting(
                christmasComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        // Enable the main activity component
        val mainActivityComponent = ComponentName(context, activityToEnable)
        context.packageManager.setComponentEnabledSetting(
            mainActivityComponent,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )


    }


    private fun getEnabledActivities(): List<String> {
        val enabledActivities = mutableListOf<String>()
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)

        for (activityInfo in packageInfo.activities) {
            val componentName = ComponentName(context.packageName, activityInfo.name)
            val state = pm.getComponentEnabledSetting(componentName)

            if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
                enabledActivities.add(activityInfo.name)
            }
        }

        return enabledActivities
    }

}
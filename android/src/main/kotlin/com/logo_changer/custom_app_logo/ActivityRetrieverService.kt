package com.logo_changer.custom_app_logo

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

class ActivityRetrieverService {


  /**   the function [getActivityInfo] retrieves the MainActivity and its aliases  which are stored in the
   main manifest file , it uses the shared preferences for local persistence which adds the activities that are new to storage
    and before that it checks if the activity alias or activity is in the storage **/
     fun getActivityInfo(context: Context): Map<String, Any> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myAct", Context.MODE_PRIVATE)
        try {
            val pm: PackageManager = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            for (activityInfo in packageInfo.activities) {
                if(!sharedPreferences.contains(activityInfo.name)){
                    with(sharedPreferences.edit()) {
                        putString(activityInfo.name ,
                            Base64.encodeToString(getActivityLogos(activityInfo , context), Base64.DEFAULT))
                        apply()
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw e
        }

        return mapOf(
            "activityLogos" to sharedPreferences.all
        )
    }

    /**
     * [getActivityLogos] retrieves the icon Data in each activity
     */
    private fun getActivityLogos(activityInfo: ActivityInfo , context: Context): ByteArray {
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


    /**
     *[drawableToByteArray] converts the icon Data from Bitmap to ByteArray to be handled as Base64String in the flutter side
     */
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

}
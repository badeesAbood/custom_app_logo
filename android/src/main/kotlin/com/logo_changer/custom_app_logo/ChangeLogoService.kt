package com.logo_changer.custom_app_logo
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

class ChangeLogoService {

    /**
     *[getEnabledActivities] this gets all the enabled activities to be handled in the change AppLogo
     */
    private fun getEnabledActivities(context: Context): List<String> {
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
    /**
     *[changeAppLogo] this function gets the activity name which will be enabled
     * disabling all the other activities
     */
     fun changeAppLogo(activityToEnable:  String , context: Context) {
        val enabledActivities = getEnabledActivities(context) ;
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

}
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.*

class ChangeLogoService {

    /**
     * Retrieves a list of enabled activities in the current application.
     *
     * @param context The application context.
     * @return A list of enabled activity names.
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
     * Changes the app logo by disabling all activities and enabling the specified activity.
     *
     * @param activityToEnable The name of the activity to enable.
     * @param context The application context.
     */
    fun changeAppLogo(activityToEnable: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Disable all activities
                val enabledActivities = getEnabledActivities(context)
                enabledActivities.forEach { activityName ->
                    val componentName = ComponentName(context, activityName)
                    context.packageManager.setComponentEnabledSetting(
                        componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }

                // Simulate some processing delay if needed
                delay(2000)

                // Enable the specified activity
                withContext(Dispatchers.Main) {
                    val mainActivityComponent = ComponentName(context, activityToEnable)
                    context.packageManager.setComponentEnabledSetting(
                        mainActivityComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            } catch (e: Exception) {
                // Handle any exceptions here
                e.printStackTrace()
            }
        }
    }
}

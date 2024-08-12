import 'package:custom_app_logo/activitiesInofResult.dart';
import 'package:flutter/services.dart';

import 'custom_app_logo_platform_interface.dart';

/// An implementation of [CustomAppLogoPlatform] that uses method channels.
class MethodChannelCustomAppLogo extends CustomAppLogoPlatform {
  static const MethodChannel _channel = MethodChannel('my_plugin');

  static Future<ActivitiesInfoResult> getActivityInfo() async {
    try {
      final Map<Object?, Object?> result = await _channel.invokeMethod('getActivityInfo');
      print(result);
      return ActivitiesInfoResult.fromMap(result);
    } catch (e) {
      print("Failed to get activity names: '${e.toString()}'.");
      return ActivitiesInfoResult(activties: {});
    }
  }

  static Future<bool> changeAppLogo({required String activityName}) async {
    try {
      await _channel.invokeMethod("changeAppLogo",{"activityName" : activityName});
      return true;
    } catch (e) {
      return false;
    }
  }
}

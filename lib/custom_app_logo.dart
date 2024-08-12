import 'dart:io';

import 'package:custom_app_logo/activityModel.dart';
import 'package:custom_app_logo/custom_app_logo_method_channel.dart';

class CustomAppLogo {
  Future<List<ActivityModel>> getActivities() async {
    final result = await MethodChannelCustomAppLogo.getActivityInfo();
    return result.activties.entries
        .map((e) => ActivityModel(
            activtiyName: e.key,
            activtiyLogo: e.value,
            onChangeActivtiyLogo: () async {
               await MethodChannelCustomAppLogo.changeAppLogo(activityName: e.key);
               exit(0) ;
            }))
        .toList();
  }
}

import 'package:flutter_test/flutter_test.dart';
import 'package:custom_app_logo/custom_app_logo.dart';
import 'package:custom_app_logo/custom_app_logo_platform_interface.dart';
import 'package:custom_app_logo/custom_app_logo_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockCustomAppLogoPlatform
    with MockPlatformInterfaceMixin
    implements CustomAppLogoPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final CustomAppLogoPlatform initialPlatform = CustomAppLogoPlatform.instance;

  test('$MethodChannelCustomAppLogo is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelCustomAppLogo>());
  });

  test('getPlatformVersion', () async {
    CustomAppLogo customAppLogoPlugin = CustomAppLogo();
    MockCustomAppLogoPlatform fakePlatform = MockCustomAppLogoPlatform();
    CustomAppLogoPlatform.instance = fakePlatform;

    expect(await customAppLogoPlugin.getActivities(), isNotEmpty);
  });
}

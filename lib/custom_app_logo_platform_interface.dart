import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'custom_app_logo_method_channel.dart';

abstract class CustomAppLogoPlatform extends PlatformInterface {
  /// Constructs a CustomAppLogoPlatform.
  CustomAppLogoPlatform() : super(token: _token);

  static final Object _token = Object();

  static CustomAppLogoPlatform _instance = MethodChannelCustomAppLogo();

  /// The default instance of [CustomAppLogoPlatform] to use.
  ///
  /// Defaults to [MethodChannelCustomAppLogo].
  static CustomAppLogoPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [CustomAppLogoPlatform] when
  /// they register themselves.
  static set instance(CustomAppLogoPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}


import 'dart:typed_data';
import 'dart:ui';

class ActivityModel {
  final String activtiyName;

  final Uint8List activtiyLogo;

  final VoidCallback onChangeActivtiyLogo;

  ActivityModel({required this.activtiyName, required this.activtiyLogo, required this.onChangeActivtiyLogo});
}

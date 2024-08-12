import 'dart:convert';
import 'dart:typed_data';

class ActivitiesInfoResult {
  final Map<String, Uint8List> activties;

  ActivitiesInfoResult({
    required this.activties,
  });

  factory ActivitiesInfoResult.fromMap(Map<Object?, Object?> map) {
    return ActivitiesInfoResult(
      activties: (map['activityLogos'] as Map<Object?, Object?>).map(
        (key, value) =>
            MapEntry(key.toString(), base64Decode(value.toString().replaceAll(" ", "").replaceAll("\n", ""))),
      ),
    );
  }
}

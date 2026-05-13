import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeMlService {
  static const MethodChannel _channel = MethodChannel("mlkit_photo_analyze");

  static Future<Map> onDeviceMethod(String imagePath) async {
    final result = await _channel.invokeMethod(
      'imageLabeling',
      {'imagePath': imagePath},
    );
    return Map.from(result);
  }
}
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import '../../data/permission/permission_handler.dart'; // Adjust path accordingly

class PermissionProvider extends ChangeNotifier {
  bool _isCameraGranted = false;
  bool get isCameraGranted => _isCameraGranted;



  Future<void> updateCameraStatus() async {
    final status = await Permission.camera.status;
    _isCameraGranted = status.isGranted;

    notifyListeners();
  }

  // The method the UI calls when the button is pressed
  Future<void> requestCameraPermission(BuildContext context) async {
    await PermissionService.handleCameraPermission(context);
    await updateCameraStatus();
  }
}
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:fluttertoast/fluttertoast.dart';

class PermissionService {
  /// Checks camera permission and handles the UI logic
  static Future<void> handleCameraPermission(BuildContext context) async {
    PermissionStatus status = await Permission.camera.status;

    // If it's the first time or denied, request it
    if (status.isDenied) {
      status = await Permission.camera.request();
    }

    // Handle the result
    // Check if the widget is still mounted to avoid "Async Gap" errors
    if (!context.mounted) return;

    if (status.isGranted) {
      Fluttertoast.showToast(msg: 'Camera permission granted');
    } else if (status.isPermanentlyDenied) {
      // User clicked "Don't ask again", must go to settings
      _showPermissionDialog(context);
    } else if (status.isRestricted) {
      Fluttertoast.showToast(msg: 'Camera permission restricted by system.');
    } else {
      Fluttertoast.showToast(msg: 'Permission is required to continue.');
    }
  }

  // Initial check for Main.dart (non-UI blocking)
  static Future<void> initialCheck() async {
    await Permission.camera.request();
  }

  static Future<void> _showPermissionDialog(BuildContext context) async {
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Camera Access Required"),
          content: const Text(
              "This app needs camera access to function. Please enable it in system settings."),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text("Later"),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
                openAppSettings();
              },
              child: const Text("Open Settings"),
            ),
          ],
        );
      },
    );
  }
}
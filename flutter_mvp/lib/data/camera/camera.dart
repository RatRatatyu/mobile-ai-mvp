import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mvp/core/ui_state_provider.dart';
import 'package:provider/provider.dart';

class CameraHelper extends StatefulWidget {
  const CameraHelper({super.key});

  @override
  State<CameraHelper> createState() => _CameraHelperState();
}

class _CameraHelperState extends State<CameraHelper> {

  CameraController? _controller;
  Future<void>? _initializeControllerFuture;

  @override
  void initState() {
    super.initState();
    initCamera();
  }

  Future<void> initCamera() async {
    final cameras = await availableCameras();

    if (cameras.isEmpty) {
      return;
    }

    final backCamera = cameras.first;

    _controller = CameraController(
      backCamera,
      ResolutionPreset.medium,
    );

    _initializeControllerFuture = _controller!.initialize();

    setState(() {});
  }

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final uiStateProvider = context.watch<UiStateProvider>();

    if (uiStateProvider.latestImagePath.isNotEmpty) {
      return Column(
        children: [
          Expanded(
            flex: 3,
            child: Container(
              margin: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.grey.shade300),
              ),
              clipBehavior: Clip.antiAlias,
              child: Image.file(
                File(uiStateProvider.latestImagePath),
                fit: BoxFit.cover,
              ),
            ),
          ),
          _buildActionButton(
            icon: Icons.refresh,
            onTap: uiStateProvider.onCleanPhoto,
          ),
        ],
      );
    }

    if (_controller == null || _initializeControllerFuture == null) {
      return const Center(child: CircularProgressIndicator());
    }

    return FutureBuilder<void>(
      future: _initializeControllerFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          return Column(
            children: [
              Expanded(
                flex: 3,
                child: Container(
                  margin: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Colors.grey.shade300),
                  ),
                  clipBehavior: Clip.antiAlias,
                  child: CameraPreview(_controller!),
                ),
              ),
              _buildActionButton(
                icon: Icons.camera_alt,
                onTap: () async {
                  try {
                    await _initializeControllerFuture;
                    final image = await _controller?.takePicture();
                    if (image != null) {
                      uiStateProvider.onTakenPhoto(image.path);
                    }
                  } catch (e) {
                    debugPrint("Camera error: $e");
                  }
                },
              ),
            ],
          );
        } else {
          return const Center(child: CircularProgressIndicator());
        }
      },
    );
  }

  Widget _buildActionButton({required IconData icon, required VoidCallback onTap}) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Ink(
        decoration: ShapeDecoration(
          color: Colors.deepPurple.shade400,
          shape: const CircleBorder(),
        ),
        child: IconButton(
          iconSize: 32,
          onPressed: onTap,
          icon: Icon(icon, color: Colors.white),
        ),
      ),
    );
  }
}



import 'package:camera/camera.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

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
    if (_controller == null ||
        _initializeControllerFuture == null) {
      return const Center(
        child: CircularProgressIndicator(),
      );
    }

    return FutureBuilder(
      future: _initializeControllerFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState ==
            ConnectionState.done) {
          return CameraPreview(_controller!);
        }

        return const Center(
          child: CircularProgressIndicator(),
        );
      },
    );
  }
}
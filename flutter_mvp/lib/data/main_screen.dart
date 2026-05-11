import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../core/provider_menadger.dart';
import 'camera/camera.dart';



class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key});
  @override
  Widget build(BuildContext context) {


    return Scaffold(
      appBar: AppBar(
        title: Text("Flutter App", style: Theme.of(context).textTheme.titleLarge,),
        actions: [
          Switch(value: true, onChanged: (null))
        ],
      ),
      body: Column(
        children: [
          ShowingCamera(),

          Expanded(
              child: Placeholder()
          )
        ],
      ),
    );
  }
}


class ShowingCamera extends StatelessWidget {
  const ShowingCamera({super.key});

  @override
  Widget build(BuildContext context) {
    final permissionProv = context.watch<PermissionProvider>();

    return Expanded(
      flex: 3,
      child: permissionProv.isCameraGranted
          ? const CameraHelper()
          : Center(
        child: ElevatedButton(
          onPressed: () => permissionProv.requestCameraPermission(context),
          child: const Text("Разрешить доступ к камере"),
        ),
      ),
    );
  }
}


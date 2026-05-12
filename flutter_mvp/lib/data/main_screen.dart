import 'package:flutter/material.dart';
import 'package:flutter_mvp/core/ui_state_provider.dart';
import 'package:provider/provider.dart';

import '../core/provider_menadger.dart';
import 'camera/camera.dart';



class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key});
  @override
  Widget build(BuildContext context) {
    final uiState = context.watch<UiStateProvider>();

    return Scaffold(
      appBar: AppBar(
        title: Text("Flutter App", style: Theme.of(context).textTheme.titleLarge,),
        actions: [
          Switch(value: true, onChanged: (null))
        ],
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          ShowingCamera(),

          Expanded(
              child: Container(
                  width: double.infinity,
                  margin: EdgeInsets.all(15),
                  child: uiState.isLoading
                      ? const Center(child: CircularProgressIndicator(),)
                      : Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("Объект: ${uiState.label}",
                        style: Theme.of(context).textTheme.headlineSmall,),
                      Text("Увереность: ${uiState.score}%",
                        style: Theme.of(context).textTheme.headlineSmall,),
                      Text("Время: 0",
                        style: Theme.of(context).textTheme.headlineSmall,),

                    ],
                  )
              )
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


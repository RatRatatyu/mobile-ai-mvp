import 'package:flutter/material.dart';
import 'package:flutter_mvp/core/ui_state_provider.dart';
import 'package:provider/provider.dart';

import 'core/provider_menadger.dart';
import 'data/main_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(
            create: (_) => UiStateProvider()
        ),
        ChangeNotifierProvider(
          create: (_) => PermissionProvider(),
        ),
      ],
      child: const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        colorScheme: .fromSeed(seedColor: Colors.deepPurple),
      ),
      home: const MyHomePage(),
    );
  }
}

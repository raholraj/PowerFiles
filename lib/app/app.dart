import 'package:flutter/material.dart';
import 'router.dart';
import 'theme.dart';

class PowerFilesApp extends StatelessWidget {
  const PowerFilesApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'PowerFiles',
      debugShowCheckedModeBanner: false,
      theme: buildAppTheme(),
      darkTheme: buildAppTheme(),
      themeMode: ThemeMode.dark,
      routerConfig: appRouter,
    );
  }
}

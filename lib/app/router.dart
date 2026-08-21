import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../features/file_browser/file_browser_screen.dart';
import '../features/storage_analyzer/storage_analyzer_screen.dart';
import '../features/scanner/scanner_screen.dart';
import '../features/ai_chat/ai_chat_screen.dart';
import '../features/settings/settings_screen.dart';
import '../features/editor/editor_screen.dart';
import '../features/terminal/terminal_screen.dart';
import '../features/telegram_bot/telegram_settings_screen.dart';
import '../features/projects/projects_screen.dart';
import '../shared/widgets/main_shell.dart';
import '../features/file_browser/onboarding_screen.dart';
import '../features/settings/ai_providers_screen.dart';
import '../features/settings/local_models_screen.dart';

final appRouter = GoRouter(
  initialLocation: '/files',
  routes: [
    ShellRoute(
      builder: (context, state, child) => MainShell(child: child),
      routes: [
        GoRoute(
          path: '/files',
          builder: (context, state) => const FileBrowserScreen(),
        ),
        GoRoute(
          path: '/storage',
          builder: (context, state) => const StorageAnalyzerScreen(),
        ),
        GoRoute(
          path: '/scanner',
          builder: (context, state) => const ScannerScreen(),
        ),
        GoRoute(
          path: '/ai',
          builder: (context, state) => const AiChatScreen(),
        ),
        GoRoute(
          path: '/settings',
          builder: (context, state) => const SettingsScreen(),
        ),
      ],
    ),
    GoRoute(
      path: '/editor',
      builder: (context, state) => const EditorScreen(),
    ),
    GoRoute(
      path: '/terminal',
      builder: (context, state) => const TerminalScreen(),
    ),
    GoRoute(
      path: '/telegram',
      builder: (context, state) => const TelegramSettingsScreen(),
    ),
    GoRoute(
      path: '/projects',
      builder: (context, state) => const ProjectsScreen(),
    ),
    GoRoute(
      path: '/onboarding',
      builder: (context, state) => const OnboardingScreen(),
    ),
    GoRoute(
      path: '/ai-providers',
      builder: (context, state) => const AiProvidersScreen(),
    ),
    GoRoute(
      path: '/local-models',
      builder: (context, state) => const LocalModelsScreen(),
    ),
  ],
);

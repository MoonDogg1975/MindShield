# MindShield App - Preview Instructions

This document provides instructions for previewing the MindShield app UI components.

## Available Previews

1. **Login Screen**
   - Light theme: `LoginScreen`
   - Dark theme: `LoginScreenDark`

2. **Register Screen**
   - Light theme: `RegisterScreen`
   - Dark theme: `RegisterScreenDark`

## Previewing the App

### Option 1: Using Android Studio's Preview Pane

1. Open the project in Android Studio
2. Navigate to `app/src/main/java/com/mindshield/app/ui/preview/Preview.kt`
3. Click on the split or design view to see the previews
4. Use the refresh button to update the preview after making changes

### Option 2: Running on a Device/Emulator

1. Connect an Android device or start an emulator
2. Run the following command in the project root directory:

```powershell
# For Login Screen (Light Theme)
.\launch_preview.ps1 LoginScreen

# For Login Screen (Dark Theme)
.\launch_preview.ps1 LoginScreenDark

# For Register Screen (Light Theme)
.\launch_preview.ps1 RegisterScreen

# For Register Screen (Dark Theme)
.\launch_preview.ps1 RegisterScreenDark
```

## Testing Authentication Flow

The preview uses a fake authentication repository that simulates successful login/registration with any non-empty credentials:

- **Email**: Any non-empty string (e.g., `test@example.com`)
- **Password**: Any non-empty string (e.g., `password`)

## Troubleshooting

1. **Preview not updating**
   - Try clicking the refresh button in the preview pane
   - Invalidate caches and restart Android Studio

2. **App not installing**
   - Make sure USB debugging is enabled on your device
   - Ensure no other instances of the app are running

3. **Preview not showing**
   - Make sure you have the latest version of Android Studio
   - Check for any build errors in the "Build" tab

## Next Steps

- Implement the remaining screens and navigation
- Add more previews as new screens are developed
- Write UI tests for the previews

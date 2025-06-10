# Launch the preview activity
# Usage: .\launch_preview.ps1 [preview_name]
# Available previews: LoginScreen, LoginScreenDark, RegisterScreen, RegisterScreenDark

param(
    [string]$previewName = "LoginScreen"
)

# Build the app
Write-Host "Building the app..." -ForegroundColor Yellow
& .\gradlew assembleDebug

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

# Install the app
Write-Host "Installing the app..." -ForegroundColor Yellow
& adb install -r -t .\app\build\outputs\apk\debug\app-debug.apk

if ($LASTEXITCODE -ne 0) {
    Write-Host "Installation failed!" -ForegroundColor Red
    exit 1
}

# Launch the preview activity
Write-Host "Launching $previewName preview..." -ForegroundColor Green
$intent = "-a android.intent.action.VIEW -n com.mindshield.app/.ui.preview.PreviewActivity -e preview_name $previewName"
& adb shell am start $intent

if ($LASTEXITCODE -eq 0) {
    Write-Host "Preview launched successfully!" -ForegroundColor Green
} else {
    Write-Host "Failed to launch preview!" -ForegroundColor Red
}

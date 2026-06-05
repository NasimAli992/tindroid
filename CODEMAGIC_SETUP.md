# Codemagic Configuration Guide for Tindroid

This guide explains the Codemagic configuration files for building the Tindroid Android app.

## Overview

The `codemagic.yaml` file defines three automated workflows for continuous integration and deployment:

1. **Android Release Build** - Creates signed release APK and AAB for production
2. **Android Debug Build** - Creates debug APK and runs unit tests
3. **Android Instrumented Tests** - Runs device/emulator tests

## Prerequisites

### 1. Android Signing Configuration

Before using the release build workflow, configure your signing credentials:

#### Create keystore.properties file (for local builds):
```properties
storeFile=path/to/your/keystore.jks
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

#### Configure in Codemagic UI:
1. Go to **Project Settings → Code signing**
2. Upload your keystore file (.jks or .p12)
3. Set the following environment variables:
   - `CM_KEYSTORE_PATH` - Path to keystore file
   - `CM_KEYSTORE_PASSWORD` - Keystore password
   - `CM_KEY_ALIAS` - Key alias
   - `CM_KEY_PASSWORD` - Key password

### 2. Google Play Credentials (Optional)

For automatic Play Store deployment:

1. Create a Google Cloud service account with Play Developer API permissions
2. Download the JSON credentials file
3. In Codemagic UI: **Project Settings → Code signing → Google Play**
4. Upload the credentials and store as `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS`

### 3. Firebase Configuration

For push notifications to work:

1. Download `google-services.json` from Firebase Console
2. Place it in the `app/` directory
3. The build will automatically use it during compilation

## Environment Variables

Configure these variables in Codemagic UI under **Project Settings → Environment variables**:

| Variable | Required | Description |
|----------|----------|-------------|
| `DEVELOPER_EMAIL` | Optional | Email for build notifications |
| `SLACK_CHANNEL_ID` | Optional | Slack channel for notifications |
| `CM_KEYSTORE_PATH` | Release builds | Path to signing keystore |
| `CM_KEYSTORE_PASSWORD` | Release builds | Keystore password |
| `CM_KEY_ALIAS` | Release builds | Key alias in keystore |
| `CM_KEY_PASSWORD` | Release builds | Key password |
| `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS` | Play Store | Google Cloud credentials JSON |

## Workflow Details

### Android Release Build
- **Trigger**: Push to main branch or manual trigger
- **Duration**: ~120 minutes
- **Instance**: Mac Mini M1 (sufficient for Android builds)
- **Output**:
  - Release APK: `app/build/outputs/apk/release/app-release.apk`
  - App Bundle: `app/build/outputs/bundle/release/app-release.aab`

**Build Steps**:
1. Set up Android SDK environment
2. Create `keystore.properties` from secure credentials
3. Build signed release APK with optimizations
4. Build Android App Bundle for Play Store
5. Publish to Play Store (internal track, draft mode)

### Android Debug Build
- **Trigger**: Pull requests or manual trigger
- **Duration**: ~60 minutes
- **Instance**: Mac Mini M1
- **Output**:
  - Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
  - Test reports: `build/reports/tests/`

**Build Steps**:
1. Build debug APK
2. Run unit tests
3. Notify via email/Slack on success/failure

### Android Instrumented Tests
- **Trigger**: On demand or scheduled
- **Duration**: ~90 minutes
- **Instance**: Mac Mini M1
- **Output**:
  - Test reports: `build/reports/androidTests/connected/`

**Build Steps**:
1. Build instrumented test APK
2. Run connected tests (requires device/emulator)
3. Generate test reports

## Gradle Configuration

The project uses:
- **Gradle**: 9.0.1 (AGP - Android Gradle Plugin)
- **Min SDK**: 27
- **Target SDK**: 36
- **Compile SDK**: 36
- **Java**: 17
- **Desugar**: Enabled for Java 11+ features

## Build Customization

### Version Management

Versions are automatically managed via git tags:
- **Version Code**: Git commit count
- **Version Name**: Git tag (e.g., v1.2.3 → 1.2.3)

To override during builds, use:
```bash
-PversionCode=123
-PversionName=1.0.0
```

### Gradle JVM Memory

Configured to use 4GB JVM memory to handle large builds:
```bash
-Xmx4096m -XX:MaxPermSize=1024m
```

### Build Optimization

- `--build-cache`: Enables Gradle build cache for faster rebuilds
- `minifyEnabled`: Enabled for release builds (ProGuard)
- `shrinkResources`: Unused resources removed from release APK

## Notifications

### Email Notifications
- Sent to `$DEVELOPER_EMAIL`
- Triggered on success/failure

### Slack Notifications
- Sent to `$SLACK_CHANNEL_ID`
- Includes build number, branch, and commit message
- Sent at build start and on completion

### Google Play Store
- Automatic submission to internal testing track
- Submitted as draft for review before release

## Troubleshooting

### Build Fails with Keystore Error
- Verify `keystore.properties` exists
- Check credentials are correct in Codemagic UI
- Ensure keystore file format is correct (.jks)

### Gradle Build Cache Issues
```bash
./gradlew clean assemble*
```

### Firebase Initialization Fails
- Ensure `google-services.json` is in `app/` directory
- Verify Google Services plugin version in `build.gradle`

### Version Code Issues
```bash
# Use static version if git tags not available
-PversionCode=10000
-PversionName=1.0.0
```

## Testing Locally

To test the build configuration locally:

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires keystore)
./gradlew assembleRelease

# Unit tests
./gradlew test

# Run all checks
./gradlew check
```

## Next Steps

1. **Set up Codemagic Account**: https://codemagic.io
2. **Connect Repository**: Link your GitHub repo
3. **Configure Signing**: Upload keystore and credentials
4. **Set Environment Variables**: Configure email, Slack, Play Store details
5. **Test Build**: Run manual build to verify configuration
6. **Enable Webhooks**: Auto-trigger builds on push/PR
7. **Monitor Builds**: Check build logs and artifacts

## Documentation Links

- [Codemagic Android Documentation](https://docs.codemagic.io/getting-started/building-an-android-app/)
- [Android Gradle Plugin Guide](https://developer.android.com/studio/releases/gradle-plugin)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [Google Play Console](https://play.google.com/console)

## Support

For issues with Codemagic configuration:
- Check [Codemagic Documentation](https://docs.codemagic.io/)
- Review build logs in Codemagic UI
- Post on [Tinode Discussion Forum](https://groups.google.com/d/forum/tinode)

# Riru - InternalBrowserRedirect

A module of [Riru](https://github.com/RikkaApps/Riru). Redirect internal browser activity to ACTION_VIEW.

[中文说明](README_zh.md)

## Requirements

* [Riru](https://github.com/RikkaApps/Riru) > 19 installed.
* Android 7.0+



## Feature

Redirect some application's internal browser to external browser (eg. TIM's QQBrowser to Chrome)



## Documents

See also [Documents](https://kr328.github.io/Riru-InternalBrowserRedirect-Rules/lang-detect)



## Build

1. Install JDK ,Gradle ,Android SDK ,Android NDK

2. Create `local.properties` on project root directory
   ```properties
   sdk.dir=/path/to/android-sdk
   ndk.dir=/path/to/android-ndk
   cmake.dir=/path/to/android-cmake/*version*
   ```

3. Create `keystore.properties` on project root directory
   ```properties
   storePassword=your_store_password
   keyPassword=your_key_password
   keyAlias=your_key_alias
   storeFile=/path/to/your/store
   ```

4. Run command 
   ```bash
   ./gradlew build
   ```

5. Pick riru-internal-browser-redirect.zip from module/build/outputs

## TODO
 - [ ] Short Resolve
 - [ ] Force External Flag for Rule

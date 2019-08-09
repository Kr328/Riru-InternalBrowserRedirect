# Riru - InternalBrowserRedirect

[Riru](https://github.com/RikkaApps/Riru) 模块. 重定向应用的内置浏览器到外部浏览器. ~~(简单来说就是 去你大爷的内置浏览器)~~

[English](https://github.com/Kr328/Riru-InternalBrowserRedirect/blob/master/README.md)

## 安装要求

* [Riru - Core](https://github.com/RikkaApps/Riru) 版本大于或等于 19 .
* Android 7.0-9.0




## 功能

重定向应用的内置浏览器到外部浏览器



## 文档

参见 [文档](https://kr328.github.io/Riru-InternalBrowserRedirect-Rules/lang-detect)



## 构建

1. 安装 JDK ,Gradle ,Android SDK ,Android NDK

2. 在 **项目根目录** 建立 `local.properties`
   ```properties
   sdk.dir=/路径/到/AndroidSdk
   ndk.dir=/路径/到/AndroidNdk
   cmake.dir=/路径/到/AndroidCmake/*版本*
   ```
3. 在 **项目根目录** 建立 `keystore.properties`
   ```properties
   storePassword=
   keyPassword=
   keyAlias=
   storeFile=
   ```
4. 执行以下命令
   ```bash 
   ./gradlew build
   ```

5. 从 module/build/outputs 获取 riru-internal-browser-redirect.zip



## 反馈

Telegram 群组 [Kr328 Riru Modules](https://t.me/kr328_riru_modules)


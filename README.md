# Riru - InternalBrowserRedirect

A module of [Riru](https://github.com/RikkaApps/Riru). Redirect internal browser activity to ACTION_VIEW.

[中文说明](https://github.com/Kr328/Riru-InternalBrowserRedirect/blob/master/README_zh.md)

## Requirements

* [Riru](https://github.com/RikkaApps/Riru) > 19 installed.
* Android 8.0-9.0 (below not tests)



## Feature

Redirect some application's internal browser to external browser (eg. TIM's QQBrowser to Chrome)

**Double click same url in 5s will fallback to internal browser**

Currently support apps

- WeChat
- QQ
- TIM
- Bilibili



## Custom Rules

1. Create file **/data/misc/riru/modules/ibr/config.\<package\>.json (replace \<package\> to target package name)**

2. Write rule in created file 

   example

   ```json
   {
   	"name": "TIM", 
   	"rules": [          
   		{
   			"extra-key": "url",   
   			"ignore-url": ".*bilibili\\.com.*",
   			"force-url": "" 
   		}
   	]
   } 
   ```
   | key        | value                                  |
   | ---------- | -------------------------------------- |
   | name       | logcat display name                    |
   | rules      | rules or just keep empty for debugging |
   | extra-key  | load url from extra which is key       |
   | ignore-url | ignore apply url regex                 |
   | force-url  | force apply url regex                  |

3. logcat to check it work (TAG: **InternalBrowserRedirect**)

4. (Optional) Share this rules by submissing on github issue



## Build

  1.Install JDK ,Gradle ,Android SDK ,Android NDK

  2.Configure with your environment [local.properties](https://github.com/Kr328/Riru-InternalBrowserRedirect/blob/master/local.properties)

  3.Run command 

``` Gradle 
./gradlew build
```
  4.Pick riru-internal-browser-redirect.zip from module/build/outputs



## Feedback

Telegram Group [Kr328 Riru Modules](https://t.me/kr328_riru_modules)
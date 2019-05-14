# Riru - InternalBrowserRedirect

[Riru](https://github.com/RikkaApps/Riru) 模块. 重定向应用的内置浏览器到外部浏览器. ~~(简单来说就是 去你大爷的内置浏览器)~~

[English](https://github.com/Kr328/Riru-InternalBrowserRedirect/blob/master/README.md)

## 安装要求

* [Riru - Core](https://github.com/RikkaApps/Riru) 版本大于或等于 19 .
* Android 8.0-9.0 (低版本未测试)


## 功能

重定向应用的内置浏览器到外部浏览器

**5s内再次点击同一链接会回滚到使用内置浏览器**

当前支持的App

- 微信
- QQ
- TIM
- Bilibili


## 自定义规则

1. 创建文件 **/data/misc/riru/modules/ibr/config.\<package\>.json (将 \<package\> 替换为目标包名)**

2. 在创建的文件中写入规则

   样例

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
   | name       | 在日志中显示的名称                      |
   | rules      | 规则列表 可以留空用于调试                |
   | extra-key  | 从 extra 中提取原始链接的 key           |
   | ignore-url | 忽略特定url的正则表达式                 |
   | force-url  | 强制生效的url的正则表达                |

3. 通过日志检查是否正常工作 (TAG: **InternalBrowserRedirect**)

4. (可以) 通过 Github ISSUE 分享你的规则 :)



## 构建

  1.安装 JDK ,Gradle ,Android SDK ,Android NDK

  2.根据你的环境配置 [local.properties](https://github.com/Kr328/Riru-InternalBrowserRedirect/blob/master/local.properties)

  3.执行以下命令

``` Gradle 
./gradlew build
```
  4.从 module/build/outputs 拿取 riru-internal-browser-redirect.zip



## 反馈

Telegram 群组 [Kr328 Riru Modules](https://t.me/kr328_riru_modules)

